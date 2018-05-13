package com.example.lalal.MyNewGraduate;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lalal.Po.User;
import com.example.lalal.Tools.ConStant.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {

    private EditText login_username;
    private EditText login_password;

    private Button login_button;
    private Button register_button;

    private TextView find_password;

    private String username;
    private String password;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_username = (EditText)findViewById(R.id.login_username);
        login_password = (EditText)findViewById(R.id.login_password);
        login_button = (Button)findViewById(R.id.login_button);
        register_button = (Button)findViewById(R.id.register_button);
        find_password = (TextView)findViewById(R.id.find_password);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = login_username.getText().toString();
                password = login_password.getText().toString();
                url = Constants.LOGINURL+"?Name="+username+"&Pwd="+password;
                new UserLoginTask().execute(url);
            }
        });
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        find_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ForgetActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class UserLoginTask extends AsyncTask<String,Void,String>{
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
            if(s==null)
                Toast.makeText(MainActivity.this,"网络连接失败",Toast.LENGTH_SHORT).show();
            else {
                System.out.println("收到："+s);
                if(s.equals(Constants.NOUSER))
                    Toast.makeText(MainActivity.this,"用户名不存在",Toast.LENGTH_SHORT).show();
                else if(s.equals(Constants.DEFEATE))
                    Toast.makeText(MainActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                    System.out.println("收到："+s);
                    String[] str = s.split("#");
                    Constants.user = new User();
                    Constants.user.setUserid(Integer.valueOf(str[0]));
                    Constants.user.setUsername(str[1]);
                    Constants.user.setUserpwd(str[2]);
                    Constants.user.setUserimg(str[3]);
                    String path = Constants.PROJECTPATH+Constants.user.getUsername()+"/";
                    Constants.userpath = path;
                    File file = new File(path);
                    if(!file.exists())
                        file.mkdir();
                    Intent intent = new Intent(MainActivity.this,MainFragmentActivity.class);
                    startActivity(intent);
                }
            }
        }
    }
}
