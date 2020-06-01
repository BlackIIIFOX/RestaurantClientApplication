package com.tamagotchi.restaurantclientapplication.ui.still;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tamagotchi.restaurantclientapplication.R;
import com.tamagotchi.restaurantclientapplication.data.model.OrderVisitInfo;
import com.tamagotchi.restaurantclientapplication.ui.main.MainActivity;
import com.tamagotchi.restaurantclientapplication.ui.main.MainViewModel;
import com.tamagotchi.restaurantclientapplication.ui.main.MainViewModelFactory;
import com.tamagotchi.restaurantclientapplication.ui.main.Navigation;
import com.tamagotchi.restaurantclientapplication.ui.slidingpanel.SlidingPanelRestaurants;
import com.tamagotchi.restaurantclientapplication.ui.slidingpanel.SlidingPanelStillFeedback;
import com.tamagotchi.restaurantclientapplication.ui.start.StartActivity;

import androidx.annotation.Nullable;

public class StillFragment extends Fragment {

    private static final String TAG = "StillFragment";

    private View stillFragment;
    private MainViewModel viewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this, new MainViewModelFactory()).get(MainViewModel.class);
        stillFragment = inflater.inflate(R.layout.fragment_still, container, false);

        initButtons();
        initListOrders();

        return stillFragment;
    }

    private void initButtons() {
        initButtonExit();
        initButtonEditAboutMe();
        initButtonFeedback();
    }

    //TODO: Проблемы с аутентификацией, нужно править
    private void initButtonExit() {
        AppCompatImageButton button = stillFragment.findViewById(R.id.appExitButton);
        button.setOnClickListener((view) -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
            builder.setTitle(R.string.logOut)
                    .setMessage(R.string.logOutConfirm)
                    .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                        viewModel.logOut();
                    })
                    .setNegativeButton(R.string.no, (dialogInterface, i) -> {
                        // Ничего не делаем
                    })
                    .show();
        });
    }

    private void initButtonEditAboutMe() {

    }

    private void initButtonFeedback() {
        Button button = stillFragment.findViewById(R.id.feedbackButton);
        button.setOnClickListener((view) -> {
            SlidingPanelStillFeedback slidingPanelStillFeedback = new SlidingPanelStillFeedback();
            slidingPanelStillFeedback.show(requireActivity().getSupportFragmentManager(), TAG);
        });
    }

    private void initListOrders() {

    }
}
