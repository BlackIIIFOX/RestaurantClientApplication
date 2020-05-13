package com.tamagotchi.restaurantclientapplication.ui.still;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.tamagotchi.restaurantclientapplication.R;
import androidx.annotation.Nullable;

public class StillFragment extends Fragment {

    private StillViewModel stillViewModel;

    public static StillFragment newInstance() {
        return new StillFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        stillViewModel = new ViewModelProvider(this).get(StillViewModel.class);

        return inflater.inflate(R.layout.fragment_still, container, false);
    }
}
