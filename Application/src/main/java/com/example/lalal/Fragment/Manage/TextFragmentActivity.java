package com.example.lalal.Fragment.Manage;

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
import android.os.Build;
import android.provider.Settings;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.lalal.MyNewGraduate.R;
import com.example.lalal.Tools.BLTH.BlueToothTools;

import java.util.Set;

public class TextFragmentActivity extends Activity {

    private final int requestCode = 0;
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayAdapter mArrayAdapter;
    private final int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private BlueToothTools mBluetoothTools = new BlueToothTools();
    private boolean scanflag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textfragment);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        checkgps();
        appendaccess();
        checkbluetooth();
        YSearchDevices();
        WSearchDevices();
    }

    //检查gps
    public void checkgps(){
        //打开GPS
        if(!isGpsEnable(TextFragmentActivity.this)){
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
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);
    }

    //显示已配对设备
    public void YSearchDevices(){
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                System.out.println("已配对："+device.getName() + "MAC" + device.getAddress());
                mBluetoothTools.setDeviceName(device.getName());
                mBluetoothTools.setDeviceAddr(device.getName());

                //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
    }

    //显示未配对设备
    public void WSearchDevices(){
        // Register the BroadcastReceiver
        mBluetoothAdapter.startDiscovery();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
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
            }
        }
    };
}

