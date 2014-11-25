/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package study.android.net.nsd;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.net.nsd.NsdManager;
import android.util.Log;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("NewApi") 
public class NsdHelper {
    NsdChatActivity mContext;

    NsdManager mNsdManager;
    NsdManager.ResolveListener mResolveListener;
    NsdManager.DiscoveryListener mDiscoveryListener;
    NsdManager.RegistrationListener mRegistrationListener;
    private final List<NsdServiceInfo> mServerInfoList = new ArrayList<NsdServiceInfo>();


    public static final String TAG = "NsdHelper";
    public String mServiceName = "NsdChat";
    public static final String SERVICE_TYPE = "_http._tcp.";

//    NsdServiceInfo mService;

    public NsdHelper(Context context) {
        mContext = (NsdChatActivity) context;
        mNsdManager = (NsdManager) context
                .getSystemService(Context.NSD_SERVICE);
    }

    public void initializeNsd() {
        initializeResolveListener();
        initializeDiscoveryListener();
        initializeRegistrationListener();
        // mNsdManager.init(mContext.getMainLooper(), this);
    }

    public void initializeDiscoveryListener() {
        mDiscoveryListener = new NsdManager.DiscoveryListener() {
            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d(TAG, "Service discovery started");
                mContext.show("Service discovery started");
            }
            @Override
            public void onServiceFound(NsdServiceInfo service) {
                Log.d(TAG, "onServiceFound: " + NsdServiceInfoToString(service));
                mContext.show("onServiceFound:" + NsdServiceInfoToString(service));
                addServerInfo(service);
            }
            @Override
            public void onServiceLost(NsdServiceInfo service) {
                Log.d(TAG, "onServiceLost: " + NsdServiceInfoToString(service));
                mContext.show("onServiceLost:" + NsdServiceInfoToString(service));
                removeServerInfo(service);  
            }
            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i(TAG, "onDiscoveryStopped: " + serviceType);
                mContext.show("onDiscoveryStopped: " + serviceType);
            }
            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "onStartDiscoveryFailed, Error code: " + errorCode);
                mContext.show("onStartDiscoveryFailed, Error code: " + errorCode);

                mNsdManager.stopServiceDiscovery(this);
            }
            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "onStopDiscoveryFailed, Error code: " + errorCode);
                mContext.show("onStopDiscoveryFailed, Error code: " + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }
        };
    }

    public void initializeResolveListener() {
        mResolveListener = new NsdManager.ResolveListener() {
            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo,
                    int errorCode) {
                Log.e(TAG, "onResolveFailed, Error code: " + errorCode);
                mContext.show("onResolveFailed, Error code: " + errorCode);
            }
            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                Log.d(TAG, "onServiceResolved: " + NsdServiceInfoToString(serviceInfo));
                mContext.show("onServiceResolved: " + NsdServiceInfoToString(serviceInfo));
                String oldName = serviceInfo.getServiceName();
//                for(int i=0; i<oldName.length(); i++){
//                    mContext.show(i + ": " + oldName.charAt(i) + " + " + (short)oldName.charAt(i));                    
//                }
//                oldName = "fd\032safifodafel;safj\032espoafejsa;fke";
//                for(int i=0; i<oldName.length(); i++){
//                    mContext.show(i + ": " + oldName.charAt(i));                    
//                }
                String newName = oldName.replace("\\032", " ");
                mContext.show("oldName: " + oldName + " * " + "newName: " + newName );
                serviceInfo.setServiceName(newName);                
                reWriteServerInfo(serviceInfo);
            }
        };
    }

    public void initializeRegistrationListener() {
        mRegistrationListener = new NsdManager.RegistrationListener() {
            @Override
            public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
                mServiceName = NsdServiceInfo.getServiceName();
                Log.d(TAG, "onServiceRegistered: " + NsdServiceInfoToString(NsdServiceInfo));
                mContext.show("onServiceRegistered: " + NsdServiceInfoToString(NsdServiceInfo));
            }
            @Override
            public void onRegistrationFailed(NsdServiceInfo arg0, int arg1) {
                Log.d(TAG, "onRegistrationFailed: " );
                mContext.show("onRegistrationFailed: ");
            }
            @Override
            public void onServiceUnregistered(NsdServiceInfo arg0) {
                Log.d(TAG, "onServiceUnregistered: " + NsdServiceInfoToString(arg0));
                mContext.show("onServiceUnregistered: " + NsdServiceInfoToString(arg0));
            }
            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo,
                    int errorCode) {
                Log.d(TAG, "onUnregistrationFailed: ");
                mContext.show("onUnregistrationFailed: ");
            }
        };
    }

    private boolean isServiceRegistered = false;
    public void registerService(int port) {
        NsdServiceInfo serviceInfo = new NsdServiceInfo();
        serviceInfo.setPort(port);
        serviceInfo.setServiceName(mServiceName);
        serviceInfo.setServiceType(SERVICE_TYPE);
        mNsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD,
                mRegistrationListener);
        isServiceRegistered = true;
    }
    public void unRegisterService() {
        if(isServiceRegistered){
            mNsdManager.unregisterService(mRegistrationListener);
        }
        isServiceRegistered = false;
    }

    private boolean isDiscoverServicesStarted = false;
    public void discoverServices() {
        mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD,
                mDiscoveryListener);
        isDiscoverServicesStarted = true;
    }

    public void stopDiscovery() {
        if(isDiscoverServicesStarted && mDiscoveryListener != null){
            mNsdManager.stopServiceDiscovery(mDiscoveryListener);
        }
        mServerInfoList.clear();
        isDiscoverServicesStarted = false;
    }

    private String NsdServiceInfoToString(NsdServiceInfo info){
        String name = info.getServiceName();
        String type = info.getServiceType();
        InetAddress host = info.getHost();
        int port = info.getPort();
        StringBuilder sb = new StringBuilder();
        sb.append("name: " + name + "、");
        sb.append("type: " + type + "、");
        sb.append("host: " + ((host == null) ? "null" : host.getHostAddress() + ":" + port));
        return sb.toString();
    }
    public void addServerInfo(NsdServiceInfo info) {
        Iterator<NsdServiceInfo> iter = mServerInfoList.iterator();
        NsdServiceInfo element;
        boolean isExist = false;
        while (iter.hasNext()) {
            element = (NsdServiceInfo) iter.next();
            if (element.getServiceName().equals(info.getServiceName())) {
                isExist = true;
                break;
            }
        }
        if (!isExist) {
//            info.setHost(null);
            mServerInfoList.add(info);
        }
    }
    public void reWriteServerInfo(NsdServiceInfo info) {
//        Iterator<NsdServiceInfo> iter = mServerInfoList.iterator();
//        NsdServiceInfo element = null;
//        boolean isExist = false;
//        while (iter.hasNext()) {
//            element = (NsdServiceInfo) iter.next();
//            if (element.getServiceName().equals(info.getServiceName())) {
//                isExist = true;
//                break;
//            }
//        }
//        mContext.show("In function reWriteServerInfo.");
        int index = 0;
        NsdServiceInfo element;
        boolean isExist = false;
        while(index < mServerInfoList.size()){
            if(mServerInfoList.get(index).getServiceName().equals(info.getServiceName())){
                isExist = true;
                break;
            }
            index++;
        }
        if (isExist) {
//            mContext.show("has Exist");
            mServerInfoList.set(index, info);
        }else{
//            mContext.show("Not Exist");
            mServerInfoList.add(info);            
        }
    }
    public void removeServerInfo(NsdServiceInfo info) {
        Iterator<NsdServiceInfo> iter = mServerInfoList.iterator();
        NsdServiceInfo element = null;
        boolean isExist = false;
        while (iter.hasNext()) {
            element = (NsdServiceInfo) iter.next();
            if (element.getServiceName().equals(info.getServiceName())) {
                isExist = true;
                break;
            }
        }
        if (isExist) {
            mServerInfoList.remove(element);
        }
    }

    public void showServerInfo() {
        Iterator<NsdServiceInfo> iter = mServerInfoList.iterator();
        NsdServiceInfo info;
        int cnt = 1;
        Log.d(TAG, "==========there are " + mServerInfoList.size() + " elements============");
        mContext.show("==========there are " + mServerInfoList.size() + " elements============");
        while (iter.hasNext()) {
            info = (NsdServiceInfo) iter.next();
            Log.d(TAG, cnt + ". " + NsdServiceInfoToString(info));
            mContext.show(cnt + ". " + NsdServiceInfoToString(info));
            cnt++;
        }
    }

    private int indexcnt = 0;
    public void resolveServerInfo(){
        if(indexcnt < mServerInfoList.size()){
            NsdServiceInfo info = mServerInfoList.get(indexcnt);
            if(info.getHost() == null){
                mNsdManager.resolveService(info, mResolveListener);
            }else{
                mContext.show("No Need To Resolve.");                
            }
            indexcnt++;
        }else{
            indexcnt = 0;
        }
    }
}
