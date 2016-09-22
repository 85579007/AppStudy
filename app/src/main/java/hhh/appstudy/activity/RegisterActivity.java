package hhh.appstudy.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;

import hhh.appstudy.R;
import hhh.appstudy.entities.Result;
import hhh.appstudy.helper.Code;
import hhh.appstudy.https.BasicRequestCallBack;
import hhh.appstudy.https.XUtils;
import hhh.appstudy.view.MEditText;
import hhh.appstudy.view.TitleView;

/**
 * Created by Administrator on 2016/4/24 0024.
 */
public class RegisterActivity extends BaseActivity{
    @ViewInject(R.id.reg_title)
    private TitleView title;
    @ViewInject(R.id.reg_user)
    private MEditText meUser;
    @ViewInject(R.id.reg_pass)
    private MEditText mePwd;
    @ViewInject(R.id.reg_rpass)
    private MEditText meRepwd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ViewUtils.inject(this);

        title.setBackClickListener(clickLis);
        title.setRightClickListener(clickLis);
    }

    private View.OnClickListener clickLis=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.title_back:
                    finish();
                    break;
                case R.id.title_tv_right:
                    signUp();
                    break;
            }
        }
    };

    private void signUp() {
        String phone=getIntent().getStringExtra("phone");
        String user=meUser.getText();
        String pwd=mePwd.getText();
        String repwd=meRepwd.getText();
        if(phone==null){
            XUtils.show(getString(R.string.error));
            return;
        }
        if(!user.matches(Code.EMAIL_MATCH)){
            XUtils.show(getString(R.string.email_format_error));
            return;
        }
        if(!pwd.matches(Code.PWD_MATCH)){
            XUtils.show(getString(R.string.pwd_format_error));
            return;
        }
        if(!pwd.equals(repwd)){
            XUtils.show(getString(R.string.pwd_twice_error));
            return;
        }
        String token= user+"|"+phone+"|"+System.currentTimeMillis();
        Log.i("reg","token="+token);
        token= Base64.encodeToString(token.getBytes(),Base64.NO_WRAP);
        RequestParams params=new RequestParams();
        params.addBodyParameter("phone",phone);
        params.addBodyParameter("pwd",pwd);
        params.addBodyParameter("email",user);
        params.addBodyParameter("token",token);
        Log.i("reg",phone+","+pwd+","+token);
        XUtils.send(XUtils.REG,params, new BasicRequestCallBack<Result<Boolean>>() {
            @Override
            public void success(Result<Boolean> data) {
                XUtils.show(data.message);

                    finish();

            }
        },true);
    }

    public static void startActivity(Context context, String phone){
        Intent in=new Intent(context,RegisterActivity.class);
        in.putExtra("phone",phone);
        context.startActivity(in);
    }
}
