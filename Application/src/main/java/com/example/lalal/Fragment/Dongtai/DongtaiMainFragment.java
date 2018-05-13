package com.example.lalal.Fragment.Dongtai;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class DongtaiMainFragment extends Fragment {

    private Button fenxiang,otherdongtai1,editdongtai;
    private ListView listviewmsg;
    private BaseAdapter msgadapter;
    private List<Map<Integer,String>> listmsg;
    private TextView username_msg,messagetext_msg,messagetime_msg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_dongtaimain,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fenxiang = view.findViewById(R.id.fenxiang);
        otherdongtai1 = view.findViewById(R.id.otherdongtai1);
        listviewmsg = view.findViewById(R.id.listviewmsg);
        editdongtai = view.findViewById(R.id.editdongtai);
        init();
    }

    public void init(){
        fenxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = Constants.ADDMESSAGEURL + "?Userid=" + Constants.user.getUserid() + "&Msgtext=" +  "今日已走："+"5656"+"步";
                AddMessageTask addMessageTask = new AddMessageTask();
                addMessageTask.execute(path);
            }
        });
        otherdongtai1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = Constants.SHOWMESSAGEURL+"?Userid=" + Constants.user.getUserid();
                ShowMessageTask showMessageTask = new ShowMessageTask();
                showMessageTask.execute(path);
            }
        });
        editdongtai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),EditSSActivity.class);
                startActivity(intent);
            }
        });
    }

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
                map.put(3,jsonObject.get("3").toString().substring(0,19));
                list.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void showmsg(){
        msgadapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return listmsg.size();
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
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View view = null;
                if (convertView==null) {
                    //因为getView()返回的对象，adapter会自动赋给ListView
                    view = inflater.inflate(R.layout.list_message, null);
                }else{
                    view=convertView;
                    Log.i("info","配对有缓存，不需要重新生成"+position);
                }
                username_msg = view.findViewById(R.id.username_msg);
                messagetext_msg = view.findViewById(R.id.messagetext_msg);
                messagetime_msg = view.findViewById(R.id.messagetime_msg);
                username_msg.setText("用户："+listmsg.get(position).get(1));
                messagetext_msg.setText(listmsg.get(position).get(2));
                messagetime_msg.setText(listmsg.get(position).get(3));
                return view;
            }
        };
    }



    class AddMessageTask extends AsyncTask<String,Void,String>{
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
                Toast.makeText(getActivity(),"分享失败",Toast.LENGTH_SHORT).show();
            else if(s.equals(Constants.SUCCESSFUL))
                Toast.makeText(getActivity(),"分享成功",Toast.LENGTH_SHORT).show();
        }
    }

    class ShowMessageTask extends AsyncTask<String,Void,String>{
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
                Toast.makeText(getActivity(),"没有任何动态",Toast.LENGTH_SHORT).show();
            else{
                listmsg = new ArrayList<Map<Integer,String>>();
                listmsg = FromJToLM(s);

            }
            if(listmsg!=null){
                System.out.println(listmsg.get(0).get(3));
                showmsg();
                msgadapter.notifyDataSetChanged();
                listviewmsg.setAdapter(msgadapter);
            }
        }
    }

}
