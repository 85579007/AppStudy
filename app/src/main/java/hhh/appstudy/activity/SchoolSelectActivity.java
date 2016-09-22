package hhh.appstudy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import hhh.appstudy.R;
import hhh.appstudy.helper.Code;

/**
 * Created by Administrator on 2016/5/22 0022.
 */
public class SchoolSelectActivity extends BaseActivity {
    @ViewInject(R.id.select_title)
    private EditText etInput;
    @ViewInject(R.id.select_list)
    private ListView items;

    private SchoolAdapter adapter;

    public class SchoolAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }

    public static void startActivityForResult(Activity activity){
        Intent intent=new Intent(activity,SchoolSelectActivity.class);
        activity.startActivityForResult(intent, Code.REQ_SELECT_SCHOOL);
    }

    @OnClick({R.id.select_back,R.id.select_searchbtn})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.select_back:
                finish();
                break;
            case R.id.select_searchbtn:
                break;
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_school);
        ViewUtils.inject(this);
    }
}
