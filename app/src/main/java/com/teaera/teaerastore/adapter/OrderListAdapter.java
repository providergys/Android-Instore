package com.teaera.teaerastore.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teaera.teaerastore.R;
import com.teaera.teaerastore.net.Model.OrderInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by admin on 13/10/2017.
 */

public class OrderListAdapter extends BaseAdapter {

    private static LayoutInflater inflater=null;
    Context context;
    ArrayList<OrderInfo> orders;
    int selectedPos = 0;
    private OnOrderItemClickListener onOrderItemClickListener;

    public OrderListAdapter(Activity activity, ArrayList<OrderInfo> orders, OnOrderItemClickListener onOrderItemClickListener) {

        context = activity;
        this.orders = orders;

        this.onOrderItemClickListener = onOrderItemClickListener;

        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateOrders(ArrayList<OrderInfo> orders, int position) {
        this.orders = orders;
        this.selectedPos = position;
    }

    @Override
    public int getCount() {
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
        RelativeLayout relativeLayout;
        TextView order;
        TextView date;
        TextView name;
        Button status;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        final OrderListAdapter.Holder holder = new OrderListAdapter.Holder();
        final View rowView = inflater.inflate(R.layout.order_list_item, null);
        holder.relativeLayout = rowView.findViewById(R.id.relativeLayout);
        if (selectedPos == position) {
            holder.relativeLayout.setBackgroundResource(R.color.base_color);
        } else {
            holder.relativeLayout.setBackgroundColor(Color.WHITE);
        }
        holder.order = rowView.findViewById(R.id.orderTextView);
        holder.date = rowView.findViewById(R.id.dateTextView);
        holder.name = rowView.findViewById(R.id.nameTextView);
        holder.status = rowView.findViewById(R.id.statusButton);

        int orderId = Integer.parseInt(orders.get(position).getId());
        holder.order.setText(String.format("ORDER: #%05d", orderId));
        holder.date.setText(getReceivedDate(orders.get(position).getTimestamp()));
        holder.name.setText(orders.get(position).getUserName());

        switch (orders.get(position).getStatus()) {
            case "0":
                holder.status.setText(R.string.order_new);
                holder.status.setTextColor(Color.WHITE);
                holder.status.setBackgroundResource(R.color.new_status_color);
            break;

            case "1":
                holder.status.setText(R.string.order_progress);
                holder.status.setTextColor(Color.WHITE);
                holder.status.setBackgroundResource(R.color.progress_color);
                break;

            case "2":
                holder.status.setText(R.string.order_ready);
                holder.status.setTextColor(Color.WHITE);
                holder.status.setBackgroundResource(R.color.select_color);
                break;

            case "3":
                holder.status.setText(R.string.order_completed);
                holder.status.setTextColor(Color.WHITE);
                holder.status.setBackgroundResource(R.color.progress_color);
                break;
        }

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setSelected(true);
//                holder.relativeLayout.setBackgroundColor(Color.DKGRAY);
                onOrderItemClickListener.onOrderItemClickListener(orders.get(position), position);
            }
        });
        return rowView;
    }

    public interface OnOrderItemClickListener {
        public void onOrderItemClickListener(OrderInfo info, int position);
    }

    public String getReceivedDate(String dateString) {

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
