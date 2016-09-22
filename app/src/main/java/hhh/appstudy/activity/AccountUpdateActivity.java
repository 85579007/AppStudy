package hhh.appstudy.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;

import hhh.appstudy.R;
import hhh.appstudy.entities.Result;
import hhh.appstudy.entities.User;
import hhh.appstudy.helper.Code;
import hhh.appstudy.https.BasicRequestCallBack;
import hhh.appstudy.https.XUtils;
import hhh.appstudy.view.MEditText;
import hhh.appstudy.view.TitleView;

/**
 * Created by hhh on 2016/5/24.
 */
public class AccountUpdateActivity extends BaseActivity {
    @ViewInject(R.id.account_title)
    private TitleView title;
    @ViewInject(R.id.account_phone)
    private MEditText mePhone;
    @ViewInject(R.id.account_email)
    private MEditText meEmail;

    private User user;
    private Boolean isUpdatePhone;

    private View.OnClickListener l=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.title_back:
                    finish();
                    break;
                case R.id.title_tv_right:
                    save();
                    break;
            }
        }
    };

    private void save() {
        RequestParams params=new RequestParams();
        params.addBodyParameter("token",user.getToken());
        if(isUpdatePhone) {
            String phone = mePhone.getText().trim();
            if (phone.matches(Code.PHONE_MATCH) && !phone.equals(user.getPhone())){
                params.addBodyParameter("phone", phone);
            }else{
                return;
            }
        }else{
            String email=meEmail.getText().trim();
            if(email.matches(Code.EMAIL_MATCH)&&!email.equals(user.getEmail())){
                params.addBodyParameter("email",email);
            }else{
                return;
            }
        }
        XUtils.send(XUtils.UPINFO, params, new BasicRequestCallBack<Result<User>>() {
            @Override
            public void success(Result<User> data) {
                XUtils.show(data.message);
                if(data.code ==Result.STATE_SUC){
                    ((MyApp)getApplication()).setU(data.data);
                    finish();
                }
            }
        },true);
    }

    public static void startActivity(Context context,boolean isUpdatePhone){
        Intent intent=new Intent(context,AccountUpdateActivity.class);
        intent.putExtra("isUpdatePhone",isUpdatePhone);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ViewUtils.inject(this);
        isUpdatePhone=getIntent().getBooleanExtra("isUpdatePhone",false);

        title.setBackClickListener(l);
        title.setRightClickListener(l);

        user=((MyApp)getApplication()).getU();
        if(isUpdatePhone&&user.getPhone()!=null) {
            mePhone.setText(user.getPhone());
        }else{
            meEmail.setText(user.getEmail());
        }
    }
}
