package study.android.DebugLog;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;


import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class DebugFunc {
    private DebugActivity mContext;
    public DebugFunc(DebugActivity activity){
        this.mContext = activity;
        info(getLocalIP());
        info(getIPByWifiManager());
    }
    private void info(String info){
        mContext.info(info);
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
}
