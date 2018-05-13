package com.example.lalal.Fragment.Personal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lalal.MyNewGraduate.R;
import com.example.lalal.Tools.ConStant.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowFriendActivity extends Activity {

    private Button chakanshenqing,chakan;
    private ListView shenqingList,haoyouList;
    private BaseAdapter shenqingadapter,friendadapter;
    private List<Map<Integer,String>> listshenqing ;
    private List<Map<Integer, String>>  listfriend;
    private TextView friendname;
    public static int ption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_friend);
        chakanshenqing = findViewById(R.id.chakanshenqing);
        chakan = findViewById(R.id.chakan);
        shenqingList = findViewById(R.id.shenqingList);
        haoyouList = findViewById(R.id.haoyouList);
        init();
    }

    public void init(){
        chakanshenqing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowStatusTask showStatusTask = new ShowStatusTask();
                showStatusTask.execute(Constants.SHOWSTTATUSURL+"?Userid="+Constants.user.getUserid());
            }
        });
        shenqingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ption = position;
                showMultiBtnDialog();
            }
        });
        chakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = Constants.SHOWFRIENDNUL+"?Userid="+Constants.user.getUserid();
                ShowFriendTask showFriendTask = new ShowFriendTask();
                showFriendTask.execute(path);
            }
        });
    }

    //
    public void friend(){
        friendadapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return listfriend.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = ShowFriendActivity.this.getLayoutInflater();
                View view = null;
                if (convertView==null) {
                    //因为getView()返回的对象，adapter会自动赋给ListView
                    view = inflater.inflate(R.layout.list_friend, null);
                }else{
                    view=convertView;
                    Log.i("info","配对有缓存，不需要重新生成"+position);
                }
                friendname = view.findViewById(R.id.friendname);
                friendname.setText(listfriend.get(position).get(2));
                return view;
            }
        };
    }

    //
    public void sheniqng(){
        shenqingadapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return listshenqing.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = ShowFriendActivity.this.getLayoutInflater();
                View view = null;
                if (convertView==null) {
                    //因为getView()返回的对象，adapter会自动赋给ListView
                    view = inflater.inflate(R.layout.list_friend, null);
                }else{
                    view=convertView;
                    Log.i("info","配对有缓存，不需要重新生成"+position);
                }
                friendname = view.findViewById(R.id.friendname);
                friendname.setText(listshenqing.get(position).get(2));
                return view;
            }
        };
    }

    //
    private void showMultiBtnDialog(){
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(ShowFriendActivity.this);
        normalDialog.setTitle("处理请求");
        normalDialog.setPositiveButton("允许",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // ...To-do
                        String path = Constants.CHANGESTATUSURL+ "?Userid="+Constants.user.getUserid()+"&User_friend_id="+listshenqing.get(ption).get(1)+ "&Status="+1;
                        System.out.println("listshenqing:"+listshenqing);
                        System.out.println("ption:"+ption);
                        System.out.println("地址："+path);
                        ChangeStatusTask changeStatusTask = new ChangeStatusTask();
                        changeStatusTask.execute(path);

                    }
                });
        normalDialog.setNeutralButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // ...To-do

                    }
                });
        normalDialog.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // ...To-do
                String path = Constants.CHANGESTATUSURL+
                        "?Userid="+Constants.user.getUserid()+
                        "&User_friend_id="+listshenqing.get(ption).get(1)+
                        "&Status="+2;
                ChangeStatusTask changeStatusTask = new ChangeStatusTask();
                changeStatusTask.execute(path);
            }
        });
        // 创建实例并显示
        normalDialog.show();
    }

    //
    public List<Map<Integer, String>> FromJToLM(String string) {
        List<Map<Integer, String>> list = new ArrayList<Map<Integer,String>>();
        Map<Integer,String> map;
        try {
            JSONArray jsonstr = new JSONArray(string);
            System.out.println(jsonstr);
            for(int i=0;i<jsonstr.length();i++){
                JSONObject jsonObject = jsonstr.getJSONObject(i);
                map = new HashMap<Integer, String>();
                map.put(1,jsonObject.get("1").toString());
                map.put(2,jsonObject.get("2").toString());
                list.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    //
    class ShowStatusTask extends AsyncTask<String,Void,String>{

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
            if(s.equals(Constants.NOMSG))
                Toast.makeText(ShowFriendActivity.this,"没有申请列表",Toast.LENGTH_SHORT).show();
            else{
                listshenqing = new ArrayList<Map<Integer,String>>();
                listshenqing = FromJToLM(s);
            }
            if(listshenqing!=null){
                sheniqng();
                shenqingadapter.notifyDataSetChanged();
                shenqingList.setAdapter(shenqingadapter);
            }
        }
    }

    //
    class ChangeStatusTask extends AsyncTask<String,Void,String>{

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
            if(s.equals(Constants.SUCCESSFUL))
                Toast.makeText(ShowFriendActivity.this,"操作成功",Toast.LENGTH_SHORT).show();
            else if(s.equals(Constants.DEFEATE))
                Toast.makeText(ShowFriendActivity.this,"操作失败",Toast.LENGTH_SHORT).show();
        }
    }

    //
    class ShowFriendTask extends AsyncTask<String,Void,String>{

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
            if(s.equals(Constants.NOFRIEND))
                Toast.makeText(ShowFriendActivity.this,"未添加好友",Toast.LENGTH_SHORT).show();
            else {
                listfriend = new ArrayList<Map<Integer,String>>();
                listfriend = FromJToLM(s);
                if(listfriend!=null){
                    friend();
                    friendadapter.notifyDataSetChanged();
                    haoyouList.setAdapter(friendadapter);
                }
            }
        }
    }

}
