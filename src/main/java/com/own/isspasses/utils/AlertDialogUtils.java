package com.own.isspasses.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogUtils {

    public static void displayAlertDialog(Context context, String title, String message, String positiveBtnText, String negativeBtnText, String neutralBtnText, final AlertDialogListener listener){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        // Positive Btn Case..
        alertDialog.setPositiveButton(positiveBtnText, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(listener != null)
                    listener.onPositiveBtnClick();
            }
        });

        // Negative Btn Case..
        if(negativeBtnText != null){
            alertDialog.setNegativeButton(negativeBtnText, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int arg1) {
                    dialog.dismiss();
                    if(listener != null)
                        listener.onNegativeBtnClick();
                }
            });
        }

        // Neutral Btn Case..
        if(neutralBtnText != null) {
            alertDialog.setNeutralButton(neutralBtnText, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (listener != null)
                        listener.onNeutralBtnClick();
                }
            });
        }

        alertDialog.show();
    }

    public static abstract class AlertDialogListener{
        public abstract void onPositiveBtnClick();
        public void onNegativeBtnClick(){};
        public void onNeutralBtnClick(){};
    }
}
