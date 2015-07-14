package study.android.DebugLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;


import android.content.res.AssetManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Message;
import android.util.Log;

public class DebugFunc {
    private DebugActivity mContext;
    public DebugFunc(DebugActivity activity){
        this.mContext = activity;
//        info(getLocalIP());
//        info(getIPByWifiManager());
//        info(activity.getBaseContext().getFilesDir().getAbsolutePath());
//        mContext.getBaseContext().getFilesDir().getAbsoluteFile();
//        info(mContext.getFileDir().getAbsolutePath());
        testMessage();
        info(readFile("index.html"));
    }
    private void info(String info){
        mContext.info(info);
    }
    private void testMessage(){
        Message message = new Message();
        info("Message what: " + message.what);
        info("Message arg1: " + message.arg1);
        info("Message arg2: " + message.arg2);
    }
    //需要权限: <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    private String getIPByWifiManager(){
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(android.content.Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int intaddr = wifiInfo.getIpAddress();
        byte[] byteaddr = new byte[] { (byte) (intaddr & 0xff), 
                (byte) (intaddr >> 8 & 0xff),
                (byte) (intaddr >> 16 & 0xff), 
                (byte) (intaddr >> 24 & 0xff) };
        InetAddress addr = null;
        try {
            addr = InetAddress.getByAddress(byteaddr);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return addr.getHostAddress();
    }
    private String getLocalIP(){
        String address = null;
        try {
            for (Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces(); nifs.hasMoreElements();) {
                if(null != address){
                    break;
                }
                NetworkInterface nif = nifs.nextElement();
//                    logToConsole("name of network interface: " + nif.getName());
                for (Enumeration<InetAddress> iaenum = nif.getInetAddresses(); iaenum.hasMoreElements();) {
                    InetAddress interfaceAddress = iaenum.nextElement();
                      if (!interfaceAddress.isLoopbackAddress()) {
                          if (interfaceAddress instanceof Inet4Address) {
//                                  logToConsole(interfaceAddress.getHostName() + " -- " + interfaceAddress.getHostAddress());
                              address = interfaceAddress.getHostAddress();
                              break;
                          }
                      }
                }
            }
        } catch (SocketException se) {
            se.printStackTrace();
        }
        return address;
    }


    public String readFile(String location) {
        return readFile(location, "UTF-8");
    }

    public String readFile(String location, String encoding) {
        StringBuilder sb = new StringBuilder();
        try {
            AssetManager asm = mContext.getBaseContext().getAssets();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    asm.open(location), encoding));
            String str = br.readLine();
            while (str != null) {
                sb.append(str + "\n");
                str = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            Log.w("jxcore-FileManager", "readfile failed");
            info("jxcore-FileManager: " + "readfile failed");
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }

    public int aproxFileSize(String location) {
        int size = 0;
        try {
            AssetManager asm = mContext.getBaseContext().getAssets();
            InputStream st = asm.open(location, AssetManager.ACCESS_UNKNOWN);
            size = st.available();
            st.close();
        } catch (IOException e) {
            Log.w("jxcore-FileManager", "aproxFileSize failed");
            e.printStackTrace();
            return 0;
        }
        return size;
    }
}
