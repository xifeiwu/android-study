package study.android.content.Intent;

import study.android.activity.LOG;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class ShareActivity extends Activity{
    private String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        // Get the intent that started this activity
        Intent intent = getIntent();
        Uri data = intent.getData();

        // Figure out what to do based on the intent type
        if (intent.getType().indexOf("image/") != -1) {
            // Handle intents with image data ...
        } else if (intent.getType().equals("text/plain")) {
            // Handle intents with text ...
//            LOG.i(LOG.TAG, "recevie plain text.");
            HandlePlainText(intent.getExtras());
        }
    }

    private void HandlePlainText(Bundle bundle){
        Intent result = new Intent();//"com.example.RESULT_ACTION", Uri.parse("content://result_uri"
        result.putExtra("backvalue", "Your content is:\n" + bundle.getString(Intent.EXTRA_SUBJECT) + "\n" + bundle.getString(Intent.EXTRA_TEXT));
        setResult(Activity.RESULT_OK, result);
        finish();
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

}
