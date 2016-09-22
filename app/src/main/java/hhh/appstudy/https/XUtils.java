package hhh.appstudy.https;

import android.content.Context;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.File;

import hhh.appstudy.R;
import hhh.appstudy.utils.Loading;

/**
 * Created by hhh on 2016/4/14.
 */
public class XUtils {
    public static final String U="http://119.29.193.241/CI/index.php/";
    public static final String LOGIN="user/login";
    public static final String REG="user/register";
    public static final String RESET="user/sendemail";
    public static final String GETEMAIL="user/queryemail";
    public static final String FEEDBACK="feedback/add";

    public static final String UPINFO="user/upinfo";
    //public static final String SCHOOL="";
    public static final String CHECKVER="appversion/checkversion";

    public static String apkfile= Environment.getExternalStorageDirectory()+ File.separator+"study"+File.separator+"download"+File.separator+"study.apk";

    private static BitmapUtils bitmapUtils;
    private static Context mContext;
    private static HttpUtils httpUtils;
    private static DbUtils dbUtils;
    private static HttpHandler httpHandler;



    public static void init(Context context){
        mContext=context;
        if(bitmapUtils==null){
            bitmapUtils=new BitmapUtils(mContext);
            bitmapUtils.configDefaultLoadingImage(R.drawable.loading);
            bitmapUtils.configDefaultLoadFailedImage(R.drawable.nulldata);
            bitmapUtils.configDiskCacheEnabled(true);
        }
        if(httpUtils==null){
            httpUtils=new HttpUtils();
        }
        if(dbUtils==null){
            dbUtils=DbUtils.create(mContext,"study.db");
        }
    }

    public static <T> void send(String url, RequestParams params, RequestCallBack<T> callBack,boolean loading){
        if(loading){
            Loading.show();
        }
        if(params==null){
            httpHandler=httpUtils.send(HttpRequest.HttpMethod.GET,U+url,callBack);
        }else{
            httpHandler=httpUtils.send(HttpRequest.HttpMethod.POST,U+url,params,callBack);
        }
    }

    public static void download(String url,RequestCallBack<File> callback){
        httpHandler=httpUtils.download(url,apkfile,true,callback);
    }

    public static void cancel(){
        if(httpHandler!=null){
            httpHandler.cancel();
            httpHandler=null;
        }
    }

    public static void display(ImageView img, String url){
        bitmapUtils.display(img,url);
    }

    public static void show(String text){
        Toast.makeText(mContext,text,Toast.LENGTH_SHORT).show();;
    }

    public static void show(int resId){
        Toast.makeText(mContext,resId,Toast.LENGTH_SHORT).show();
    }
}
