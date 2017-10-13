package me.henry.ziggslab.tcp;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;



import java.util.Vector;

public class TCPSocketConnect implements Runnable, Parcelable {
    public boolean isConnect = false;// 是否连接服务器
    private boolean isWrite = false;// 是否发送数据
    private static Vector<byte[]> datas = new Vector<byte[]>();// 待发送数据队列
    private Object lock = new Object();// 连接锁对象
    private  TCPSocketFactory mSocket;// socket连接
    private WriteRunnable writeRunnable;// 发送数据线程
    private String ip = null;
    private int port = -1;
    public boolean isSuccess; // 是否连接成功
    /**
     * 重连的时间间隔
     */
    private long retryInterval = 4000;

    /**
     * 创建连接
     *
     * @param callback 回调接口
     *                 线程池对象 默认4秒重连
     */
    public TCPSocketConnect(TCPSocketCallback callback) {
        mSocket = new  TCPSocketFactory(callback);// 创建socket连接
        writeRunnable = new WriteRunnable();// 创建发送线程

    }

    protected TCPSocketConnect(Parcel in) {
    }

    public static final Creator<TCPSocketConnect> CREATOR = new Creator<TCPSocketConnect>() {
        @Override
        public TCPSocketConnect createFromParcel(Parcel in) {
            return new TCPSocketConnect(in);
        }

        @Override
        public TCPSocketConnect[] newArray(int size) {
            return new TCPSocketConnect[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public void run() {
        if (ip == null || port == -1) {
            return;
        }
        isConnect = true;
        while (isConnect) {
            synchronized (lock) {
                try {
                    Log.d("tcp", ">TCP连接服务器<");
                    mSocket.connect(ip, port);// 连接服务器
                } catch (Exception e) {

                    try {
                        Log.d("tcp", ">TCP连接服务器失败, 4秒后重新连接<");
                        resetConnect();// 断开连接
                        lock.wait(retryInterval);
                        continue;
                    } catch (InterruptedException e1) {
                        continue;
                    }
                }
            }
            Log.d("tcp", ">TCP连接服务器成功<");
            isSuccess = true;
            isWrite = true;// 设置可发送数据
            new Thread(writeRunnable).start();// 启动发送线程
            try {
                mSocket.read();// 获取数据
            } catch (Exception e) {
                Log.d("tcp", ">TCP连接异常<", e);
            } finally {
                Log.d("tcp", ">TCP连接中断<");
                resetConnect();// 断开连接
            }
        }
        Log.d("tcp", ">=TCP结束连接线程=<");
    }

    public String getIp() {
        if (mSocket.getIp()==null){
            return "mSock null";
        }else {
            return mSocket.getIp();
        }

    }

    /**
     * 关闭服务器连接
     */
    public void disconnect() {
        synchronized (lock) {
            isConnect = false;
            isSuccess = false;
            lock.notify();
            resetConnect();
        }
    }

    public void stopConnect() {
        isConnect = false;
        isSuccess = false;
    }

    /**
     * 重置连接
     */
    public void resetConnect() {
        Log.d("tcp", ">TCP重置连接<");
        writeRunnable.stop();// 发送停止信息
        mSocket.disconnect();
    }

    /**
     * 向发送线程写入发送数据
     */
    public void write(byte[] buffer) {
        writeRunnable.write(buffer);
    }

    /**
     * 设置IP和端口
     *
     * @param host
     * @param port
     */
    public void setAddress(String host, int port) {
        this.ip = host;
        this.port = port;
    }

    /**
     * 发送数据
     */
    private boolean writes(byte[] buffer) {
        try {
            mSocket.write(buffer);
            Thread.sleep(1);
            return true;
        } catch (Exception e) {
            resetConnect();
            return false;
        }
    }


    /**
     * 发送线程
     *
     * @author Esa
     */
    private class WriteRunnable implements Runnable {
        private Object wlock = new Object();// 发送线程锁对象

        @Override
        public void run() {
            Log.d("tcp", ">TCP发送线程开启<");
            try {
                while (isWrite) {
                    synchronized (wlock) {
                        if (datas.size() <= 0) {
                            try {
                                wlock.wait();// 等待发送数据
                            } catch (InterruptedException e) {
                                continue;
                            }
                        }
                        while (datas.size() > 0) {
                            byte[] buffer = datas.remove(0);// 获取一条发送数据
                            if (isWrite) {
                                writes(buffer);// 发送数据
                            } else {
                                wlock.notify();
                            }
                        }
                    }
                }
                Log.d("tcp", ">TCP发送线程结束<");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 添加数据到发送队列
         *
         * @param buffer 数据字节
         */
        public void write(byte[] buffer) {
            synchronized (wlock) {
                datas.add(buffer);// 将发送数据添加到发送队列
                wlock.notify();// 取消等待
            }
        }

        public void stop() {
            synchronized (wlock) {
                isWrite = false;
                wlock.notify();
            }
        }
    }
}
