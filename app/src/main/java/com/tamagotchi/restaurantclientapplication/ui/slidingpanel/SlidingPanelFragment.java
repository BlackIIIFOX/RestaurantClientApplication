package com.tamagotchi.restaurantclientapplication.ui.slidingpanel;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelStoreOwner;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tamagotchi.restaurantclientapplication.R;
import com.tamagotchi.restaurantclientapplication.data.model.OrderVisitInfo;
import com.tamagotchi.restaurantclientapplication.ui.main.MainViewModel;
import com.tamagotchi.restaurantclientapplication.ui.main.MainViewModelFactory;
import com.tamagotchi.restaurantclientapplication.ui.main.Navigation;

import java.util.Calendar;

public class SlidingPanelFragment extends BottomSheetDialogFragment {

    private static final String TAG = "SlidingPanelFragment";
    private static SlidingPanelFragment slidingPanelFragment;
    private static SimpleDateFormat dataFormat = new SimpleDateFormat("dd.MM.yyyy");
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("kk:mm");
    private static Calendar calendar;
    private MainViewModel viewModel;
    private View viewSlidingPanel;

    public static SlidingPanelFragment newInstance() {
        if (slidingPanelFragment != null) {
            return slidingPanelFragment;
        } else {
            slidingPanelFragment = new SlidingPanelFragment();
            return slidingPanelFragment;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider((ViewModelStoreOwner) this, new MainViewModelFactory()).get(MainViewModel.class);
        viewSlidingPanel = inflater.inflate(R.layout.fragment_sliding_panel, container, false);

        initVisitInfo();
        initSelectedRestaurant();
        initNextStep();
        updateVisitInfo();

        return viewSlidingPanel;
    }

    private void initVisitInfo() {
        viewModel.getOrderVisitInfo().observe(getViewLifecycleOwner(), orderVisitInfo -> {
            setTextInTextView(viewSlidingPanel.findViewById(R.id.countGuest), String.valueOf(orderVisitInfo.getNumberOfVisitors()));
            calendar = orderVisitInfo.getVisitTime();
            setTextInTextView(viewSlidingPanel.findViewById(R.id.textVisitDate), dataFormat.format(calendar.getTime()));
            setTextInTextView(viewSlidingPanel.findViewById(R.id.textVisitTime), timeFormat.format(calendar.getTime()));
        });
    }

    private void setInitialDateTime() {
        setTextInTextView(viewSlidingPanel.findViewById(R.id.textVisitDate), dataFormat.format(calendar.getTime()));
        setTextInTextView(viewSlidingPanel.findViewById(R.id.textVisitTime), timeFormat.format(calendar.getTime()));
        OrderVisitInfo orderVisitInfo = viewModel.getOrderVisitInfo().getValue();
        orderVisitInfo.setVisitTime(calendar);
        viewModel.setOrderVisitInfo(orderVisitInfo);
    }

    private void initSelectedRestaurant() {
        viewModel.getSelectedRestaurant().observe(getViewLifecycleOwner(), restaurant -> {
            setTextInTextView(viewSlidingPanel.findViewById(R.id.restaurantAddress), restaurant.getAddress());
            setRestaurantPresentParam(viewSlidingPanel.findViewById(R.id.carParking), restaurant.getCardPaymentPresent());
            setRestaurantPresentParam(viewSlidingPanel.findViewById(R.id.wifi), restaurant.getWifiPresent());
            setRestaurantPresentParam(viewSlidingPanel.findViewById(R.id.creditCard), restaurant.getParkingPresent());
        });
    }

    private void setTextInTextView(TextView textView, String text) {
        textView.setText(text);
    }

    private void setRestaurantPresentParam(ImageView imageView, boolean paramStatus) {
        if (paramStatus) {
            DrawableCompat.setTint(imageView.getDrawable(), ContextCompat.getColor(imageView.getContext(), R.color.colorSelectItem));
        } else {
            DrawableCompat.setTint(imageView.getDrawable(), ContextCompat.getColor(imageView.getContext(), R.color.colorUnSelectItem));
        }
    }

    private void initNextStep() {
        Button makeOrder = viewSlidingPanel.findViewById(R.id.buttonMakeOrder);
        makeOrder.setOnClickListener((view) -> {
            viewModel.setSelectedNavigation(Navigation.Menu);
            slidingPanelFragment.dismiss();
        });
    }

    private void updateVisitInfo() {
        updateVisitGuests();
        updateVisitDate();
        updateVisitTime();
    }

    private void updateVisitGuests() {
        addGuest();
        removeGuest();
    }

    private void addGuest() {
        AppCompatImageButton button = viewSlidingPanel.findViewById(R.id.addGuest);
        button.setOnClickListener((view) -> {
            OrderVisitInfo orderVisitInfo = viewModel.getOrderVisitInfo().getValue();
            int numberOfVisitors = orderVisitInfo.getNumberOfVisitors() + 1;
            orderVisitInfo.setNumberOfVisitors(numberOfVisitors);
            setTextInTextView(viewSlidingPanel.findViewById(R.id.countGuest), String.valueOf(numberOfVisitors));
        });

    }

    private void removeGuest() {
        AppCompatImageButton button = viewSlidingPanel.findViewById(R.id.removeGuest);
        button.setOnClickListener((view) -> {
            OrderVisitInfo orderVisitInfo = viewModel.getOrderVisitInfo().getValue();
            int numberOfVisitors = orderVisitInfo.getNumberOfVisitors() - 1;
            if (numberOfVisitors < 1) {
                Toast.makeText(requireContext(),"Cannot be less than 1", Toast.LENGTH_SHORT).show();
            } else {
                orderVisitInfo.setNumberOfVisitors(numberOfVisitors);
                setTextInTextView(viewSlidingPanel.findViewById(R.id.countGuest), String.valueOf(numberOfVisitors));
            }
        });
    }

    private void updateVisitDate() {
        AppCompatImageButton button = viewSlidingPanel.findViewById(R.id.buttonVisitDate);
        button.setOnClickListener((view) -> {
            new DatePickerDialog(requireContext(), d, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void updateVisitTime() {
        AppCompatImageButton button = viewSlidingPanel.findViewById(R.id.buttonVisitTime);
        button.setOnClickListener((view) -> {
            new TimePickerDialog(requireContext(), t, calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE), true).show();
        });
    }

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar tmpVisitTime = Calendar.getInstance();
            Calendar tmpTime = calendar;

            tmpVisitTime.add(Calendar.MINUTE, 30);

            tmpTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            tmpTime.set(Calendar.MINUTE, minute);

            if (tmpVisitTime.before(tmpTime)) {
                calendar = tmpTime;
                setInitialDateTime();
            } else {
                Toast.makeText(requireContext(),"Too early...", Toast.LENGTH_SHORT).show();
            }
        }
    };

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar tmpVisitTime = Calendar.getInstance();
            Calendar tmpDate = calendar;

            tmpDate.set(Calendar.YEAR, year);
            tmpDate.set(Calendar.MONTH, monthOfYear);
            tmpDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);


            if (!tmpVisitTime.after(tmpDate)) {
                calendar = tmpDate;
                setInitialDateTime();
            } else {
                Toast.makeText(requireContext(),"Too early...", Toast.LENGTH_SHORT).show();
                Log.d(TAG, tmpVisitTime.toString());
                Log.d(TAG, tmpDate.toString());
                Log.d(TAG, calendar.toString());
            }
        }
    };
}