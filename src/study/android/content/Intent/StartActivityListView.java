package study.android.content.Intent;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.http.protocol.HTTP;

import study.android.LOG;
import study.android.R;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class StartActivityListView extends ListView {
    public static enum ToDo {
        ACTION_DIAL,
        ACTOPN_VIEW_WEBPAGE,
        ACTOPN_VIEW_LOCATION,
        ACTOPN_VIEW,
        ACTION_SEND,
        ACTION_INSERT_CALENDER,
        ACTION_PICK_CONTACT,
        INTENT_CHOOSER,
        INTENT_CHOOSER_ACTION_SEND,
        INTENT_CHOOSER_ACTION_VIEW,
        INTENT_CHOOSER_ACTION_INSERT,
        INTENT_CHOOSER_ACTION_PICK,
    };
    private String[] strArray = new String[]{
            "ACTION_DIAL", 
            "ACTOPN_VIEW_WEBPAGE", 
            "ACTOPN_VIEW_LOCATION", 
            "ACTOPN_VIEW", 
            "ACTION_SEND",
            "ACTION_INSERT_CALENDER",
            "ACTION_PICK_CONTACT",            
            "INTENT_CHOOSER",
            "INTENT_CHOOSER_ACTION_SEND",
            "INTENT_CHOOSER_ACTOPN_VIEW",
            "INTENT_CHOOSER_ACTION_INSERT",
            "INTENT_CHOOSER_ACTION_PICK",
            };
    private StringAdapter adapter;
    private StartOtherAppsByIntent mContext;

    public StartActivityListView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub 
        mContext = (StartOtherAppsByIntent) context;
        List<String> data = Arrays.asList(strArray);
        adapter = new StringAdapter(mContext, R.layout.listview_item, data);
        this.setAdapter(adapter);
        this.setOnItemClickListener(itmeClickListener);
    }
    
    private OnItemClickListener itmeClickListener = new OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            // TODO Auto-generated method stub
            ToDo todo = ToDo.values()[arg2]; //do your own bounds checking
            Intent intent;
            switch(todo){
            case ACTION_DIAL:
                LOG.i(LOG.TAG, "ACTION_DIAL: " + arg2);
                actionDail();
                break;
            case ACTOPN_VIEW_WEBPAGE:
                LOG.i(LOG.TAG, "ACTOPN_VIEW_WEBPAGE: " + arg2);
                actionView_WebPage();
                break;
            case ACTOPN_VIEW_LOCATION:
                LOG.i(LOG.TAG, "ACTOPN_VIEW_WEBPAGE: " + arg2);
                actionView_Location();
                break;
            case ACTOPN_VIEW:
                LOG.i(LOG.TAG, "ACTOPN_VIEW: " + arg2);
                break;
            case ACTION_SEND:
                LOG.i(LOG.TAG, "ACTION_SEND: " + arg2);
                actionSend_Email();
                break;
            case ACTION_INSERT_CALENDER:
                LOG.i(LOG.TAG, "ACTION_INSERT_CALENDER: " + arg2);
                actionInsert_Calendar();
                break;
            case ACTION_PICK_CONTACT:
                LOG.i(LOG.TAG, "ACTION_PICK_CONTACT: " + arg2);
                actionPick_Contact();
                break;
            case INTENT_CHOOSER:
                LOG.i(LOG.TAG, "INTENT_CHOOSER: " + arg2);
                break;
            case INTENT_CHOOSER_ACTION_SEND:
                LOG.i(LOG.TAG, "INTENT_CHOOSER_ACTION_SEND: " + arg2);
                intent = new Intent(Intent.ACTION_SEND);
                showIntentChooser(intent);
                break;
            case INTENT_CHOOSER_ACTION_VIEW:
                LOG.i(LOG.TAG, "INTENT_CHOOSER_ACTOPN_VIEW: " + arg2);
                 intent = new Intent(Intent.ACTION_VIEW);
                showIntentChooser(intent);
                break;
            case INTENT_CHOOSER_ACTION_INSERT:
                LOG.i(LOG.TAG, "INTENT_CHOOSER_ACTION_INSERT: " + arg2);
                intent = new Intent(Intent.ACTION_INSERT);
                showIntentChooser(intent);
                break;
            case INTENT_CHOOSER_ACTION_PICK:
                LOG.i(LOG.TAG, "INTENT_CHOOSER_ACTION_PICK: " + arg2);
                intent = new Intent(Intent.ACTION_PICK);
                showIntentChooser(intent);
                break;
            }
        }    
    };

    private void actionDail(){
        Uri number = Uri.parse("tel:5551234");
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
//        mContext.startActivity(callIntent);
        mContext.startActivityForResult(callIntent, ToDo.ACTION_DIAL.ordinal());
    }
    private void actionView_WebPage(){
        Uri webpage = Uri.parse("http://www.baidu.com");
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
        mContext.startActivityForResult(webIntent, ToDo.ACTOPN_VIEW_WEBPAGE.ordinal());
    }
    private void actionView_Location(){
        // Map point based on address
        Uri location = Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California");
        // Or map point based on latitude/longitude
        // Uri location = Uri.parse("geo:37.422219,-122.08364?z=14"); // z param is zoom level
        final Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        DialogInterface.OnClickListener yes = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                mContext.startActivityForResult(mapIntent, ToDo.ACTOPN_VIEW_LOCATION.ordinal());
            }

        };
        DialogInterface.OnClickListener no = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                LOG.i(LOG.TAG, "queryActivities: " + "NONO");
            }
        };
        queryActivities(mapIntent, yes, no);
    }
    private void actionSend_Email(){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        // The intent does not have a URI, so declare the "text/plain" MIME type
        emailIntent.setType(HTTP.PLAIN_TEXT_TYPE);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"jon@example.com"}); // recipients
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Email subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message text");
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"));
        // You can also attach multiple items by passing an ArrayList of Uris
        mContext.startActivityForResult(emailIntent, ToDo.ACTION_SEND.ordinal());
    }
    private void actionInsert_Calendar(){
        final Intent calendarIntent = new Intent(Intent.ACTION_INSERT);
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2012, 0, 19, 7, 30);
        Calendar endTime = Calendar.getInstance();
        endTime.set(2012, 0, 19, 10, 30);
