package com.teaera.teaerastore.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

        holder.menuTextView.setText(orderItems.get(position).getMenuName());
        holder.detailsTextView.setText(orderItems.get(position).getOptions());
        holder.quantityTextView.setText(orderItems.get(position).getQuantity());
        float totalPrice = Float.parseFloat(orderItems.get(position).getPrice())*Integer.parseInt(orderItems.get(position).getQuantity());
        holder.costTextView.setText(String.format("$%.2f", totalPrice));

        if (checkList.get(position) == 0) {
            holder.checkImageButton.setImageResource(R.drawable.refund_uncheck);
            holder.checkImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.checkImageButton.setImageResource(R.drawable.refund_check);
                    onRefundCheckListener.onRefundCheckClicked(position);
//                    if (orderItems.get(position).getRefunded().equals("0")) {
//                        holder.checkImageButton.setImageResource(R.drawable.refund_check);
//                        onRefundCheckListener.onRefundCheckClicked(position);
//                    }
                }
            });
        } else {
            holder.checkImageButton.setImageResource(R.drawable.refund_check);
        }

        return rowView;
    }

    public void updateCheckList(ArrayList<Integer> checkList) {
        this.checkList = checkList;
    }

    public interface OnRefundCheckListener {
        public void onRefundCheckClicked(int position);
    }
}
