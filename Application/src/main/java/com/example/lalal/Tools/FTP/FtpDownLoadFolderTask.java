package com.example.lalal.Tools.FTP;

import android.os.AsyncTask;

/**
 * Created by ZhangHao on 2017/5/19.
 * ftp下载文件夹
 */

public class FtpDownLoadFolderTask extends AsyncTask<String, Integer, Boolean> {
    //ftp工具类
    private FtpHelper ftpHelper;
    //回调
    private ftpNetCallBack callBack;
    //ftp目录
    private String ftpFolder;
    //本地文件夹路径
    private String localFilePath;

    public FtpDownLoadFolderTask(FtpHelper ftpHelper, ftpNetCallBack callBack,
                                 String ftpFolder, String localFilePath) {
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
                int count = ftpHelper.downloadFolder(ftpFolder, localFilePath);
                if (count > 0) {
                    result = true;
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
        callBack.downLoadFinish(result);
    }
}
