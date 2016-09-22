package hhh.appstudy.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by hhh on 2016/4/20.
 */
public class Loading {
    private static Dialog loading;
    private static Context mContext;

    public static void init(Context context){
        mContext=context;
    }

    public static void show(){
        if(loading==null){
            loading=new AlertDialog.Builder(mContext).create();
            loading.setCanceledOnTouchOutside(false);
            Window w=loading.getWindow();
            w.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }else{
            loading.show();
        }
    }

    public static void dismiss(){
        if(loading!=null&&loading.isShowing()){
            loading.dismiss();;
        }
    }

    public static void destroy(){
        dismiss();
        if(loading!=null){
            loading=null;
            mContext=null;
        }
    }
}
