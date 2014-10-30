package study.android.surfaceview;

import study.android.activity.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class SurfaceViewActivity extends Activity implements OnClickListener {
    
    private Button leftBtn, rightBtn;  
    private MySurfaceView mysfView;
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
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
        mysfView = (MySurfaceView) this.findViewById(R.layout.surfaceview);
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
}
