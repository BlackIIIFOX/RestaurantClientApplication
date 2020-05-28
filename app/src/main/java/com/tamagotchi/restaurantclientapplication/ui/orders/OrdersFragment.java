package com.tamagotchi.restaurantclientapplication.ui.orders;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.common.logging.Logger;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.tamagotchi.restaurantclientapplication.R;
import com.tamagotchi.restaurantclientapplication.data.model.FullMenuItem;
import com.tamagotchi.restaurantclientapplication.data.repositories.OrderRepository;
import com.tamagotchi.restaurantclientapplication.ui.main.MainViewModel;
import com.tamagotchi.restaurantclientapplication.ui.main.MainViewModelFactory;
import com.tamagotchi.tamagotchiserverprotocol.models.ErrorResponse;
import com.tamagotchi.tamagotchiserverprotocol.models.OrderCreateModel;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import ru.yandex.money.android.sdk.Amount;
import ru.yandex.money.android.sdk.Checkout;
import ru.yandex.money.android.sdk.GooglePayCardNetwork;
import ru.yandex.money.android.sdk.MockConfiguration;
import ru.yandex.money.android.sdk.PaymentMethodType;
import ru.yandex.money.android.sdk.PaymentParameters;
import ru.yandex.money.android.sdk.SavePaymentMethod;
import ru.yandex.money.android.sdk.TestParameters;

public class OrdersFragment extends Fragment {

    private static final String TAG = "OrdersFragment";

    private MainViewModel viewModel;
    private View ordersFragment;
    public static final int REQUEST_CODE_TOKENIZE = 15462;

