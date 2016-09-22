package hhh.appstudy.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import hhh.appstudy.https.XUtils;

/**
 * Created by Administrator on 2016/6/27 0027.
 */
public class ViewHolder {
    private SparseArray<View> views;
    private View convertView;

    public ViewHolder(Context context, int position, int layoutId, ViewGroup parent) {
        views = new SparseArray<View>();
        convertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        convertView.setTag(this);
    }

    public View getConvertView() {
        return convertView;
    }

    public static ViewHolder get(Context context, View convertView, int position, int layoutId, ViewGroup parent) {
        if (convertView == null) {
            return new ViewHolder(context, position, layoutId, parent);
        }
        return (ViewHolder) convertView.getTag();
    }

    public <T extends View> T getView(int id) {
        View view = views.get(id);
        if (view == null) {
            view = convertView.findViewById(id);
            views.put(id, view);
        }
        return (T) view;
    }

    public ViewHolder setText(int id,String txt){
        TextView textView=getView(id);
        textView.setText(txt);
        return this;
    }

    public ViewHolder setImageDrawable(int id,int drawId){
        ImageView imageView=getView(id);
        imageView.setImageResource(drawId);
        return this;
    }

    public ViewHolder setImageBitmap(int id, Bitmap bm){
        ImageView imageView=getView(id);
        imageView.setImageBitmap(bm);
        return this;
    }

    public ViewHolder setImageByUrl(int id,String url){
        ImageView imageView=getView(id);
        XUtils.display(imageView,url);
        return this;
    }
}
