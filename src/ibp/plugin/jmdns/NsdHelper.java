package ibp.plugin.jmdns;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import javax.jmdns.impl.DNSRecord;
import org.json.JSONObject;

/**
 * @author xifeiwu
 * @version 1.0
 */
@SuppressLint("NewApi")
public class NsdHelper {
    private final String DEBUG_TAG = NsdHelper.class.getName();
    
    private Context mContext;
    private Handler mHandler;
    private JmDNS mJmDNS = null;
    private ServiceListener mServiceListener;
    private WifiManager.MulticastLock mMulticastLock;
    private final String TYPE = "_http._tcp.local.";

    public NsdHelper(Context context, Handler handler) {
        mContext = context;
        mHandler = handler;
    }
    public void sendNotification(String type, String msg) {
        Bundle messageBundle = new Bundle();
        messageBundle.putString("type", type);
        messageBundle.putString("msg", msg);
        Message message = new Message();
        message.setData(messageBundle);
        mHandler.sendMessage(message);
    }
    
    private boolean isNsdInitialized = false;
    public void initializeNsd(){
        new Thread(new Runnable(){
            @Override
            public void run() {
                // TODO Auto-generated method stub 
                try {
                    WifiManager wifiManager = (WifiManager) mContext.getSystemService(android.content.Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    int intaddr = wifiInfo.getIpAddress();
                    byte[] byteaddr = new byte[] { (byte) (intaddr & 0xff), 
                            (byte) (intaddr >> 8 & 0xff),
                            (byte) (intaddr >> 16 & 0xff), 
                            (byte) (intaddr >> 24 & 0xff) };
                    InetAddress addr = InetAddress.getByAddress(byteaddr);
                    sendNotification("address", addr.getHostName() + " - " + addr.getHostAddress());            
                    mJmDNS = JmDNS.create(addr);
                    mMulticastLock = wifiManager.createMulticastLock(DEBUG_TAG);
                    mMulticastLock.setReferenceCounted(true);
                    mMulticastLock.acquire();
                } catch (IOException e) {
                    Log.d(DEBUG_TAG, "Error in JmDNS creation: " + e);
                }
                sendNotification("initializeNsd", "Initialized NSD Success.");
                isNsdInitialized = true;
            }                
        }).start();   
    }
    public void stopNsd() {
        if (mJmDNS != null) {
            stopDiscovery();
            if (mServiceListener != null) {
                mJmDNS.removeServiceListener(TYPE, mServiceListener);
                mServiceListener = null;
            }
            mJmDNS.unregisterAllServices();
            try {
                mJmDNS.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        mJmDNS = null;
        if (mMulticastLock != null && mMulticastLock.isHeld()) {
            mMulticastLock.release();
        }
        this.sendNotification("stopNsd", "Stop NSD Success.");
        isNsdInitialized = false;
    }

    private ServiceInfo mServiceInfo = null;
    private boolean isServiceRegistered = false;
    public void registerService(String name, int port, String[] props) {
        try {
            mServiceInfo = ServiceInfo.create(TYPE, name, port, 0, 0, textFromStringArray(props));
            if(!isServiceRegistered){
                mJmDNS.registerService(mServiceInfo);
                isServiceRegistered = true;
            }
        } catch (IOException e) {
            Log.d(DEBUG_TAG, "Error in JmDNS initialization: " + e);
        }
    }
    public void unRegisterService() {
        if ((mJmDNS != null) && (mServiceInfo != null) && isServiceRegistered) {
            mJmDNS.unregisterService(mServiceInfo);
            isServiceRegistered = false;
        }
    }

    private boolean isDiscoverServicesStarted = false;
    public void startDiscovery() {
        mServiceListener = new ServiceListener() {
            @Override
            public void serviceAdded(ServiceEvent event) {
                addServiceInfo(event.getInfo());
            }

            @Override
            public void serviceRemoved(ServiceEvent event) {
                removeServiceInfo(event.getInfo());
            }

            @Override
            public void serviceResolved(ServiceEvent event) {
                reWriteServiceInfo(event.getInfo());
            }
        };
        mJmDNS.addServiceListener(TYPE, mServiceListener);
        isDiscoverServicesStarted = true;
    }
    public void stopDiscovery() {
        if(isDiscoverServicesStarted && mServiceListener != null){
            mJmDNS.removeServiceListener(TYPE, mServiceListener);
            mServiceListener = null;
        }
        mServiceInfoList.clear();
        isDiscoverServicesStarted = false;
        this.sendNotification("stopDiscovery", "Stop NSD Success.");
    }
    public void resolveInfoByName(String name) {
        int index = 0;
        AbsServiceInfo element = null;
        while (index < mServiceInfoList.size()) {
            if (mServiceInfoList.get(index).getName().equals(name)) {
                element = mServiceInfoList.get(index);
                break;
            }
            index++;
        }
        if (null != element) {
            if (element.getAddress() == null) {
//                mNsdManager.resolveService(element, mResolveListener);
                mJmDNS.getServiceInfo(element.getType(), element.getName());
            } else {
                sendNotification("resolveInfoByName", "No Need To Resolve.");
            }
        }
    }

//    public void showServiceCollector() {
//        List<ServiceInfo> infolist = Arrays.asList(mJmDNS.list(TYPE, DNSConstants.SERVICE_INFO_TIMEOUT));
//        int cnt = 1;
//        Iterator<ServiceInfo> iter = infolist.iterator();
//        ServiceInfo element = null;
//        while (iter.hasNext()) {
//            element = (ServiceInfo) iter.next();
//            logger.info(cnt++ + ":");
//            logger.info(this.getAbstractServiceInfo(element).toString());
//        }
//    }

    public void showServiceList() {
        StringBuffer sb = new StringBuffer();
        int cnt = 1;
        Iterator<AbsServiceInfo> iter = mServiceInfoList.iterator();
        AbsServiceInfo element = null;
        sb.append("========there are " + mServiceInfoList.size() + " elements========\n");
        while (iter.hasNext()) {
            element = (AbsServiceInfo) iter.next();
            sb.append(cnt++ + "、" + element.toJSONObject() + "\n");
        }
        sendNotification("showServiceList", sb.toString());
    }

    public AbsServiceInfo getAbstractServiceInfo(ServiceInfo info){
        AbsServiceInfo absInfo= new AbsServiceInfo();
        absInfo.setName(info.getName());
        absInfo.setType(info.getType());
        InetAddress[] addresses = info.getInet4Addresses();
        if(addresses.length > 0){
            absInfo.setAddress(addresses[0].getHostAddress());
        }
        absInfo.setPort(info.getPort());
        if (info.getTextBytes() != null && info.getTextBytes().length > 0) {
            List<String> txtList = new ArrayList<String>();
            int off = 0;
            String txt;
            while (off < info.getTextBytes().length) {
                int len = info.getTextBytes()[off++] & 0xFF;
                if ((len == 0) || (off + len > info.getTextBytes().length)) {
                    break;
                }
                txt = readUTF(info.getTextBytes(), off, len);
                off += len;
                txtList.add(txt);
            }
            absInfo.setTxts(txtList.toArray(new String[txtList.size()]));
        }
        if (info.hasData()) {
        }
        return absInfo;
    }
    private final List<AbsServiceInfo> mServiceInfoList = new ArrayList<AbsServiceInfo>();
    public List<AbsServiceInfo> getServiceInfoList(){
        return mServiceInfoList;
    }
    //rarely used.
    private void addServiceInfo(ServiceInfo info) {
        Iterator<AbsServiceInfo> iter = mServiceInfoList.iterator();
        AbsServiceInfo element;
        boolean isExist = false;
        while (iter.hasNext()) {
            element = (AbsServiceInfo) iter.next();
            if (element.getName().equals(info.getName())) {
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            mServiceInfoList.add(this.getAbstractServiceInfo(info));
            sendNotification("serviceAdded", getAbstractServiceInfo(info).toJSONObject().toString());
        }
    }

    private void reWriteServiceInfo(ServiceInfo info) {
        int index = 0;
        boolean isExist = false;
        while (index < mServiceInfoList.size()) {
            if (mServiceInfoList.get(index).getName().equals(info.getName())) {
                isExist = true;
                break;
            }
            index++;
        }
        if (isExist) {
            mServiceInfoList.set(index, this.getAbstractServiceInfo(info));
        } else {
            mServiceInfoList.add(this.getAbstractServiceInfo(info));
        }
        sendNotification("serviceResolved", getAbstractServiceInfo(info).toJSONObject().toString());
    }
    
    private void removeServiceInfo(ServiceInfo info) {
        Iterator<AbsServiceInfo> iter = mServiceInfoList.iterator();
        AbsServiceInfo element = null;
        boolean isExist = false;
        while (iter.hasNext()) {
            element = (AbsServiceInfo) iter.next();
            if (element.getName().equals(info.getName())) {
                isExist = true;
                break;
            }
        }
        if (isExist) {
            mServiceInfoList.remove(element);
            sendNotification("serviceRemoved", getAbstractServiceInfo(info).toJSONObject().toString());
        }
    }

    /**
     * Read data bytes as a UTF stream.
     */
    public String readUTF(byte data[], int off, int len) {
        int offset = off;
        StringBuffer buf = new StringBuffer();
        for (int end = offset + len; offset < end;) {
            int ch = data[offset++] & 0xFF;
            switch (ch >> 4) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                // 0xxxxxxx
                break;
            case 12:
            case 13:
                if (offset >= len) {
                    return null;
                }
                // 110x xxxx 10xx xxxx
                ch = ((ch & 0x1F) << 6) | (data[offset++] & 0x3F);
                break;
            case 14:
                if (offset + 2 >= len) {
                    return null;
                }
                // 1110 xxxx 10xx xxxx 10xx xxxx
                ch = ((ch & 0x0f) << 12) | ((data[offset++] & 0x3F) << 6) | (data[offset++] & 0x3F);
                break;
            default:
                if (offset + 1 >= len) {
                    return null;
                }
                // 10xx xxxx, 1111 xxxx
                ch = ((ch & 0x3F) << 4) | (data[offset++] & 0x0f);
                break;
            }
            buf.append((char) ch);
        }
        return buf.toString();
    }

    /**
     * Write a UTF string with a length to a stream.
     */
    private void writeUTF(OutputStream out, String str) throws IOException {
        for (int i = 0, len = str.length(); i < len; i++) {
            int c = str.charAt(i);
            if ((c >= 0x0001) && (c <= 0x007F)) {
                out.write(c);
            } else {
                if (c > 0x07FF) {
                    out.write(0xE0 | ((c >> 12) & 0x0F));
                    out.write(0x80 | ((c >> 6) & 0x3F));
                    out.write(0x80 | ((c >> 0) & 0x3F));
                } else {
                    out.write(0xC0 | ((c >> 6) & 0x1F));
                    out.write(0x80 | ((c >> 0) & 0x3F));
                }
            }
        }
    }

    private byte[] textFromProperties(Map<String, ?> props) {
        byte[] text = null;
        if (props != null) {
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream(256);
                for (String key : props.keySet()) {
                    Object val = props.get(key);
                    ByteArrayOutputStream out2 = new ByteArrayOutputStream(100);
                    writeUTF(out2, key);
                    if (val == null) {// Skip
                    } else if (val instanceof String) {
                        out2.write('=');
                        writeUTF(out2, (String) val);
                    } else if (val instanceof byte[]) {
                        byte[] bval = (byte[]) val;
                        if (bval.length > 0) {
                            out2.write('=');
                            out2.write(bval, 0, bval.length);
                        } else {
                            val = null;
                        }
                    } else {
                        throw new IllegalArgumentException("invalid property value: " + val);
                    }
                    byte data[] = out2.toByteArray();
                    if (data.length > 255) {
                        throw new IOException("Cannot have individual values larger that 255 chars. Offending value: "
                                + key + (val != null ? "" : "=" + val));
                    }
                    out.write((byte) data.length);
                    out.write(data, 0, data.length);
                }
                text = out.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException("unexpected exception: " + e);
            }
        }
        return (text != null && text.length > 0 ? text : DNSRecord.EMPTY_TXT);
    }

    private byte[] textFromStringArray(String[] props) {
        byte[] text = null;
        if (props != null) {
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream(256);
                for (String val : props) {
                    ByteArrayOutputStream out2 = new ByteArrayOutputStream(100);
                    if (val == null) {
                        // Skip
                    } else if (val instanceof String) {
                        writeUTF(out2, (String) val);
                    } else {
                        throw new IllegalArgumentException("invalid property value: " + val);
                    }
                    byte data[] = out2.toByteArray();
                    if (data.length > 255) {
                        throw new IOException("Cannot have individual values larger that 255 chars. Offending value: "
                                + val);
                    }
                    out.write((byte) data.length);
                    out.write(data, 0, data.length);
                }
                text = out.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException("unexpected exception: " + e);
            }
        }
        return (text != null && text.length > 0 ? text : DNSRecord.EMPTY_TXT);
    }
}

class AbsServiceInfo{
    private String name;
    private String type;
    private String address;
    private int port;
    private String[] txts = null;
    public AbsServiceInfo(){
    }
    public AbsServiceInfo(String name, String address, int port){
        this.name = name;
        this.address = address;
        this.port = port;
    }
    public String getName(){
        return name;
    }
    public String getType(){
        return type;
    }
    public String getAddress(){
        return address;
    }
    public int getPort(){
        return port;
    }
    public String[] getTxts(){
        return txts;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setType(String type){
        this.type = type;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public void setPort(int port){
        this.port = port;
    }
    public void setTxts(String[] txts){
        this.txts = txts;
    }
    public JSONObject toJSONObject(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("type", type);
        map.put("address", address);
        map.put("port", port);
        map.put("txts", txts);
        JSONObject jsonObj = new JSONObject(map);
        return jsonObj;        
    }
    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append(name);
        sb.append(" - ");
        sb.append("{");
        sb.append(address + ":" + port);
        sb.append("} - ");
        sb.append("txts: {");
        if(txts != null){
            for(String txt: txts){
                sb.append(txt);
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
