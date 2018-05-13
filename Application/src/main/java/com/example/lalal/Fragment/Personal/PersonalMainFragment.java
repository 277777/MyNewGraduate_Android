package com.example.lalal.Fragment.Personal;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.ContentUris;
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
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lalal.CustomView.Add_Show_PopupWindow;
import com.example.lalal.CustomView.CircleImg;
import com.example.lalal.CustomView.SelectPicPopupWindow;
import com.example.lalal.MyNewGraduate.MainActivity;
import com.example.lalal.MyNewGraduate.R;
import com.example.lalal.MyNewGraduate.RegisterActivity;
import com.example.lalal.MyNewGraduate.TextActivity;
import com.example.lalal.Tools.ConStant.Constants;
import com.example.lalal.Tools.LoginPhoto.FileStorage;
import com.example.lalal.Tools.LoginPhoto.PermissionsChecker;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.app.Activity.RESULT_OK;


public class PersonalMainFragment extends Fragment {

    private CircleImg circleimageview;
    private TextView username_text;
    private LinearLayout addfriend,showfriend;

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

    private SelectPicPopupWindow menuWindow; // 自定义的头像编辑弹出框
    private Add_Show_PopupWindow mmWindow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_personalmain,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        circleimageview = (CircleImg) view.findViewById(R.id.circleimageview);
        username_text = (TextView) view.findViewById(R.id.username_text);
        addfriend = view.findViewById(R.id.addfriend);
        showfriend = view.findViewById(R.id.showfriend);
        mPermissionsChecker = new PermissionsChecker(getActivity());
        init();
    }

    public void init(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                startPermissionsActivity();
            }
        }
        username_text.setText("用户："+Constants.user.getUsername());
        if(Constants.user.getUserimg().equals("default"))
            circleimageview.setImageDrawable(getResources().getDrawable(R.drawable.head_default));
        else{
            String path=Constants.IPURL+Constants.user.getUserimg();
            System.out.println(path);
            UserHeadTask userHeadTask =new UserHeadTask();
            userHeadTask.execute(path);
        }
        circleimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuWindow = new SelectPicPopupWindow(getActivity(), itemsOnClick);
                menuWindow.showAtLocation(getActivity().findViewById(R.id.relative_personal_fragment),
                        Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
;            }
        });
        addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AddFriendActivity.class);
                startActivity(intent);
            }
        });
        showfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ShowFriendActivity.class);
                startActivity(intent);
            }
        });
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    Toast.makeText(getActivity(), "没有SDCard!", Toast.LENGTH_LONG)
                            .show();
                }

                break;
        }
    }


    //相册权限
    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(getActivity(), REQUEST_PERMISSION,
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
        if (DocumentsContract.isDocumentUri(getActivity(), imageUri)) {
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

        circleimageview.setImageBitmap(bitmap);
        writeFileByBitmap(bitmap);

    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection获取真实的图片路径
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    //将bitmap写入文件
    public void writeFileByBitmap(Bitmap bitmap) {
        //String path ="
        String path = Constants.PROJECTPATH +Constants.user.getUsername();
        System.out.println("存储位置："+path);
        File file = new File(path);
        File imageFile = new File(file, "touxiang" + ".png");
        if(!file.exists()){
            file.mkdirs();
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
            UserRegisterUploadTask userRegisterUploadTask = new UserRegisterUploadTask();
            userRegisterUploadTask.execute(Constants.UPLOADURL+"?Name="+Constants.user.getUsername());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleImageBeforeKitKat(Intent intent) {
        imageUri = intent.getData();
        imagePath = getImagePath(imageUri, null);
        cropPhoto();
    }

    //打开系统相机
    private void openCamera() {
        File file = new FileStorage().createIconFile();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(getActivity(), "com.lele.avatarcircledemo.fileprovider", file);//通过FileProvider创建一个content类型的Uri
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

    //用户头像
    class UserHeadTask extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            String urlsrc = strings[0];
            URL myFileURL;
            Bitmap bitmap=null;
            //获得连接
            HttpURLConnection conn= null;
            try {
                myFileURL = new URL(urlsrc);
                conn = (HttpURLConnection)myFileURL.openConnection();
                //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
                conn.setConnectTimeout(6000);
                //连接设置获得数据流
                conn.setDoInput(true);
                //不使用缓存
                conn.setUseCaches(false);
                //这句可有可无，没有影响
                //conn.connect();
                //得到数据流
                InputStream is = conn.getInputStream();
                //解析得到图片
                bitmap = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            circleimageview.setImageBitmap(bitmap);
        }
    }

    //修改头像
    class UserRegisterUploadTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            String urlStr = strings[0];
            String rsp = "";
            HttpURLConnection conn = null;
            String BOUNDARY = "|";
            try {
                URL url = new URL(urlStr);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(30000);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("User-Agent",
                        "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
                conn.setRequestProperty("Content-Type",
                        "multipart/form-data; boundary=" + BOUNDARY);

                OutputStream out = new DataOutputStream(conn.getOutputStream());
                File file = new File(Constants.userpath+"touxiang.png");
                String filePath = Constants.userpath+"touxiang.png";
                String filename = file.getName();
                String contentType = "image/png";
                StringBuffer strBuf = new StringBuffer();
                strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                strBuf.append("Content-Disposition: form-data; name=\"" + filePath
                        + "\"; filename=\"" + filename + "\"\r\n");
                strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
                out.write(strBuf.toString().getBytes());
                DataInputStream in = new DataInputStream(new FileInputStream(file));
                int bytes = 0;
                byte[] bufferOut = new byte[1024];
                while ((bytes = in.read(bufferOut)) != -1) {
                    out.write(bufferOut, 0, bytes);
                }
                in.close();
                byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
                out.write(endData);
                out.flush();
                out.close();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                rsp = buffer.toString();
                reader.close();
                reader = null;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                    conn = null;
                }
            }
            return rsp;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println("收到："+s);
            if(s.equals(Constants.DEFEATE))
                Toast.makeText(getActivity(),"修改失败",Toast.LENGTH_SHORT).show();
            else if (s.equals(Constants.SUCCESSFUL)){
                Toast.makeText(getActivity(),"修改成功",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
