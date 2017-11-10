package com.teaera.teaeracafe.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.teaera.teaeracafe.R;
import com.teaera.teaeracafe.net.Model.MenuInfo;

import java.util.ArrayList;

/**
 * Created by admin on 06/08/2017.
 */

public class SubMenuListAdapter extends BaseAdapter {

    private static LayoutInflater inflater=null;
    Context context;
    ArrayList<MenuInfo> menus;
    int [] imageId;


    public SubMenuListAdapter(Activity activity, ArrayList<MenuInfo> menus, int[] imageId) {

        context = activity;
        this.menus = menus;
        this.imageId = imageId;

        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public SubMenuListAdapter(Activity activity, ArrayList<MenuInfo> menus) {

        context = activity;
        this.menus = menus;

        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateMenus(ArrayList<MenuInfo> menus) {
        this.menus = menus;
    }

    @Override
    public int getCount() {
        return menus.size();
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
        TextView title;
        ImageView menuImageView;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {

        SubMenuListAdapter.Holder holder = new SubMenuListAdapter.Holder();
        View rowView = inflater.inflate(R.layout.menu_list_item, null);
        holder.title = (TextView) rowView.findViewById(R.id.title);
        holder.menuImageView = (ImageView) rowView.findViewById(R.id.profileImage);
        holder.menuImageView.setVisibility(View.GONE);
        holder.title.setText(menus.get(position).getMenuName());

        return rowView;
    }
}

