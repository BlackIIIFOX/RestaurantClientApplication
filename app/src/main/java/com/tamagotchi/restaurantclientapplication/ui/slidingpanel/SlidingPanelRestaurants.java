package com.tamagotchi.restaurantclientapplication.ui.slidingpanel;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tamagotchi.restaurantclientapplication.R;
import com.tamagotchi.restaurantclientapplication.data.model.OrderVisitInfo;
import com.tamagotchi.restaurantclientapplication.data.repositories.FilesRepository;
import com.tamagotchi.restaurantclientapplication.ui.main.MainViewModel;
import com.tamagotchi.restaurantclientapplication.ui.main.MainViewModelFactory;
import com.tamagotchi.restaurantclientapplication.ui.main.Navigation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SlidingPanelRestaurants extends BottomSheetDialogFragment {

    private static final String TAG = "SlidingPanelRestaurants";

    private static SimpleDateFormat dataFormat = new SimpleDateFormat("dd.MM.yyyy");
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("kk:mm");
    private static Calendar calendar;
    private MainViewModel viewModel;
    private View viewSlidingPanel;
    private CompositeDisposable listImagesDownloadSubscribers  = new CompositeDisposable();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this, new MainViewModelFactory()).get(MainViewModel.class);
        viewSlidingPanel = inflater.inflate(R.layout.fragment_sliding_panel, container, false);

        initVisitInfo();
        initSelectedRestaurant();
        initNextStep();
        updateVisitInfo();

        return viewSlidingPanel;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        listImagesDownloadSubscribers.dispose();
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
            //TODO: Сделано не очень красиво, но пока как решение пойдет
            FilesRepository filesRepository = FilesRepository.getInstance();

            LinearLayout galleryItemLayout = viewSlidingPanel.findViewById(R.id.galleryItem);
            int photoHeight = viewSlidingPanel.findViewById(R.id.RestaurantGallery).getLayoutParams().height;
            int photoWidth = (int) (photoHeight * 1.25);

            for (Integer photoId : restaurant.getPhotos()) {
                Disposable subscriber = filesRepository.getImageById(photoId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(image -> {
                            ImageView imageView = new ImageView(requireContext());
                            imageView.setId(photoId);
                            imageView.setPadding(5, 5, 5, 5);
                            imageView.setLayoutParams(new ViewGroup.LayoutParams(photoWidth,photoHeight));
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            imageView.setImageBitmap(image);
                            galleryItemLayout.addView(imageView);
                        }, error -> {
                            ImageView imageView = new ImageView(requireContext());
                            imageView.setId(photoId);
                            imageView.setPadding(5, 5, 5, 5);
                            imageView.setLayoutParams(new ViewGroup.LayoutParams(photoWidth,photoHeight));
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_photo));
                            galleryItemLayout.addView(imageView);
                        });

                listImagesDownloadSubscribers.add(subscriber);
            }

            if (restaurant.getPhotos().size() == 0) {
                HorizontalScrollView horizontalScrollView = viewSlidingPanel.findViewById(R.id.RestaurantGallery);
                HorizontalScrollView.LayoutParams params = (HorizontalScrollView.LayoutParams) horizontalScrollView.getLayoutParams();
                params.height = 0;
                horizontalScrollView.setLayoutParams(params);
            }

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
            this.dismiss();
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
                Toast.makeText(requireContext(), "Cannot be less than 1", Toast.LENGTH_SHORT).show();
            } else {
                orderVisitInfo.setNumberOfVisitors(numberOfVisitors);
                setTextInTextView(viewSlidingPanel.findViewById(R.id.countGuest), String.valueOf(numberOfVisitors));
            }
        });
    }

    private void updateVisitDate() {
        AppCompatImageButton datePickerDialogButton = viewSlidingPanel.findViewById(R.id.buttonVisitDate);

        datePickerDialogButton.setOnClickListener(view -> {
            DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar tmpVisitTime = Calendar.getInstance();
                    Calendar tmpDate = calendar;

                    view.setMinDate(calendar.getTimeInMillis());

                    tmpDate.set(Calendar.YEAR, year);
                    tmpDate.set(Calendar.MONTH, monthOfYear);
                    tmpDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                    if (!tmpVisitTime.after(tmpDate)) {
                        calendar = tmpDate;
                        setInitialDateTime();
                    } else {
                        Toast.makeText(requireContext(), "Too early...", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            Calendar tmpVisitTime = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), onDateSetListener, tmpVisitTime.get(Calendar.YEAR), tmpVisitTime.get(Calendar.MONTH), tmpVisitTime.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(tmpVisitTime.getTimeInMillis());
            datePickerDialog.show();
        });
    }

    private void updateVisitTime() {
        AppCompatImageButton timePickerDialogButton = viewSlidingPanel.findViewById(R.id.buttonVisitTime);

        timePickerDialogButton.setOnClickListener(view -> {
            TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
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
                        Toast.makeText(requireContext(), "Too early...", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), onTimeSetListener, calendar.getTime().getHours(), calendar.getTime().getMinutes(), true);
            timePickerDialog.show();
        });
    }
}