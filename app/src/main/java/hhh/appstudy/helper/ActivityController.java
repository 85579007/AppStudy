package hhh.appstudy.helper;

import android.app.Activity;
import android.os.Process;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hhh on 2016/4/19.
 */
public class ActivityController {
    private static List<Activity> m=new LinkedList<>();

    public static void addActivity(Activity a){
        m.add(a);
    }

    public static void removeActivity(Activity a){
        m.remove(a);
    }

    public static void closeAll(){
        for(Activity a:m){
            a.finish();
        }
        Process.killProcess(Process.myPid());
    }
}
