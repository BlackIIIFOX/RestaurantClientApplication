package com.tamagotchi.restaurantclientapplication.ui.slidingpanel;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tamagotchi.restaurantclientapplication.R;
import com.tamagotchi.restaurantclientapplication.ui.main.MainViewModel;
import com.tamagotchi.restaurantclientapplication.ui.main.MainViewModelFactory;

public class SlidingPanelFragment extends Fragment {

    private MainViewModel viewModel;
    private View viewSlidingPanel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this, new MainViewModelFactory()).get(MainViewModel.class);
        viewSlidingPanel = inflater.inflate(R.layout.fragment_sliding_panel, container, false);

        initVisitInfo();
        initSelectedRestaurant();

        return viewSlidingPanel;
    }

    private void initVisitInfo() {
        viewModel.getOrderVisitInfo().observe(getViewLifecycleOwner(), orderVisitInfo -> {
            setTextInTextView(viewSlidingPanel.findViewById(R.id.restaurantAddress), String.valueOf(orderVisitInfo.getNumberOfVisitors()));

            //TODO: Надежды нет, везде добавить проверку и потом добавить дату и время
        });
    }

    /**
     * Получаем текущий ресторан из ViewModel.
     */
    private void initSelectedRestaurant() {
        viewModel.getSelectedRestaurant().observe(getViewLifecycleOwner(), restaurant -> {
            // Вот мы его и получили, дальше можно с ним работать.
            setTextInTextView(viewSlidingPanel.findViewById(R.id.restaurantAddress), restaurant.getAddress());

            //Показываем пользователю какие блага есть в ресторани (там парковочка бесплатная, wi-fi, оплата картой)
            if (restaurant.getCardPaymentPresent()) {
                setRestaurantPresentParam(viewSlidingPanel.findViewById(R.id.carParking));
            }

            if (restaurant.getWifiPresent()) {
                setRestaurantPresentParam(viewSlidingPanel.findViewById(R.id.wifi));
            }

            if (restaurant.getParkingPresent()) {
                setRestaurantPresentParam(viewSlidingPanel.findViewById(R.id.creditCard));
            }
        });
    }

    private void setTextInTextView(TextView textView, String text) {
        textView.setText(text);
    }

    private void setRestaurantPresentParam(ImageView imageView) {
        DrawableCompat.setTint(imageView.getDrawable(), ContextCompat.getColor(imageView.getContext(), R.color.colorSelectItem));
    }
}