//        Calendar.getInstance().set(year, month, day, hourOfDay, minute)
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
        calendarIntent.putExtra(Events.TITLE, "Ninja class");
        calendarIntent.putExtra(Events.EVENT_LOCATION, "Secret dojo");        

        DialogInterface.OnClickListener yes = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                mContext.startActivityForResult(calendarIntent, ToDo.ACTION_INSERT_CALENDER.ordinal());
                LOG.i(LOG.TAG, "queryActivities: " + "YESYES");
            }

        };
        DialogInterface.OnClickListener no = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                LOG.i(LOG.TAG, "queryActivities: " + "NONO");
            }
        };
        queryActivities(calendarIntent, yes, no);
    }
    private void actionPick_Contact(){
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        mContext.startActivityForResult(pickContactIntent, ToDo.ACTION_PICK_CONTACT.ordinal());
    }

    static boolean isIntentSafe;
    private boolean queryActivities(Intent intent, DialogInterface.OnClickListener yes, DialogInterface.OnClickListener no){
        isIntentSafe = false;
        PackageManager packageManager = mContext.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        isIntentSafe = activities.size() > 0;
        if(!isIntentSafe){
            mContext.mAlertDialog("错误", "未找到匹配Intent的Activity，是否继续？", yes, no);
        }
        return isIntentSafe;
    }
    
    private void showIntentChooser(Intent intent){
        // Always use string resources for UI text. This says something like "Share this photo with"
        String title = intent.getAction().toString();
        // Create and start the chooser
        Intent chooser = Intent.createChooser(intent, title);
        mContext.startActivity(chooser);
    }
}
class StringAdapter extends ArrayAdapter<String>{
    private Context mContext;
    private int layoutResourceId;
    private List<String> strList;

    public StringAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.layoutResourceId = resource;
        this.strList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if(convertView==null){
            // inflate the layout
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutResourceId, null);
        }
        TextView textViewItem = (TextView) convertView.findViewById(R.id.userinfo);
        textViewItem.setText(strList.get(position));
        return convertView;
    }
}
