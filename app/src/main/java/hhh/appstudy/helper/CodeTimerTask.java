package hhh.appstudy.helper;

import android.os.AsyncTask;
import android.provider.Telephony;
import android.widget.TextView;

import hhh.appstudy.R;

/**
 * Created by hhh on 2016/4/25.
 */
public class CodeTimerTask extends AsyncTask<Void,Void,Void> {
    private int time=90;
    private TextView textView;
    private static CodeTimerTask task;
    private static boolean isNew;
    private boolean isRun;

    private CodeTimerTask(){}

    public static CodeTimerTask getInstance(){
        if(task==null){
            task=new CodeTimerTask();
            isNew=true;
        }
        return task;
    }

    public void startTimer(TextView textView){
        this.textView=textView;
        if(isNew){
            execute();
        }
    }

    @Override
    protected void onPreExecute() {
        time=90;
        isNew=false;
        isRun=true;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            for(;time>=0;time--){
                publishProgress();
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        if (textView!=null) {
            textView.setText(String.format("%ds",time));
            if(textView.isEnabled()) {
                textView.setEnabled(false);
            }
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        end();
    }

    private void end(){
        if(textView!=null){
            textView.setEnabled(true);
            textView.setText(R.string.validate_getcode);
        }

        cancel();
    }

    public void cancel(){
        if(task!=null) {
            isRun=false;
            task.cancel();
            task = null;
            isNew = true;
        }
    }

    public boolean isRun() {
        return isRun;
    }
}
