package com.example.lalal.Tools.FTP;

import android.os.AsyncTask;

import java.io.File;

/**
 * Created by ZhangHao on 2017/5/19.
 * 上传本地文件（夹）到ftp
 */

public class FtpUploadTask extends AsyncTask<String, Integer, Boolean> {
    //ftp工具类
    private FtpHelper ftpHelper;
    //回调
    private ftpNetCallBack callBack;
    //ftp文件夹目录
    private String ftpFolder;
    //本地文件夹路径
    private String localFilePath;

    public FtpUploadTask(FtpHelper ftpHelper, ftpNetCallBack callBack, String localFilePath, String ftpFolder) {
        this.ftpHelper = ftpHelper;
        this.callBack = callBack;
        this.ftpFolder = ftpFolder;
        this.localFilePath = localFilePath;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        boolean result = false;
        try {
            if (ftpHelper != null && ftpHelper.isConnect()) {
                //判断本地文件是否是文件夹
                File localFile = new File(localFilePath);
                if (localFile.exists() && localFile.isDirectory()) {
                    //上传文件夹
                    int count = ftpHelper.uploadFolder(localFilePath, ftpFolder);
                    if (count > 0) {
                        //上传数量大于0时
                        result = true;
                    }
                } else if (localFile.exists() && localFile.isFile()) {
                    //上传文件
                    result = ftpHelper.uploadFile(localFilePath, ftpFolder);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        callBack.uploadFinish(result,0);
    }
}
