package com.tamagotchi.restaurantclientapplication.ui.slidingpanel;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

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

    public static SlidingPanelFragment newInstance() {
        return new SlidingPanelFragment();
    }
    private MainViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this, new MainViewModelFactory()).get(MainViewModel.class);

        return inflater.inflate(R.layout.sliding_panel_fragment, container, false);
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
