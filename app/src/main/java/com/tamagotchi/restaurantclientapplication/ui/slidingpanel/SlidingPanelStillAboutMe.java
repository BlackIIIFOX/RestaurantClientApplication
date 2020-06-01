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

public class SlidingPanelStillAboutMe extends BottomSheetDialogFragment {
    private static final String TAG = "SlidingPanelStillAboutMe";

    private MainViewModel viewModel;
    private View slidingPanelStillAboutMe;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this, new MainViewModelFactory()).get(MainViewModel.class);
        slidingPanelStillAboutMe = inflater.inflate(R.layout.activity_still_edit_about_me, container, false);

        setName();
        editName();

        return slidingPanelStillAboutMe;
    }

    private void setName() {
        EditText editText = slidingPanelStillAboutMe.findViewById(R.id.editTextUserName);
        String userName = viewModel.getUser().getValue().getFullName();
        if (userName != null) {
            editText.setText(userName);
        }
    }

    private void editName() {
        Button button = slidingPanelStillAboutMe.findViewById(R.id.editNameButton);
        button.setOnClickListener((view) -> {
            EditText editText = slidingPanelStillAboutMe.findViewById(R.id.editTextUserName);
            if (!editText.getText().toString().equals("")) {
                viewModel.setUserName(editText.getText().toString());
                viewModel.refreshCurrentUser();
                this.dismiss();
            }
        });
    }
}
