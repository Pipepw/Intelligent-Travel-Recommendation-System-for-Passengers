package com.newweather.intelligenttravel.util;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.newweather.intelligenttravel.R;

public class WaitDialog {
    private static AlertDialog alertDialog;
    public static void showProgressDialog(Context context){
        if(alertDialog == null){
            alertDialog = new AlertDialog.Builder(context, R.style.CustomProgressDialog)
                    .create();
        }
        View view = LayoutInflater.from(context).inflate(R.layout
                .custom_progress_dialog_view,null);
        alertDialog.setView(view,0,0,0,0);
        TextView tvTip = view.findViewById(R.id.tvTip);
        tvTip.setText("加载中。。。");
        alertDialog.show();
    }
    public static void dismiss(){
        if(alertDialog != null && alertDialog.isShowing()){
            alertDialog.dismiss();

        }
    }
}
