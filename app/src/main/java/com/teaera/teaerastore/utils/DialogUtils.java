package com.teaera.teaerastore.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by admin on 24/08/2017.
 */

public class DialogUtils {
    public static void showDialog(final Context context, String title, String message, final DialogInterface.OnClickListener okListener, final DialogInterface.OnClickListener cancelListener) {

        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (okListener != null) {
                            okListener.onClick(dialog, which);
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (cancelListener != null) {
                            cancelListener.onClick(dialog, which);
                        }
                    }
                })
                .show();
    }
}
