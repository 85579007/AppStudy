package hhh.appstudy.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import hhh.appstudy.R;
import hhh.appstudy.entities.User;
import hhh.appstudy.view.TitleView;
import hhh.appstudy.view.UserInfoView;

/**
 * Created by hhh on 2016/5/18.
 */
public class SafeActivity extends BaseActivity {
    @ViewInject(R.id.safe_title)
    private TitleView title;
    @ViewInject(R.id.safe_phone)
    private UserInfoView uvPhone;
    @ViewInject(R.id.safe_email)
    private UserInfoView uvEmail;
    @ViewInject(R.id.safe_modifypwd)
    private UserInfoView uvModifyPwd;
    @ViewInject(R.id.safe_save)
    private Button btnSave;

    private User user;

    private View.OnClickListener l=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.title_back:
                    finish();
                    break;
                case R.id.safe_email:
                    ValidateActivity.startActivity(SafeActivity.this,ValidateActivity.ACTION_UPDATE_EMAIL);
                    break;
                case R.id.safe_modifypwd:
                    break;
                case R.id.safe_phone:
                    ValidateActivity.startActivity(SafeActivity.this,ValidateActivity.ACTION_UPDATE_PHONE);
                    break;
                case R.id.safe_save:
                    break;
            }
        }
    };

    public static void startActivity(Context context){
        Intent intent=new Intent(context,SafeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe);
        ViewUtils.inject(this);

        title.setBackClickListener(l);
        uvPhone.setOnClickListener(l);
        uvEmail.setOnClickListener(l);
        uvModifyPwd.setOnClickListener(l);
        btnSave.setOnClickListener(l);

        initData();

    }

    private void initData() {
        user=((MyApp)getApplication()).getU();
        String phone=user.getPhone();
        if(!TextUtils.isEmpty(phone)){
            phone=phone.replace(phone.substring(3,9),"*******");
            uvPhone.setText(phone);
        }
        String email=user.getEmail();
        if(!TextUtils.isEmpty(email)){
            String[] strs=email.split("@");
            if(strs[0].length()>=3){
                strs[0]=strs[0].replace(strs[0].substring(3),"******");
            }else{
                strs[0]=strs[0].substring(0,1)+"******";
            }
            uvEmail.setText(strs[0]+"@"+strs[1]);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }
}
