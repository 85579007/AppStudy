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
public class UpdatepwdActivity extends BaseActivity {
    @ViewInject(R.id.pwd_title)
    private TitleView title;
    @ViewInject(R.id.pwd_oldpwd)
    private MEditText meOldpwd;
    @ViewInject(R.id.pwd_newpwd)
    private MEditText meNewpwd;
    @ViewInject(R.id.pwd_repwd)
    private MEditText meRepwd;

    private User user;

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
        String oldpwd=meOldpwd.getText().trim();
        final String newpwd=meNewpwd.getText().trim();
        String repwd=meRepwd.getText().trim();
        if(!oldpwd.matches(Code.PWD_MATCH)){
            XUtils.show(R.string.pwd_format_error);
            return ;
        }
        if(!newpwd.matches(Code.PWD_MATCH)){
            XUtils.show(R.string.pwd_format_error);
            return ;
        }
        if(!newpwd.equals(repwd)){
            XUtils.show(R.string.pwd_twice_error);
            return ;
        }
        RequestParams params=new RequestParams();
        params.addBodyParameter("token",user.getToken());
        params.addBodyParameter("pwd",newpwd);
        params.addBodyParameter("oldpwd",oldpwd);
        XUtils.send(XUtils.UPINFO, params, new BasicRequestCallBack<Result<Boolean>>() {
            @Override
            public void success(Result<Boolean> data) {
                XUtils.show(data.message);
                if(data.code ==Result.STATE_SUC){
                    ((MyApp)getApplication()).getU().setPwd(newpwd);
                    finish();
                }
            }
        },true);
    }

    public static void startActivity(Context context){
        Intent intent=new Intent(context,UpdatepwdActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwdupdate);
        ViewUtils.inject(this);
        user=((MyApp)getApplication()).getU();

        title.setBackClickListener(l);
        title.setRightClickListener(l);
    }
}
