package study.android.webkit.WebViewClient;

import study.android.activity.LOG;
import study.android.activity.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.LinearLayout;

public class TestWebViewClient extends Activity{
    public static String TAG = "TestWebViewClient";
    public static TestWebViewClient instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        instance = this;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        init();
    }

    private LinearLayoutSoftKeyboardDetect root;
    public WebView appView;
    
    public void init(){
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        LOG.i(TestWebViewClient.TAG, "Default Display: " + width + " * " + height);
        root = new LinearLayoutSoftKeyboardDetect(this, width, height);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, 0.0F));
        appView = new WebView(this);
//        appView.loadUrl("javascript:");
        appView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1.0F));

        // Add web view but make it invisible while loading URL
//        appView.setVisibility(View.INVISIBLE);
        
        // need to remove appView from any existing parent before invoking root.addView(appView)
        ViewParent parent = appView.getParent();
        if ((parent != null) && (parent != root)) {
            LOG.d(TAG, "removing appView from existing parent");
            ViewGroup parentGroup = (ViewGroup) parent;
            parentGroup.removeView(appView);
        }
        root.addView((View) appView);
        setContentView(root);

//        setContentView(R.layout.webview);
//        appView=(WebView)findViewById(R.id.wv);
        
        appView.setScrollBarStyle(0);//滚动条风格，为0就是不给滚动条留空间，滚动条覆盖在网页上
        WebSettings settings = appView.getSettings();
        settings.setJavaScriptEnabled(true);//可用JS
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        LOG.i(TestWebViewClient.TAG, settings.getUserAgentString());
        
        appView.setWebChromeClient(new ChromeClient());
        appView.setWebViewClient(new ViewClient());
        appView.loadUrl("file:///android_asset/www/index.html");//"http://www.baidu.com"
    }
}
class ChromeClient extends WebChromeClient{
    public void onProgressChanged(WebView view,int progress){//载入进度改变而触发
        LOG.i(TestWebViewClient.TAG, "progress: " + progress);
        super.onProgressChanged(view, progress);   
    }

    @Override
    public void onCloseWindow(WebView window) {
        // TODO Auto-generated method stub
        super.onCloseWindow(window);
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        LOG.i(TestWebViewClient.TAG, "onConsoleMessage: ");
        // TODO Auto-generated method stub
        return super.onConsoleMessage(consoleMessage);
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        LOG.i(TestWebViewClient.TAG, "onCreateWindow: ");
        // TODO Auto-generated method stub
        return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
    }

    @Override
    public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
        LOG.i(TestWebViewClient.TAG, "onJsBeforeUnload: ");
        // TODO Auto-generated method stub
        return super.onJsBeforeUnload(view, url, message, result);
    }


//    @Override
//    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//        LOG.i(TestWebViewClient.TAG, "onJsAlert: ");
//        // TODO Auto-generated method stub
//        return super.onJsAlert(view, url, message, result);
//    }
//    @Override
//    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
//        LOG.i(TestWebViewClient.TAG, "onJsConfirm: ");
//        // TODO Auto-generated method stub
//        return super.onJsConfirm(view, url, message, result);
//    }
//    @Override
//    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
//        LOG.i(TestWebViewClient.TAG, "onJsPrompt: ");
//        // TODO Auto-generated method stub
//        return super.onJsPrompt(view, url, message, defaultValue, result);
//    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        LOG.i(TestWebViewClient.TAG, "onReceivedTitle: " + title);
        // TODO Auto-generated method stub
        super.onReceivedTitle(view, title);
    }

    @Override
    public void onRequestFocus(WebView view) {
        LOG.i(TestWebViewClient.TAG, "onRequestFocus: ");
        // TODO Auto-generated method stub
        super.onRequestFocus(view);
    }
    /**
    Javascript弹出框有如下三种： 
    alert();  
    window.confirm("Are you srue?");  
    window.prompt("Please input some word";,"this is text");
    */
    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        LOG.i(TestWebViewClient.TAG, "onJsAlert: ");
        // 对alert的简单封装
        new AlertDialog.Builder(TestWebViewClient.instance).setTitle("Alert").setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO
                    }
                }).create().show();
        result.confirm();
        return true;
    }

    /**
     * 处理confirm弹出框
     */
    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        LOG.i(TestWebViewClient.TAG, "onJsConfirm: ");
        result.confirm();
        return super.onJsConfirm(view, url, message, result);
    }

    /** 
     * 处理prompt弹出框 
     */  
    @Override  
    public boolean onJsPrompt(WebView view, String url, String message,  
            String defaultValue, JsPromptResult result) {
        LOG.i(TestWebViewClient.TAG, "onJsPrompt: ");
        result.confirm();  
        return super.onJsPrompt(view, url, message, message, result);  
    }
}
class ViewClient extends WebViewClient{

    @Override
    public void onLoadResource(WebView view, String url) {
        // TODO Auto-generated method stub
        super.onLoadResource(view, url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        // TODO Auto-generated method stub
        super.onPageFinished(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        // TODO Auto-generated method stub
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
        // TODO Auto-generated method stub
        super.onReceivedLoginRequest(view, realm, account, args);
    }

    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        // TODO Auto-generated method stub
        super.onScaleChanged(view, oldScale, newScale);
    }
    
}