package com.example.lalal.MyNewGraduate;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lalal.CustomView.CircleImageView;
import com.example.lalal.CustomView.CircleImg;
import com.example.lalal.CustomView.SelectPicPopupWindow;
import com.example.lalal.Fragment.Personal.PermissionsActivity;
import com.example.lalal.Tools.ConStant.Constants;
import com.example.lalal.Tools.LoginPhoto.FileStorage;
import com.example.lalal.Tools.LoginPhoto.PermissionsChecker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends Activity {

    private EditText register_username;
    private EditText register_password;
    private EditText recheck_password;
    private Button finish_button;

    private String username,password,repassword;
    private String url;

    private CircleImg registerimageview;

    private SelectPicPopupWindow menuWindow; // 自定义的头像编辑弹出框
    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;//相册选取
    private static final int CODE_CAMERA_REQUEST = 0xa1; //拍照
    private static final int CODE_CROP_RESULT_REQUEST = 0xa2; //剪裁图片
    private static final int REQUEST_PERMISSION = 0xa5;  //权限请求

    private PermissionsChecker mPermissionsChecker; // 权限检测器
    static final String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private Uri imageUri;//原图保存地址
    private boolean isClickCamera;
    private String imagePath;

    private static boolean chrosephoto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_username = (EditText)findViewById(R.id.register_username);
        register_password = (EditText)findViewById(R.id.register_password);
        recheck_password = (EditText)findViewById(R.id.recheck_password);
        finish_button = (Button)findViewById(R.id.finish_button);
        registerimageview = (CircleImg)findViewById(R.id.registerimageview);
        mPermissionsChecker = new PermissionsChecker(this);

        registerimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuWindow = new SelectPicPopupWindow(RegisterActivity.this, itemsOnClick);
                menuWindow.showAtLocation(findViewById(R.id.main_register),
                        Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });

        finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = register_username.getText().toString();
                password = register_password.getText().toString();
                repassword = recheck_password.getText().toString();
                if(!password.equals(repassword))
                    Toast.makeText(RegisterActivity.this,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
                else {
                    url = Constants.REGISTERURL+"?Name="+username+"&Pwd="+password;
                    new UserRegisterTask().execute(url);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            //相册
            case CODE_GALLERY_REQUEST:
                if (Build.VERSION.SDK_INT >= 19) {
                    if(data!=null)
                        handleImageOnKitKat(data);
                } else {
                    handleImageBeforeKitKat(data);
                }
                break;
             //剪裁
            case CODE_CROP_RESULT_REQUEST:
                Bitmap bitmap = null;
                try {
                    if (isClickCamera) {
                        //bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        bitmap = data.getParcelableExtra("data");
                    } else {
                        bitmap = data.getParcelableExtra("data");
                        System.out.println("相册地址："+imagePath);
                    }
                    writeFileByBitmap(bitmap);
                    setImageToHeadView(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            //相机
            case CODE_CAMERA_REQUEST:
                if (hasSdcard()) {
                    if (resultCode == RESULT_OK) {
                        cropPhoto();
                    }
                } else {
                    Toast.makeText(this, "没有SDCard!", Toast.LENGTH_LONG)
                            .show();
                }

                break;
        }
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                // 相机
                case R.id.takePhotoBtn:
                    //检查权限(6.0以上做权限判断)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                            startPermissionsActivity();
                        } else {
                            openCamera();
                        }
                    } else {
                        openCamera();
                    }
                    isClickCamera = true;
                    break;
                // 相册
                case R.id.pickPhotoBtn:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                            startPermissionsActivity();
                        } else {
                            selectFromAlbum();
                        }
                    } else {
                        selectFromAlbum();
                    }
                    isClickCamera = false;
                    break;
                default:
                    break;
            }
        }
    };

    //相册权限
    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_PERMISSION,
                PERMISSIONS);
    }

    //从相册选择
    private void selectFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, CODE_GALLERY_REQUEST);
    }

    //根据假地址获取真地址
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        imagePath = null;
        imageUri = data.getData();
        if (DocumentsContract.isDocumentUri(this, imageUri)) {
            //如果是document类型的uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(imageUri);
            if ("com.android.providers.media.documents".equals(imageUri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.downloads.documents".equals(imageUri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            //如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(imageUri, null);
        } else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            //如果是file类型的Uri,直接获取图片路径即可
            imagePath = imageUri.getPath();
        }
        cropPhoto();
    }

    //裁剪
    private void cropPhoto() {
        File file = new FileStorage().createCropFile();
        Uri outputUri = Uri.fromFile(file);//缩略图保存地址
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(imageUri, "image/*");
//        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, CODE_CROP_RESULT_REQUEST);
    }

    //提取保存裁剪之后的图片数据，并设置头像部分的View
    private void setImageToHeadView(Bitmap bitmap) {

        registerimageview.setImageBitmap(bitmap);

    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    //将bitmap写入文件
    public static void writeFileByBitmap(Bitmap bitmap) {
        chrosephoto = true;
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();//手机设置的存储位置
        //String path ="/storage/emulated/0/MyNewGraduate/";
        path = path +"/MyNewGraduate";
        System.out.println("存储位置："+path);
        File file = new File(path);
        File imageFile = new File(file, "touxiang" + ".png");
        if(!file.exists()){
            file.mkdir();
        }
        if (imageFile.exists()) {
            imageFile.delete();
        }
        try {
            imageFile.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //打开系统相机
    private void openCamera() {
        File file = new FileStorage().createIconFile();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(this, "com.lele.avatarcircledemo.fileprovider", file);//通过FileProvider创建一个content类型的Uri
        } else {
            imageUri = Uri.fromFile(file);
        }
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        startActivityForResult(intent, CODE_CAMERA_REQUEST);
    }

    //检查设备是否存在SDCard的工具方法
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }

    private void handleImageBeforeKitKat(Intent intent) {
        imageUri = intent.getData();
        imagePath = getImagePath(imageUri, null);
        cropPhoto();
    }



    class UserRegisterTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            String result = "";
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(200);
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {

                    return null;
                }
                InputStreamReader in = new InputStreamReader(connection.getInputStream());
                BufferedReader buf = new BufferedReader(in);
                String readLine = null;
                while ((readLine = buf.readLine()) != null) {
                    result += readLine;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s==null){
                Toast.makeText(RegisterActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
            }
            else {
                System.out.println("收到："+s);
                if(s.equals(Constants.EXITNAME))
                    Toast.makeText(RegisterActivity.this,"用户名已存在",Toast.LENGTH_SHORT).show();
                else if(s.equals(Constants.DEFEATE))
                    Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                else if (s.equals(Constants.SUCCESSFUL)){
                    Toast.makeText(RegisterActivity.this,"注册成功，请登录",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        }
    }

}
