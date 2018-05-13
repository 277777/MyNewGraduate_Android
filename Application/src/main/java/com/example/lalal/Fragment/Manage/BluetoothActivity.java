package com.example.lalal.Fragment.Manage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lalal.MyNewGraduate.R;
import com.example.lalal.Tools.BLTH.BlueToothTools;
import com.example.lalal.Tools.BLTH.BluetoothServer;
import com.example.lalal.Tools.BLTH.Bluetoothdevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BluetoothActivity extends Activity{

    private SwitchCompat btoothswitch;
    private ListView resultoldList;
    private ListView resultnewList;
    private RelativeLayout searcholdLayout;
    private RelativeLayout searchnewLayout;
    private TextView device_name;
    private TextView device_address;
    private Button ypdsousuo;
    private Button wpdsousuo;

    private BluetoothAdapter mBluetoothAdapter;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final int SCAN_PERIOD = 15000;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int requestCode = 0;
    private static int ypdnumber = 0;
    private static int wpdnumber = 0 ;
    private List<BlueToothTools> listypd = new ArrayList<>();
    private List<BlueToothTools> listwpd = new ArrayList<>();
    private Map<Integer,BluetoothDevice> ypdmap ;
    private Map<Integer,BluetoothDevice> wpdmap ;
    private List<Map<Integer,BluetoothDevice>> listmapwpd = new ArrayList<Map<Integer, BluetoothDevice>>();

    private BaseAdapter adapterypd;
    private BaseAdapter adapterwpd;

    private BluetoothServer mBluetoothServer = new BluetoothServer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        btoothswitch = (SwitchCompat) findViewById(R.id.btoothswitch);
        resultoldList = (ListView) findViewById(R.id.resultoldList);
        resultnewList = (ListView) findViewById(R.id.resultnewList);
        searcholdLayout = (RelativeLayout) findViewById(R.id.searcholdLayout);
        searchnewLayout = (RelativeLayout) findViewById(R.id.searchnewLayout);
        ypdsousuo = (Button)findViewById(R.id.ypdsousuo);
        wpdsousuo = (Button)findViewById(R.id.wpdsousuo);
        searcholdLayout.setVisibility(View.INVISIBLE);
        searchnewLayout.setVisibility(View.INVISIBLE);
        btoothswitch.setSwitchMinWidth(200);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        checkgps();
        appendaccess();
        //主按钮
        btoothswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    searcholdLayout.setVisibility(View.VISIBLE);
                    searchnewLayout.setVisibility(View.VISIBLE);
                    checkbluetooth();
                } else {
                    searcholdLayout.setVisibility(View.INVISIBLE);
                    searchnewLayout.setVisibility(View.INVISIBLE);
                    resultoldList.setAdapter(null);
                    resultnewList.setAdapter(null);
                }
            }
        });
        //已配对按钮
        ypdsousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listypd.clear();
                ypdnumber = 0;
                YSearchDevices();
            }
        });
        //未配对按钮
        wpdsousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listwpd.clear();
                listmapwpd.clear();
                wpdnumber = 0 ;
                WSearchDevices();
            }
        });
        //已配对列表按钮
        resultoldList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new Bluetoothdevice().mBluetoothDevice = ypdmap.get(position);
                mBluetoothServer.ConnectBluetooth(ypdmap.get(position));


            }
        });
        //未配对列表按钮
        resultnewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("你看看："+wpdmap.size());
                System.out.println("你看看："+wpdmap);
                System.out.println("你看看："+listmapwpd);
                new Bluetoothdevice().mBluetoothDevice = listmapwpd.get(position).get(1);
                mBluetoothAdapter.cancelDiscovery();
                System.out.println("position:"+position+"   "+"device"+wpdmap.get(position));
                mBluetoothServer.ConnectBluetooth(listmapwpd.get(position).get(1));

            }
        });
    }

    //检查gps
    public void checkgps(){
        //打开GPS
        if(!isGpsEnable(BluetoothActivity.this)){
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent,requestCode);
        }
    }

    // gps是否可用
    public static final boolean isGpsEnable(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    //申请位置信息
    public void appendaccess(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }
    }

    //检查蓝牙
    public void checkbluetooth(){
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            System.out.println("Device does not support Bluetooth");
            finish();
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);
    }

    //搜索已配对设备
    public void YSearchDevices(){
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                System.out.println("已配对："+device.getName() + "MAC" + device.getAddress());
                ypdmap = new HashMap<Integer, BluetoothDevice>();
                ypdmap.put(1,device);
                ypdnumber++;
                BlueToothTools mYBluetoothTools = new BlueToothTools();
                mYBluetoothTools.setDeviceName(device.getName());
                mYBluetoothTools.setDeviceAddr(device.getAddress());
                listypd.add(mYBluetoothTools);
                listviewypdlist();
                adapterypd.notifyDataSetChanged();
                resultoldList.setAdapter(adapterypd);
                //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
    }

    //显示已配对设备
    public void listviewypdlist(){
        adapterypd = new BaseAdapter() {
            @Override
            public int getCount() {
                return listypd.size();
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
                LayoutInflater inflater = BluetoothActivity.this.getLayoutInflater();
                View view = null;
                if (convertView==null) {
                    //因为getView()返回的对象，adapter会自动赋给ListView
                    view = inflater.inflate(R.layout.list_item, null);
                }else{
                    view=convertView;
                    Log.i("info","有缓存，不需要重新生成"+position);
                }
                device_name = (TextView)view.findViewById(R.id.device_name);
                device_address = (TextView)view.findViewById(R.id.device_address);
                device_name.setText(listypd.get(position).getDeviceName());//设置参数
                device_address.setText(listypd.get(position).getDeviceAddr());//设置参数
                return view;
            }
        };
    }

    //显示未配对设备
    public void WSearchDevices(){
        // Register the BroadcastReceiver
        mBluetoothAdapter.startDiscovery();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
        new MyAsynTask().execute();
    }

    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                System.out.println("未配对："+device.getName() + "MAC" + device.getAddress());
                if(isExit(listypd,device.getAddress())==false){
                    wpdmap = new HashMap<Integer, BluetoothDevice>();
                    BlueToothTools mWBluetoothTools = new BlueToothTools();
                    wpdmap.put(1,device);
                    wpdnumber++;
                    listmapwpd.add(wpdmap);
                    if(device.getName()==null){
                        mWBluetoothTools.setDeviceName(device.getAddress());
                    }else{
                        mWBluetoothTools.setDeviceName(device.getName());
                    }
                    mWBluetoothTools.setDeviceAddr(device.getAddress());
                    listwpd.add(mWBluetoothTools);
                }
            }
        }
    };


    //显示未配对设备
    public void listviewwpdlist(){
        adapterwpd = new BaseAdapter() {
            @Override
            public int getCount() {
                return listwpd.size();
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
                LayoutInflater inflater = BluetoothActivity.this.getLayoutInflater();
                View view = null;
                if (convertView==null) {
                    //因为getView()返回的对象，adapter会自动赋给ListView
                    view = inflater.inflate(R.layout.list_item, null);
                }else{
                    view=convertView;
                    Log.i("info","配对有缓存，不需要重新生成"+position);
                }
                device_name = (TextView)view.findViewById(R.id.device_name);
                device_address = (TextView)view.findViewById(R.id.device_address);
                device_name.setText(listwpd.get(position).getDeviceName());//设置参数
                device_address.setText(listwpd.get(position).getDeviceAddr());//设置参数
                return view;
            }
        };
    }

    //更新未配对列表
    class MyAsynTask extends AsyncTask{
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Thread.sleep(SCAN_PERIOD);
                for(int i=0;i<listwpd.size();i++)
                    System.out.println("配对a"+listwpd.get(i).getDeviceAddr());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            System.out.println("配对hahahah");
            listviewwpdlist();
            adapterwpd.notifyDataSetChanged();
            resultnewList.setAdapter(adapterwpd);
        }
    }

    //未配对列表检测
    private boolean isExit(List<BlueToothTools> list, String str){
        boolean flag = false;
        for(int i=0;i<list.size();i++){
            if(list.get(i).getDeviceAddr()==str){
                flag = true;
                break;
            }else
                flag = false;
        }
        return flag;
    }

}
