package hhh.appstudy.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import hhh.appstudy.R;
import hhh.appstudy.helper.Code;
import hhh.appstudy.helper.CodeTimerTask;
import hhh.appstudy.helper.MyTextWatcher;
import hhh.appstudy.https.XUtils;
import hhh.appstudy.utils.Loading;
import hhh.appstudy.view.MEditText;
import hhh.appstudy.view.TitleView;

/**
 * Created by hhh on 2016/4/22.
 */
public class ValidateActivity extends BaseActivity {
    public static final int ACTION_REG=1;
    public static final int ACTION_RESET_PWD=2;
    public static final int ACTION_UPDATE_PHONE=3;
    public static final int ACTION_UPDATE_EMAIL=4;

    @ViewInject(R.id.validate_title)
    private TitleView title;
    @ViewInject(R.id.validate_phone)
    private MEditText mePhone;
    @ViewInject(R.id.validate_code)
    private MEditText meCode;
    @ViewInject(R.id.validate_get_code)
    private TextView tvGetCode;
    private String phone;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    XUtils.show((String)msg.obj);
                    break;
                case 1:
                    XUtils.show(msg.arg1);
                    if(msg.arg2==1){
                        CodeTimerTask.getInstance().cancel();
                    }
                    break;
            }
        }
    };

    private View.OnClickListener clickListen= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.title_back:
                    finish();
                    break;
                case R.id.title_tv_right:
                    phone=mePhone.getText();
                    String code=meCode.getText();
                    if(!phone.matches(Code.PHONE_MATCH)){
                        XUtils.show(R.string.phone_format_error);
                        return;
                    }
                    Loading.show();
                    SMSSDK.submitVerificationCode("86",phone,code);
                    break;
                case R.id.validate_get_code:
                    getCode();
                    break;
            }
        }
    };

    private void getCode() {
        phone=mePhone.getText();
        if(phone.matches(Code.PHONE_MATCH)){
            SMSSDK.getVerificationCode("86",phone);
            CodeTimerTask.getInstance().startTimer(tvGetCode);
        }
    }

    private TextWatcher phoneWatcher =new MyTextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            if(s.toString().matches(Code.PHONE_MATCH)){
                tvGetCode.setEnabled(true);
            }else{
                tvGetCode.setEnabled(false);
            }
        }
    };

    private TextWatcher codeWatcher=new MyTextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            if(s.length()==4){
                title.setRightTextVisible(View.VISIBLE);
            }else{
                title.setRightTextVisible(View.GONE);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SMSSDK.initSDK(this,"120d698547b28","0ca1265b4c5827cb9db111cf2cf1f113");
        setContentView(R.layout.activity_validate);
        ViewUtils.inject(this);
        title.setBackClickListener(clickListen);
        title.setRightClickListener(clickListen);
        mePhone.addTextChangeListener(phoneWatcher);
        meCode.addTextChangeListener(codeWatcher);
        tvGetCode.setOnClickListener(clickListen);

        SMSSDK.registerEventHandler(eh);
        if(phone!=null){
            mePhone.setText(phone);
        }
        if(CodeTimerTask.getInstance().isRun()){
            CodeTimerTask.getInstance().startTimer(tvGetCode);
        }
    }

    private EventHandler eh=new EventHandler(){
        @Override
        public void afterEvent(int event, int result, Object data) {
           if(result==SMSSDK.RESULT_COMPLETE){
               switch (event){
                   case SMSSDK.EVENT_GET_VERIFICATION_CODE:
                       handler.sendMessage(handler.obtainMessage(0,R.string.code_send_success,0));
                       break;
                   case SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE:
                       Loading.dismiss();
                       switch (getIntent().getIntExtra("action",-1)){
                           case ACTION_REG:
                               RegisterActivity.startActivity(ValidateActivity.this,phone);
                               break;
                           case ACTION_RESET_PWD:
                               ResetpwdActivity.startActivity(ValidateActivity.this,phone);
                               break;
                           case ACTION_UPDATE_EMAIL:
                               AccountUpdateActivity.startActivity(ValidateActivity.this,false);
                               break;
                           case ACTION_UPDATE_PHONE:
                               break;
                       }
                       finish();
                       break;
               }
           }else{
               Log.e("study","======code error======"+ JSON.toJSONString(data));
               switch (event){
                   case SMSSDK.EVENT_GET_VERIFICATION_CODE:
                       handler.sendMessage(handler.obtainMessage(1,R.string.code_send_fail,1));
                       break;
                   case SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE:
                       handler.sendMessage(handler.obtainMessage(1,R.string.code_validate_fail,0));
                       break;
               }
           }
        }
    };

    public static void startActivity(Context context, int action){
        Intent in=new Intent(context,ValidateActivity.class);
        in.putExtra("action",action);
        context.startActivity(in);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }
}
