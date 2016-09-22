package hhh.appstudy.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import hhh.appstudy.R;
import hhh.appstudy.helper.ActivityController;
import hhh.appstudy.https.XUtils;

/**
 * Created by hhh on 2016/4/19.
 */
public class BaseActivity extends FragmentActivity {
    private boolean isExit;
    private Handler handler=new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        ActivityController.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.ACTION_DOWN){
            if(getClass().getName().equals(MainActivity.class.getName())){
                if(!isExit){
                    isExit=true;
                    XUtils.show(R.string.exit);
                    handler.postDelayed(r,2000);
                }else{
                    ActivityController.closeAll();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private Runnable r=new Runnable() {
        @Override
        public void run() {
            isExit=false;
        }
    };
}
