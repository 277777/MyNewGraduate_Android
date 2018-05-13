package com.example.lalal.Service;

import android.app.Notification;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.example.lalal.Tools.Normal.PackageName;

public class NotificationMonitorService extends NotificationListenerService {

    private PackageName packageName=new PackageName();
    private String msg;

    // 在收到消息时触发
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        // TODO Auto-generated method stub
        Bundle extras = sbn.getNotification().extras;
        // 获取接收消息APP的包名
        String notificationPkg = sbn.getPackageName();
        // 获取接收消息的抬头
        String notificationTitle = extras.getString(Notification.EXTRA_TITLE);
        // 获取接收消息的内容
        String notificationText = extras.getString(Notification.EXTRA_TEXT);
        //
        if(packageName.packflag(notificationPkg)==true){
            notificationPkg = packageName.packname(notificationPkg);
            msg = "软件："+notificationPkg+"   发送人："+notificationTitle+"   发送消息："+notificationText;
            Log.i(notificationPkg, msg);
        }

    }

}
