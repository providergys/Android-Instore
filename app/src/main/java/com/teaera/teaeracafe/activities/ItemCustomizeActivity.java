package com.teaera.teaeracafe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;

import com.teaera.teaeracafe.R;
import com.teaera.teaeracafe.adapter.OptionListAdapter;
import com.teaera.teaeracafe.app.Application;
import com.teaera.teaeracafe.net.Model.CartInfo;
import com.teaera.teaeracafe.net.Model.CategoryInfo;
import com.teaera.teaeracafe.net.Model.MenuInfo;
import com.teaera.teaeracafe.net.Model.OptionInfo;
import com.teaera.teaeracafe.preference.CartPrefs;
import com.teaera.teaeracafe.utils.DialogUtils;
import com.teaera.teaeracafe.utils.DownloadImageTask;

import java.util.ArrayList;
import java.util.zip.Inflater;

import static android.view.View.inflate;

public class ItemCustomizeActivity extends BaseActivity implements View.OnClickListener, OptionListAdapter.OnChangeOptionListener, OptionListAdapter.OnSpinnerClickListener {

    private RelativeLayout quantityLayout;
    private RelativeLayout notesLayout;

    private ListView optionListView;
    private TextView itemTextView;
    private ImageView menuImageView;
    private Button addButton;
    private EditText qualtityEditText;
    private ImageView cartImageView;
    private TextView cartTextView;
    private EditText notesEditText;


    private MenuInfo menuInfo;
    private CategoryInfo categoryInfo;
    private OptionListAdapter optionListAdapter;
    private int quantity = 1;
    private Float totalPrice = 0.0f;
    private ArrayList<String> optionValues = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_customize);

        menuInfo = (MenuInfo) getIntent().getSerializableExtra("menu");
        categoryInfo = (CategoryInfo) getIntent().getSerializableExtra("category");

        for (int i = 0 ; i < menuInfo.getOptions().size(); i++) {
            optionValues.add(menuInfo.getOptions().get(i).getOptionValues().get(0).getOptionValueDescription());
        }

        cartImageView = (ImageView) findViewById(R.id.cartImageView);
        cartTextView = (TextView) findViewById(R.id.cartTextView);

        itemTextView = (TextView) findViewById(R.id.itemTextView);
        menuImageView = (ImageView) findViewById(R.id.menuImageView);
        new DownloadImageTask(menuImageView).execute(Application.getServerImagePrefix() + menuInfo.getImage());
        itemTextView.setText(menuInfo.getMenuName());


        optionListView = (ListView) findViewById(R.id.optionListView);
        optionListAdapter = new OptionListAdapter(this, menuInfo);
        optionListView.setAdapter(optionListAdapter);

        quantityLayout = (RelativeLayout) findViewById(R.id.quantityLayout);
        notesLayout = (RelativeLayout) findViewById(R.id.notesLayout);
        notesEditText = (EditText) findViewById(R.id.notesEditText);

        qualtityEditText = (EditText)findViewById(R.id.qualtityEditText);
        qualtityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0)
                    vaildQuantity();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addButton = (Button) findViewById(R.id.addButton);
        addButton.setText(String.format("Add to Cart ($%.2f)", Float.parseFloat(menuInfo.getPrice())));
        addButton.setOnClickListener(this);

        Button cartButton = (Button) findViewById(R.id.cartButton);
        cartButton.setOnClickListener(this);

        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(this);

    }

    @Override
    public void onResume(){
        super.onResume();

        cartIconUpdate();
    }

    private void cartIconUpdate() {
        if (!CartPrefs.isCartsExists(this) || CartPrefs.getCarts(this).size() == 0) {
            cartImageView.setVisibility(View.INVISIBLE);
            cartTextView.setVisibility(View.INVISIBLE);
        } else {
            int count = CartPrefs.getCarts(this).size();
            cartImageView.setVisibility(View.VISIBLE);
            cartTextView.setVisibility(View.VISIBLE);
            cartTextView.setText(Integer.toString(count));
        }
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        // get content height
        int contentHeight = optionListView.getChildAt(0).getHeight();

        // set listview height
        ViewGroup.LayoutParams lp = optionListView.getLayoutParams();
        lp.height = contentHeight * menuInfo.getOptions().size();
        optionListView.setLayoutParams(lp);
    }

    public void vaildQuantity() {
        String text = qualtityEditText.getText().toString();
        try {
            quantity = Integer.parseInt(text);
            if (quantity > 0) {
                totalPrice = Float.parseFloat(menuInfo.getPrice())*quantity;
                addButton.setText(String.format("Add to Cart ($%.2f)", totalPrice));
            } else {
                qualtityEditText.setText("1");
                totalPrice = Float.parseFloat(menuInfo.getPrice());
                addButton.setText(String.format("Add to Cart ($%.2f)", totalPrice));
                DialogUtils.showDialog(ItemCustomizeActivity.this, "Error", "Type vaild quantity value!", null, null);
            }

        } catch (NumberFormatException e) {

            qualtityEditText.setText("1");
            addButton.setText(String.format("Add to Cart ($%.2f)", Float.parseFloat(menuInfo.getPrice())));
            DialogUtils.showDialog(ItemCustomizeActivity.this, "Error", "Type vaild quantity value!", null, null);
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.backButton:
                finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                break;

            case R.id.cartButton:
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                break;

            case R.id.addButton:
                addToCart();
                finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                break;
        }
    }

    public void addToCart() {

        ArrayList<CartInfo> carts = CartPrefs.getCarts(this);
        if (!CartPrefs.isCartsExists(this)) {
            carts = new ArrayList<CartInfo>();
        }

        String option = "";
        for (int i = 0; i < menuInfo.getOptions().size(); i++) {
            option = option + optionValues.get(i) + "\n";
        }
        option = option + "Note: " + notesEditText.getText().toString();

        CartInfo cartInfo = new CartInfo();
        cartInfo.setCartIndex(carts.size());
        cartInfo.setLocationId(categoryInfo.getLocationID());
        cartInfo.setMenuId(menuInfo.getId());
        cartInfo.setMenuName(menuInfo.getMenuName());
        cartInfo.setOptions(option);
        cartInfo.setQuantity(Integer.toString(quantity));
        cartInfo.setPrice(menuInfo.getPrice());
        cartInfo.setDrinkable(categoryInfo.getDrinkable());

        if(Application.getIsRedeemed()) {
            cartInfo.setRedeemed("1");
        } else {
            cartInfo.setRedeemed("0");
        }


        carts.add(cartInfo);
        CartPrefs.setCarts(this, carts);
        cartIconUpdate();
    }

    @Override
    public void onOptionClicked(int optionPosition, int item) {
        optionValues.set(optionPosition, menuInfo.getOptions().get(optionPosition).getOptionValues().get(item).getOptionValueDescription());
    }

    @Override
    public void onSpinnerClicked() {
        InputMethodManager imm = (InputMethodManager)getSystemService(this.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
}
