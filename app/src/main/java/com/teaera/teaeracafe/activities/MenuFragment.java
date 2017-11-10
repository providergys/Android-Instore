package com.teaera.teaeracafe.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.teaera.teaeracafe.R;
import com.teaera.teaeracafe.adapter.CartListAdapter;
import com.teaera.teaeracafe.adapter.MenuListAdapter;
import com.teaera.teaeracafe.app.Application;
import com.teaera.teaeracafe.net.Model.CategoryInfo;
import com.teaera.teaeracafe.net.Model.LocationInfo;
import com.teaera.teaeracafe.net.Model.PromotedMenuInfo;
import com.teaera.teaeracafe.net.Request.GetPromotedMenuRequest;
import com.teaera.teaeracafe.net.Response.PromotedMenuResponse;
import com.teaera.teaeracafe.preference.CartPrefs;
import com.teaera.teaeracafe.preference.CategoryPrefs;
import com.teaera.teaeracafe.preference.LocationPrefs;
import com.teaera.teaeracafe.preference.PromotedMenuPrefs;
import com.teaera.teaeracafe.utils.DialogUtils;
import com.teaera.teaeracafe.utils.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment implements MenuItemFragment.OnPromotedMenuClickListener {

    private Spinner locationSpinner;
    private TextView locationTextView;
    private ViewPager viewPager;
    private ViewPagerIndicator indicator;
    private ListView menuListView;

    private ManuAdapter adapter;
    private MenuListAdapter menuListAdapter;
    private ArrayList<LocationInfo> locations;
    private ArrayList<CategoryInfo> categories;
    private ArrayList<String> locationNames = new ArrayList<String>();
    private ArrayList<CategoryInfo> sortedCategories = new ArrayList<CategoryInfo>();
    private ArrayList<PromotedMenuInfo> promotedMenuInfos = new ArrayList<PromotedMenuInfo>();

    private MenuItemFragment.OnPromotedMenuClickListener onPromotedMenuClickListener;
    private SweetAlertDialog loaderDialog;

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        init();
    }

    public void init() {

        locations = LocationPrefs.getLocations(this.getActivity());
        categories = CategoryPrefs.getCategories(this.getActivity());
        promotedMenuInfos = PromotedMenuPrefs.getPromotedMenu(this.getActivity());

        viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
        indicator = (ViewPagerIndicator) getActivity().findViewById(R.id.pageIndicator);
        for (int i = 0; i < locations.size(); i++) {
            locationNames.add(locations.get(i).getLocationName());
        }

        List<MenuItemFragment> fragments = new ArrayList<>();
        for (int i = 0; i < promotedMenuInfos.size(); i++) {
            fragments.add(MenuItemFragment.createInstance(promotedMenuInfos.get(i), (MenuItemFragment.OnPromotedMenuClickListener) this));
        }

        adapter = new ManuAdapter(getChildFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        indicator.setPager(viewPager);

        locationSpinner = (Spinner) getActivity().findViewById(R.id.locationSpinner);
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                locationTextView.setText(locationSpinner.getSelectedItem().toString());
                updateCategories(position);
                menuListAdapter.updateCategories(sortedCategories);
                menuListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,locationNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);
        locationSpinner.setSelection(0);
        this.updateCategories(0);

        locationTextView = (TextView) getActivity().findViewById(R.id.locationTextView1);
        locationTextView.setText(locationSpinner.getSelectedItem().toString());

        menuListView = (ListView) getActivity().findViewById(R.id.menuListView);
        menuListAdapter = new MenuListAdapter(this.getActivity(), sortedCategories);
        menuListView.setAdapter(menuListAdapter);


        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), SubMenuActivity.class);
                intent.putExtra("category", sortedCategories.get(position));
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });
    }

    private void updateCategories(int index) {

        sortedCategories.clear();
        for (int i=0; i<categories.size(); i++) {
            if (categories.get(i).getLocationID().equals(locations.get(index).getId())) {
                sortedCategories.add(categories.get(i));
            }
        }
    }

    @Override
    public void onPromotedMenuClicked(String menuId, String categoryId) {
        showLoader(R.string.empty);

        Application.getServerApi().getMenu(new GetPromotedMenuRequest(menuId, categoryId)).enqueue(new Callback<PromotedMenuResponse>(){

            @Override
            public void onResponse(Call<PromotedMenuResponse> call, Response<PromotedMenuResponse> response) {
                hideLoader();
                if (response.body().isError()) {
                    DialogUtils.showDialog(getActivity(), "Error", response.body().getMessage(), null, null);
                } else {
                    Intent intent = new Intent(getActivity(), ItemCustomizeActivity.class);
                    intent.putExtra("menu", response.body().getMenu());
                    intent.putExtra("category", response.body().getCategory());
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                }
            }

            @Override
            public void onFailure(Call<PromotedMenuResponse> call, Throwable t) {
                hideLoader();
                if (t.getLocalizedMessage() != null) {
                    Log.d("PromotedMenu", t.getLocalizedMessage());
                } else {
                    Log.d("PromotedMenu", "Unknown error");
                }
            }
        });
    }


    public void showLoader(int resId) {
        loaderDialog = new SweetAlertDialog(this.getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        loaderDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.loader_color));
        loaderDialog.setTitleText(getString(resId));
        loaderDialog.setCancelable(false);
        loaderDialog.show();
    }

    public void hideLoader() {
        if (loaderDialog != null) {
            loaderDialog.dismissWithAnimation();
        }
    }

    public static class ManuAdapter extends FragmentStatePagerAdapter {

        private List<MenuItemFragment> fragments;

        public ManuAdapter(FragmentManager fm, List<MenuItemFragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
