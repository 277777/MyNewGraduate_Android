package com.example.lalal.Tools.Normal;

import android.os.Handler;

import com.example.lalal.Tools.BLTH.BluetoothServer;

public class ThreadSend {

    private String sendstr;
    private Handler handler = new Handler();

    public ThreadSend(String sendstr){
        this.sendstr = sendstr;
        BluetoothServer.sendflag = false;
    }
    public void Tsend(){
        Runnable runnable=new Runnable(){
            @Override
            public void run() {
                if(BluetoothServer.sendflag==true){
                    System.out.println("开始");
                    handler.removeCallbacks(this);
                }else{
                    if (BluetoothServer.bluetoothflag == true) {
                        BluetoothServer.send(sendstr);
                    }
                    handler.postDelayed(this, 500);
                }
            }
        };
        handler.postDelayed(runnable,500);
    }
}
