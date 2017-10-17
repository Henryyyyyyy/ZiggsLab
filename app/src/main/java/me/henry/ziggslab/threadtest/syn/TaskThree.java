package me.henry.ziggslab.threadtest.syn;

import android.util.Log;

/**
 * Created by zj on 2017/10/17.
 * me.henry.ziggslab.threadtest.syn
 */

public class TaskThree implements Runnable {
    private Object obj;
    private int count = 0;
    private  boolean hasLock=false;
    public TaskThree(Object obj) {
        this.obj = obj;
    }

    @Override
    public void run() {
        synchronized (obj) {

            while (count < 5) {
                Log.e("momo", "Task  3-----------------" + count);
                count++;
                try {
                    if (count == 3&&!hasLock) {
                        hasLock=true;
                        obj.wait();
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.e("momo", "Task  3--end");
            obj.notifyAll();

        }
    }
}
