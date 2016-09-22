package hhh.appstudy.https;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import hhh.appstudy.R;
import hhh.appstudy.utils.Loading;

/**
 * Created by hhh on 2016/4/22.
 */
public abstract class BasicRequestCallBack<T> extends RequestCallBack<String> {
    private Type type;

    public BasicRequestCallBack(){
        Type superClass= this.getClass().getGenericSuperclass();
        this.type=((ParameterizedType)superClass).getActualTypeArguments()[0];
    }

    @Override
    public void onSuccess(ResponseInfo<String> responseInfo) {
        Loading.dismiss();
        if(responseInfo!=null){
            String json=responseInfo.result;
            Log.i("AppStudy json",json);
            if(json.matches("^\\{.*\\}$")){
                T t= JSON.parseObject(json,type);
                //T t= JSON.parseArray(json,type);
                if(t!=null) {
                   // Log.i("AppStudy",t.toString());
                    success(t);
                }else{
                    XUtils.show(R.string.no_data_return);
                }
            }else{
//                XUtils.show("返回格式无法解析");
                XUtils.show(R.string.data_format_error);
            }
        }else{
            XUtils.show(R.string.data_load_fail);
        }
    }

    @Override
    public void onFailure(HttpException e, String s) {
        Loading.dismiss();
        XUtils.show(R.string.network_fail);
        Log.e("study","===error==="+s);
        e.printStackTrace();
        failure();
    }

    public abstract void success(T data);

    public void failure(){

    }
}
