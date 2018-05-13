package com.example.lalal.Fragment.Manage;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lalal.MyNewGraduate.R;
import com.example.lalal.Tools.FTP.FtpHelper;
import com.example.lalal.Tools.FTP.FtpUploadListTask;
import com.example.lalal.Tools.FTP.FtpUploadTask;
import com.example.lalal.Tools.FTP.ftpNetCallBack;
import com.example.lalal.Tools.FUPLOAD.Consant;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;

import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicCSActivity extends Activity implements ftpNetCallBack {

    private Button filecata;
    private Button upup;
    private List<String> musicfilelist ;
    private FtpHelper ftp;
    private String currentFtpPath = FtpHelper.REMOTE_PATH + "/Music";
    private TextView textviewfile;
    private TextView jindutiao;
    private String textviewfilestring="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_cs);
        filecata = (Button)findViewById(R.id.filecata);
        upup = (Button)findViewById(R.id.upup);
        textviewfile = (TextView)findViewById(R.id.textviewfile);
        jindutiao = (TextView)findViewById(R.id.jindutiao);

        textviewfile.setMovementMethod(ScrollingMovementMethod.getInstance());
        //initFtp();

        filecata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGrantExternalRW(MusicCSActivity.this)) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                }
                FileCata();

            }
        });

        upup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jindutiao.setText("开始上传...");
                upload(musicfilelist,musicfilelist.size());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == Consant.REQUESTCODE_FROM_ACTIVITY){
                musicfilelist = new ArrayList<String>();
                musicfilelist = data.getStringArrayListExtra(Constant.RESULT_INFO);
                System.out.println("选中了"+musicfilelist.size()+"个文件");
                for(int i=0;i<musicfilelist.size();i++)
                    System.out.println("选中了："+musicfilelist.get(i));
                updatetextview();
            }
        }
    }

    //文件选择
    public void FileCata(){
        new LFilePicker()
                .withActivity(MusicCSActivity.this)
                .withRequestCode(Consant.REQUESTCODE_FROM_ACTIVITY)
                .withIconStyle(Constant.ICON_STYLE_BLUE)
                .withTitle("选择音乐文件")
                .withBackgroundColor("#87CEFA")
                .start();
    }

    //解决安卓6.0以上版本不能读取外部存储权限的问题
    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
            return false;
        }
        return true;
    }

    //初始化ftp
    private void initFtp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (ftp == null) {
                    try {
                        ftp = new FtpHelper();
                        ftp.openConnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    //上传
    private void upload(List<String> localFilePath,int number) {
        if( number != 0 )
            new FtpUploadListTask(ftp, this, musicfilelist, currentFtpPath,musicfilelist.size()).execute();
        else  new FtpUploadTask(ftp, this, localFilePath.get(0), currentFtpPath).execute();
    }

    //将文件列表更新到textview
    private void updatetextview(){
        if(musicfilelist!=null){
            String str1 = "您已经选择了"+musicfilelist.size()+"个文件";
            textviewfilestring = str1+"\n";
            textviewfilestring += "文件如下："+"\n";
            for (int i =0 ; i < musicfilelist.size() ; i++ ){
                textviewfilestring = textviewfilestring + infoString(musicfilelist.get(i))+"\n";
            }
            textviewfile.setText(textviewfilestring);

        }
    }

    //获取文件名
    public String infoString(String string){
        String str[] = new String[20];
        str = string.split("/");
        return str[str.length-1];
    }

    @Override
    public void getFtpFileList(List<FTPFile> ftpFileList) {

    }

    @Override
    public void downLoadFinish(boolean result) {

    }

    @Override
    public void uploadFinish(boolean result,int num) {
        if (result) {
            if(num == musicfilelist.size())
                System.out.println(num+"个文件上传成功");
            else System.out.println(num+"个文件上传成功，还有"+(musicfilelist.size()-num)+"个文件上传失败");
        } else {
            System.out.println("上传失败");
        }
    }

}

