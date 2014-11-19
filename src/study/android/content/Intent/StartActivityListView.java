package study.android.content.Intent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import study.android.activity.LOG;
import study.android.activity.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
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
        ACTOPN_VIEW
    };
    private String[] strArray = new String[]{"ACTION_DIAL", "ACTOPN_VIEW"};
    private StringAdapter adapter;
    private Activity mContext;

    public StartActivityListView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub 
        mContext = (Activity) context;
        List<String> data = Arrays.asList(strArray);
        adapter = new StringAdapter(mContext, R.layout.listview_item, data);
        this.setAdapter(adapter);
        this.setOnItemClickListener(itmeClickListener);
//        ToDo todo = ToDo.values()[1]; //do your own bounds checking
//        switch(todo){
//        case ACTION_DIAL:
//            Log.i(LOG.TAG, "ACTION_DIAL: " + 0);
//            break;
//        case ACTOPN_VIEW:
//            Log.i(LOG.TAG, "ACTOPN_VIEW: " + 1);
//            break;
//        }
    }
    
    private OnItemClickListener itmeClickListener = new OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            // TODO Auto-generated method stub
            ToDo todo = ToDo.values()[arg2]; //do your own bounds checking
            switch(todo){
            case ACTION_DIAL:
                Log.v(LOG.TAG, "ACTION_DIAL: " + arg2);
                actionDail();
                break;
            case ACTOPN_VIEW:
                Log.v(LOG.TAG, "ACTOPN_VIEW: " + arg2);
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
