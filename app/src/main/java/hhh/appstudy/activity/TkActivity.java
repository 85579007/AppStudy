package hhh.appstudy.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.ResType;
import com.lidroid.xutils.view.annotation.ResInject;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.TabPageIndicator;

import java.util.Arrays;

import hhh.appstudy.R;
import hhh.appstudy.adapter.MyPagerAdapter;
import hhh.appstudy.entities.User;
import hhh.appstudy.view.TitleView;

/**
 * Created by Administrator on 2016/6/27 0027.
 */
public class TkActivity extends BaseActivity {
    @ViewInject(R.id.tk_vp)
    private ViewPager viewPager;
    @ViewInject(R.id.tk_indicator)
    private TabPageIndicator indicator;
    @ViewInject(R.id.tk_title)
    private TitleView titleView;
    @ResInject(id=R.array.tk,type= ResType.StringArray)
    private String[] tkType;
    private User user;

    private View.OnClickListener l=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.title_back:
                    finish();
                    break;
                case R.id.title_img_right:
                    CenterActivity.startActivity(TkActivity.this);
                    break;
            }
        }
    };

    public static void  startActivity(Context context){
        Intent intent=new Intent(context,TkActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tk);
        ViewUtils.inject(this);

        titleView.setBackClickListener(l);
        titleView.setRightClickListener(l);

        init();
    }

    private void init() {
        FragmentPagerAdapter pagerAdapter=new MyPagerAdapter(getSupportFragmentManager(), Arrays.asList(tkType));
        viewPager.setAdapter(pagerAdapter);
        indicator.setViewPager(viewPager);

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        user=((MyApp)getApplication()).getU();
        if(user!=null){
            titleView.setImageUrl(user.getPhoto_url());
        }
    }

}
