package com.tamagotchi.restaurantclientapplication.ui.slidingpanel;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tamagotchi.restaurantclientapplication.R;

public class SlidingPanelFragment extends Fragment {

    public static SlidingPanelFragment newInstance() {
        return new SlidingPanelFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sliding_panel_fragment, container, false);
    }
}
