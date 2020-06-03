package com.tamagotchi.restaurantclientapplication.ui.still;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tamagotchi.restaurantclientapplication.R;
import com.tamagotchi.restaurantclientapplication.ui.main.MainViewModel;
import com.tamagotchi.restaurantclientapplication.ui.main.MainViewModelFactory;
import com.tamagotchi.restaurantclientapplication.ui.slidingpanel.SlidingPanelStillAboutMe;
import com.tamagotchi.restaurantclientapplication.ui.slidingpanel.SlidingPanelStillFeedback;
import com.tamagotchi.tamagotchiserverprotocol.models.OrderModel;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class StillFragment extends Fragment {

    private static final String TAG = "StillFragment";

    private View stillFragment;
    private MainViewModel viewModel;
    private ListView ordersListView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this, new MainViewModelFactory()).get(MainViewModel.class);
        stillFragment = inflater.inflate(R.layout.fragment_still, container, false);

        initUser();
        initButtons();
        initListOrders();

        return stillFragment;
    }

    private void initUser() {
        TextView textView = stillFragment.findViewById(R.id.userNameView);
        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                if (user.getFullName() != null) {
                    textView.setText(user.getFullName());
                } else if (user.getLogin() != null) {
                    textView.setText(user.getLogin());
                }
            }
        });
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
        AppCompatImageButton button = stillFragment.findViewById(R.id.profileEditButton);
        button.setOnClickListener((view) -> {
            SlidingPanelStillAboutMe SlidingPanelStillAboutMe = new SlidingPanelStillAboutMe();
            SlidingPanelStillAboutMe.show(requireActivity().getSupportFragmentManager(), TAG);
        });
    }

    private void initButtonFeedback() {
        Button button = stillFragment.findViewById(R.id.feedbackButton);
        button.setOnClickListener((view) -> {
            SlidingPanelStillFeedback slidingPanelStillFeedback = new SlidingPanelStillFeedback();
            slidingPanelStillFeedback.show(requireActivity().getSupportFragmentManager(), TAG);
        });
    }

    private void initListOrders() {
        ordersListView = stillFragment.findViewById(R.id.userOrdersList);
        initOrdersSubscribe();
    }

    private void initOrdersSubscribe() {
        viewModel.getUserOrders().observe(getViewLifecycleOwner(),this::initListViewAdapter);
        viewModel.refreshOrders();
    }

    private void initListViewAdapter(List<OrderModel> orders) {
        ordersListView.setAdapter(new OrdersAdapterListView(this.getContext(), new ArrayList<>(orders)));
    }
}
