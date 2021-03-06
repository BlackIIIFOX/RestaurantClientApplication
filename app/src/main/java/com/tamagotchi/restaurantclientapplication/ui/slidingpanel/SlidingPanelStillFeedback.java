package com.tamagotchi.restaurantclientapplication.ui.slidingpanel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tamagotchi.restaurantclientapplication.R;
import com.tamagotchi.restaurantclientapplication.ui.main.MainViewModel;
import com.tamagotchi.restaurantclientapplication.ui.main.MainViewModelFactory;

public class SlidingPanelStillFeedback extends BottomSheetDialogFragment {

    private static final String TAG = "SlidingPanelStillFeedback";

    private MainViewModel viewModel;
    private View slidingPanelStillFeedback;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this, new MainViewModelFactory()).get(MainViewModel.class);
        slidingPanelStillFeedback = inflater.inflate(R.layout.activity_still_feedback, container, false);

        sendFeedback();

        return slidingPanelStillFeedback;
    }

    private void sendFeedback() {
        Button button = slidingPanelStillFeedback.findViewById(R.id.sendFeedbackButton);
        button.setOnClickListener((view) -> {
            EditText editText = slidingPanelStillFeedback.findViewById(R.id.feedbackEditText);
            if (!editText.getText().toString().equals("")) {
                viewModel.sendFeedback(editText.getText().toString());
                this.dismiss();
            }
        });
    }
}
