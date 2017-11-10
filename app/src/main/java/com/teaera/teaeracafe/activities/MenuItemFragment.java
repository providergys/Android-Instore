package com.teaera.teaeracafe.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teaera.teaeracafe.R;
import com.teaera.teaeracafe.app.Application;
import com.teaera.teaeracafe.net.Model.PromotedMenuInfo;
import com.teaera.teaeracafe.utils.DownloadImageTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuItemFragment extends Fragment {

    private RelativeLayout relativeLayout;
    private TextView title;
    private ImageView imageView;
    private PromotedMenuInfo promotedMenuInfo;
    private OnPromotedMenuClickListener onPromotedMenuClickListener;

    public static MenuItemFragment createInstance(PromotedMenuInfo promotedMenuInfo, OnPromotedMenuClickListener onPromotedMenuClickListener) {
        MenuItemFragment fragment = new MenuItemFragment();
        fragment.promotedMenuInfo = promotedMenuInfo;
        fragment.onPromotedMenuClickListener = onPromotedMenuClickListener;
        return fragment;
    }

    public MenuItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_menu_item, container, false);
        relativeLayout = (RelativeLayout) root.findViewById(R.id.relativeLayout);
        title = (TextView) root.findViewById(R.id.title);
        imageView = (ImageView) root.findViewById(R.id.image);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title.setText(promotedMenuInfo.getMenuName());
        new DownloadImageTask(imageView).execute(Application.getServerImagePrefix() + promotedMenuInfo.getImage());
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPromotedMenuClickListener.onPromotedMenuClicked(promotedMenuInfo.getId(), promotedMenuInfo.getCategoryId());
            }
        });
    }

    public interface OnPromotedMenuClickListener {
        public void onPromotedMenuClicked(String menuId, String categoryId);
    }

}
