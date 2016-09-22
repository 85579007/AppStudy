package hhh.appstudy.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/6/27 0027.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    private List<T> list;
    private Context context;
    private int layoutId;

    public CommonAdapter(Context context,List list,int layoutId) {
        this.context=context;
        this.list=list;
        this.layoutId=layoutId;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=ViewHolder.get(context,convertView,position,layoutId,parent);
        convert(viewHolder,getItem(position));
        return viewHolder.getConvertView();
    }

    public abstract void convert(ViewHolder viewHolder, T item);

}
