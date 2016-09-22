package hhh.appstudy.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;

import hhh.appstudy.R;
import hhh.appstudy.entities.Result;
import hhh.appstudy.https.BasicRequestCallBack;
import hhh.appstudy.https.XUtils;
import hhh.appstudy.view.TitleView;

/**
 * Created by Administrator on 2016/4/24 0024.
 */
public class ResetpwdActivity extends BaseActivity {
    @ViewInject(R.id.reset_email)
    private TextView email;
    @ViewInject(R.id.reset_btn)
    private Button btn;
    @ViewInject(R.id.reset_title)
    private TitleView title;

    private View.OnClickListener l=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RequestParams params=new RequestParams();
            params.addBodyParameter("email",email.getText().toString());
            XUtils.send(XUtils.RESET, params, new BasicRequestCallBack<Result<Boolean>>() {
                @Override
                public void success(Result<Boolean> data) {
                    XUtils.show(data.message);
                    finish();
                }
            },true);
        }
    };

    public static void startActivity(Context context,String phone){
        Intent in=new Intent(context,ResetpwdActivity.class);
        in.putExtra("phone",phone);
        context.startActivity(in);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpwd);
        ViewUtils.inject(this);
        btn.setOnClickListener(l);
        btn.setEnabled(false);
        getEmailByPhone(getIntent().getStringExtra("phone"));
    }

    private void getEmailByPhone(String phone) {
        RequestParams params=new RequestParams();
        params.addBodyParameter("phone",phone);
        XUtils.send(XUtils.GETEMAIL, params, new BasicRequestCallBack<Result<String>>() {
            @Override
            public void success(Result<String> data) {
                XUtils.show(data.data);
                if(data.code ==Result.STATE_SUC){
                    btn.setEnabled(true);
                    email.setText(data.data);
                }
            }
        },true);
    }

}
