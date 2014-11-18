package org.apache.cordova;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;


import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;

public class TestCordovaActivity extends Activity{
    public static String TAG = "TestCordovaActivity";
    public TestCordovaActivity(){
        
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }
    public Activity getActivity(){
        return this;
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
        ConfigXmlParser parser = new ConfigXmlParser();
        parser.parse(this.getActivity());
        ArrayList<PluginEntry> pluginEntries = parser.getPluginEntries();
    }

//    private void testXmlPullParser()
//    {
////        org.xmlpull.v1.XmlPullParserFactory factory = new org.xmlpull.v1.XmlPullParserFactory();
//        XmlResourceParser xpp;
//        xpp.setInput( new StringReader ( "<foo>Hello World!</foo>" ) );
//        int eventType = xpp.getEventType();
//        while (eventType != XmlResourceParser.END_DOCUMENT) {
//         if(eventType == XmlResourceParser.START_DOCUMENT) {
//             Log.i(TAG, "Start document");
//         } else if(eventType == XmlResourceParser.START_TAG) {
//             Log.i(TAG, "Start tag "+xpp.getName());
//         } else if(eventType == XmlResourceParser.END_TAG) {
//             Log.i(TAG, "End tag "+xpp.getName());
//         } else if(eventType == XmlResourceParser.TEXT) {
//             Log.i(TAG, "Text "+xpp.getText());
//         }
//         eventType = xpp.next();
//        }
//        System.out.println("End document");
//    }
//    The above example will generate the following output:
//
//        Start document
//        Start tag foo
//        Text Hello World!
//        End tag foo
//        End document
}
