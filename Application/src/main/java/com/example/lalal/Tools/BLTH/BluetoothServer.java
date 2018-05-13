package com.example.lalal.Tools.BLTH;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.regex.Pattern;

public class BluetoothServer {

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static BluetoothAdapter mBluetoothAdapter;
    public static ConnectThread mConnectThread;
    public static ConnectedThread mConnectedThread;
    public static boolean bluetoothflag = false ;
    public static int heartsportnumber;
    public static String hsff = "N";
    public static boolean sendflag = false;


    //连接
    public void ConnectBluetooth(BluetoothDevice bluetoothDevice) {
        mConnectThread = new ConnectThread(bluetoothDevice);
        mConnectThread.start();
    }

    //连接线程
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            Log.v("开始", "开始进入");
            System.out.println("开始进入");
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;
            System.out.println("mmDevice:"+mmDevice);
            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            //mBluetoothAdapter.cancelDiscovery();

            try {
                System.out.println("开始连接");
                bluetoothflag = true;
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                }
                return;
            }

            synchronized (BluetoothServer.this) {
                mConnectThread = null;
            }

            // Do work to manage the connection (in a separate thread)
            System.out.println("开始消息");
            Bluetoothdevice.mBluetoothSocket = mmSocket;
            manageConnectedSocket(mmSocket);
        }

        /**
         * Will cancel an in-progress connection, and close the socket
         */
        public void cancel() {
            try {
                bluetoothflag = false;
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    //消息
    public void manageConnectedSocket(BluetoothSocket bluetoothSocket) {
        System.out.println("开始消息管理");
        if (mConnectThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        mConnectedThread = new ConnectedThread(bluetoothSocket);
        mConnectedThread.start();
    }

    //消息线程
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = null;  // buffer store for the stream
            int bytes; // bytes returned from read()
            System.out.println("接收消息");
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    if (mmInStream.available() != 0) {
                        buffer = new byte[mmInStream.available()];
                        System.out.println("avali：" + mmInStream.available());
                        bytes = mmInStream.read(buffer);
                        // Send the obtained bytes to the UI activity
                        String str = new String(buffer);
                        System.out.println("长度：" + bytes + " " + "内容：" + str);
                        if(str.equals("K")){
                            sendflag = true;
                        }
                        else {
                            if(!hsff.equals("N")){
                                if(hsff.equals("H")){
                                    if(str.length()>1){
                                        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
                                        if(pattern.matcher(str.trim()).matches())
                                            heartsportnumber = Integer.valueOf(str.trim());
                                    }
                                }
                                else if(hsff.equals("S")){
                                    if(str.length()>0)
                                        heartsportnumber = Integer.valueOf(str.trim());
                                }
                            }
                        }

                    }
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    public static void send(String string){
        hsff = string;
        mConnectedThread.write(string.getBytes());
    }

    public static int number(){
        return heartsportnumber;
    }

}
