package study.android.activity;

import java.util.logging.Logger;

import org.apache.cordova.TestCordovaActivity;

import study.android.content.Intent.StartActivityListView;
import study.android.content.Intent.StartOtherAppsByIntent;
import study.android.content.pm.PMActivity;
import study.android.content.pm.PMActivity_ListView;
import study.android.surfaceview.SurfaceViewActivity;
import study.android.webkit.WebView.WebViewActivity;
import study.android.webkit.WebViewClient.TestWebViewClient;
import study.android.widget.ListViewActivity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity{
    public static String TAG = "StudyAndroid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);
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
    
    public void startWebviewActivity(View view){
        Intent intent = new Intent(this, WebViewActivity.class);
        startActivity(intent);
    }
    
    public void startPackageManagerActivity(View view){
        Intent intent = new Intent(this, PMActivity_ListView.class);
        startActivity(intent);
    }
    
    public void startHandlerActivity(View view){
        Intent intent = new Intent(this, HandlerActivity.class);
        startActivity(intent);
    }
    
    public void startSurfaceViewActivity(View view){
        Intent intent = new Intent(this, SurfaceViewActivity.class);
        startActivity(intent);
    }
    
    public void startListViewActivity(View view){
        Intent intent = new Intent(this, ListViewActivity.class);
        startActivity(intent);
    }
    
    public void startTestCordovaActivity(View view){
        Intent intent = new Intent(this, TestCordovaActivity.class);
        startActivity(intent);
    }
    
    public void startTestWebViewClient(View view){
        Intent intent = new Intent(this, TestWebViewClient.class);
        startActivity(intent);
    }
    
    public void startStartOtherAppsByIntent(View view){
        Intent intent = new Intent(this, StartOtherAppsByIntent.class);
        startActivity(intent);
    }
}
