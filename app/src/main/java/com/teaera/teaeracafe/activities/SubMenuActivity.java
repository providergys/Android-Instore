package com.teaera.teaeracafe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.teaera.teaeracafe.R;
import com.teaera.teaeracafe.adapter.SubMenuListAdapter;
import com.teaera.teaeracafe.app.Application;
import com.teaera.teaeracafe.net.Model.CategoryInfo;
import com.teaera.teaeracafe.net.Model.MenuInfo;
import com.teaera.teaeracafe.net.Request.GetMenuRequest;
import com.teaera.teaeracafe.net.Response.MenuResponse;
import com.teaera.teaeracafe.preference.CartPrefs;
import com.teaera.teaeracafe.utils.DialogUtils;
import com.teaera.teaeracafe.utils.DownloadImageTask;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SubMenuActivity extends BaseActivity implements View.OnClickListener {

    private TextView menuTextView;
    private TextView firstMenuTextView;
    private ImageView firstImageView;
    private TextView secondMenuTextView;
    private ImageView secondImageView;
    private TextView thirdMenuTextView;
    private ImageView thirdImageView;
    private ListView menuListView;
    private ImageView cartImageView;
    private TextView cartTextView;


    private CategoryInfo categoryInfo;
    private SubMenuListAdapter subMenuListAdapter;
    private ArrayList<MenuInfo> menus;
    private ArrayList<MenuInfo> listedMenus = new ArrayList<MenuInfo>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_menu);

        this.categoryInfo = (CategoryInfo) getIntent().getSerializableExtra("category");

        init();
    }

    @Override
    public void onResume(){
        super.onResume();

        updateCardIcon();
    }

    void init() {

        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(this);

        Button cartButton = (Button) findViewById(R.id.cartButton);
        cartButton.setOnClickListener(this);

        menuListView = (ListView) findViewById(R.id.menuListView);
        cartImageView = (ImageView) findViewById(R.id.cartImageView);
        cartTextView = (TextView) findViewById(R.id.cartTextView);

        updateCardIcon();
        loadMenuItems();
        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SubMenuActivity.this, ItemCustomizeActivity.class);
                intent.putExtra("menu", listedMenus.get(position));
                intent.putExtra("category", categoryInfo);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });
    }

    private void updateMenuList() {

        updatePromotedMenu();
        subMenuListAdapter = new SubMenuListAdapter(SubMenuActivity.this, listedMenus);
        menuListView.setAdapter(subMenuListAdapter);
    }

    private void updatePromotedMenu() {
        menuTextView = (TextView) findViewById(R.id.menuTextView);
        firstMenuTextView = (TextView) findViewById(R.id.firstMenuTextView);
        secondMenuTextView = (TextView) findViewById(R.id.secondMenuTextView);
        thirdMenuTextView = (TextView) findViewById(R.id.thirdMenuTextView);

        firstImageView = (ImageView) findViewById(R.id.firstImageView);
        secondImageView = (ImageView) findViewById(R.id.secondImageView);
        thirdImageView = (ImageView) findViewById(R.id.thirdImageView);
        firstImageView.setOnClickListener(this);
        secondImageView.setOnClickListener(this);
        thirdImageView.setOnClickListener(this);

        for (int i=0; i<menus.size(); i++) {
            switch (menus.get(i).getPromoted()) {
                case "1":
                    firstMenuTextView.setText(menus.get(i).getMenuName());
                    new DownloadImageTask(firstImageView).execute(Application.getServerImagePrefix() + menus.get(i).getImage());
                    break;
                case "2":
                    secondMenuTextView.setText(menus.get(i).getMenuName());
                    new DownloadImageTask(secondImageView).execute(Application.getServerImagePrefix() + menus.get(i).getImage());
                    break;
                case "3":
                    thirdMenuTextView.setText(menus.get(i).getMenuName());
                    new DownloadImageTask(thirdImageView).execute(Application.getServerImagePrefix() + menus.get(i).getImage());
                    break;
                default:
                    listedMenus.add(menus.get(i));
            }

        }
    }

    private void updateCardIcon() {
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

    private void promotedMenuDetails(String promoted) {
        for (int i=0; i<menus.size(); i++) {
            if (menus.get(i).getPromoted().equals(promoted)) {
                Intent intent = new Intent(SubMenuActivity.this, ItemCustomizeActivity.class);
                intent.putExtra("menu", menus.get(i));
                intent.putExtra("category", categoryInfo);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        }
    }

    private void loadMenuItems() {

        showLoader(R.string.empty);

        Application.getServerApi().getMenus(new GetMenuRequest(this.categoryInfo.getId())).enqueue(new Callback<MenuResponse>(){

            @Override
            public void onResponse(Call<MenuResponse> call, Response<MenuResponse> response) {
                hideLoader();
                if (response.body().isError()) {
                    DialogUtils.showDialog(SubMenuActivity.this, "Error", response.body().getMessage(), null, null);
                } else {
                    menus = response.body().getMenus();
                    if (menus != null && menus.size() > 0) {
                        updateMenuList();
                    }
                }
            }

            @Override
            public void onFailure(Call<MenuResponse> call, Throwable t) {
                hideLoader();
                if (t.getLocalizedMessage() != null) {
                    Log.d("SubMenu", t.getLocalizedMessage());
                } else {
                    Log.d("SubMenu", "Unknown error");
                }
            }
        });
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
            case R.id.firstImageView:
                promotedMenuDetails("1");
                break;
            case R.id.secondImageView:
                promotedMenuDetails("2");
                break;
            case R.id.thirdImageView:
                promotedMenuDetails("3");
                break;
        }
    }
}
