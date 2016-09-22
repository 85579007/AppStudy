package hhh.appstudy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import hhh.appstudy.R;

/**
 * Created by hhh on 2016/4/15.
 */
public class MEditText extends RelativeLayout implements View.OnFocusChangeListener, View.OnClickListener, View.OnTouchListener, TextWatcher {
    public static final int INPUT_NORMAL=0;
    public static final int INPUT_PASSWORD=1;
    public static final int INPUT_NUMBER=3;

    private RelativeLayout rlRoot;
    private ImageView imgLabel;
    private EditText etText;
    private ImageView imgEye;
    private ImageView imgDel;
    private boolean eyeEnable;
    private boolean delEnable;

    public MEditText(Context context) {
        super(context);
        init(null);
    }

    public MEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        LayoutInflater.from(getContext()).inflate(R.layout.layout_medit,this);
        rlRoot=(RelativeLayout)findViewById(R.id.medit_root);
        imgLabel=(ImageView)findViewById(R.id.medit_label);
        etText=(EditText)findViewById(R.id.medit_input);
        imgEye=(ImageView)findViewById(R.id.medit_show);
        imgDel=(ImageView)findViewById(R.id.medit_del);
        etText.setOnFocusChangeListener(this);
        etText.addTextChangedListener(this);
        imgEye.setOnTouchListener(this);
        imgDel.setOnClickListener(this);
        if(attrs==null){
            return;
        }
        TypedArray a=getContext().obtainStyledAttributes(attrs,R.styleable.MEditText);
        int N=a.getIndexCount();
        for(int i=0;i<N;i++){
            int index=a.getIndex(i);
            switch (index){
                case R.styleable.MEditText_me_del_enable:
                    delEnable=a.getBoolean(index,false);
                    break;
                case R.styleable.MEditText_me_eye_enable:
                    eyeEnable=a.getBoolean(index,false);
                    break;
                case R.styleable.MEditText_me_hint:
                    setHint(a.getString(index));
                    break;
                case R.styleable.MEditText_me_label_src:
                    imgLabel.setImageDrawable(a.getDrawable(index));
                    break;
                case R.styleable.MEditText_me_text:
                    setText(a.getString(index));
                    break;
                case R.styleable.MEditText_me_input_type:
                    setInputType(a.getInt(index,0));
                    break;
                case R.styleable.MEditText_me_max_length:
                    int max=a.getInt(index,20);
                    if(max!=-1) {
                        setMaxLength(max);
                    }
                    break;
            }
        }
        a.recycle();
    }

    private void setMaxLength(int anInt) {
        etText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(anInt)});
    }

    private void setInputType(int anInt) {
        switch (anInt){
            case INPUT_NORMAL:
                etText.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_NORMAL);
                break;
            case INPUT_PASSWORD:
                etText.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case INPUT_NUMBER:
                etText.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL);
                break;
        }
    }

    public String getText(){
        return etText.getText().toString();
    }
    public void setText(String text){
        etText.setText(text);
    }

    public void setHint(String text){
        etText.setHint(text);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        rlRoot.setSelected(hasFocus);
        if(delEnable&&hasFocus&&etText.getText().length()>0){
            imgDel.setVisibility(View.VISIBLE);
        }else if(delEnable&&!hasFocus){
            imgDel.setVisibility(View.GONE);
        }
        if(eyeEnable&&hasFocus){
            imgEye.setVisibility(View.VISIBLE);
        }else{
            imgEye.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        etText.getText().clear();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            setInputType(INPUT_NORMAL);
            return true;
        }else if(event.getAction()==MotionEvent.ACTION_UP){
            setInputType(INPUT_PASSWORD);
            return true;
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(s.length()>0){
            imgDel.setVisibility(VISIBLE);
        }else{
            imgDel.setVisibility(GONE);
        }
    }

    public void addTextChangeListener(TextWatcher watcher){
        etText.addTextChangedListener(watcher);
    }
}
