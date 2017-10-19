package com.teaera.teaerastore.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teaera.teaerastore.R;
import com.teaera.teaerastore.adapter.DetailsOrderListAdapter;
import com.teaera.teaerastore.adapter.RefundOrderListAdapter;
import com.teaera.teaerastore.net.Model.OrderInfo;

import java.util.ArrayList;

public class refundActivity extends BaseActivity implements View.OnClickListener, RefundOrderListAdapter.OnRefundCheckListener {

    private RelativeLayout authRelativeLayout;
    private TextView orderNumberTextView;
    private TextView dateTextView;
    private TextView nameTextView;
    private TextView emailTextView;

    private ListView orderListView;
    private RefundOrderListAdapter refundOrderListAdapter;
    private ArrayList<Integer> checkList = new ArrayList<Integer>();
    private OrderInfo orderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund);
        orderInfo = (OrderInfo) getIntent().getSerializableExtra("orderInfo");

        for (int i=0; i<orderInfo.getDetails().size(); i++) {
            checkList.add(0);
        }
        init();
    }

    private void init() {

        authRelativeLayout = findViewById(R.id.authRelativeLayout);
        authRelativeLayout.setVisibility(View.GONE);

        orderNumberTextView = findViewById(R.id.orderNumberTextView);
        dateTextView = findViewById(R.id.dateTextView);
        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);

        orderNumberTextView.setText("ORDER: #" + orderInfo.getId());
        dateTextView.setText(orderInfo.getTimestamp());
        nameTextView.setText(orderInfo.getUserName());
        emailTextView.setText(orderInfo.getEmail());

        orderListView = findViewById(R.id.orderListView);
        refundOrderListAdapter = new RefundOrderListAdapter(this, orderInfo.getDetails(), checkList);
        orderListView.setAdapter(refundOrderListAdapter);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                break;
        }
    }

    @Override
    public void onRefundCheckClicked(int position) {
        if (checkList.get(position) == 1) {
            checkList.set(position, 0);
        } else {
            checkList.set(position, 1);
        }
        refundOrderListAdapter.updateCheckList(checkList);
        refundOrderListAdapter.notifyDataSetChanged();
    }
}
