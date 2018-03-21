package com.teaera.teaerastore.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teaera.teaerastore.R;
import com.teaera.teaerastore.net.Model.OrderItemInfo;

import java.util.ArrayList;

/**
 * Created by admin on 17/10/2017.
 */

public class DetailsOrderListAdapter extends BaseAdapter {

    private static LayoutInflater inflater=null;
    private Context context;
    private ArrayList<OrderItemInfo> orderItems;


    public DetailsOrderListAdapter(Activity activity, ArrayList<OrderItemInfo> orderItems) {

        this.context = activity;
        this.orderItems = orderItems;

        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return orderItems.size();
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
        TextView menuTextView;
        TextView costTextView;
        TextView quantityTextView;
        TextView detailsTextView;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        final Holder holder = new Holder();
        View rowView = inflater.inflate(R.layout.order_details_list_item, null);

        holder.menuTextView = rowView.findViewById(R.id.menuTextView);
        holder.detailsTextView =  rowView.findViewById(R.id.detailsTextView);
        holder.costTextView = rowView.findViewById(R.id.costTextView);
        holder.quantityTextView = rowView.findViewById(R.id.quantityTextView);

        holder.menuTextView.setText(orderItems.get(position).getMenuName());
        holder.detailsTextView.setText(orderItems.get(position).getOptions());
        int quantity = Integer.parseInt(orderItems.get(position).getQuantity()) - Integer.parseInt(orderItems.get(position).getRefundQuantity());
        holder.quantityTextView.setText(Integer.toString(quantity));

        //float totalPrice = Float.parseFloat(orderItems.get(position).getPrice())*Integer.parseInt(orderItems.get(position).getQuantity());
        holder.costTextView.setText(String.format("$%.2f", Float.parseFloat(orderItems.get(position).getPrice())));

        return rowView;
    }

}
