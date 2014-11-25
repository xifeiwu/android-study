package study.android.activity;

import java.util.Vector;
import java.util.logging.Logger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
/**
 * 
 * @author xifei
 * notice: the value of screenW
 */
@SuppressLint("ClickableViewAccessibility") public class LoggerView extends SurfaceView implements Callback, Runnable {
    public Logger logger = Logger.getLogger(LoggerView.class.getName());
    private SurfaceHolder sfh;
    private Paint paint;
    private Canvas canvas;
    private Activity mContext;

    public LoggerView(Context context) {
        // TODO Auto-generated constructor stub　　
        super(context);
        mContext = (Activity) context;
        sfh = this.getHolder();
        sfh.addCallback(this);
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(20);
        paint.setAntiAlias(true);
        DisplayMetrics metrics = new DisplayMetrics();        
        mContext.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenW = metrics.widthPixels;
        screenH = metrics.heightPixels;
        
        this.info("size of DisplayMetrics: " + this.screenW + "*" + this.screenH);
    }

    private int di, dp, dpx, dpy;
    private String dstr;
    public void myDraw() {
        canvas = sfh.lockCanvas();
        canvas.drawColor(Color.LTGRAY);
        for (di = 0; di <  vecLength; di++) {
            dstr = subStrVec.elementAt(di);
            dp = subPosVec.elementAt(di);
            dpy = dp & 0xffff;
            dpx = (dp >> 16) & 0xffff;
            canvas.drawText(dstr, dpx, dpy + offsetY + distanceY, paint);
        }
        sfh.unlockCanvasAndPost(canvas);
    }

    private void logic() {
    }

    private float offsetY = 0;
    private float originY;
    private float distanceY = 0;
    private boolean isPressed = false;
    private float eventY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        event.getX();
        eventY = event.getY();
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            isPressed = true;
            originY = eventY;
            break;
        case MotionEvent.ACTION_MOVE:
            if (isPressed) {
                distanceY = eventY - originY;
            }
            break;
        case MotionEvent.ACTION_UP:
            if (isPressed) {
                isPressed = false;
                int min = (int) (screenH - (vecLength + 1) * paint.getTextSize());
                if(min < 0){
                    offsetY += distanceY;
                    if(offsetY > 0)
                        offsetY = 0;
                    if(offsetY < min)
                        offsetY = min;
                }
                distanceY = 0;
            }
            break;
        }
        return true;
    }
    private void updateOffsetY(){
        int min = (int) (screenH - (vecLength + 1) * paint.getTextSize());
        if(min < 0){
            offsetY = min;
        }        
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
    }

    private int screenW, screenH;
    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        // TODO Auto-generated method stub
        screenW = getWidth();
        screenH = getHeight();
        this.info("size from surfaceCreated: " + this.screenW + "*" + this.screenH);
        flag = true;
        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        // TODO Auto-generated method stub
        flag = false;
    }

    public boolean flag;
    public Thread mThread;
    public long start, during;
    public long MILLISPERSECOND = 100;
    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (flag) {
            start = System.currentTimeMillis();
            logic();
            myDraw();
            during = System.currentTimeMillis() - start;
            if (during < MILLISPERSECOND) {
                try {
                    Thread.sleep(MILLISPERSECOND - during);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private Vector<String> subStrVec = new Vector<String>();
    private Vector<Integer> subPosVec = new Vector<Integer>();
    private int vecLength = 0;

    /**
     * 初始化字符串Vector。清空字符串。
     * @param mainStr: 将要添加的字符串。
     */
    public void refreshSubVec() {
        if (subStrVec != null) {
            subStrVec.removeAllElements();
        } else {
            subStrVec = new Vector<String>();
        }
        if (subPosVec != null) {
            subPosVec.removeAllElements();
        } else {
            subPosVec = new Vector<Integer>();
        }
        // subColor = new Vector<Character>();
        vecLength = 0;
    }
    /**
     * 向绘制数组中添加字符串。
     * @param mainStr: 将要添加的字符串。
     */
    private void appendToSubVec(String mainStr) {
        int i, strLen = mainStr.length();
        char ch;
        String substr;
        int start, end, px, py;
        float curLen;
        float[] chLen = new float[strLen];
        paint.getTextWidths(mainStr, chLen);

        start = 0;
        curLen = 0;
        for (i = 0; i < strLen; i++) {
            ch = mainStr.charAt(i);
            if (ch == '\n') {
                end = i;
//                logger.info("substring1: " + start + "and " + end);
                substr = mainStr.substring(start, end);
                subStrVec.add(substr);
                vecLength++;
                start = end + 1;
                i++;
                curLen = (i < strLen) ? 2 * paint.getTextSize() : 0;
                px = (int) (2 * paint.getTextSize());
                py = (int) ((vecLength + 1) * paint.getTextSize());
                subPosVec.add((px << 16) | py);
                continue;
            }
            curLen += chLen[i];
//            logger.info(i+": "+chLen[i]);
            if (curLen > screenW) {
                i--;
                end = i;
//                logger.info("substring2: " + start + "and " + end);
                substr = mainStr.substring(start, end);
                subStrVec.add(substr);
                vecLength++;
                start = end;
                curLen = 0;
                px = 0;
                py = (int) ((vecLength + 1) * paint.getTextSize());
                subPosVec.add((px << 16) | py);
            }
        }
        //收尾工作
        if ((curLen > 0)) {
            end = i;
//            logger.info("substring3: " + start + "and " + end);
            substr = mainStr.substring(start, end);
//            logger.info("substr: " + substr);
            subStrVec.add(substr);
            vecLength++;
            start = end;
            curLen = 0;
            px = (int) 0;
            py = (int) ((vecLength + 1) * paint.getTextSize());
            subPosVec.add((px << 16) | py);
        }
        updateOffsetY();
    }
    public void info(String message){
        appendToSubVec(message);   
    }
}
