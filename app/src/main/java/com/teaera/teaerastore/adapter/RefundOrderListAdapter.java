package com.teaera.teaerastore.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.teaera.teaerastore.R;
import com.teaera.teaerastore.net.Model.OrderItemInfo;

import java.util.ArrayList;

/**
 * Created by admin on 17/10/2017.
 */

public class RefundOrderListAdapter extends BaseAdapter {

    private static LayoutInflater inflater=null;
    private Context context;
    private ArrayList<OrderItemInfo> orderItems;
    private OnRefundCheckListener onRefundCheckListener;

    private ArrayList<Integer> checkList;

    public RefundOrderListAdapter(Activity activity, ArrayList<OrderItemInfo> orderItems, ArrayList<Integer> checkList) {

        this.context = activity;
        this.orderItems = orderItems;
        this.onRefundCheckListener = (OnRefundCheckListener) activity;
        this.checkList = checkList;

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
        ImageButton checkImageButton;
        Button minusButton;
        Button plusButton;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        final Holder holder = new Holder();
        View rowView = inflater.inflate(R.layout.refund_order_list_item, null);

        holder.menuTextView = rowView.findViewById(R.id.menuTextView);
        holder.detailsTextView =  rowView.findViewById(R.id.detailsTextView);
        holder.costTextView = rowView.findViewById(R.id.costTextView);
        holder.quantityTextView = rowView.findViewById(R.id.quantityTextView);
        holder.checkImageButton = rowView.findViewById(R.id.checkImageButton);
        holder.minusButton = rowView.findViewById(R.id.minusButton);
        holder.plusButton = rowView.findViewById(R.id.plusButton);

        holder.menuTextView.setText(orderItems.get(position).getMenuName());
        holder.detailsTextView.setText(orderItems.get(position).getOptions());
        //float totalPrice = Float.parseFloat(orderItems.get(position).getPrice())*Integer.parseInt(orderItems.get(position).getQuantity());
        holder.costTextView.setText(String.format("$%.2f", Float.parseFloat(orderItems.get(position).getPrice())));

        if (checkList.get(position) == 0) {
            holder.quantityTextView.setText("0");
            holder.checkImageButton.setImageResource(R.drawable.refund_uncheck);
            holder.checkImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRefundCheckListener.onRefundCheckClicked(position);
                }
            });
        } else {
            holder.quantityTextView.setText(orderItems.get(position).getQuantity());
            holder.checkImageButton.setImageResource(R.drawable.refund_check);
            if (checkList.get(position) == 2) {
                holder.checkImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onRefundCheckListener.onUnRefundCheckClicked(position);
                    }
                });

                holder.plusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onRefundCheckListener.onPlusClicked(position);
                    }
                });

                holder.minusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onRefundCheckListener.onMinusClicked(position);
                    }
                });
            }
        }

        return rowView;
    }

    public void updateCheckList(ArrayList<Integer> checkList) {
        this.checkList = checkList;
    }

    public void updateOrderItems(ArrayList<OrderItemInfo> orderItems) {
        this.orderItems = orderItems;
    }

    public interface OnRefundCheckListener {
        public void onRefundCheckClicked(int position);
        public void onUnRefundCheckClicked(int position);
        public void onPlusClicked(int position);
        public void onMinusClicked(int position);
    }
}
