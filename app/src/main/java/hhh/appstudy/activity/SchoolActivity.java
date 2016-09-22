package hhh.appstudy.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.ResType;
import com.lidroid.xutils.view.annotation.ResInject;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.Calendar;

import hhh.appstudy.R;
import hhh.appstudy.entities.Result;
import hhh.appstudy.entities.User;
import hhh.appstudy.helper.Code;
import hhh.appstudy.https.BasicRequestCallBack;
import hhh.appstudy.https.XUtils;
import hhh.appstudy.view.TitleView;
import hhh.appstudy.view.UserInfoView;

/**
 * Created by hhh on 2016/5/19.
 */
public class SchoolActivity extends BaseActivity {
    @ViewInject(R.id.school_title)
    private TitleView title;
    @ViewInject(R.id.school_role)
    private UserInfoView uvRole;
    @ViewInject(R.id.school_school)
    private UserInfoView uvSchool;
    @ViewInject(R.id.school_yuanxi)
    private UserInfoView uvYuanxi;
    @ViewInject(R.id.school_glass)
    private UserInfoView uvGlass;
    @ViewInject(R.id.school_intime)
    private UserInfoView uvIntime;
    @ViewInject(R.id.school_save)
    private Button btnSave;
    @ResInject(id=R.array.role,type= ResType.StringArray)
    private String[] roles;

    private User user;
    private String[] years=new String[50];
    private Boolean needUpdate;

    private View.OnClickListener l=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.title_back:
                    finish();
                    break;
                case R.id.school_save:
                    save();
                    break;
            }
        }
    };

    private UserInfoView.clickListener clickListener=new UserInfoView.clickListener() {
        @Override
        public void onClickListener(View v) {
            switch (v.getId()){
                case R.id.school_intime:
                    Log.i("schoolact","showyear");
                    showYear();
                    break;
                case R.id.school_role:
                    showRole();
                    break;
                case R.id.school_school:
                    SchoolSelectActivity.startActivityForResult(SchoolActivity.this);
                    break;
                case R.id.school_yuanxi:
                    break;
                case R.id.school_glass:
                    break;
            }
        }
    };

    private void save() {
        String school=uvSchool.getText();
        RequestParams params=new RequestParams();
        params.addBodyParameter("token",user.getToken());
        if(!TextUtils.isEmpty(school)&&!school.equals(user.getSchool())){
            needUpdate=true;
            params.addBodyParameter("school",school);
        }
        String yuanxi=uvYuanxi.getText();
        if(!TextUtils.isEmpty(yuanxi)&&!yuanxi.equals(user.getDepartment())){
            needUpdate=true;
            params.addBodyParameter("department",yuanxi);
        }
        String glass=uvGlass.getText();
        if(!TextUtils.isEmpty(glass)&&!glass.equals(user.getGrade_class())){
            needUpdate=true;
            params.addBodyParameter("grade_class",glass);
        }
        String year=uvIntime.getText();
        if(!TextUtils.isEmpty(year)&&!year.equals(user.getYear())){
            needUpdate=true;
            params.addBodyParameter("year",year);
        }
        int role=uvRole.getValue();
        if(role>=0&&role!=user.getRole_id()){
            needUpdate=true;
            params.addBodyParameter("role",String.valueOf(role));
        }
        if(needUpdate){
            XUtils.send(XUtils.UPINFO, params, new BasicRequestCallBack<Result<User>>() {
                @Override
                public void success(Result<User> data) {
                    XUtils.show(data.message);
                    if(data.code ==Result.STATE_SUC){
                        ((MyApp)getApplication()).setU(data.data);
                        setResult(Code.RES_UPDATE_SCHOOL);
                        finish();
                    }
                }
            },true);
        }
    }

    private void showRole() {
        Dialog dialog=new AlertDialog.Builder(this).setItems(roles, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uvRole.setValue(which);
                uvRole.setText(roles[which]);
                dialog.dismiss();
            }
        }).show();
        dialog.getWindow().setGravity(Gravity.TOP|Gravity.CENTER);
    }

    private void showYear() {
        if(years[0]==null){
            Calendar calendar=Calendar.getInstance();
            int y=calendar.get(Calendar.YEAR);
            for(int i=0;i<years.length;i++){
                years[i]=String.format("%d年",y-i);
            }
        }
        Dialog dialog=new AlertDialog.Builder(this).setItems(years, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uvIntime.setText(years[which]);
                dialog.dismiss();
            }
        }).show();
        dialog.getWindow().setGravity(Gravity.TOP|Gravity.CENTER);
    }


    public static void startActivityForResult(Activity activity){
        Intent intent=new Intent(activity,SchoolActivity.class);
        activity.startActivityForResult(intent, Code.REQ_UPDATE_SCHOOL);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school);
        ViewUtils.inject(this);
        initData();
        title.setBackClickListener(l);
        btnSave.setOnClickListener(l);

        uvRole.setClickListener(clickListener);
        uvIntime.setClickListener(clickListener);
        uvSchool.setClickListener(clickListener);
        uvYuanxi.setClickListener(clickListener);
        uvGlass.setClickListener(clickListener);

    }

    private void initData() {
        user=((MyApp)getApplication()).getU();
        if(user==null){
            finish();
            return;
        }
        if(user.getRole_id()>=0) {
            uvRole.setValue(user.getRole_id());
            uvRole.setText(roles[user.getRole_id()]);
        }
        if(user.getSchool()!=null) {
            uvSchool.setText(user.getSchool());
        }
        if(user.getDepartment()!=null) {
            uvYuanxi.setText(user.getDepartment());
        }
        if(user.getGrade_class()!=null){
            uvGlass.setText(user.getGrade_class());
        }
        if(user.getYear()!=null) {
            uvIntime.setText(user.getYear());
        }
    }
}
