package com.example.lalal.Fragment.Personal;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lalal.MyNewGraduate.MainActivity;
import com.example.lalal.MyNewGraduate.MainFragmentActivity;
import com.example.lalal.MyNewGraduate.R;
import com.example.lalal.Po.User;
import com.example.lalal.Tools.ConStant.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddFriendActivity extends Activity {

    private SearchView searchid;
    private TextView textview_add;
    private boolean checked=false;
    private static User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        searchid = findViewById(R.id.searchid);
        textview_add = findViewById(R.id.textview_add);
        init();
    }

    private void init(){
        searchid.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (checked==true){
                    System.out.println("收到："+query);
                    textview_add.setText("");
                    checked = false;
                    FindUserNameTask findUserNameTask = new FindUserNameTask();
                    findUserNameTask.execute(Constants.FINDUSERURL+"?Name="+query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("收到中："+newText);
                checked = true;
                return false;
            }
        });
        textview_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFriendTask addFriendTask = new AddFriendTask();
                String path = Constants.ADDFRIENDURL+"?Userid="+ Constants.user.getUserid()+"&User_friend_id="+user.getUserid();
                addFriendTask.execute(path);
            }
        });
    }

    class FindUserNameTask extends AsyncTask<String ,Void,String>{

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
            if(s.equals(Constants.NOUSER))
                Toast.makeText(AddFriendActivity.this,"用户不存在",Toast.LENGTH_SHORT).show();
            else{
                String[] str = s.split("#");
                user.setUserid(Integer.valueOf(str[0]));
                user.setUsername(str[1]);
                textview_add.setText(str[1]);
            }

        }
    }

    class AddFriendTask extends AsyncTask<String ,Void,String>{

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
                Toast.makeText(AddFriendActivity.this,"申请失败",Toast.LENGTH_SHORT).show();
            else if(s.equals(Constants.SUCCESSFUL))
                Toast.makeText(AddFriendActivity.this,"申请成功，等待用户审核",Toast.LENGTH_SHORT).show();
            else if(s.equals(Constants.EXITFRIEND))
                Toast.makeText(AddFriendActivity.this,"申请失败,你们已经是朋友关系",Toast.LENGTH_SHORT).show();
        }

    }

}
