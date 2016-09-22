package hhh.appstudy.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hhh.appstudy.R;
import hhh.appstudy.adapter.CommonAdapter;
import hhh.appstudy.adapter.ViewHolder;

/**
 * Created by Administrator on 2016/6/28 0028.
 */
public class SingleFragment extends Fragment {
    @ViewInject(R.id.single_listview)
    private PullToRefreshListView listView;
    private String type;
    private List<String> strs;
    private CommonAdapter<String> adapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type=getArguments().getString("tk");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_single,container,false);
        ViewUtils.inject(this,view);
        init();
        return view;
    }

    private void init() {
        strs=new ArrayList<String>();
        for(int i=0;i<10;i++){
            strs.add("single "+type+i);
        }
        adapter=new CommonAdapter<String>(getActivity(),strs,R.layout.item_single) {
            @Override
            public void convert(ViewHolder viewHolder, String item) {
                viewHolder.setText(R.id.single_text,item);
                viewHolder.setImageDrawable(R.id.single_img,R.drawable.next);
            }
        };
        listView.setAdapter(adapter);

        ILoadingLayout startLabels=listView.getLoadingLayoutProxy(true,false);
        startLabels.setPullLabel("下拉刷新");
        startLabels.setRefreshingLabel("正在拉...");
        startLabels.setReleaseLabel("放开刷新...");
        ILoadingLayout endLabels=listView.getLoadingLayoutProxy(false,true);
        endLabels.setPullLabel("上拉刷新");
        endLabels.setRefreshingLabel("正在载入...");
        endLabels.setReleaseLabel("放开刷新...");

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                DateFormat df=new SimpleDateFormat("HH:mm:ss");
                String time=df.format(new Date());
                for(int i=0;i<5;i++){
                    strs.add(i,"头部添加"+i+"数据 "+time);
                }
                new FinishRefresh().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                DateFormat df=new SimpleDateFormat("HH:mm:ss");
                String time=df.format(new Date());
                for(int i=0;i<5;i++){
                    strs.add("底部添加"+i+"数据 "+time);
                }
                new FinishRefresh().execute();
            }
        });
    }

    private class FinishRefresh extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            listView.onRefreshComplete();
            adapter.notifyDataSetChanged();
        }
    }
}
