package com.example.ahmed.sfa.Activities.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.Nullable;

/**
 * Created by Ahmed on 4/1/2017.
 */

public class Alert {
    private  Activity activity;


    public Alert(Activity activity){
        this.activity = activity;
    }

    /**
     * @param title is the title of the alert popup
     * @param message the message to show on the alert
     * @param positiveButtonCaption text caption of the positive button if null passed it will be assumed as ok
     * @param negativeButtonCaption text caption of the negative button if null passed it will be assigned cancel
     * @param positiveButtonClickListener @nullable on click listener for the positive button will dissmiss alert if submitted null
     * @param negativeButtonClickListener @nullable on click listener for the negative button will dissmiss alert if submitted null
     */

    public void showAlert(String title , String message, String positiveButtonCaption,@Nullable String negativeButtonCaption,
                          @Nullable DialogInterface.OnClickListener positiveButtonClickListener,@Nullable DialogInterface.OnClickListener negativeButtonClickListener) {

        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        if(positiveButtonCaption==null)positiveButtonCaption = "Ok";
        if(negativeButtonCaption==null)negativeButtonCaption = "Cancel";
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        DialogInterface.OnClickListener nulllistener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        };

        if(positiveButtonClickListener==null)positiveButtonClickListener = nulllistener;
        if(negativeButtonClickListener==null)negativeButtonClickListener = nulllistener;

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,negativeButtonCaption,negativeButtonClickListener);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,positiveButtonCaption,positiveButtonClickListener);
        alertDialog.show();
    }

    public void showAlert(String title, String message, @Nullable String positiveButtonCaption, @Nullable DialogInterface.OnClickListener positiveButtonClickListener){
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        if(positiveButtonCaption==null)positiveButtonCaption = "Ok";

        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        DialogInterface.OnClickListener nulllistener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        };

        if(positiveButtonClickListener==null)positiveButtonClickListener = nulllistener;

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,positiveButtonCaption,positiveButtonClickListener);
        alertDialog.show();
    }
}
