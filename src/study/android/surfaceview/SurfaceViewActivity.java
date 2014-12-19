package study.android.surfaceview;

import study.android.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class SurfaceViewActivity extends Activity implements OnClickListener {
    
    private Button leftBtn, rightBtn;  
    private MySurfaceView mysfView;
    private SurfaceViewActivity instance;
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        instance = this;
//        MySurfaceView msfv = new MySurfaceView(this, null);
        setContentView(R.layout.surfaceview);
        leftBtn = (Button) this.findViewById(R.id.leftBtn);  
        rightBtn = (Button) this.findViewById(R.id.rightBtn);  
        leftBtn.setOnClickListener(this);  
        rightBtn.setOnClickListener(this);

    }  
  
    @Override  
    public void onClick(View v) {  
        int id = v.getId();  
        if (id == R.id.leftBtn) {  
            MySurfaceView.notice = "点击了LeftButton";  
        } else if (id == R.id.rightBtn) {  
            MySurfaceView.notice = "点击了RightButton";  
        }  
    }  

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        mysfView = (MySurfaceView) this.findViewById(R.id.MySurfaceView);
        registerForContextMenu(mysfView);

        mysfView.setOnLongClickListener(new OnLongClickListener(){
            @Override
            public boolean onLongClick(View arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(instance, "on long click.", Toast.LENGTH_LONG).show();
                return false;
            }
            
        });
        super.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        if(this.mysfView != null){
            mysfView.surfaceDestroyed(null);
        }
        super.onPause();
    }
    

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("操作选项");
        menu.add(0, 0, 0, "运行文件");
        menu.add(0, 1, 0, "删除文件");
        menu.add(0, 2, 0, "上传保存数据");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
        case 0:
            break;
        case 1:
            break;
        case 2:
            break;
        default:
            break;
        }
        return super.onContextItemSelected(item);
    }

    private final int LIST_SERVICE_INFO = 0, REGISTER_SERVICE = 1, UNREGISTER_SERVICE = 2, OTHER_OPERATION = 3;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu);
        menu.add(0, LIST_SERVICE_INFO, 0, "服务列表");
        menu.add(0, REGISTER_SERVICE, 0, "发布服务");
        menu.add(0, UNREGISTER_SERVICE, 0, "注销服务");
        menu.add(0, OTHER_OPERATION, 0, "其它操作");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
        case LIST_SERVICE_INFO:
            Toast.makeText(this, "服务列表", Toast.LENGTH_SHORT).show();
            break;
        case REGISTER_SERVICE:
            Toast.makeText(this, "发布服务", Toast.LENGTH_SHORT).show();
            break;
        case UNREGISTER_SERVICE:
            Toast.makeText(this, "注销服务", Toast.LENGTH_SHORT).show();
            break;
        case OTHER_OPERATION:
            Toast.makeText(this, "其它操作", Toast.LENGTH_SHORT).show();
            break;
        }
        return super.onOptionsItemSelected(item);
    }

}
