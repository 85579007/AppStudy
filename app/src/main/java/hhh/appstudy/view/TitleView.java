package hhh.appstudy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hhh.appstudy.R;
import hhh.appstudy.https.XUtils;

/**
 * Created by hhh on 2016/4/13.
 */
public class TitleView extends RelativeLayout {
    private TextView title;
    private TextView tvRight;
    private ImageView back;
    private CircleImageView imgRight;
    private String curUrl;

    public TitleView(Context context) {
        super(context);
        init(null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_title, this);
        title= (TextView) findViewById(R.id.title_title);
        tvRight= (TextView) findViewById(R.id.title_tv_right);
        back=(ImageView)findViewById(R.id.title_back);
        imgRight=(CircleImageView) findViewById(R.id.title_img_right);
        if(attrs==null){
            return;
        }
        TypedArray a=getContext().obtainStyledAttributes(attrs, R.styleable.TitleView);
        int N=a.getIndexCount();
        for(int i=0;i<N;i++){
            int index=a.getIndex(i);
            switch(index){
                case R.styleable.TitleView_tv_back_visibility:
                    setVisibility(back,a.getInt(index,0));
                    break;
                case R.styleable.TitleView_tv_img_right_visibility:
                    setVisibility(imgRight,a.getInt(index,2));
                    break;
                case R.styleable.TitleView_tv_right:
                    tvRight.setText(a.getString(index));
                    break;
                case R.styleable.TitleView_tv_tv_right_visibility:
                    setVisibility(tvRight,a.getInt(index,2));
                    break;
                case R.styleable.TitleView_tv_title:
                    title.setText(a.getString(index));
                    break;
            }
        }
        a.recycle();
    }

    private void setVisibility(View v,int visable){
        switch(visable){
            case 0:
                v.setVisibility(View.VISIBLE);
                break;
            case 1:
                v.setVisibility(View.INVISIBLE);
                break;
            case 2:
                v.setVisibility(View.GONE);
                break;
        }
    }

    public void setTitle(String text){
        title.setText(text);
    }

    public void setRightText(String text){
        tvRight.setText(text);
    }

    public void setBackClickListener(View.OnClickListener listener){
        back.setOnClickListener(listener);
    }

    public void setRightClickListener(View.OnClickListener listener){
        tvRight.setOnClickListener(listener);
        imgRight.setOnClickListener(listener);
    }

    public void setImageUrl(String url){
        if(url.equals(curUrl)){
            return;
        }
        curUrl=url;
        XUtils.display(imgRight,url);
    }

    public void setRightTextVisible(int visible){
        tvRight.setVisibility(visible);
    }
}
