package study.android.content.pm;

import java.util.ArrayList;  
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;  

import study.android.activity.R;
import android.app.Activity;  
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;  
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;  
import android.content.pm.PackageInfo;  
import android.content.pm.PackageManager;  
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;  
import android.os.Bundle;  
import android.view.KeyEvent;
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.ViewGroup;  
import android.widget.AdapterView;
import android.widget.ImageView;  
import android.widget.ListView;  
import android.widget.SimpleAdapter;  
import android.widget.TextView;  
import android.widget.AdapterView.OnItemClickListener;
  
public class PMActivity_ListView extends Activity {  
    ListView lv;  
    MyAdapter adapter;  
    ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();  
      
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.pm_listview_main);  
          
        lv = (ListView)findViewById(R.id.lv);  
        //得到PackageManager对象  
        PackageManager pm = getPackageManager();  
        //得到系统安装的所有程序包的PackageInfo对象
        //List<ApplicationInfo> packs = pm.getInstalledApplications(0);

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = getPackageManager().queryIntentActivities(mainIntent, 0);
        for(ResolveInfo resolve:apps){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("pkgName", resolve.activityInfo.packageName);//应用程序名称
            map.put("activityName", resolve.activityInfo.name);//应用程序名称
            map.put("icon", resolve.loadIcon(pm));//图标
            map.put("labelName", resolve.loadLabel(pm).toString());//应用程序包名
            //循环读取并存到HashMap中，再增加到ArrayList上，一个HashMap就是一项
            items.add(map);
        }
        /*
        List<PackageInfo> packs = pm.getInstalledPackages(0);
        for(PackageInfo pi:packs)  
        {  
            HashMap<String, Object> map = new HashMap<String, Object>();
            //显示用户安装的应用程序，而不显示系统程序
//          if((pi.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0&&
//                  (pi.applicationInfo.flags&ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)==0)
//          {
//              //这将会显示所有安装的应用程序，包括系统应用程序
//              map.put("icon", pi.applicationInfo.loadIcon(pm));//图标
//              map.put("pkgName", pi.applicationInfo.loadLabel(pm));//应用程序名称
//              map.put("labelName", pi.applicationInfo.packageName);//应用程序包名
//              //循环读取并存到HashMap中，再增加到ArrayList上，一个HashMap就是一项
//              items.add(map);  
//          }  
            //这将会显示所有安装的应用程序，包括系统应用程序  
            map.put("icon", pi.applicationInfo.loadIcon(pm));//图标  
            map.put("pkgName", pi.applicationInfo.loadLabel(pm));//应用程序名称  
            map.put("labelName", pi.applicationInfo.packageName);//应用程序包名  
            //循环读取并存到HashMap中，再增加到ArrayList上，一个HashMap就是一项
            items.add(map);  
        }
        */
        /** 
         * 参数：Context 
         * ArrayList(item的集合) 
         * item的layout 
         * 包含ArrayList中的HashMap的key的数组 
         * key所对应的值的相应的控件id 
         */  
        adapter = new MyAdapter(this, items, R.layout.pm_listview_piitem,   
                new String[]{"icon", "pkgName", "labelName"},  
                new int[]{R.id.icon, R.id.pkgName, R.id.labelName});  
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(clickListener);
    }
    private OnItemClickListener clickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            HashMap<String, Object> map = items.get(i);
            //该应用的包名
            String pkgName = (String) map.get("pkgName");
            //应用的主activity类
            String activityName = (String) map.get("activityName");
//            ResolveInfo info = items.get(i);
//            String pkg = info.activityInfo.labelName;
            ComponentName componet = new ComponentName(pkgName, activityName);
            Intent intent = new Intent();
            intent.setComponent(componet);
            startActivity(intent);
        }  
    };
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回键
        if(keyCode == KeyEvent.KEYCODE_BACK){
            ConfirmExit();//按了返回键，但已经不能返回，则执行退出确认
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
                PMActivity_ListView.this.finish();//关闭activity
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
  
class MyAdapter extends SimpleAdapter  
{  
    private int[] appTo;  
    private String[] appFrom;  
    private ViewBinder appViewBinder;  
    private List<? extends Map<String, ?>>  appData;  
    private int appResource;  
    private LayoutInflater appInflater;  
      
    public MyAdapter(Context context, List<? extends Map<String, ?>> data,  
            int resource, String[] from, int[] to) {  
        super(context, data, resource, from, to);  
        appData = data;  
        appResource = resource;  
        appFrom = from;  
        appTo = to;  
        appInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
    }  
      
    public View getView(int position, View convertView, ViewGroup parent)  
    {  
        return createViewFromResource(position, convertView, parent, appResource);  
          
    }  
      
    private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource)  
    {  
        View v;  
        if(convertView == null)  
        {  
            v = appInflater.inflate(resource, parent,false);  
            final int[] to = appTo;  
            final int count = to.length;  
            final View[] holder = new View[count];  
              
            for(int i = 0; i < count; i++)  
            {  
                holder[i] = v.findViewById(to[i]);  
            }  
            v.setTag(holder);  
        }else  
        {  
            v = convertView;  
        }  
        bindView(position, v);  
        return v;     
    }  
      
    private void bindView(int position, View view)  
    {  
        final Map dataSet = appData.get(position);  
        if(dataSet == null)  
        {  
            return;  
        }  
          
        final ViewBinder binder = appViewBinder;  
        final View[] holder = (View[])view.getTag();  
        final String[] from = appFrom;  
        final int[] to = appTo;  
        final int count = to.length;  
          
        for(int i = 0; i < count; i++)  
        {  
            final View v = holder[i];  
            if(v != null)  
            {  
                final Object data = dataSet.get(from[i]);  
                String text = data == null ? "":data.toString();  
                if(text == null)  
                {  
                    text = "";  
                }  
                  
                boolean bound = false;  
                if(binder != null)  
                {  
                    bound = binder.setViewValue(v, data, text);  
                }  
                  
                if(!bound)  
                {  
                    /** 
                     * 自定义适配器，关在在这里，根据传递过来的控件以及值的数据类型， 
                     * 执行相应的方法，可以根据自己需要自行添加if语句。另外，CheckBox等 
                     * 集成自TextView的控件也会被识别成TextView，这就需要判断值的类型 
                     */  
                    if(v instanceof TextView)  
                    {  
                        //如果是TextView控件，则调用SimpleAdapter自带的方法，设置文本  
                        setViewText((TextView)v, text);  
                    }else if(v instanceof ImageView)  
                    {  
                        //如果是ImageView控件，调用自己写的方法，设置图片  
                        setViewImage((ImageView)v, (Drawable)data);  
                    }else  
                    {  
                        throw new IllegalStateException(v.getClass().getName() + " is not a " +  
                                "view that can be bounds by this SimpleAdapter");  
                    }  
                }  
            }  
        }  
    }
    
    public void setViewImage(ImageView v, Drawable value)  
    {  
        v.setImageDrawable(value);  
    }
}  
