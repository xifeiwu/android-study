package study.android.activity;

import study.android.content.Intent.StartOtherAppsByIntent;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.KeyEvent;

public class StudyActivity extends Activity{

    public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回键
        if(keyCode == KeyEvent.KEYCODE_BACK){
            ConfirmExit();//按了返回键，但已经不能返回，则执行退出确认
            LOG.i(MainActivity.TAG, "KEYCODE_BACK");
            return true; 
        }   
        return super.onKeyDown(keyCode, event);   
    }
    public void ConfirmExit(){//退出确认
        AlertDialog.Builder ad=new AlertDialog.Builder(this);
        ad.setTitle("退出");
        ad.setMessage("是否退出软件?");
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() {//退出按钮
            @Override
            public void onClick(DialogInterface dialog, int i) {
                // TODO Auto-generated method stub
                StudyActivity.this.finish();//关闭activity
 
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
    

    public void mAlertDialog(String title, String message, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negetiveListener){//退出确认
        AlertDialog.Builder ad = new AlertDialog.Builder(this);//context
        ad.setTitle(title);
        ad.setMessage(message);
        ad.setPositiveButton("是", positiveListener);
        ad.setNegativeButton("否",negetiveListener);
        ad.show();
    }
    public void mAlertDialog_yes(String title, String message, DialogInterface.OnClickListener positiveListener){//退出确认
        AlertDialog.Builder ad = new AlertDialog.Builder(this);//context
        ad.setTitle(title);
        ad.setMessage(message);
        ad.setPositiveButton("是", positiveListener);
        ad.show();
    }
}
