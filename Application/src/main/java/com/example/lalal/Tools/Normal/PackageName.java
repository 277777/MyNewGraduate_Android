package com.example.lalal.Tools.Normal;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lalal on 2018/3/6.
 */

public class PackageName {
    //支付宝  com.eg.android.AlipayGphone
    //微博    com.sina.weibo
    //淘宝    com.taobao.taobao
    //微信    com.tencent.mm
    //qq      com.tencent.mobileqq
    //电话    com.android.dialer
    //短信    com.android.mms
    //邮件    com.netease.mobimail.oneplus
    private static Map<String,String> map;
    private static Map<String,Boolean> mapflag;

    static {
        map = new HashMap<>();
        map.put("com.eg.android.AlipayGphone","支付宝");
        map.put("com.sina.weibo","微博");
        map.put("com.taobao.taobao","淘宝");
        map.put("com.tencent.mm","微信");
        map.put("com.tencent.mobileqq","qq");
        map.put("com.android.dialer","电话");
        map.put("com.android.mms","短信");
        map.put("com.netease.mobimail.oneplus","邮件");

        mapflag = new HashMap<>();
        mapflag.put("com.eg.android.AlipayGphone",false);
        mapflag.put("com.sina.weibo",false);
        mapflag.put("com.taobao.taobao",false);
        mapflag.put("com.tencent.mm",false);
        mapflag.put("com.tencent.mobileqq",false);
        mapflag.put("com.android.dialer",false);
        mapflag.put("com.android.mms",false);
        mapflag.put("com.netease.mobimail.oneplus",false);
    }

    public String packname(String string){
        if(map.containsKey(string)==true)
            string = map.get(string);
        return string;
    }

    public Boolean packflag(String string){
        if(mapflag.containsKey(string)==true)
            return mapflag.get(string);
        return false;
    }

    public void setpackflag(String string, Boolean flag){
        mapflag.put(string,flag);
    }

}
