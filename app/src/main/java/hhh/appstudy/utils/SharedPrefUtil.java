package hhh.appstudy.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by hhh on 2016/5/25.
 */
public class SharedPrefUtil {
    private static final String NAME="study.spf";

    public static void updateMode(Context context,boolean isNight){
        SharedPreferences sp=context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        if(sp.getBoolean("isNight",false)!=isNight) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("isNight", isNight);
            editor.commit();

            context.sendBroadcast(new Intent("hhh.appstudy.MODE_CHANGED"));
        }
    }

    public static boolean isNightMode(Context context){
        SharedPreferences sp=context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return sp.getBoolean("isNight",false);
    }
}
