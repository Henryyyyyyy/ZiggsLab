package me.henry.ziggslab.threadtest.syn;

import android.util.Log;

/**
 * Created by zj on 2017/10/17.
 * me.henry.ziggslab.threadtest.syn
 */

public class TaskTwo implements Runnable{
    private Object obj;
    private int count=0;
    private  boolean hasLock=false;
    public TaskTwo(Object obj) {
        this.obj = obj;
    }
    @Override
    public void run() {
        synchronized (obj) {
            obj.notifyAll();
            while (count < 5) {
                Log.e("momo", "Task  2-----------------" + count);
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
            Log.e("momo", "Task  2--end");
        }
    }
}
