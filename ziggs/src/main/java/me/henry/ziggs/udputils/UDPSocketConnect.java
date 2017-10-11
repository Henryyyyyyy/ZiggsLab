package me.henry.ziggs.udputils;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.SocketException;
import java.util.Vector;

public class UDPSocketConnect implements Runnable {
    private String TAG = "UDPSocketConnect";
    private static Vector<byte[]> datas = new Vector<byte[]>();// 待发送数据队列
    private String host = null;
    private int port = -1;
    private boolean isConnect = false;
    private boolean isReceive = false;// 是否连接服务器
    private boolean isSend = false;
    private UDPSocketFactory mSocket;
    private SendRunnable sendRunnable;//发送数据的线程？
    private UDPSocketCallback callback;

    private int udpTimeout = -1;//指定超时时间
    private Context mContext;
    /**
     * 是否需要覆盖超时统计，在场景每一个命令都需要独立的超时统计，插座则需要覆盖上一个超时统计,默认需要
     */
    private boolean isNeedCover = true;

    //UDP接收完一个DataPackage是否需要Thread.sleep,默认是需要，特殊情况，在主机升级时候是不需要！！！已经过测试！1需要sleep，0不需要
    private boolean isReceiveDataNeedThreadSleep = true;


    public UDPSocketConnect() {
        sendRunnable = new SendRunnable();
    }

    @Override
    public void run() {

        if (host == null || port == -1) {// || !Tools.isIPV4(host)
            return;
        }
        isConnect = true;
        while (isConnect) {
            try {
                //XLog.d(TAG, "UDP正在连接");
                mSocket = new UDPSocketFactory();
            } catch (SocketException se) {
                try {
                    Thread.sleep(1000 * 3);//3秒重连一次，无限重连？
                    continue;
                } catch (InterruptedException e) {
                    continue;
                }
            }
            if (udpTimeout == -1)//未指定超时时间
            {
                mSocket.setSendAddress(host, port);
            }//默认5秒超时
            else {
                mSocket.setSendAddress(host, port, udpTimeout);
            }

            Log.d(TAG, "主机host -> " + host + ":" + port);
            mSocket.setUDPSocketCallback(mContext, callback, isNeedCover, isReceiveDataNeedThreadSleep);

            isReceive = true;
            isSend = true;
            //XLog.d(TAG, "UDP连接成功");
            new Thread(sendRunnable).start();
            //XLog.d(TAG, "开始接受UDP消息");

            while (isReceive) {
                try {
                    mSocket.receive();
                } catch (IOException e) {
                    break;
                }
            }
        }
        try {
            if (sendRunnable != null)
                sendRunnable.stop();
            isReceive = false;
            if (mSocket != null)
                mSocket.close();
            //mSocket = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public void setSendAddress(Context context, String host, int port) {
        setSendAddress(context, host, port, -1);
    }

    public void setSendAddress(Context context, String host, int port, int timeout) {
        this.mContext = context;
        this.udpTimeout = timeout;
        this.host = host;
        this.port = port;
        Log.d(TAG, "UDP通讯地址:" + host + "：" + port);
    }

    /**
     * 是否需要覆盖超时统计，在场景每一个命令都需要独立的超时统计，插座则需要覆盖上一个超时统计,默认需要,默认需要检测权限
     *
     * @param callback
     */
    public void setUDPSocketCallback(UDPSocketCallback callback) {
        this.callback = callback;
        this.isNeedCover = true;
        this.isReceiveDataNeedThreadSleep = true;
    }

    public void setUDPSocketCallback(UDPSocketCallback callback, boolean isReceiveDataNeedThreadSleep) {
        this.callback = callback;
        this.isNeedCover = true;
        this.isReceiveDataNeedThreadSleep = isReceiveDataNeedThreadSleep;
    }

    /**
     * 是否需要覆盖超时统计，在场景每一个命令都需要独立的超时统计，插座则需要覆盖上一个超时统计,默认需要
     *
     * @param callback
     * @param isNeedCover
     * @param isNeedCheckPermission 是否需要检测权限，一般调用此方法都是传false，不需要检测
     */
    public void setUDPSocketCallback(UDPSocketCallback callback, boolean isNeedCover, boolean isNeedCheckPermission) {
        this.callback = callback;
        this.isNeedCover = isNeedCover;
    }

    public void send(byte[] data) {
        sendRunnable.send(data);
    }

    public void stop() {
        isConnect = false;
        isReceive = false;
        if (mSocket != null)
            mSocket.close();
        if (sendRunnable != null)
            sendRunnable.stop();
    }

    public boolean IsConnect() {
        return isConnect;
    }

    private class SendRunnable implements Runnable {
        private Object lock = new Object();

        @Override
        public void run() {
            //XLog.d(TAG, "UDP发送线程开启");
            synchronized (lock) {
                while (isSend) {
                    if (datas.size() < 1) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            continue;
                        }
                    }
                    while (datas.size() > 0) {
                        byte[] buffer = new byte[0];// 获取一条发送数据
                        try {
                            buffer = datas.remove(0);
                        } catch (Exception e) {
                        }
                        if (isSend) {
                            try {
                                if (mSocket != null)
                                    mSocket.send(buffer);
                            } catch (IOException e) {
                                //e.printStackTrace();
                            }
                        } else {
                            lock.notify();
                        }
                    }
                }
            }
            //XLog.d(TAG, "UDP发送线程结束");
        }

        public void stop() {
            synchronized (lock) {
                isSend = false;
                lock.notify();
            }
        }

        public void send(byte[] data) {
            synchronized (lock) {
                datas.add(data);
                lock.notify();
            }
        }
    }
}
