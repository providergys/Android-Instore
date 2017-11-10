package com.teaera.teaeracafe.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teaera.teaeracafe.R;
import com.teaera.teaeracafe.net.Model.LocationInfo;
import com.teaera.teaeracafe.net.Model.OrderInfo;
import com.teaera.teaeracafe.preference.LocationPrefs;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by admin on 14/08/2017.
 */

public class NotificationListAdapter extends BaseAdapter {

    private static LayoutInflater inflater=null;
    private Context context;
    private ArrayList<OrderInfo> orders;

    public NotificationListAdapter(Activity activity, ArrayList<OrderInfo> orders) {

        context = activity;
        this.orders = orders;

        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        if (orders == null) {
            return 0;
        }

        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class Holder
    {
        RelativeLayout completedRelativeLayout;
        TextView costTextView;
        TextView orderNumberTextView;
        TextView locationTextView;
        TextView estimateTextView;

        RelativeLayout pendingRelativeLayout;
        TextView orderNumberTextView1;
        TextView orderNumberTextView11;
        TextView locationTextView1;
        TextView estimateTextView1;
        TextView receivedDateTextView;
        ImageView stateImageView;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {

        Holder holder = new Holder();
        View rowView = inflater.inflate(R.layout.notification_list_item, null);

        holder.completedRelativeLayout = (RelativeLayout) rowView.findViewById(R.id.completedRelativeLayout);
        holder.costTextView = (TextView) rowView.findViewById(R.id.costTextView);
        holder.orderNumberTextView = (TextView) rowView.findViewById(R.id.orderNumberTextView);
        holder.locationTextView = (TextView) rowView.findViewById(R.id.locationTextView);
        holder.estimateTextView = (TextView) rowView.findViewById(R.id.estimateTextView);

        holder.pendingRelativeLayout = (RelativeLayout) rowView.findViewById(R.id.pendingRelativeLayout);
        holder.orderNumberTextView1 = (TextView) rowView.findViewById(R.id.orderNumberTextView1);
        holder.orderNumberTextView11 = (TextView) rowView.findViewById(R.id.orderNumberTextView11);
        holder.locationTextView1 = (TextView) rowView.findViewById(R.id.locationTextView1);
        holder.estimateTextView1 = (TextView) rowView.findViewById(R.id.estimateTextView1);
        holder.receivedDateTextView = (TextView) rowView.findViewById(R.id.receivedDateTextView);

        if (orders.get(position).getStatus().equals("0")) {                      // pending
            holder.pendingRelativeLayout.setVisibility(View.VISIBLE);
            holder.completedRelativeLayout.setVisibility(View.GONE);

            holder.orderNumberTextView1.setText(String.format("Order #%s", orders.get(position).getId()));
            holder.orderNumberTextView11.setText(String.format("Order #%s", orders.get(position).getId()));
            holder.locationTextView1.setText(getLocationName(orders.get(position).getLocationId()));
            holder.estimateTextView1.setText("Estimate Pickup Time: " + getEstimatedTime(orders.get(position).getTimeStamp(), orders.get(position).getLocationId()));
            holder.receivedDateTextView.setText(getReceivedDate(orders.get(position).getTimeStamp()));

        } else if (orders.get(position).getStatus().equals("1")) {               // in progress
            holder.pendingRelativeLayout.setVisibility(View.VISIBLE);
            holder.completedRelativeLayout.setVisibility(View.GONE);

            holder.orderNumberTextView1.setText(String.format("Order #%s", orders.get(position).getId()));
            holder.orderNumberTextView11.setText(String.format("Order #%s", orders.get(position).getId()));
            holder.locationTextView1.setText(getLocationName(orders.get(position).getLocationId()));
            holder.estimateTextView1.setText("Estimate Pickup Time: " + getEstimatedTime(orders.get(position).getTimeStamp(), orders.get(position).getLocationId()));
            holder.receivedDateTextView.setText(getReceivedDate(orders.get(position).getTimeStamp()));
        } else if (orders.get(position).getStatus().equals("2"))  {
            holder.pendingRelativeLayout.setVisibility(View.VISIBLE);
            holder.completedRelativeLayout.setVisibility(View.GONE);

            holder.orderNumberTextView1.setText(String.format("Order #%s", orders.get(position).getId()));
            holder.orderNumberTextView11.setText(String.format("Order #%s", orders.get(position).getId()));
            holder.locationTextView1.setText(getLocationName(orders.get(position).getLocationId()));
            holder.estimateTextView1.setText("Estimate Pickup Time: " + getEstimatedTime(orders.get(position).getTimeStamp(), orders.get(position).getLocationId()));
            holder.receivedDateTextView.setText(getReceivedDate(orders.get(position).getTimeStamp()));
        } else {                                                            // completed
            holder.pendingRelativeLayout.setVisibility(View.GONE);
            holder.completedRelativeLayout.setVisibility(View.VISIBLE);

            holder.costTextView.setText(String.format("$%s", orders.get(position).getTotalPrice()));
            holder.orderNumberTextView.setText(String.format("Order #%s", orders.get(position).getId()));
            holder.locationTextView.setText(getLocationName(orders.get(position).getLocationId()));
            holder.estimateTextView1.setText(getReceivedDate(orders.get(position).getTimeStamp()));
        }

        return rowView;
    }

    private String getLocationName(String locationId) {
        ArrayList<LocationInfo> info = LocationPrefs.getLocations(context);
        for (int i=0; i<info.size(); i++) {
            if (info.get(i).getId().equals(locationId)) {
                return info.get(i).getLocationName();
            }
        }

        return "";
    }

    private String getEstimatedTime(String dateString, String locationId) {

        int waitingTime = 0;
        ArrayList<LocationInfo> info = LocationPrefs.getLocations(context);
        for (int i=0; i<info.size(); i++) {
            if (info.get(i).getId().equals(locationId)) {
                waitingTime = Integer.parseInt(info.get(i).getWaitingTime());
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat timeSdf = new SimpleDateFormat("hh:mm a");

        try {
            Date date = sdf.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, waitingTime);
            return  timeSdf.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    private String getReceivedDate(String dateString) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat newSdf = new SimpleDateFormat("MM/dd/yy, hh:mm a");

        try {
            Date date = sdf.parse(dateString);
            return  newSdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }
}
