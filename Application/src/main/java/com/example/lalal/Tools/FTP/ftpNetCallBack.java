package com.example.lalal.Tools.FTP;

import org.apache.commons.net.ftp.FTPFile;

import java.util.List;

public interface ftpNetCallBack {

    //获取文件夹下文件列表
    void getFtpFileList(List<FTPFile> ftpFileList);

    //下载
    void downLoadFinish(boolean result);

    //上传
    void uploadFinish(boolean result, int num);
}
