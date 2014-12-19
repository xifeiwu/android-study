package study.android.DebugLog;

import android.os.Bundle;
import study.android.activity.LoggerView;
import study.android.activity.StudyActivity;

public class DebugActivity extends StudyActivity{
    private LoggerView loggerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        loggerView = new LoggerView(this);
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
        loggerView.info("onResule");
    }

}
