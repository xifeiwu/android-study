package study.android;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class HandlerActivity extends Activity{


    private Button btn;
    private ImageView imageView;
    private String imgPath = "http://f.hiphotos.baidu.com/image/w%3D2048/sign=05793c21bba1cd1105b675208d2ac9fc/43a7d933c895d14350ee3c3272f082025aaf0703.jpg";
    private static final int DOWNLOAD_IMG = 1;
    private ProgressDialog dialog = null;

    private Handler handler = new Handler() {

        // 处理子线程给我们发送的消息。
        @Override
        public void handleMessage(android.os.Message msg) {
            byte[] data = (byte[])msg.obj;
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            imageView.setImageBitmap(bitmap);
            if(msg.what == DOWNLOAD_IMG){
                dialog.dismiss();
            }    
        };
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handler);
        initComponent();
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new Thread(new MyThread()).start();
                dialog.show();
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    private void initComponent() {
        btn = (Button) this.findViewById(R.id.button1);
        imageView = (ImageView) this.findViewById(R.id.imageView1);
        dialog = new ProgressDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage("正在下载，请稍后...");
        dialog.setCancelable(false);
    }
    
    // 使用Handler Message MessageQueue Looper等方式去访问网络资源的时候，我们必须要开启一个子线程
    public class MyThread implements Runnable{

        // 在run方法中完成网络耗时的操作
        @Override
        public void run() {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(imgPath);
            HttpResponse httpResponse = null;
            try {
                httpResponse = httpClient.execute(httpGet);
                if(200 == httpResponse.getStatusLine().getStatusCode()){
                    byte[] data = EntityUtils.toByteArray(httpResponse.getEntity());
                    // 这里的数据data我们必须发送给UI的主线程，所以我们通过Message的方式来做桥梁。
                    Message message = Message.obtain();
                    message.obj = data;
                    message.what = DOWNLOAD_IMG;
                    handler.sendMessage(message);
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        
    }

}
