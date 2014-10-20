package study.android.surfaceview;
import android.content.Context;  
import android.graphics.Canvas;  
import android.graphics.Color;  
import android.graphics.Paint;  
import android.util.AttributeSet;  
import android.view.SurfaceHolder;  
import android.view.SurfaceView;  
  
public class MySurfaceView extends SurfaceView implements  
        SurfaceHolder.Callback, Runnable {  
    public static String notice = "自定义SurfaceView";  
  
    private SurfaceHolder sfh;  
    private Canvas canvas;  
    private Paint paint;  
    private int x = 30, y = 80, move_x = 2;  
  
    public MySurfaceView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        this.setKeepScreenOn(true);  
        this.setFocusable(true);  
        sfh = this.getHolder();  
        sfh.addCallback(this);  
        paint = new Paint();  
        paint.setAntiAlias(true);  
        paint.setTextSize(20);  
    }  
  
    @Override  
    public void run() {  
        while (true) {  
            draw();  
            logic();  
            try {  
                Thread.sleep(100);  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
    }  
  
    private void draw() {  
        canvas = sfh.lockCanvas();  
        canvas.drawColor(Color.WHITE);  
        canvas.drawText(notice, x, y, paint);  
        sfh.unlockCanvasAndPost(canvas);  
    }  
  
    private void logic() {  
        x += move_x;  
        if (x >= 200 || x <= 30) {  
            move_x = -move_x;  
        }  
    }  
  
    @Override  
    public void surfaceCreated(SurfaceHolder holder) {  
        new Thread(this).start();  
    }  
  
    @Override  
    public void surfaceChanged(SurfaceHolder holder, int format, int width,  
            int height) {  
    }  
  
    @Override  
    public void surfaceDestroyed(SurfaceHolder holder) {  
    }  
}