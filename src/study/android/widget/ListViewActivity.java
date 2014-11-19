package study.android.widget;
import java.util.ArrayList;   
import java.util.List;

import study.android.activity.R;
import android.app.Activity;  
import android.content.Context;
import android.os.Bundle;  
import android.view.LayoutInflater;
import android.view.View;  
import android.view.ViewGroup;
import android.widget.ArrayAdapter;  
import android.widget.Button;  
import android.widget.ListView;  
import android.widget.TextView;
public class ListViewActivity extends Activity {

    private ListView list;  
    private StringAdapter adapter;  
    private ArrayList<String> data;  
    private Button addBtn;  
    private int i = 0;  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.listview);  
          
        list = (ListView) findViewById(R.id.list);  
        data = new ArrayList<String>();  
        for (i = 0; i < 5; i++) {  
            data.add(new String("" + (i + 1)));  
        }  
          
        adapter = new StringAdapter(this, R.layout.listview_item, data);//android.R.layout.simple_list_item_1
        list.setAdapter(adapter);  
          
          
        addBtn = (Button) findViewById(R.id.add);  
        addBtn.setOnClickListener(new View.OnClickListener() {  
              
            @Override  
            public void onClick(View v) {  
                System.out.println(i);  
                data.add(new String("" + (++i)));  
                System.out.println(i);
                adapter.notifyDataSetChanged();  
            }  
        });  
          
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
