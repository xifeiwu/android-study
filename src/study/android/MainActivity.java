package study.android;

import java.util.logging.Logger;

import org.apache.cordova.TestCordovaActivity;

import study.android.DebugLog.DebugActivity;
import study.android.content.Intent.StartActivityListView;
import study.android.content.Intent.StartOtherAppsByIntent;
import study.android.content.Intent.StartActivityListView.ToDo;
import study.android.content.pm.PMActivity;
import study.android.content.pm.PMActivity_ListView;
import study.android.net.nsd.NsdChatActivity;
import study.android.surfaceview.SurfaceViewActivity;
import study.android.webkit.WebView.AppFrameworkActivity;
import study.android.webkit.WebView.WebViewActivity;
import study.android.webkit.WebViewClient.TestWebViewClient;
import study.android.widget.ListViewActivity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends StudyActivity{
    public static String TAG = "StudyAndroid";
    private ListView activityList;
    private MainActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);
        activityList = (ListView) this.findViewById(R.id.listview);
        activityList.setAdapter(new arrayAdapter(this, R.layout.main_item, activityArray));
        activityList.setOnItemClickListener(itmeClickListener);
        mContext = this;
        LOG.setTAG(TAG);
        LOG.setLogLevel(LOG.INFO);
        LOG.i(TAG, "onCreate");
        activityStates("onCreate");
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        activityStates("onStart");
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        activityStates("onRestart");
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        activityStates("onResume");
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        activityStates("onPause");
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        activityStates("onStop");
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        activityStates("onDestroy");
    }

    private Logger logger = Logger.getLogger(MainActivity.class.getName());
    private void activityStates(String state){
        Toast.makeText(this, MainActivity.class.getName() + " - " + state, Toast.LENGTH_SHORT).show();
        logger.info(state);
    }

    public static enum ToDo {
        HANDLERACITIV,
        STARTOTHERAPPSBYINTENT,
        PACKAGEMANAGERACTIVITY,
        NASCHATACTIVITY,
        SURFACEVIEWACTIVITY,
        WEBVIEWACTIVITY,
        APPFRAMEWORKACTIVITY,
        TESTWEBVIEWCLIENT,
        LISTVIEWACTIVITY,
        TESTCORDOVAACTIVITY,
        DEBUGACTIVITY,
    };

    private ActivityInfo[] activityArray = new ActivityInfo[]{
            new ActivityInfo(
                    ToDo.HANDLERACITIV.ordinal(),
                    "HandlerActivity", 
                    "Handler控件使用Demo。"),
            new ActivityInfo(
                    ToDo.STARTOTHERAPPSBYINTENT.ordinal(),
                    "StartOtherAppsByIntent", 
                    "Intent控件使用Demo。"),
            new ActivityInfo(
                    ToDo.PACKAGEMANAGERACTIVITY.ordinal(), 
                    "PackageManagerActivity", 
                    "PackageManager控件使用Demo。"),
            new ActivityInfo(
                    ToDo.NASCHATACTIVITY.ordinal(),
                    "NsdChatActivity", 
                    "基于网络发现功能的文字通讯。"),
            new ActivityInfo(
                    ToDo.SURFACEVIEWACTIVITY.ordinal(),
                    "SurfaceViewActivity", 
                    "SurfaceView控件使用Demo。"),
            new ActivityInfo(
                    ToDo.WEBVIEWACTIVITY.ordinal(),
                    "WebviewActivity", 
                    "Webview控件使用Demo。"),
            new ActivityInfo(
                    ToDo.APPFRAMEWORKACTIVITY.ordinal(),
                    "AppFrameworkActivity", 
                    "远程加载html5界面。"),
            new ActivityInfo(
                    ToDo.TESTWEBVIEWCLIENT.ordinal(),
                    "TestWebViewClient", 
                    "WebViewClient控件使用Demo。"),
            new ActivityInfo(
                    ToDo.LISTVIEWACTIVITY.ordinal(),
                    "ListViewActivity", 
                    "ListView控件使用Demo。"),
            new ActivityInfo(
                    ToDo.TESTCORDOVAACTIVITY.ordinal(),
                    "TestCordovaActivity", 
                    "学习Cordova中的方法"),
            new ActivityInfo(
                    ToDo.DEBUGACTIVITY.ordinal(),
                    "DebugActivity", 
                    "用来输出Debug信息"),
    };
    private OnItemClickListener itmeClickListener = new OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            // TODO Auto-generated method stub
            ToDo todo = ToDo.values()[arg2]; //do your own bounds checking
            Intent intent = null;
            switch(todo){
            case WEBVIEWACTIVITY:
                intent = new Intent(mContext, WebViewActivity.class);
                break;
            case APPFRAMEWORKACTIVITY:
                intent = new Intent(mContext, AppFrameworkActivity.class);
                break;
            case PACKAGEMANAGERACTIVITY:
                intent = new Intent(mContext, PMActivity_ListView.class);
                break;
            case HANDLERACITIV:
                intent = new Intent(mContext, HandlerActivity.class);
                break;
            case SURFACEVIEWACTIVITY:
                intent = new Intent(mContext, SurfaceViewActivity.class);
                break;
            case LISTVIEWACTIVITY:
                intent = new Intent(mContext, ListViewActivity.class);
                break;
            case TESTCORDOVAACTIVITY:
                intent = new Intent(mContext, TestCordovaActivity.class);
                break;
            case TESTWEBVIEWCLIENT:
                intent = new Intent(mContext, TestWebViewClient.class);
                break;
            case STARTOTHERAPPSBYINTENT:
                intent = new Intent(mContext, StartOtherAppsByIntent.class);
                break;
            case NASCHATACTIVITY:
                intent = new Intent(mContext, NsdChatActivity.class);
                break;
            case DEBUGACTIVITY:
                intent = new Intent(mContext, DebugActivity.class);
                break;
            }
            if(null != intent){
                startActivity(intent);
            }
        }   
    };
}

class arrayAdapter extends ArrayAdapter<ActivityInfo>{
    private Context mContext;
    private int resourceId;
    private ActivityInfo[] adapter;

    public arrayAdapter(Context context, int resource, ActivityInfo[] objects) {
        super(context, resource, objects);
        // TODO Auto-generated constructor stub
        mContext = context;
        resourceId = resource;
        adapter = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if(convertView==null){
            // inflate the layout
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resourceId, null);
        }
        TextView textViewName = (TextView) convertView.findViewById(R.id.name);
        textViewName.setText(adapter[position].getName());
        TextView textViewDetail = (TextView) convertView.findViewById(R.id.detail);
        textViewDetail.setText(adapter[position].getDetail());
        return convertView;
    }    
}

class ActivityInfo{
    private int id;
    private String name;
    private String detail;
    public ActivityInfo(int id, String name, String detail){
        this.id = id;
        this.name = name;
        this.detail = detail;
    }
    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getDetail(){
        return detail;
    }
}