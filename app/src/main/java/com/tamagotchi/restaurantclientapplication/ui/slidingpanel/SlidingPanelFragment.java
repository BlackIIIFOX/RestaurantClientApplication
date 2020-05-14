package com.tamagotchi.restaurantclientapplication.ui.slidingpanel;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelStoreOwner;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tamagotchi.restaurantclientapplication.R;
import com.tamagotchi.restaurantclientapplication.ui.main.MainViewModel;
import com.tamagotchi.restaurantclientapplication.ui.main.MainViewModelFactory;
import com.tamagotchi.restaurantclientapplication.ui.main.Navigation;

public class SlidingPanelFragment extends BottomSheetDialogFragment {

    private static final String TAG = "SlidingPanelFragment";
    private MainViewModel viewModel;
    private View viewSlidingPanel;

    public static SlidingPanelFragment newInstance() {
        return new SlidingPanelFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider((ViewModelStoreOwner) this, new MainViewModelFactory()).get(MainViewModel.class);
        viewSlidingPanel = inflater.inflate(R.layout.fragment_sliding_panel, container, false);

        initVisitInfo();
        initSelectedRestaurant();
        initNextStep();

        return viewSlidingPanel;
    }

    private void initNextStep() {
        Button makeOrder = viewSlidingPanel.findViewById(R.id.buttonMakeOrder);
        makeOrder.setOnClickListener((view) -> {
            viewModel.setSelectedNavigation(Navigation.Menu);
        });

    }

    private void initVisitInfo() {
        viewModel.getOrderVisitInfo().observe(getViewLifecycleOwner(), orderVisitInfo -> {

            Log.d(TAG, String.valueOf(orderVisitInfo.getNumberOfVisitors()));

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

            Log.d(TAG, restaurant.getAddress());

            //Показываем пользователю какие блага есть в ресторани (там парковочка бесплатная, wi-fi, оплата картой)
            if (restaurant.getCardPaymentPresent()) {
                Log.d(TAG, String.valueOf(restaurant.getCardPaymentPresent()));
                setRestaurantPresentParam(viewSlidingPanel.findViewById(R.id.carParking));
            }

            if (restaurant.getWifiPresent()) {
                Log.d(TAG, String.valueOf(restaurant.getWifiPresent()));
                setRestaurantPresentParam(viewSlidingPanel.findViewById(R.id.wifi));
            }

            if (restaurant.getParkingPresent()) {
                Log.d(TAG, String.valueOf(restaurant.getParkingPresent()));
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