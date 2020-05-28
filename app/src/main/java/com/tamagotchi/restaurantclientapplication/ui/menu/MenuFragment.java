package com.tamagotchi.restaurantclientapplication.ui.menu;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.tamagotchi.restaurantclientapplication.R;
import com.tamagotchi.restaurantclientapplication.data.Result;
import com.tamagotchi.restaurantclientapplication.data.model.FullMenuItem;
import com.tamagotchi.restaurantclientapplication.data.repositories.FilesRepository;
import com.tamagotchi.restaurantclientapplication.services.ImageRedactor;
import com.tamagotchi.restaurantclientapplication.ui.main.MainViewModel;
import com.tamagotchi.restaurantclientapplication.ui.main.MainViewModelFactory;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MenuFragment extends Fragment {

    private static final String TAG = "MenuFragment";
    private View viewMenuFragment;
    private MainViewModel viewModel;
    private ListView listView;

    private FilesRepository filesRepository;
    private List<FullMenuItem> fullMenuItems;

    private Drawable emptyDishImage;

    private CompositeDisposable listImagesDownloadSubscribers;
    private MenuItemsAdapter menuItemsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this, new MainViewModelFactory()).get(MainViewModel.class);
        viewMenuFragment = inflater.inflate(R.layout.fragment_menu, container, false);
        listView = viewMenuFragment.findViewById(R.id.listViewMenu);

        emptyDishImage = ContextCompat.getDrawable(viewMenuFragment.getContext(), R.drawable.ic_photo);
        menuItemsAdapter = new MenuItemsAdapter();
        listImagesDownloadSubscribers = new CompositeDisposable();

        if (viewModel.getSelectedRestaurant().getValue() != null) {
            initListView();
        } else {
            throw new RuntimeException("Restaurant must be selected.");
        }

        return viewMenuFragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        listImagesDownloadSubscribers.dispose();
    }

    private void initListView() {
        filesRepository = FilesRepository.getInstance();

        viewModel.getSelectedRestaurantMenu().observe(getViewLifecycleOwner(), result -> {
            if (result instanceof Result.Success) {

                fullMenuItems = (List<FullMenuItem>) ((Result.Success) result).getData();
                listView.setAdapter(menuItemsAdapter);
            } else {
                // TODO: обработка ошибки
            }
        });
    }

    private class MenuItemsAdapter extends BaseAdapter {
        public void refresh() {
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return fullMenuItems.size();
        }

        @Override
        public Object getItem(int position) {
            return fullMenuItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return fullMenuItems.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View menuItem = getLayoutInflater().inflate(R.layout.menu_item, null);

            FullMenuItem fullMenuItem = fullMenuItems.get(position);

            ImageView photo = menuItem.findViewById(R.id.photoItemMenu);
            TextView name = menuItem.findViewById(R.id.dishName);
            TextView price = menuItem.findViewById(R.id.dishPrice);
            TextView count = menuItem.findViewById(R.id.countItemMenu);
            TextView description = menuItem.findViewById(R.id.dishDescription);

            photo.setImageDrawable(emptyDishImage);

            Disposable subscriber = filesRepository.getImageById(fullMenuItem.getDish().getPhotos().get(0))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(image -> {
                        photo.setImageBitmap(ImageRedactor.getRoundedCornerBitmap(image, 50));
                    }, error -> {
                    });

            listImagesDownloadSubscribers.add(subscriber);

            name.setText(fullMenuItems.get(position).getDish().getName());
            price.setText(Integer.toString(fullMenuItem.getPrice()));

            //TODO: Возможно не красиво, подумать!
            int itemCount = 0;
            for (FullMenuItem item: viewModel.getUserMenu().getValue()) {
                if (item.getId() == fullMenuItem.getId()) {
                    itemCount += 1;
                }
            }
            count.setText(Integer.toString(itemCount));

            if (fullMenuItem.getDish().getDescription().equals("")) {
                description.setHeight(0);
            } else {
                description.setText(fullMenuItem.getDish().getDescription());
            }

            //TODO: Возможно не красиво, подумать!
            AppCompatImageButton add = menuItem.findViewById(R.id.addItemMenu);
            add.setOnClickListener((view) -> {
                viewModel.addToUserMenu(fullMenuItem);
                count.setText(Integer.toString(Integer.parseInt(count.getText().toString()) + 1));
                });

            AppCompatImageButton remove = menuItem.findViewById(R.id.removeItemMenu);
            remove.setOnClickListener((view) -> {
                int numberOfDishes = Integer.parseInt(count.getText().toString()) - 1;

                if (numberOfDishes < 0) {
                    Toast.makeText(requireContext(), "Cannot be less than 0", Toast.LENGTH_SHORT).show();
                } else {
                    viewModel.removeFromUserMenu(fullMenuItem);
                    count.setText(Integer.toString(numberOfDishes));
                }
            });

            return menuItem;
        }
    }
}


