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

import java.text.SimpleDateFormat;
import java.util.Date;

import study.android.activity.LoggerView;
import study.android.activity.StudyActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class NsdChatActivity extends StudyActivity {
	NsdHelper mNsdHelper;
	private LoggerView loggerView;
	public static final String TAG = "NsdChat";


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loggerView = new LoggerView(this);
		setContentView(loggerView);

		mNsdHelper = new NsdHelper(this);
		mNsdHelper.initializeNsd();
	}

	public void show(String message){
	    loggerView.info(message);
	}
    @Override
    protected void onResume() {
        super.onResume();
//        if(null == mNsdHelper){
//            mNsdHelper = new NsdHelper(this);
//            mNsdHelper.initializeNsd();            
//        }else {
//            mNsdHelper.discoverServices();
//        }
    }

    @Override
    protected void onPause() {
        if (mNsdHelper != null) {
            mNsdHelper.stopDiscovery();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mNsdHelper != null) {
            mNsdHelper.unRegisterService();
            mNsdHelper.stopDiscovery();
        }
        super.onDestroy();
    }
	
	public void onResolveService(View v){
	    this.mNsdHelper.resolveServerInfo();
	}
    
    private final int START_DISCOVER = 0, STOP_DISCOVER = 1, LIST_SERVICE_INFO = 2, RESOLVE_SERVICE = 3,
            REGISTER_SERVICE = 4, UNREGISTER_SERVICE = 5, CLEAR_SCREEN = 6;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu);
        menu.add(0, START_DISCOVER, 0, "开始监控");
        menu.add(0, STOP_DISCOVER, 0, "停止监控");
        menu.add(0, LIST_SERVICE_INFO, 0, "服务列表");
        menu.add(0, RESOLVE_SERVICE, 0, "解析服务");
        menu.add(0, REGISTER_SERVICE, 0, "发布服务");
        menu.add(0, UNREGISTER_SERVICE, 0, "注销服务");
        menu.add(0, CLEAR_SCREEN, 0, "清空屏幕");
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
        case START_DISCOVER:
            loggerView.info("开始监控");
            if(null == mNsdHelper){
                mNsdHelper = new NsdHelper(this);
                mNsdHelper.initializeNsd();            
            }else {
                mNsdHelper.discoverServices();
            }
            break;
        case STOP_DISCOVER:
            loggerView.info("停止监控");
            if (mNsdHelper != null) {
                mNsdHelper.stopDiscovery();
            }
            break;
        case LIST_SERVICE_INFO:
            loggerView.info("服务列表");
            mNsdHelper.showServerInfo(); 
            break;
        case RESOLVE_SERVICE:
            loggerView.info("解析服务");
            mNsdHelper.resolveServerInfo(); 
            break;
        case REGISTER_SERVICE:
            String[] props = new String[]{"Platform=HammerHead", "string"};
            loggerView.info("发布服务");
            mNsdHelper.registerService(8000);
            break;
        case UNREGISTER_SERVICE:
            loggerView.info("注销服务");
            mNsdHelper.unRegisterService();
            break;
        case CLEAR_SCREEN:
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");       
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间       
            String curtime = formatter.format(curDate); 
            loggerView.refreshSubVec();
            loggerView.info(curtime);
            break;
        }
        return super.onOptionsItemSelected(item);
    }

}
