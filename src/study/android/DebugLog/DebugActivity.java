package study.android.DebugLog;

import android.os.Bundle;
import android.view.MotionEvent;
import study.android.LOG;
import study.android.LoggerView;
import study.android.StudyActivity;

public class DebugActivity extends StudyActivity{
    
    //private String TAG = this.TAG;
    
    private LoggerView loggerView;
    private DebugFunc debugFunc;
    private DebugJXcore debugJXcore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        loggerView = new LoggerView(this);
        //debugFunc = new DebugFunc(this);
        debugJXcore = new DebugJXcore(this);
        this.setContentView(loggerView);
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
        info("onResume");
    }

    public void info(String info){
        loggerView.info(info);        
    }
}
