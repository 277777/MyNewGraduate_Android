package com.example.lalal.Tools.FTP;

import android.os.AsyncTask;

import java.util.List;

/**
 * Created by ZhangHao on 2017/5/19.
 * 上传多个文件到ftp
 */

public class FtpUploadListTask extends AsyncTask<String, Integer, Boolean> {
    //ftp工具类
    private FtpHelper ftpHelper;
    //回调
    private ftpNetCallBack callBack;
    //ftp文件夹目录
    private String ftpFolder;
    //本地文件夹路径
    private List<String> localFilePath;
    //文件数量
    private static int ftplistsize;

    public FtpUploadListTask(FtpHelper ftpHelper, ftpNetCallBack callBack, List<String> localFilePath, String ftpFolder, int listsize) {
        this.ftpHelper = ftpHelper;
        this.callBack = callBack;
        this.ftpFolder = ftpFolder;
        this.localFilePath = localFilePath;
        ftplistsize = listsize;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        boolean result = false;
        try {
            if (ftpHelper != null && ftpHelper.isConnect()) {
                //上传多个文件
                int count = ftpHelper.uploadMoreFile(localFilePath,ftpFolder);
                if(count == ftplistsize)
                    result = true;
                else result = false;
                ftplistsize = count;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        callBack.uploadFinish(result,ftplistsize);
    }
}
