package com.example.lalal.Tools.FTP;

import android.os.AsyncTask;

import org.apache.commons.net.ftp.FTPFile;

import java.util.List;

/**
 * Created by ZhangHao on 2017/5/19.
 */

public class FtpFileListTask extends AsyncTask<String, Integer, List<FTPFile>> {
    //ftp工具类
    private FtpHelper ftpHelper;
    //回调
    private ftpNetCallBack callBack;
    //ftp目录
    private String ftpFolder;

    public FtpFileListTask(FtpHelper ftpHelper, ftpNetCallBack callBack, String ftpFolder) {
        this.ftpHelper = ftpHelper;
        this.callBack = callBack;
        this.ftpFolder = ftpFolder;
    }

    @Override
    protected List<FTPFile> doInBackground(String... params) {
        List<FTPFile> lists = null;
        try {
            if (ftpHelper != null && ftpHelper.isConnect()) {
                lists = ftpHelper.listFiles(ftpFolder);
                for (int i = 0; i < lists.size(); i++) {
                    FTPFile ftpFile = lists.get(i);
                    if (ftpFile.getName().equals(".") || ftpFile.getName().equals("..")) {
                        lists.remove(i);
                        i--;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lists;
    }

    @Override
    protected void onPostExecute(List<FTPFile> ftpFileList) {
        super.onPostExecute(ftpFileList);
        callBack.getFtpFileList(ftpFileList);
    }
}
