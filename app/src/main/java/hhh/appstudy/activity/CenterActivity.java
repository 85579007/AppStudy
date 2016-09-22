package hhh.appstudy.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;

import hhh.appstudy.R;
import hhh.appstudy.entities.Result;
import hhh.appstudy.entities.User;
import hhh.appstudy.helper.Code;
import hhh.appstudy.https.BasicRequestCallBack;
import hhh.appstudy.https.XUtils;
import hhh.appstudy.view.FeedbackDialog;
import hhh.appstudy.view.TitleView;

/**
 * Created by hhh on 2016/4/22.
 */
public class CenterActivity extends BaseActivity implements FeedbackDialog.MyClickListener {
    @ViewInject(R.id.center_title)
    private TitleView title;
    @ViewInject(R.id.center_photo)
    private ImageView photo;
    @ViewInject(R.id.center_nick)
    private TextView tvNick;
    @ViewInject(R.id.center_account)
    private TextView tvAccount;
    @ViewInject(R.id.center_school)
    private TextView tvSchool;
    @ViewInject(R.id.center_list)
    private ListView list;
    @ViewInject(R.id.center_exit)
    private Button btExit;

    private User user;

    private String[] strlist = new String[]{"校内信息", "账号与安全", "系统设置", "意见反馈"};
    private int[] piclist = new int[]{R.drawable.user, R.drawable.unlogin, R.drawable.eye, R.drawable.del};

    private View.OnClickListener l=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.title_back:
                    finish();
                    break;
                case R.id.center_photo:
                    UserinfoActivity.startActivityForReuslt(CenterActivity.this);
                    break;
                case R.id.center_exit:
                    ((MyApp)getApplication()).setU(null);
                    finish();
                    break;
            }
        }
    };

    public static void startActivity(Context context) {
        Intent in = new Intent(context, CenterActivity.class);
        context.startActivity(in);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);
        ViewUtils.inject(this);

        init();

        title.setBackClickListener(l);
        photo.setOnClickListener(l);
        btExit.setOnClickListener(l);
    }

    private void initUser(){
        user=((MyApp)getApplication()).getU();
        if(user!=null){
            XUtils.display(photo,user.getPhoto_url());
            tvNick.setText("昵称:"+user.getNick());
            tvAccount.setText("账号:"+user.getName());
            tvSchool.setText("学校:"+user.getSchool());
        }
    }

    private void init() {
        initUser();
        SimpleAdapter adapter = new SimpleAdapter(this, getData(), R.layout.layout_center_listitem,
                new String[]{"item_txt", "item_pic"},
                new int[]{R.id.center_listitem_label, R.id.center_listitem_pic});
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        SchoolActivity.startActivityForResult(CenterActivity.this);
                        break;
                    case 1:
                        SafeActivity.startActivity(CenterActivity.this);
                        break;
                    case 2:
                        SettingActivity.startActivity(CenterActivity.this);
                        break;
                    case 3:
                        showFeedback();
                        break;
                }
            }
        });
    }

    private void showFeedback() {
        FeedbackDialog dialog=new FeedbackDialog(this);
        dialog.setMyClickListener(this);
        dialog.show();
    }

    private ArrayList<HashMap<String, Object>> getData() {
        ArrayList<HashMap<String, Object>> mlist = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < 4; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("item_txt", strlist[i]);
            map.put("item_pic", piclist[i]);
            mlist.add(map);
        }
        return mlist;
    }

    @Override
    public void OnClick(DialogInterface dialogInterface, String string) {
        dialogInterface.dismiss();
        sendMessage(string);
    }

    private void sendMessage(String string) {
        if(string.length()<15){
            XUtils.show(R.string.minletter_error);
            return;
        }
        String token=((MyApp)getApplication()).getU().getToken();
        if(token==null){
            XUtils.show(R.string.needlogin_error);
            return;
        }
        RequestParams params=new RequestParams();
        params.addBodyParameter("token",token);
        params.addBodyParameter("content",string);
        XUtils.send(XUtils.FEEDBACK, params, new BasicRequestCallBack<Result<Boolean>>() {
            @Override
            public void success(Result<Boolean> data) {
                XUtils.show(data.message);
            }
        },true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode== Code.REQ_UPDATE_SCHOOL&&resultCode==Code.RES_UPDATE_SCHOOL){

        }else if(requestCode==Code.REQ_UPDATE_INFO&&resultCode==Code.RES_UPDATE_INFO){
            Log.i("centeract","up info");
            initUser();
        }
    }
}
