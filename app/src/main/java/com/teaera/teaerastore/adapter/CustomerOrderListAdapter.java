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

import java.util.ArrayList;

/**
 * Created by admin on 24/10/2017.
 */

public class CustomerOrderListAdapter extends BaseAdapter {

    private static LayoutInflater inflater=null;
    Context context;
    ArrayList<OrderInfo> orders;
    private OnOrderItemClickListener onOrderItemClickListener;

    public CustomerOrderListAdapter(Activity activity, ArrayList<OrderInfo> orders, OnOrderItemClickListener onOrderItemClickListener) {

        context = activity;
        this.orders = orders;

        this.onOrderItemClickListener = onOrderItemClickListener;

        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateOrders(ArrayList<OrderInfo> orders) {
        this.orders = orders;
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

        final Holder holder = new Holder();
        final View rowView = inflater.inflate(R.layout.customer_order_list_item, null);
        holder.relativeLayout = rowView.findViewById(R.id.relativeLayout);

        holder.order = rowView.findViewById(R.id.orderTextView);
        holder.date = rowView.findViewById(R.id.dateTextView);
        holder.status = rowView.findViewById(R.id.statusButton);

        holder.order.setText("ORDER: #" + orders.get(position).getId());
        holder.date.setText(orders.get(position).getTimestamp());

        switch (orders.get(position).getStatus()) {
            case "0":
                holder.status.setText(R.string.order_new);
                holder.status.setBackgroundResource(R.color.new_status_color);
                break;

            case "1":
                holder.status.setText(R.string.order_progress);
                holder.status.setTextColor(Color.WHITE);
                holder.status.setBackgroundResource(R.color.progress_color);
                break;

            case "2":
                holder.status.setText(R.string.order_ready);
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
                onOrderItemClickListener.onOrderItemClickListener(orders.get(position));
            }
        });
        return rowView;
    }

    public interface OnOrderItemClickListener {
        public void onOrderItemClickListener(OrderInfo info);
    }
}

