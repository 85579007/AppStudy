package hhh.appstudy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import hhh.appstudy.R;

/**
 * Created by Administrator on 2016/5/16 0016.
 */
public class CircleImageView extends ImageView {
    private int instroke;
    private int outstroke;
    private int width;
    private int height;
    private int size;

    public CircleImageView(Context context) {
        super(context);
        init(null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a=getContext().obtainStyledAttributes(attrs, R.styleable.CircleImageView);
        int N=a.getIndexCount();
        for(int i=0;i<N;i++){
            int index=a.getIndex(i);
            switch (index){
                case R.styleable.CircleImageView_cv_instroke:
                    instroke=a.getDimensionPixelSize(index,0);
                    break;
                case R.styleable.CircleImageView_cv_outstroke:
                    outstroke=a.getDimensionPixelSize(index,0);
                    break;
            }
        }
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable=getDrawable();
        Paint p;
        if(drawable!=null&&drawable instanceof BitmapDrawable){
            Bitmap srcBmp=((BitmapDrawable) drawable).getBitmap();
            if(srcBmp!=null){
                srcBmp=createCircleBitmap(srcBmp);
                Rect src=new Rect(0,0,size,size);
                Rect dst=new Rect((width-size)/2,(height-size)/2,(width+size)/2,(height+size)/2);
                canvas.drawBitmap(srcBmp,src,dst,null);
                p=new Paint();
                p.setAntiAlias(true);
                p.setColor(Color.RED);
                p.setStrokeWidth(instroke);
                p.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(width/2,height/2,(size+instroke)/2,p);
                p.setARGB(150,255,0,0);
                p.setStrokeWidth(outstroke);
                canvas.drawCircle(width/2,height/2,(size+instroke*2+outstroke)/2,p);
            }else{
                super.onDraw(canvas);
            }
        }else{
            super.onDraw(canvas);
        }
    }

    private Bitmap createCircleBitmap(Bitmap bitmap){
        size=(width>height?height:width)-2*instroke-2*outstroke;
        Bitmap output=Bitmap.createBitmap(size,size,Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(output);
        Paint p=new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.WHITE);
        canvas.drawARGB(0,0,0,0);
        canvas.drawCircle(size/2,size/2,size/2,p);
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Rect src=new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
        Rect dst=new Rect(0,0,size,size);
        canvas.drawBitmap(bitmap,src,dst,p);
        return output;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width=w;
        height=h;
    }

    public int getInstroke() {
        return instroke;
    }

    public void setInstroke(int instroke) {
        this.instroke = instroke;
    }

    public int getOutstroke() {
        return outstroke;
    }

    public void setOutstroke(int outstroke) {
        this.outstroke = outstroke;
    }
}
