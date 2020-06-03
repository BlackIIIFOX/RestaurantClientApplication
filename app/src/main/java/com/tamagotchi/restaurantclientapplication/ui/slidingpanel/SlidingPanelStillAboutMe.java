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
import com.tamagotchi.tamagotchiserverprotocol.models.UserModel;

public class SlidingPanelStillAboutMe extends BottomSheetDialogFragment {
    private static final String TAG = "SlidingPanelStillAboutMe";

    private MainViewModel viewModel;
    private View slidingPanelStillAboutMe;
    private EditText editTextName;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this, new MainViewModelFactory()).get(MainViewModel.class);
        slidingPanelStillAboutMe = inflater.inflate(R.layout.activity_still_edit_about_me, container, false);
        editTextName = slidingPanelStillAboutMe.findViewById(R.id.editTextUserName);

        setName();
        editName();

        return slidingPanelStillAboutMe;
    }

    private void setName() {
        UserModel user = viewModel.getUser().getValue();
        if (user != null) {
            if (user.getFullName() != null) {
                editTextName.setText(user.getFullName());
            }
        }
    }

    private void editName() {
        Button button = slidingPanelStillAboutMe.findViewById(R.id.editNameButton);
        button.setOnClickListener((view) -> {
            if (!editTextName.getText().toString().equals("")) {
                viewModel.setUserName(editTextName.getText().toString());
                this.dismiss();
            }
        });
    }
}
