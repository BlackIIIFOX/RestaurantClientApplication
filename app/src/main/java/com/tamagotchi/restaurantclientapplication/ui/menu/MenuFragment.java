package com.tamagotchi.restaurantclientapplication.ui.menu;

import android.os.Bundle;
import android.telephony.mbms.MbmsErrors;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.tamagotchi.restaurantclientapplication.R;
import com.tamagotchi.restaurantclientapplication.ui.main.MainViewModel;
import com.tamagotchi.restaurantclientapplication.ui.main.MainViewModelFactory;

public class MenuFragment extends Fragment {

    private static final String TAG = "MenuFragment";
    private View viewMenuFragment;
    private MainViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this, new MainViewModelFactory()).get(MainViewModel.class);
        viewMenuFragment = inflater.inflate(R.layout.fragment_menu, container, false);

//        viewModel.getSelectedRestaurant().observe(getViewLifecycleOwner(), orderVisitInfo -> {
//
//        });

        return viewMenuFragment;
    }

}
