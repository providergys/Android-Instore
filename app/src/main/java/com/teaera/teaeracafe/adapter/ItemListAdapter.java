package com.teaera.teaeracafe.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.teaera.teaeracafe.R;
import com.teaera.teaeracafe.net.Model.CartInfo;
import com.teaera.teaeracafe.net.Model.OrderDetailsInfo;

import java.util.ArrayList;

/**
 * Created by admin on 25/10/2017.
 */

public class ItemListAdapter extends BaseAdapter {

    private static LayoutInflater inflater=null;
    private Context context;
    private ArrayList<OrderDetailsInfo> orderDetails;


    public ItemListAdapter(Activity activity, ArrayList<OrderDetailsInfo> orderDetails) {

        this.context = activity;
        this.orderDetails = orderDetails;

        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateCategories(ArrayList<OrderDetailsInfo> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Override
    public int getCount() {
        return orderDetails.size();
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
        TextView detailsTextView;
        TextView quantityTextView;
        ImageView arrowImageView;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        final ItemListAdapter.Holder holder = new ItemListAdapter.Holder();
        View rowView = inflater.inflate(R.layout.item_list, null);

        holder.menuTextView = (TextView) rowView.findViewById(R.id.menuTextView);
        holder.detailsTextView = (TextView) rowView.findViewById(R.id.detailsTextView);
        holder.costTextView = (TextView) rowView.findViewById(R.id.costTextView);
        holder.quantityTextView = (TextView) rowView.findViewById(R.id.quantityTextView);
        holder.arrowImageView = (ImageView) rowView.findViewById(R.id.arrowImageView);

        holder.menuTextView.setText(orderDetails.get(position).getMenuName());
        holder.detailsTextView.setText(orderDetails.get(position).getOptions());
        holder.quantityTextView.setText(orderDetails.get(position).getQuantity());
        float totalPrice = Float.parseFloat(orderDetails.get(position).getPrice())*Integer.parseInt(orderDetails.get(position).getQuantity());
        holder.costTextView.setText(String.format("$%.3f", totalPrice));
        holder.arrowImageView.setVisibility(View.GONE);

        return rowView;
    }

}
