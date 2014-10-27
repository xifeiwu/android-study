add new activity to project DevAndroid:
1. add all code of the activity.
2. add info of the activity to AndroidMenifest.xml, such as:
    <activity android:name=".DesktopActivity"
              android:label="@string/app_name">
    </activity>
   add uses-permission of the activity, such as:
    <uses-permission android:name="android.permission.INTERNET" />
3. add new button in layout of MainActivity, and corresponding onclick method of the button:
    <Button
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/DesktopActivity"
        android:onClick="startDesktopActivity" />
4. add code of onclick method of button in MainActivity:
    public void startDesktopActivity(View view){
        Intent intent = new Intent(this, DesktopActivity.class);
        startActivity(intent);
    }
