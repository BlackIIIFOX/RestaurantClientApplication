package com.tamagotchi.restaurantclientapplication.ui.still;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tamagotchi.restaurantclientapplication.R;
import com.tamagotchi.restaurantclientapplication.services.OrderStatusToStringConverter;
import com.tamagotchi.restaurantclientapplication.services.RfcToCalendarConverter;
import com.tamagotchi.tamagotchiserverprotocol.models.OrderModel;
import com.tamagotchi.tamagotchiserverprotocol.models.enums.StaffStatus;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

public class OrdersAdapterListView extends ArrayAdapter<OrderModel> {

    private String TAG = "OrdersAdapterListView";

    private OrderStatusToStringConverter statusConverter;
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("dd.MM.yyyy kk:mm");
    private static final String textError = "???";

    public OrdersAdapterListView(Context context, ArrayList<OrderModel> orders) {
        super(context, R.layout.still_item, orders);
        statusConverter = new OrderStatusToStringConverter(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        OrderModel order = getItem(position);
        if (order == null)
            throw new RuntimeException("Can not be null");

        FullOrderHolder viewHolder; // view lookup cache stored in tag
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            viewHolder = new FullOrderHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.still_item, parent, false);
            viewHolder.state = convertView.findViewById(R.id.orderState);
            viewHolder.id = convertView.findViewById(R.id.orderId);
            viewHolder.time = convertView.findViewById(R.id.orderTime);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FullOrderHolder) convertView.getTag();
        }

        // Populate the data from the data object via the viewHolder object
        // into the template view.
        String orderStatusString = order.getOrderStatus() != null ?
                statusConverter.convert(order.getOrderStatus()) : textError;

        if (order.getOrderCooksStatus() == StaffStatus.Notified && order.getOrderWaitersStatus() == StaffStatus.Notified) {
            orderStatusString += " (" + getContext().getResources().getString(R.string.staff_notified_status) + ")";
        }

        viewHolder.state.setText(orderStatusString);
        viewHolder.id.setText(order.getId() != null ? order.getId().toString() : textError);

        String timeString = textError;
        try {
            Calendar startTime = RfcToCalendarConverter.convert(order.getVisitTime().getStart());
            timeString = timeFormat.format(startTime.getTime());
        } catch (IllegalArgumentException | ParseException ex) {
            Log.e(TAG, "Can't convert visit time", ex);
        }

        viewHolder.time.setText(timeString);

        // Return the completed view to render on screen
        return convertView;
    }

    // View lookup cache
    private static class FullOrderHolder {
        TextView id;
        TextView state;
        TextView time;
    }
}
