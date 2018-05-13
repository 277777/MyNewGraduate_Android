package com.example.lalal.Fragment.Dongtai;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lalal.MyNewGraduate.R;
import com.example.lalal.Tools.ConStant.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EditSSActivity extends Activity {

    private TextView quxiaoedit,querenedit;
    private EditText edittext;
    private ImageView editview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ss);
        initState();
        quxiaoedit = findViewById(R.id.quxiaoedit);
        querenedit = findViewById(R.id.querenedit);
        editview = findViewById(R.id.editview);
        edittext = findViewById(R.id.edittext);
        init();
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        }
//
//    }

    private void initState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    public void init(){
        //确认
        querenedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edittext.getText().toString()!=null){
                    String path = Constants.ADDMESSAGEURL + "?Userid=" + Constants.user.getUserid() + "&Msgtext=" + edittext.getText().toString();
                    AddMessageTask addMessageTask = new AddMessageTask();
                    addMessageTask.execute(path);
                }else {
                    Toast.makeText(EditSSActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //取消
        quxiaoedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //加入走路
        editview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = edittext.getText().toString();
                System.out.println("今日已收到："+str);
                str = str + "   今日已走"+"322"+"步";
                edittext.setText(str);
            }
        });
    }

    class AddMessageTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            String result = "";
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(200);
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return null;
                }
                InputStreamReader in = new InputStreamReader(connection.getInputStream());
                BufferedReader buf = new BufferedReader(in);
                String readLine = "";
                while ((readLine = buf.readLine()) != null) {
                    result += readLine;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println("收到："+s);
            if(s.equals(Constants.DEFEATE))
                Toast.makeText(EditSSActivity.this,"分享失败",Toast.LENGTH_SHORT).show();
            else if(s.equals(Constants.SUCCESSFUL))
                Toast.makeText(EditSSActivity.this,"分享成功",Toast.LENGTH_SHORT).show();
        }
    }
}
