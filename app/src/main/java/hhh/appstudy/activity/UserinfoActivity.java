package hhh.appstudy.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.ResType;
import com.lidroid.xutils.view.annotation.ResInject;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import hhh.appstudy.R;
import hhh.appstudy.entities.Result;
import hhh.appstudy.entities.User;
import hhh.appstudy.helper.Code;
import hhh.appstudy.https.BasicRequestCallBack;
import hhh.appstudy.https.XUtils;
import hhh.appstudy.view.TitleView;
import hhh.appstudy.view.UserInfoView;

/**
 * Created by hhh on 2016/5/18.
 */
public class UserinfoActivity extends BaseActivity {
    @ViewInject(R.id.userinfo_title)
    private TitleView title;
    @ViewInject(R.id.userinfo_img)
    private UserInfoView uvPhoto;
    @ViewInject(R.id.userinfo_name)
    private UserInfoView uvName;
    @ViewInject(R.id.userinfo_nick)
    private UserInfoView uvNick;
    @ViewInject(R.id.userinfo_sex)
    private UserInfoView uvSex;
    @ViewInject(R.id.userinfo_save)
    private Button btnSave;
    @ResInject(id=R.array.sex,type= ResType.StringArray)
    private String[] sexItems;

    private long size;
    private InputStream is;

    private Uri uriTempFile;
    private User user;
    private int sex=-1;
    private boolean needUpdate=false;
    private String photoFilename;

