package com.teaera.teaeracafe.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.teaera.teaeracafe.R;
import com.teaera.teaeracafe.activities.ItemCustomizeActivity;
import com.teaera.teaeracafe.net.Model.MenuInfo;
import com.teaera.teaeracafe.net.Model.OptionInfo;

import java.util.ArrayList;

/**
 * Created by admin on 13/09/2017.
 */

public class OptionListAdapter extends BaseAdapter {

    private static LayoutInflater inflater=null;
    private OnChangeOptionListener changeOptionListener;
    private OnSpinnerClickListener spinnerClickListener;
    Context context;
    MenuInfo menuInfo;


    public OptionListAdapter(Activity activity, MenuInfo menuInfo) {

        context = activity;
        this.menuInfo = menuInfo;
        this.changeOptionListener = (OnChangeOptionListener) activity;
        this.spinnerClickListener = (OnSpinnerClickListener) activity;

        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return menuInfo.getOptions().size();
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
        TextView optionTextView;
        TextView optionValueTextView;
        Spinner spinner;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        final OptionListAdapter.Holder holder = new OptionListAdapter.Holder();
        View rowView = inflater.inflate(R.layout.menu_item_view, null);
        holder.optionTextView = (TextView) rowView.findViewById(R.id.optionTextView);
        holder.optionValueTextView = (TextView) rowView.findViewById(R.id.optionValueTextView);
        holder.spinner = (Spinner) rowView.findViewById(R.id.spinner);

        OptionInfo optionInfo = menuInfo.getOptions().get(position);
        holder.optionTextView.setText(optionInfo.getOptionDescription());
        ArrayList<String> optionValueNames = new ArrayList<String>();
        for (int j = 0; j < optionInfo.getOptionValues().size(); j++) {
            optionValueNames.add(optionInfo.getOptionValues().get(j).getOptionValueDescription());
        }


        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                changeOptionListener.onOptionClicked(position, pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    spinnerClickListener.onSpinnerClicked();
                }
                return false;
            }
        });

        ArrayAdapter adapter = new ArrayAdapter(context,android.R.layout.simple_spinner_item,optionValueNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(adapter);
        holder.spinner.setSelection(0);

        return rowView;
    }

    public interface OnChangeOptionListener {
        public void onOptionClicked(int optionPosition, int item);
    }
    public interface OnSpinnerClickListener {
        public void onSpinnerClicked();
    }
}