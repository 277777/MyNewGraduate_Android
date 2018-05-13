package com.example.lalal.Fragment.Manage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.example.lalal.MyNewGraduate.R;
import com.example.lalal.Tools.BLTH.BluetoothServer;
import com.example.lalal.Tools.Normal.ThreadSend;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class HeartActivity extends Activity {

    private List<View> heartbeatViews;
    private Thread heartbeatThread;
    private TextView notesBg,notesAnima,dongtaiedit;
    private boolean threadstop = false;


//    study_tool_note_animation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart);
        heartbeatViews = new ArrayList<View>();
        notesBg = findViewById(R.id.study_tool_note_background);
        initAnimationBackground(notesBg);
        notesAnima = findViewById(R.id.study_tool_note_animation);
        heartbeatViews.add(notesAnima);
        dongtaiedit = findViewById(R.id.dongtaiedit);
        ThreadSend threadSend = new ThreadSend("H");
        threadSend.Tsend();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        threadstop = false;
        ThreadSend threadSend = new ThreadSend("N");
        threadSend.Tsend();
    }

    private class HeatbeatThread extends Thread {
        public void run() {
            try {
                sleep(100);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            while (true) {
                if (threadstop == true) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            System.out.println("BluetoothServer.number():" + BluetoothServer.number());
                            notesAnima.setText(String.valueOf(BluetoothServer.number()));
                            for (View view : heartbeatViews) {
                                playHeartbeatAnimation(view);
                            }
                        }
                    });
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        ;
    }


    /**
     * 开始心跳
     */
    private void startHeartBeat() {
        threadstop = true;
        if (heartbeatThread == null) {
            heartbeatThread = new HeatbeatThread();
        }
        if (!heartbeatThread.isAlive()) {
            heartbeatThread.start();
        }
    }

    /**
     * 停止心跳
     */
    private void stopHeartBeat() {
        if (heartbeatThread != null && heartbeatThread.isInterrupted()) {
            heartbeatThread.interrupt();
            heartbeatThread = null;
            System.gc();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startHeartBeat();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopHeartBeat();
    }

    private void initAnimationBackground(View view) {
        view.setAlpha(0.2f);
        view.setScaleX(1.4f);
        view.setScaleY(1.4f);

    }

    // 按钮模拟心脏跳动
    private void playHeartbeatAnimation(final View heartbeatView) {
        AnimationSet swellAnimationSet = new AnimationSet(true);
        swellAnimationSet.addAnimation(new ScaleAnimation(1.0f, 1.8f, 1.0f, 1.8f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f));
        swellAnimationSet.addAnimation(new AlphaAnimation(1.0f, 0.3f));

        swellAnimationSet.setDuration(500);
        swellAnimationSet.setInterpolator(new AccelerateInterpolator());
        swellAnimationSet.setFillAfter(true);

        swellAnimationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AnimationSet shrinkAnimationSet = new AnimationSet(true);
                shrinkAnimationSet.addAnimation(new ScaleAnimation(1.8f, 1.0f, 1.8f, 1.0f, Animation.RELATIVE_TO_SELF,
                        0.5f, Animation.RELATIVE_TO_SELF, 0.5f));
                shrinkAnimationSet.addAnimation(new AlphaAnimation(0.3f, 1.0f));
                shrinkAnimationSet.setDuration(1000);
                shrinkAnimationSet.setInterpolator(new DecelerateInterpolator());
                shrinkAnimationSet.setFillAfter(false);
                heartbeatView.startAnimation(shrinkAnimationSet);// 动画结束时重新开始，实现心跳的View
            }
        });
        heartbeatView.startAnimation(swellAnimationSet);
    }

    private class MyView extends View {
        private Handler mHandler;

        private int mColor;
        private float cx;
        private float cy;
        private float radius;

        public MyView(Context context) {
            super(context);
            mHandler = new Handler(getMainLooper());
            setBackgroundColor(Color.WHITE);
            Thread mThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    MyView.this.invalidate();
                    mHandler.postDelayed(this, 300);
                }
            });
            mThread.start();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            // TODO Auto-generated method stub
            super.onDraw(canvas);
            update();
            Paint p = new Paint();
            p.setColor(mColor);
            canvas.drawCircle(cx, cy, radius, p);
        }

        private void update() {
            Random random = new Random();
            cx = (float) (random.nextInt(720)); // 随机生成圆心横坐标（0至200）
            cy = (float) (random.nextInt(1180)); // 随机生成圆心纵坐标（0至400）
            radius = (float) (10 + random.nextInt(90)); // 随机生成圆的半径（10至100）

            int r = random.nextInt(256);
            int g = random.nextInt(256);
            int b = random.nextInt(256);
            mColor = Color.rgb(r, g, b); // 随机生成颜色
        }
    }


}

