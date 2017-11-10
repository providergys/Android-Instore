package com.teaera.teaeracafe.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.teaera.teaeracafe.R;
import com.teaera.teaeracafe.net.Model.CartInfo;

import java.util.ArrayList;

/**
 * Created by admin on 24/09/2017.
 */

public class CartListAdapter extends BaseAdapter {

    private static LayoutInflater inflater=null;
    private Context context;
    private ArrayList<CartInfo> carts;
    private OnQuantityChangeListener onQuantityChangeListener;


    public CartListAdapter(Activity activity, ArrayList<CartInfo> carts) {

        this.context = activity;
        this.carts = carts;
        this.onQuantityChangeListener = (OnQuantityChangeListener) activity;

        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateCategories(ArrayList<CartInfo> carts) {
        this.carts = carts;
    }

    @Override
    public int getCount() {
        return carts.size();
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
        Button plusButton;
        Button minusButton;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        final Holder holder = new Holder();
        View rowView = inflater.inflate(R.layout.cart_list_item, null);

        holder.menuTextView = (TextView) rowView.findViewById(R.id.menuTextView);
        holder.detailsTextView = (TextView) rowView.findViewById(R.id.detailsTextView);
        holder.costTextView = (TextView) rowView.findViewById(R.id.costTextView);
        holder.quantityTextView = (TextView) rowView.findViewById(R.id.quantityTextView);
        holder.plusButton = (Button) rowView.findViewById(R.id.plusButton);
        holder.minusButton = (Button) rowView.findViewById(R.id.minusButton);

        holder.menuTextView.setText(carts.get(position).getMenuName());
        holder.detailsTextView.setText(carts.get(position).getOptions());
        holder.quantityTextView.setText(carts.get(position).getQuantity());
        float totalPrice = Float.parseFloat(carts.get(position).getPrice())*Integer.parseInt(carts.get(position).getQuantity());
        holder.costTextView.setText(String.format("$%.2f", totalPrice));

        holder.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onQuantityChangeListener.onPlusButtonClicked(carts.get(position).getCartIndex());
            }
        });

        holder.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(carts.get(position).getQuantity());
                if (num == 0)
                    return;

                onQuantityChangeListener.onMinusButtonClicked(carts.get(position).getCartIndex());
            }
        });

        return rowView;
    }

    public interface OnQuantityChangeListener {
        public void onPlusButtonClicked(int cartIndex);
        public void onMinusButtonClicked(int cartIndex);
    }
}