    private static SimpleDateFormat dataFormat = new SimpleDateFormat("dd.MM.yyyy");
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("kk:mm");

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this, new MainViewModelFactory()).get(MainViewModel.class);
        ordersFragment = inflater.inflate(R.layout.fragment_orders, container, false);

        initOrderButton();
        initInfoAboutVisit();

        return ordersFragment;
    }

    private void initOrderButton() {
        Button makeOrderButton = ordersFragment.findViewById(R.id.buttonPayment);

        makeOrderButton.setOnClickListener(view -> {
            if (viewModel.getUserMenu().getValue() == null || viewModel.getUserMenu().getValue().isEmpty()) {
                viewModel.doOrder()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> Toast.makeText(ordersFragment.getContext(), R.string.orderSuccessCreated, Toast.LENGTH_LONG).show(),
                                error -> {
                                    String textError = error.toString();
                                    if (error instanceof HttpException) {
                                        ResponseBody body = ((HttpException) error).response().errorBody();

                                        Gson gson = new Gson();
                                        TypeAdapter<ErrorResponse> adapter = gson.getAdapter
                                                (ErrorResponse.class);

                                        try {
                                            ErrorResponse errorParser =
                                                    adapter.fromJson(body.string());

                                            textError = errorParser.getMessage();

                                        } catch (IOException ignored) {
                                        }
                                    }

                                    Toast.makeText(ordersFragment.getContext(),
                                            getResources().getString(R.string.orderErrorCreated) + "(" + textError + ")", Toast.LENGTH_LONG)
                                            .show();
                                });
            } else {
                timeToStartPayment();
                // Создание заказа будет обработано в MainActivity
            }
        });
    }

    // Иницилизируем оплату для получения токена оплаты.
    private void timeToStartPayment() {
        HashSet<PaymentMethodType> paymentMethodTypes = new HashSet<>();
        paymentMethodTypes.add(PaymentMethodType.SBERBANK);
        paymentMethodTypes.add(PaymentMethodType.GOOGLE_PAY);
        paymentMethodTypes.add(PaymentMethodType.BANK_CARD);

        HashSet<GooglePayCardNetwork> googlePayCardNetworks = new HashSet<>();
        googlePayCardNetworks.add(GooglePayCardNetwork.MASTERCARD);
        googlePayCardNetworks.add(GooglePayCardNetwork.VISA);

        PaymentParameters paymentParameters = new PaymentParameters(
                new Amount(new BigDecimal(getPayment()), Currency.getInstance("RUB")),
                "Заказ в Tamagotchi",
                "Меню в тамагочи",
                "live_AAAAAAAAAAAAAAAAAAAA",
                "12345",
                SavePaymentMethod.ON,
                paymentMethodTypes,
                null,
                null,
                ""      //Телефонный номер
        );
        paymentParameters.paymentMethodTypes.remove(PaymentMethodType.YANDEX_MONEY);

        TestParameters testParameters = new TestParameters(true,
                true, new MockConfiguration(false, true, 5));

        Intent intent = Checkout.createTokenizeIntent(getContext(),
                paymentParameters, testParameters);

        getActivity().startActivityForResult(intent, REQUEST_CODE_TOKENIZE);
    }

    private void initInfoAboutVisit() {
        initSelectedRestaurant();

        viewModel.getOrderVisitInfo().observe(getViewLifecycleOwner(), orderVisitInfo -> {
            Calendar calendar = orderVisitInfo.getVisitTime();
            setTextInTextView(ordersFragment.findViewById(R.id.orderDetailDate), dataFormat.format(calendar.getTime()));
            setTextInTextView(ordersFragment.findViewById(R.id.orderDetailTime), timeFormat.format(calendar.getTime()));
            setTextInTextView(ordersFragment.findViewById(R.id.orderDetailGuests), String.valueOf(orderVisitInfo.getNumberOfVisitors()));
        });

        initSelectedMenu();
    }

    private void initSelectedRestaurant() {
        viewModel.getSelectedRestaurant().observe(getViewLifecycleOwner(), restaurant -> {
            setTextInTextView(ordersFragment.findViewById(R.id.orderDetailAddress), restaurant.getAddress());
        });
    }

    //TODO: Какая-то хрень с жизненным циклом активити...возможно но не точно, подумать!
    //TODO: Нужно сделать нормальный адаптер для ListView и использовать его как в MenuFragment так и в OrderFragment иначе у нас будут проблемы с разметкой (они уже есть...)
    private void initSelectedMenu() {
        Map<String, Integer> selectedMenu = getMapMenu();
        if (selectedMenu != null) {
            for (Map.Entry<String, Integer> item : getMapMenu().entrySet()) {
                addTextInTextView(ordersFragment.findViewById(R.id.listMenu), "- " + item.getKey() + "     x" + item.getValue() + "\n");
            }
        }

        setTextInTextView(ordersFragment.findViewById(R.id.totalPaymentDue), Integer.toString(getPayment()));
    }

    private int getPayment() {
        List<FullMenuItem> menu = viewModel.getUserMenu().getValue();
        if (menu != null && menu.size() > 0) {
            int forPayment = 0;
            for (FullMenuItem menuItem : menu) {
                forPayment += menuItem.getPrice();
            }

            return forPayment;
        }

        return 0;
    }

    private Map<String, Integer> getMapMenu() {
        List<FullMenuItem> menu = viewModel.getUserMenu().getValue();

        if (menu != null && menu.size() > 0) {
            Map<String, Integer> selectedMenu = new HashMap<String, Integer>();

            for (FullMenuItem menuItem : menu) {
                String itemMenuName = menuItem.getDish().getName();
                if (selectedMenu.containsKey(itemMenuName)) {
                    selectedMenu.put(itemMenuName, selectedMenu.get(itemMenuName) + 1);
                } else {
                    selectedMenu.put(itemMenuName, 1);
                }
            }

            return selectedMenu;
        }

        return null;
    }

    private void setTextInTextView(TextView textView, String text) {
        textView.setText(text);
    }

    private void addTextInTextView(TextView textView, String text) {
        textView.setText(textView.getText() + text);
    }
}
