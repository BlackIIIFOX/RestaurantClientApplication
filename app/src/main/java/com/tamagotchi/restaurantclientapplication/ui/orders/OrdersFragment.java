package com.tamagotchi.restaurantclientapplication.ui.orders;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.tamagotchi.restaurantclientapplication.R;
import com.tamagotchi.restaurantclientapplication.data.model.FullMenuItem;
import com.tamagotchi.restaurantclientapplication.ui.main.MainViewModel;
import com.tamagotchi.restaurantclientapplication.ui.main.MainViewModelFactory;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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

    private Map<String, Integer> selectedMenu = new HashMap<String, Integer>();
    private int forPayment = 0;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this, new MainViewModelFactory()).get(MainViewModel.class);
        ordersFragment = inflater.inflate(R.layout.fragment_orders, container, false);

        initPaymentButton();
        initInfoAboutVisit();

        return ordersFragment;
    }

    private void initPaymentButton() {
        Button makePayment = ordersFragment.findViewById(R.id.buttonPayment);
        makePayment.setOnClickListener(view -> {
            timeToStartCheckout();
        });
    }

    // Иницилизируем оплату для получения токена оплаты.
    private void timeToStartCheckout() {
        HashSet<PaymentMethodType> paymentMethodTypes = new HashSet<>();
        paymentMethodTypes.add(PaymentMethodType.SBERBANK);
        paymentMethodTypes.add(PaymentMethodType.GOOGLE_PAY);
        paymentMethodTypes.add(PaymentMethodType.BANK_CARD);

        HashSet<GooglePayCardNetwork> googlePayCardNetworks = new HashSet<>();
        googlePayCardNetworks.add(GooglePayCardNetwork.MASTERCARD);
        googlePayCardNetworks.add(GooglePayCardNetwork.VISA);

        String orderMenu = "";
        int counter = 1;
        for(Map.Entry<String, Integer> item : this.selectedMenu.entrySet()){
            orderMenu = counter + ". " + item.getKey() + "     x" + item.getValue() + " ";
            counter += 1;
        }

        PaymentParameters paymentParameters = new PaymentParameters(
                new Amount(new BigDecimal(forPayment), Currency.getInstance("RUB")),
                "Заказ в Tamagotchi",
                orderMenu,
                "live_AAAAAAAAAAAAAAAAAAAA",
                "12345",
                SavePaymentMethod.ON,
                paymentMethodTypes,
                null,
                null,
                ""      //Телефонный номер
        );
        paymentParameters.paymentMethodTypes.remove(PaymentMethodType.YANDEX_MONEY);

        TestParameters testParameters = new TestParameters(true, true, new MockConfiguration(false, true, 5));

        Intent intent = Checkout.createTokenizeIntent(getContext(), paymentParameters, testParameters);
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

    //TODO: Какая-то хрень с жизненным циклом активити...
    //TODO: Нужно сделать нормальный адаптер для ListView и использовать его как в MenuFragment так и в OrderFragment иначе у нас будут проблемы с разметкой (они уже есть...)
    private void initSelectedMenu() {
        viewModel.getUserMenu().observe(getViewLifecycleOwner(), menu -> {
            if (menu.size() > 0) {
                for (FullMenuItem menuItem: menu) {
                    String itemMenuName = menuItem.getDish().getName();
                    if (this.selectedMenu.containsKey(itemMenuName)) {
                        selectedMenu.put(itemMenuName, this.selectedMenu.get(itemMenuName) + 1);
                    } else {
                        this.selectedMenu.put(itemMenuName,1);
                    }
                    forPayment += menuItem.getPrice();
                }

                for(Map.Entry<String, Integer> item : this.selectedMenu.entrySet()){
                    addTextInTextView(ordersFragment.findViewById(R.id.listMenu),"- " + item.getKey() + "     x" + item.getValue() + "\n");
                }

                setTextInTextView(ordersFragment.findViewById(R.id.totalPaymentDue), Integer.toString(forPayment));
            }
        });
    }

    private void setTextInTextView(TextView textView, String text) {
        textView.setText(text);
    }

    private void addTextInTextView(TextView textView, String text) {
        textView.setText(textView.getText() + text);
    }
}
