package com.example.lalal.Fragment.Manage;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.lalal.MyNewGraduate.R;
import com.example.lalal.Tools.Normal.PackageName;

public class NotificationActivity extends Activity {

    private SwitchCompat notifiswitch;
    private RelativeLayout notifilayout;

    private SwitchCompat zhifubaiswitch;                    //1.支付宝  com.eg.android.AlipayGphone
    private SwitchCompat weiboswitch;                       //2.微博    com.sina.weibo
    private SwitchCompat taobaoswitch;                      //3.淘宝    com.taobao.taobao
    private SwitchCompat weixinswitch;                      //4.微信    com.tencent.mm
    private SwitchCompat qqswitch;                           //5.qq      com.tencent.mobileqq
    private SwitchCompat dianhuaswitch;                     //6.电话    com.android.dialer
    private SwitchCompat duanxinswitch;                     //7.短信    com.android.mms
    private SwitchCompat youjianswitch;                     //8.邮件    com.netease.mobimail.oneplus

    private PackageName packageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        notifiswitch = (SwitchCompat)findViewById(R.id.notifiswitch);
        notifilayout=(RelativeLayout)findViewById(R.id.notifilayout);
        notifilayout.setVisibility(View.INVISIBLE);
        notifiswitch.setSwitchMinWidth(200);
        //
        packageName = new PackageName();
        //switch开关
        zhifubaiswitch = (SwitchCompat)findViewById(R.id.zhifubaiswitch);
        weiboswitch = (SwitchCompat)findViewById(R.id.weiboswitch);
        taobaoswitch = (SwitchCompat)findViewById(R.id.taobaoswitch);
        weixinswitch = (SwitchCompat)findViewById(R.id.weixinswitch);
        qqswitch = (SwitchCompat)findViewById(R.id.qqswitch);
        dianhuaswitch = (SwitchCompat)findViewById(R.id.dianhuaswitch);
        duanxinswitch = (SwitchCompat)findViewById(R.id.duanxinswitch);
        youjianswitch = (SwitchCompat)findViewById(R.id.youjianswitch);
        notifiswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    notifilayout.setVisibility(View.VISIBLE);
                    if (!isEnabled()) {
                        startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "监控器开关已打开", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                else{
                    notifilayout.setVisibility(View.INVISIBLE);
                    if (!isEnabled()) {
                        startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "监控器开关已打开", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });
        //支付宝  com.eg.android.AlipayGphone
        zhifubaiswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    packageName.setpackflag("com.eg.android.AlipayGphone",true);
                else packageName.setpackflag("com.eg.android.AlipayGphone",false);
            }
        });
        //微博    com.sina.weibo
        weiboswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    packageName.setpackflag("com.sina.weibo",true);
                else packageName.setpackflag("com.sina.weibo",false);
            }
        });
        //淘宝    com.taobao.taobao
        taobaoswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    packageName.setpackflag("com.taobao.taobao",true);
                else packageName.setpackflag("com.taobao.taobao",false);
            }
        });
        //微信    com.tencent.mm
        weixinswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    packageName.setpackflag("com.tencent.mm",true);
                else packageName.setpackflag("com.tencent.mm",false);
            }
        });
        //qq      com.tencent.mobileqq
        qqswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    packageName.setpackflag("com.tencent.mobileqq",true);
                else packageName.setpackflag("com.tencent.mobileqq",false);
            }
        });
        //电话    com.android.dialer
        dianhuaswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    packageName.setpackflag("com.android.dialer",true);
                else packageName.setpackflag("com.android.dialer",false);
            }
        });
        //短信    com.android.mms
        duanxinswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    packageName.setpackflag("com.android.mms",true);
                else packageName.setpackflag("com.android.mms",false);
            }
        });
        //邮件    com.netease.mobimail.oneplus
        youjianswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    packageName.setpackflag("com.netease.mobimail.oneplus",true);
                else packageName.setpackflag("com.netease.mobimail.oneplus",false);
            }
        });

    }

    // 判断是否打开了通知监听权限
    private boolean isEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


}
