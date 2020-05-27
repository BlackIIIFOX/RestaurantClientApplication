package com.tamagotchi.restaurantclientapplication.ui.orders;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.tamagotchi.restaurantclientapplication.R;
import com.tamagotchi.restaurantclientapplication.ui.main.MainViewModel;
import com.tamagotchi.restaurantclientapplication.ui.main.MainViewModelFactory;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashSet;

import ru.yandex.money.android.sdk.Amount;
import ru.yandex.money.android.sdk.Checkout;
import ru.yandex.money.android.sdk.GooglePayCardNetwork;
import ru.yandex.money.android.sdk.MockConfiguration;
import ru.yandex.money.android.sdk.PaymentMethodType;
import ru.yandex.money.android.sdk.PaymentParameters;
import ru.yandex.money.android.sdk.SavePaymentMethod;
import ru.yandex.money.android.sdk.TestParameters;

public class OrdersFragment extends Fragment {

    private MainViewModel viewModel;
    private View root;
    public static final int REQUEST_CODE_TOKENIZE = 15462;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this, new MainViewModelFactory()).get(MainViewModel.class);
        root = inflater.inflate(R.layout.fragment_orders, container, false);

        initPaymentButton();
        return root;
    }

    private void initPaymentButton() {
        Button makePayment = root.findViewById(R.id.buttonPayment);
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

        PaymentParameters paymentParameters = new PaymentParameters(
                new Amount(new BigDecimal(3245), Currency.getInstance("RUB")),
                "Заказ в Tamagotchi",
                "1. Свинина Дэ-Лафе, 2. Летучая мышь по китайски, 3. Собака по корейски, 4. 8 лет по Японски",
                "live_AAAAAAAAAAAAAAAAAAAA",
                "12345",
                SavePaymentMethod.ON,
                paymentMethodTypes,
                null,
                null,
                "+79991452927"
        );
        paymentParameters.paymentMethodTypes.remove(PaymentMethodType.YANDEX_MONEY);

        TestParameters testParameters = new TestParameters(true, true,
                new MockConfiguration(false, true, 5));

        Intent intent = Checkout.createTokenizeIntent(getContext(), paymentParameters, testParameters);
        getActivity().startActivityForResult(intent, REQUEST_CODE_TOKENIZE);
    }
}
