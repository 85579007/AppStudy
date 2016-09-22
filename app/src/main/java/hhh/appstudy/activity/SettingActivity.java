package hhh.appstudy.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnCompoundButtonCheckedChange;

import java.io.File;

import hhh.appstudy.BuildConfig;
import hhh.appstudy.R;
import hhh.appstudy.entities.AppVersion;
import hhh.appstudy.entities.Result;
import hhh.appstudy.https.BasicRequestCallBack;
import hhh.appstudy.https.XUtils;
import hhh.appstudy.utils.FileUtils;
import hhh.appstudy.utils.SharedPrefUtil;
import hhh.appstudy.view.TitleView;
import hhh.appstudy.view.UserInfoView;

/**
 * Created by hhh on 2016/5/18.
 */
public class SettingActivity extends BaseActivity {
    @ViewInject(R.id.setting_title)
    private TitleView title;
    @ViewInject(R.id.setting_mode)
    private CheckBox cbMode;
    @ViewInject(R.id.setting_checkupdate)
    private UserInfoView uvCheck;

    private AppVersion appVersion;
    private ProgressDialog progressDialog;

    private Handler handler=new Handler();
    private boolean isCancel;

    private View.OnClickListener l=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.title_back:
                    finish();
                    break;
                case R.id.setting_checkupdate:
                    checkUpdate();
                    break;
            }
        }
    };
    private Runnable r=new Runnable() {
        @Override
        public void run() {
            isCancel=false;
        }
    };

    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(SharedPrefUtil.isNightMode(context)){
                init();
            }
        }
    };

    private void init() {
        boolean isChecked=SharedPrefUtil.isNightMode(this);
        if(isChecked){
            setTheme(R.style.Night);
        }else{
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_setting);
        ViewUtils.inject(this);
        cbMode.setChecked(isChecked);
        title.setBackClickListener(l);
        uvCheck.setOnClickListener(l);

    }

    @OnCompoundButtonCheckedChange(R.id.setting_mode)
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPrefUtil.updateMode(this,isChecked);
    }


    private void checkUpdate() {
        int ver= BuildConfig.VERSION_CODE;
        RequestParams params=new RequestParams();
        params.addBodyParameter("code",String.valueOf(ver));
        XUtils.send(XUtils.CHECKVER, params, new BasicRequestCallBack<Result<AppVersion>>() {
            @Override
            public void success(Result<AppVersion> data) {
                if(data.code ==Result.STATE_SUC){
                    appVersion=data.data;
                    showVersionDialog();
                }else {
                    XUtils.show(data.message);
                }
            }
        },true);
    }

    private void showVersionDialog() {
        new AlertDialog.Builder(this).setMessage(String.format("检测到新版本%s,当前版本%s",appVersion.getName(),BuildConfig.VERSION_NAME))
                .setNegativeButton("立即更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog=new ProgressDialog(SettingActivity.this,ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setMax(100);
                        progressDialog.show();
                        progressDialog.setCanceledOnTouchOutside(true);
                        XUtils.download(appVersion.getUrl(), new RequestCallBack<File>() {
                            @Override
                            public void onSuccess(ResponseInfo<File> responseInfo) {
                                progressDialog.setProgress(100);
                                progressDialog.dismiss();
                                progressDialog=null;

                                Intent intent=new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(new File(XUtils.apkfile)), FileUtils.getMIMEType(XUtils.apkfile));
                                startActivity(intent);
                            }

                            @Override
                            public void onLoading(long total, long current, boolean isUploading) {
                                super.onLoading(total, current, isUploading);
                                int progress=(int) (current/total)*100;
                                progressDialog.setProgress(progress);
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {
                                XUtils.show(R.string.download_error);
                            }
                        });
                    }
                })
                .setPositiveButton("下次再说",null)
                .show();
    }

    public static void startActivity(Context context){
        Intent intent=new Intent(context,SettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        registerReceiver(broadcastReceiver,new IntentFilter("hhh.appstudy.MODE_CHANGED"));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.ACTION_DOWN){
            if(isCancel){
                XUtils.cancel();
            }else {
                XUtils.show(R.string.again_cancel);
                isCancel=true;
                handler.postDelayed(r,2000);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
