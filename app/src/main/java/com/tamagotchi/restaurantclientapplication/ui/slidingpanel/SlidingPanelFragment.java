package com.tamagotchi.restaurantclientapplication.ui.slidingpanel;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tamagotchi.restaurantclientapplication.R;
import com.tamagotchi.restaurantclientapplication.ui.main.MainViewModel;
import com.tamagotchi.restaurantclientapplication.ui.main.MainViewModelFactory;

public class SlidingPanelFragment extends Fragment {

    private MainViewModel viewModel;
    private View viewSlidinganel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this, new MainViewModelFactory()).get(MainViewModel.class);

        viewSlidinganel = inflater.inflate(R.layout.fragment_sliding_panel, container, false);

//        final TextView textView = root.findViewById(R.id.text_menu);
//
//        viewSlidinganel.findViewById(R.id.buttonMakeOrder).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        return viewSlidinganel;
    }

    /**
     * Получаем текущий ресторан из ViewModel.
     */
    public void InitSelectedRestaurant() {
        viewModel.getSelectedRestaurant().observe(this, restaurant -> {
            // Вот мы его и получили, дальше можно с ним работать.

        });
    }
}