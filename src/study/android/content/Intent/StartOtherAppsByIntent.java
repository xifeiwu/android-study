package study.android.content.Intent;

import study.android.activity.LOG;
import study.android.activity.MainActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

//@Deprecated
public class StartOtherAppsByIntent extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(new StartActivityListView(this));
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(LOG.TAG, "requestCode: " + requestCode + " and resultCode: " + resultCode);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回键
        if(keyCode == KeyEvent.KEYCODE_BACK){
            ConfirmExit();//按了返回键，但已经不能返回，则执行退出确认
            LOG.i(MainActivity.TAG, "KEYCODE_BACK");
            return true; 
        }   
        return super.onKeyDown(keyCode, event);   
    }
    public void ConfirmExit(){//退出确认
        AlertDialog.Builder ad=new AlertDialog.Builder(StartOtherAppsByIntent.this);
        ad.setTitle("退出");
        ad.setMessage("是否退出软件?");
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() {//退出按钮
            @Override
            public void onClick(DialogInterface dialog, int i) {
                // TODO Auto-generated method stub
                StartOtherAppsByIntent.this.finish();//关闭activity
 
            }
        });
        ad.setNegativeButton("否",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //不退出不用执行任何操作
            }
        });
        ad.show();//显示对话框
    }
}
