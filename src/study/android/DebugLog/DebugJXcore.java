package study.android.DebugLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.content.res.AssetManager;
import android.util.Log;

public class DebugJXcore {

    /**other files for test
     * 1, assets/www/jxcore_cordova.js
     * 2, assets/jxcore/*
     */
    
    private DebugActivity mContext;
    public DebugJXcore(DebugActivity activity){
        this.mContext = activity;
        //info(readFile("www/main.js"));
        //info("Size: " + aproxFileSize("www/main.js"));
        info("Cache Dir: " + mContext.getCacheDir().toString());
        info("Source Dir: " + mContext.getApplicationInfo().sourceDir);
//        info(mContext.getApplicationInfo().toString());
        info("Absolute Path: " + mContext.getFilesDir().getAbsolutePath());
        Initialize(mContext.getFilesDir().getAbsolutePath());
    }
    private void info(String info){
        mContext.info(info);
    }    

    private void Initialize(String home) {
      // assets.list is terribly slow, below trick is literally 100 times faster
      StringBuilder assets = new StringBuilder();
      assets.append("{");
      boolean first_entry = true;
      try {
        ZipFile zf = new ZipFile(
            mContext.getApplicationInfo().sourceDir);
        try {
          for (Enumeration<? extends ZipEntry> e = zf.entries(); e
              .hasMoreElements();) {
            ZipEntry ze = e.nextElement();
            String name = ze.getName();
            if (name.startsWith("assets/www/jxcore/")) {
              if (first_entry)
                first_entry = false;
              else
                assets.append(",");
              int size = this.aproxFileSize(name.substring(7));
              assets.append("\"" + name.substring(18) + "\":" + size);
            }
          }
        } finally {
          zf.close();
        }
      } catch (Exception e) {
      }
      assets.append("}");
      
      info(home + "/www/jxcore");
      info(assets.toString());

      //prepareEngine(home + "/www/jxcore", assets.toString());

      String mainFile = this.readFile("www/jxcore_cordova.js");

      String data = "process.setPaths = function(){ process.cwd = function() { return '" + home
          + "/www/jxcore';};\n" 
          + "process.userPath ='" + mContext.getCacheDir().toString() + "';\n"
          + "};"
          + mainFile;

      info(data);

      //defineMainFile(data);

      //startEngine();
    }
    
    public String readFile(String location) {
        return readFile(location, "UTF-8");
    }

    public String readFile(String location, String encoding) {
        StringBuilder sb = new StringBuilder();
        try {
            AssetManager asm = mContext.getBaseContext().getAssets();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    asm.open(location), encoding));
            String str = br.readLine();
            while (str != null) {
                sb.append(str + "\n");
                str = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            Log.w("jxcore-FileManager", "readfile failed");
            info("jxcore-FileManager: " + "readfile failed");
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }

    public int aproxFileSize(String location) {
        int size = 0;
        try {
            AssetManager asm = mContext.getBaseContext().getAssets();
            InputStream st = asm.open(location, AssetManager.ACCESS_UNKNOWN);
            size = st.available();
            st.close();
        } catch (IOException e) {
            Log.w("jxcore-FileManager", "aproxFileSize failed");
            e.printStackTrace();
            return 0;
        }
        return size;
    }
}
