package hhh.appstudy.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import hhh.appstudy.R;

/**
 * Created by hhh on 2016/5/19.
 */
public class FeedbackDialog extends Dialog implements TextWatcher, View.OnClickListener {
    private final int MAX_NUM=140;
    private EditText etSuggest;
    private TextView tvNum;
    private Button btnSend;
    private MyClickListener myClickListener;

    public void setMyClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface MyClickListener{
        public void OnClick(DialogInterface dialogInterface,String string);
    }

    public FeedbackDialog(Context context) {
        super(context);
    }

    public FeedbackDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected FeedbackDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_feedback);
        etSuggest=(EditText)findViewById(R.id.feedback_suggest);
        tvNum=(TextView)findViewById(R.id.feedback_num);
        btnSend=(Button)findViewById(R.id.feedback_send);

        etSuggest.addTextChangedListener(this);
        btnSend.setOnClickListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(s.length()>MAX_NUM){
            s.delete(MAX_NUM,s.length());
        }else {
            tvNum.setText(String.valueOf(140-s.length()));
        }
    }

    @Override
    public void onClick(View v) {
        if(myClickListener!=null){
            myClickListener.OnClick(this,etSuggest.getText().toString());
        }
    }
}
