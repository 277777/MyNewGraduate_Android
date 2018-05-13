package com.example.lalal.MyNewGraduate;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lalal.Tools.ConStant.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ForgetActivity extends Activity {

    private EditText forget_username;
    private EditText forget_password;
    private EditText reforget_password;

    private Button forget_finish_button;

    private String username,password,repassword;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        forget_username = (EditText)findViewById(R.id.forget_username);
        forget_password = (EditText)findViewById(R.id.forget_password);
        reforget_password = (EditText)findViewById(R.id.reforget_password);
        forget_finish_button = (Button)findViewById(R.id.forget_finish_button);

        forget_finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = forget_username.getText().toString();
                password = forget_password.getText().toString();
                repassword = reforget_password.getText().toString();
                if(!password.equals(repassword))
                    Toast.makeText(ForgetActivity.this,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
                else {
                    url = Constants.FORGETURL+"?Name="+username+"&Pwd="+password;
                    new UserForgetTask().execute(url);
                }
            }
        });
    }

    class UserForgetTask extends AsyncTask<String,Void,String> {
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
                    //Toast.makeText(ForgetActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    return null;
                }
                InputStreamReader in = new InputStreamReader(connection.getInputStream());
                BufferedReader buf = new BufferedReader(in);
                String readLine = null;
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
            if(s.equals(Constants.NOUSER))
                Toast.makeText(ForgetActivity.this,"用户名不存在",Toast.LENGTH_SHORT).show();
            else if(s.equals(Constants.DEFEATE))
                Toast.makeText(ForgetActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(ForgetActivity.this,"修改成功，请登录",Toast.LENGTH_SHORT).show();
                System.out.println("收到："+s);
                Intent intent = new Intent(ForgetActivity.this,MainActivity.class);
                startActivity(intent);
            }
        }
    }
}
