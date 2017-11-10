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
import com.teaera.teaeracafe.app.Application;
import com.teaera.teaeracafe.net.Model.CategoryInfo;
import com.teaera.teaeracafe.utils.DownloadImageTask;

import java.util.ArrayList;

/**
 * Created by admin on 04/08/2017.
 */

public class MenuListAdapter extends BaseAdapter {

    private static LayoutInflater inflater=null;
    private Context context;
    private ArrayList<CategoryInfo> categories;


    public MenuListAdapter(Activity activity, ArrayList<CategoryInfo> categories) {

        this.context = activity;
        this.categories = categories;

        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateCategories(ArrayList<CategoryInfo> categories) {
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.size();
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

        Holder holder = new Holder();
        View rowView = inflater.inflate(R.layout.menu_list_item, null);
        holder.title = (TextView) rowView.findViewById(R.id.title);
        holder.menuImageView = (ImageView) rowView.findViewById(R.id.profileImage);

        holder.title.setText(categories.get(position).getCategoryName());
        new DownloadImageTask(holder.menuImageView).execute(Application.getServerImagePrefix() + categories.get(position).getImage());

        return rowView;
    }
}
