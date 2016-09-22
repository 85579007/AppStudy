package hhh.appstudy.activity;

import android.app.Application;

import hhh.appstudy.entities.User;
import hhh.appstudy.https.XUtils;
import hhh.appstudy.utils.Loading;

/**
 * Created by hhh on 2016/4/20.
 */
public class MyApp extends Application {
    private User u;

    public User getU() {
        return u;
    }

    public void setU(User u) {
        this.u = u;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Loading.init(this);
        XUtils.init(this);
    }
}
