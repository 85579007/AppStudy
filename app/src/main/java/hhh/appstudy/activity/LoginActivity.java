package hhh.appstudy.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import hhh.appstudy.R;
import hhh.appstudy.entities.Result;
import hhh.appstudy.entities.User;
import hhh.appstudy.https.BasicRequestCallBack;
import hhh.appstudy.https.XUtils;
import hhh.appstudy.view.MEditText;
import hhh.appstudy.view.TitleView;

/**
 * Created by hhh on 2016/4/21.
 */
public class LoginActivity extends BaseActivity {
    @ViewInject(R.id.login_title)
    private TitleView title;
    @ViewInject(R.id.login_user)
    private MEditText meAccount;
    @ViewInject(R.id.login_pass)
    private MEditText mePwd;
    @ViewInject(R.id.login_sign_in)
    private Button btSign;

    private View.OnClickListener clickListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.title_back:
                    finish();
                    break;
                case R.id.title_tv_right:
                    break;
                case R.id.login_sign_in:
                    login();
                    break;
            }
        }
    };

    private void login() {
        String account=meAccount.getText();
        String pwd=mePwd.getText();
        RequestParams params=new RequestParams();
        if(account.matches("^1(3|4|5|7|8)\\d{9}$")){
            params.addBodyParameter("phone",account);
        }else if(account.matches("\\w+@\\w+\\.(com|cn)(.cn)?$")){
            params.addBodyParameter("email",account);
        }else{
            XUtils.show(R.string.account_format_error);
            return ;
        }
        if(!pwd.matches("^\\w{6,20}$")){
            XUtils.show(R.string.pwd_format_error);
            return;
        }
        params.addBodyParameter("pwd",pwd);
        XUtils.send( XUtils.LOGIN, params, new BasicRequestCallBack<Result<User>>() {
            @Override
            public void success(Result<User> data) {
                XUtils.show(data.message);
                if(data.code ==Result.STATE_SUC) {
                    ((MyApp) getApplication()).setU(data.data);
//                     CenterActivity.startActivity(LoginActivity.this);
                    TkActivity.startActivity(LoginActivity.this);
                }
            }
        },true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);

        title.setBackClickListener(clickListener);
        title.setRightClickListener(clickListener);
        btSign.setOnClickListener(clickListener);
    }

    @OnClick({R.id.login_reset_pwd,R.id.login_sign_up})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.login_reset_pwd:
//                ValidateActivity.startActivity(this,ValidateActivity.ACTION_RESET_PWD);
                ResetpwdActivity.startActivity(LoginActivity.this,"13500001111");
                break;
            case R.id.login_sign_up:
//                ValidateActivity.startActivity(this,ValidateActivity.ACTION_REG);
                RegisterActivity.startActivity(LoginActivity.this,"13500001111");
                break;
        }
    }

}
