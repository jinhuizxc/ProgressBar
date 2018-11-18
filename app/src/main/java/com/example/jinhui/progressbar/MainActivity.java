package com.example.jinhui.progressbar;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 *
 * Android-打造炫酷进度条
 * https://www.imooc.com/learn/657
 *
 */
public class MainActivity extends AppCompatActivity {

    private HorizontalProgressBarWithProgress progressBar01;

    private CircleProgressBar circleProgressBar;

    private static final int MSG_UPDATE = 1;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int progress = progressBar01.getProgress();
            progressBar01.setProgress(++progress);
            circleProgressBar.setProgress(++progress);
            if (progress >= 100){
                handler.removeMessages(MSG_UPDATE);
            }
            handler.sendEmptyMessageDelayed(MSG_UPDATE, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar01 = findViewById(R.id.progressBar_01);
        circleProgressBar = findViewById(R.id.progressBar_circle);
        handler.sendEmptyMessage(MSG_UPDATE);


    }
}
