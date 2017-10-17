package me.henry.ziggslab.threadtest;

import android.util.Log;

/**
 * Created by zj on 2017/10/16.
 * me.henry.ziggslab.threadtest
 */

public class TestRunnable implements  Runnable{
    private boolean isRun=true;
    @Override
    public void run() {

        while (isRun){
            Log.e("Yannis", "my fucking app is running");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                isRun=false;
            }
        }

    }
}
