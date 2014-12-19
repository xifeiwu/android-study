package study.android.webkit.WebView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import study.android.activity.MainActivity;
import study.android.activity.R;
import study.android.activity.StudyActivity;
import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class AppFrameworkActivity extends StudyActivity{
    /** Called when the activity is first created. */
    WebView wv;
    ProgressDialog pd;
    Handler handler;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.webview);
        init();//执行初始化函数
        handler=new Handler(){
            public void handleMessage(Message msg)
            {//定义一个Handler，用于处理下载线程与UI间通讯
              if (!Thread.currentThread().isInterrupted())
              {
                switch (msg.what)
                {
                case 0:
                    pd.show();//显示进度对话框         
                    break;
                case 1:
                    pd.hide();//隐藏进度对话框，不可使用dismiss()、cancel(),否则再次调用show()时，显示的对话框小圆圈不会动。
                    break;
                }
              }
              super.handleMessage(msg);
            }
        };
        String url = "http://192.168.160.176:8000/study.html?ios7";
        wv.loadUrl(url);
//        Toast.makeText(this, "http://192.168.160.176:8000/study.html?ios7", Toast.LENGTH_SHORT).show();
    }

@Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
//        File file = CacheManager.getCacheFileBaseDir();  
//        if (file != null && file.exists() && file.isDirectory()) {
//            for (File item : file.listFiles()) {
//                item.delete();
//            }
//            file.delete();
//        }
        this.deleteDatabase("webview.db");
        this.deleteDatabase("webviewCache.db");
        super.onDestroy();
    }

    //    private void getHtml() {
//        AssetManager assetManager = getAssets();
//        InputStream stream = null;
//        try {
//            stream = assetManager.open("index.html");
//        } catch (IOException e) {
//            // handle
//        } finally {
//            if (stream != null) {
//                try {
//                    stream.close();
//                } catch (IOException e) {
//                }
//            }
//        }
//    }
    public void init(){//初始化
        wv=(WebView)findViewById(R.id.wv);
        wv.getSettings().setJavaScriptEnabled(true);//可用JS
        wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存        
//        wv.addJavascriptInterface(new JavaScriptInterface(this), "AndroidFunction");
        wv.setScrollBarStyle(0);//滚动条风格，为0就是不给滚动条留空间，滚动条覆盖在网页上
        wv.setWebViewClient(new WebViewClient(){   
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
                loadurl(view,url);//载入网页
                return true;   
            }//重写点击动作,用webview载入
 
        });
        wv.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView view,int progress){//载入进度改变而触发 
                if(progress==100){
                    handler.sendEmptyMessage(1);//如果全部载入,隐藏进度对话框
                }   
                super.onProgressChanged(view, progress);   
            }
        });
 
        pd=new ProgressDialog(AppFrameworkActivity.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("数据载入中，请稍候！");
    }
    public void loadurl(final WebView view,final String url){
        handler.sendEmptyMessage(0);
        view.loadUrl(url);
    }
    public void reloadurl(final WebView view){
        handler.sendEmptyMessage(0);
        view.reload();
    }

    private final int RELOAD = 0;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu);
        menu.add(0, RELOAD, 0, "刷新页面");
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
        case RELOAD:
            reloadurl(wv);
            break;
        }
        return super.onOptionsItemSelected(item);
    }

}