    private View.OnClickListener l=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.title_back:
                    finish();
                    break;
                case R.id.userinfo_save:
                    save();
                    break;
            }
        }
    };

    private UserInfoView.clickListener clickListener=new UserInfoView.clickListener() {
        @Override
        public void onClickListener(View v) {
            switch (v.getId()){
                case R.id.userinfo_img:
                    selectPhoto();
                    break;
                case R.id.userinfo_name:
                    Log.i("userinfoact","name");
                    XUtils.show("修改姓名");
                    break;
                case R.id.userinfo_nick:

                    break;
                case R.id.userinfo_sex:
                    showSex();
                    break;
            }
        }
    };

    private void selectPhoto() {
//        Intent intent=new Intent(Intent.ACTION_PICK);
//        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, Code.REQ_SELECT_PHOTO);
        Log.i("userinfoact","show select photo dialog");
        String[] items={"相册","照相机"};
        new AlertDialog.Builder(this).setTitle("选择图片来源").setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(which==0){
                    Crop.pickImage(UserinfoActivity.this,Code.REQ_SELECT_PHOTO);
                }else{
                    uriTempFile=Uri.parse("file://"+"/"+Environment.getExternalStorageDirectory().getPath()+"/"+System.currentTimeMillis()+".jpg");
                    Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,uriTempFile);
                    startActivityForResult(intent,Code.REQ_SELECT_CAMERA);
                }

            }
        }).show();
    }

    private void showSex() {
        new AlertDialog.Builder(this).setItems(sexItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case 0:
                        uvSex.setText(R.string.male);
                        sex=0;
                        break;
                    case 1:
                        uvSex.setText(R.string.female);
                        sex=1;
                        break;
                }
            }
        }).show();
    }

    private void save() {
        RequestParams params=new RequestParams();
//        if(is!=null) {
//            params.addBodyParameter("photourl", is, size, "a.png");
//            needUpdate=true;
//        }
        Log.i("userinfoact","photo="+photoFilename);
        if(photoFilename!=null){
            params.addBodyParameter("photourl",new File(photoFilename));
            params.addBodyParameter("photo","exist");
//            File f=new File(photoFilename);
//            try {
//                is=new FileInputStream(f);
//                size=f.length();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            params.addBodyParameter("photourl",is,size,"a.jpg");

            needUpdate=true;
        }
        if(uvName!=null&&!uvName.getText().equals(user.getName())) {
            needUpdate=true;
            params.addBodyParameter("name", uvName.getText());
        }
        if(uvNick!=null&&!uvNick.getText().equals(user.getNick())) {
            needUpdate=true;
            params.addBodyParameter("nick", uvNick.getText());
        }
        if(sex!=-1&&sex!=user.getGender()) {
            needUpdate=true;
            params.addBodyParameter("sex", String.valueOf(sex));
        }
        if(needUpdate){
            params.addBodyParameter("token",user.getToken());
            XUtils.send(XUtils.UPINFO, params, new BasicRequestCallBack<Result<User>>() {
                @Override
                public void success(Result<User> data) {
                    XUtils.show(data.message);
                    if(data.code ==Result.STATE_SUC){
                        ((MyApp)getApplication()).setU(data.data);
                        setResult(Code.RES_UPDATE_INFO);
                        finish();
                    }
                }
            },true);
        }else{
            XUtils.show(R.string.not_need_update);
        }
    }

    public static void startActivityForReuslt(Activity activity){
        Intent intent=new Intent(activity,UserinfoActivity.class);
        activity.startActivityForResult(intent,Code.REQ_UPDATE_INFO);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        ViewUtils.inject(this);

        title.setBackClickListener(l);
        btnSave.setOnClickListener(l);

        uvPhoto.setClickListener(clickListener);
        uvSex.setClickListener(clickListener);
        uvName.setClickListener(clickListener);
        uvNick.setClickListener(clickListener);
        initData();
    }

    private void initData() {
        user=((MyApp)getApplication()).getU();
        if(user==null){
            XUtils.show(R.string.needlogin_error);
            finish();
            return;
        }
        uvPhoto.setPhotoUrl(user.getPhoto_url());
        uvName.setText(user.getName());
        uvNick.setText(user.getNick());
        uvSex.setText(user.getGender()==0?getString(R.string.male):getString(R.string.female));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case Code.REQ_SELECT_PHOTO:
                if(data==null){
                    XUtils.show("请重新选择图片");
                }else {
                    try {
                        Uri inUri = data.getData();
                        Uri outUri = Uri.fromFile(File.createTempFile(String.valueOf(System.currentTimeMillis()), ".jpg"));
                        Crop.of(inUri, outUri)
                                .asSquare()
                                .start(this, Code.REQ_CUT_PHOTO);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Code.REQ_SELECT_CAMERA:
//                if(data==null){
//                    XUtils.show("请重新选择图片");
//                }else {
                    try {
                        Uri outUri = Uri.fromFile(File.createTempFile(String.valueOf(System.currentTimeMillis()), ".jpg"));
                        Crop.of(uriTempFile, outUri)
                                .asSquare()
                                .start(this, Code.REQ_CUT_PHOTO);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                }
                break;
            case Code.REQ_CUT_PHOTO:
//                try {
//                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uriTempFile));
//                    uvPhoto.setImagePhoto(bitmap);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
                if(data==null){
                    XUtils.show("请重新选择图片");
                }else{
                    Uri picUri=Crop.getOutput(data);
                    photoFilename=picUri.getPath();
                    uvPhoto.setPhotoUrl(photoFilename);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode== Code.REQ_SELECT_PHOTO&&resultCode==RESULT_OK&&data!=null){
//            Log.i("userinfoview","select photo");
//            Uri uri=data.getData();
//            String[] projection=new String[]{MediaStore.Images.Media.DATA};
//            Cursor c=getContentResolver().query(uri,projection,null,null,null);
//            if(c!=null&&c.moveToFirst()){
//                String path=c.getString(c.getColumnIndex(projection[0]));
//                Uri src=Uri.fromFile(new File(path));
//                String dstfname= String.valueOf(Calendar.getInstance().getTimeInMillis())+".jpg";
//                Uri dest=Uri.fromFile(new File(getCacheDir(),dstfname));
//                toCutPhoto(path);
////                UCrop.of(src, dest)
////                        .withAspectRatio(16, 9)
////                        .withMaxResultSize(300, 300)
////                        .start(this);
//            }
//        }else if(requestCode==Code.REQ_CUT_PHOTO&&resultCode==RESULT_OK&&data!=null){
//            Log.i("userinfoview","cut photo");
//            Bitmap bmp=data.getParcelableExtra("data");
//            ByteArrayOutputStream bos=new ByteArrayOutputStream();
//            bmp.compress(Bitmap.CompressFormat.PNG,70,bos);
//            size=bos.size();
//            is=new ByteArrayInputStream(bos.toByteArray());
//            uvPhoto.setImagePhoto(bmp);
////        }else if(requestCode==UCrop.REQUEST_CROP&&resultCode==RESULT_OK){
////            Uri resultUri=UCrop.getOutput(data);
////            photoFilename=resultUri.getPath();
////            uvPhoto.setPhotoUrl(photoFilename);
//        }
//    }

    private void toCutPhoto(Uri imageUri){
        Intent intent=new Intent("com.android.camera.action.CORP");
        intent.setDataAndType(imageUri,"image/*");
        intent.putExtra("crop",true);
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        intent.putExtra("outputX",100);
        intent.putExtra("outputY",100);
//        intent.putExtra("return-data",true);
        uriTempFile=Uri.parse("file://"+"/"+ Environment.getExternalStorageDirectory().getPath()+"/"+System.currentTimeMillis()+".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uriTempFile);
        intent.putExtra("outputformat", Bitmap.CompressFormat.JPEG);
        startActivityForResult(intent,Code.REQ_CUT_PHOTO);
    }
}
