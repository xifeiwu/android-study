package study.android.content.Intent;

import study.android.LOG;
import study.android.MainActivity;
import study.android.StudyActivity;
import study.android.content.Intent.StartActivityListView.ToDo;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.view.KeyEvent;

//@Deprecated
public class StartOtherAppsByIntent extends StudyActivity{

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
        LOG.i(LOG.TAG, "requestCode: " + requestCode + " and resultCode: " + resultCode);
//        if (requestCode == StartActivityListView.ToDo.ACTION_PICK_CONTACT.ordinal()) {
//            // Make sure the request was successful
//            if (resultCode == RESULT_OK) {
//                // The user picked a contact.
//                // The Intent's data Uri identifies which contact was selected.
//
//                // Do something with the contact here (bigger example below)
//            }
//        }
        ToDo todo = ToDo.values()[requestCode];
        switch(todo){
        case ACTION_DIAL:
            break;
        case ACTOPN_VIEW_WEBPAGE:
            break;
        case ACTOPN_VIEW_LOCATION:
            break;
        case ACTOPN_VIEW:
            break;
        case ACTION_SEND:
            if(resultCode == RESULT_OK){
                String backvalue = data.getStringExtra("backvalue");
                LOG.i(LOG.TAG, "backvalue: " + backvalue);
            }
            break;
        case ACTION_INSERT_CALENDER:
            break;
        case ACTION_PICK_CONTACT:
              if (resultCode == RESULT_OK) {
                  // Get the URI that points to the selected contact
                  Uri contactUri = data.getData();
                  // We only need the NUMBER column, because there will be only one row in the result
                  String[] projection = {Phone.NUMBER};

                  // Perform the query on the contact to get the NUMBER column
                  // We don't need a selection or sort order (there's only one result for the given URI)
                  // CAUTION: The query() method should be called from a separate thread to avoid blocking
                  // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                  // Consider using CursorLoader to perform the query.
                  Cursor cursor = getContentResolver()
                          .query(contactUri, projection, null, null, null);
                  cursor.moveToFirst();

                  // Retrieve the phone number from the NUMBER column
                  int column = cursor.getColumnIndex(Phone.NUMBER);
                  String number = cursor.getString(column);
                  LOG.i(LOG.TAG, "column: " + column + " and number: " + number);

                  // Do something with the phone number..
              }
            break;
        case INTENT_CHOOSER:
            break;
        case INTENT_CHOOSER_ACTION_SEND:
            break;
        case INTENT_CHOOSER_ACTION_VIEW:
            break;
        case INTENT_CHOOSER_ACTION_INSERT:
            break;
        case INTENT_CHOOSER_ACTION_PICK:
            break;
        }
    }

}
