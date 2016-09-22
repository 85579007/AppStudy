package hhh.appstudy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hhh.appstudy.R;
import hhh.appstudy.https.XUtils;

/**
 * Created by hhh on 2016/5/17.
 */
public class UserInfoView extends RelativeLayout implements View.OnClickListener, View.OnFocusChangeListener,TextWatcher {
    private RelativeLayout rlRoot;
    private TextView tvLabel;
    private EditText etInput;
    private ImageView photo;
    private ImageView next;
    //private OnClickListener l;
    private clickListener l;
    private boolean exEnable;
    private int value;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public interface clickListener{
        public void onClickListener(View v);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public UserInfoView(Context context) {
        super(context);
        init(null);
    }

    public UserInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public UserInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_userinfo,this);
        rlRoot=(RelativeLayout)findViewById(R.id.userinfo_root);
        tvLabel=(TextView)findViewById(R.id.userinfo_label);
        etInput =(EditText) findViewById(R.id.userinfo_edit);
        photo=(ImageView) findViewById(R.id.userinfo_photo);
        next=(ImageView)findViewById(R.id.userinfo_next);
        this.setOnClickListener(this);
        etInput.setOnFocusChangeListener(this);
        etInput.addTextChangedListener(this);
        if(attrs==null){
            return;
        }
        TypedArray a=getContext().obtainStyledAttributes(attrs,R.styleable.UserInfoView);
        int N=a.getIndexCount();
        for(int i=0;i<N;i++){
            int index=a.getIndex(i);
            switch (index){
                case R.styleable.UserInfoView_uv_input_enable:
                    exEnable=a.getBoolean(index,false);
                    etInput.setEnabled(exEnable);
                    break;
                case R.styleable.UserInfoView_uv_input_hint:
                    etInput.setHint(a.getString(index));
                    break;
                case R.styleable.UserInfoView_uv_input_visible:
                    setVisibility(etInput,a.getInt(index,0));
                    break;
                case R.styleable.UserInfoView_uv_label:
                    tvLabel.setText(a.getString(index));
                    break;
                case R.styleable.UserInfoView_uv_photo_visible:
                    setVisibility(photo,a.getInt(index,0));
                    break;
                case R.styleable.UserInfoView_uv_next_visible:
                    setVisibility(next,a.getInt(index,0));
                    break;
            }
        }
        a.recycle();
    }

    private void setVisibility(View view,int visible){
        switch (visible){
            case 0:
                view.setVisibility(View.VISIBLE);
                break;
            case 1:
                view.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Log.i("userinfoview","onclick");
        if(!exEnable&&l!=null){
            //l.onClick(v);
//            rlRoot.requestFocus();
            rlRoot.setSelected(true);
            l.onClickListener(v);
        }else{
//            setEnabled(true);
            //请求获得焦点
            etInput.requestFocus();
//            etInput.requestFocusFromTouch();
//            etInput.selectAll();
            //调用系统输入法
            InputMethodManager inputManager = (InputMethodManager) etInput
                    .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(etInput, 0);

        }
    }

//    @Override
//    public void setOnClickListener(OnClickListener l) {
//        this.l=l;
//    }

    public void setClickListener(clickListener l){
        this.l=l;
    }

    public void setText(String txt){
        etInput.setText(txt);
    }

    public void setText(int resId){
        etInput.setText(resId);
    }

    public String getText(){
        return etInput.getText().toString().trim();
    }

    public void setImagePhoto(Bitmap bmp){
        photo.setImageBitmap(bmp);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        rlRoot.setSelected(hasFocus);
        if(exEnable){
            Log.i("userinfoview edittext",String.valueOf(hasFocus));
        }else {
            Log.i("userinfoview focus", String.valueOf(hasFocus));
        }
//        if(hasFocus){
//            setEnabled(true);
//        }
    }

    public void setPhotoUrl(String photo_url) {
        XUtils.display(photo,photo_url);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction()== MotionEvent.ACTION_UP){
            onClick(this);
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

}
