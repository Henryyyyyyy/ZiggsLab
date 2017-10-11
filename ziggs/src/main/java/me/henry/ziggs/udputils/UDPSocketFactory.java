package me.henry.ziggs.udputils;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;



public class UDPSocketFactory {
    private final String TAG = "UDPSocketFactory";
    Thread mThread;
    long t1;
    private int dataLength = 1024;
    private byte[] receiveData = new byte[dataLength];
    private DatagramSocket mSocket;
    private DatagramPacket receivePacket, sendPacket;
    private SocketAddress sendAddress;
    private UDPSocketCallback callback;
    private byte[] currentReceiveByteData = null;
    private byte[] sendData;
    private Context mContext;

    private int TIMEOUT = 5;//默认超时时间
    /**
     * 是否需要覆盖超时统计，在场景每一个命令都需要独立的超时统计，插座则需要覆盖上一个超时统计,默认需要
     */
    private boolean isNeedCover = true;

    //UDP接收完一个DataPackage是否需要Thread.sleep,默认是需要，特殊情况，在主机升级时候是不需要！！！已经过测试！1需要sleep，0不需要
    private boolean isReceiveDataNeedThreadSleep = true;

    public UDPSocketFactory() throws SocketException {
        receivePacket = new DatagramPacket(receiveData, receiveData.length);
        mSocket = new DatagramSocket();
    }

    /**
     * 默认Enumer.RemoteModeTimeOut秒超时
     *
     * @param host
     * @param port
     */
    public void setSendAddress(String host, int port) {
        setSendAddress(host, port, TIMEOUT);
    }

    /**
     * 设置超时时间
     *
     * @param host
     * @param port
     * @param timeout
     */
    public void setSendAddress(String host, int port, int timeout) {
        TIMEOUT = timeout;
        sendAddress = new InetSocketAddress(host, port);
    }


    public void setUDPSocketCallback(Context context, UDPSocketCallback callback, boolean isNeedCover, boolean isReceiveDataNeedThreadSleep) {
        this.mContext = context;
        this.callback = callback;
        this.isNeedCover = isNeedCover;
        this.isReceiveDataNeedThreadSleep = isReceiveDataNeedThreadSleep;
    }

    public void send(byte[] data) throws IOException {
        if (data == null)
            return;
        sendBuffer(data);
    }

    // ==============================================================自定义UDP超时===========TODO start

    /**
     * 发送数据包,并启动自定义设置超时机制
     *
     * @param data
     * @throws IOException
     */
    private void sendBuffer(byte[] data) throws IOException {
        if (data == null || sendAddress == null)
            return;
        if (data.length < dataLength) {
            sendData = data;
            sendPacket = new DatagramPacket(data, data.length, sendAddress);
            if (mSocket != null && sendPacket != null) {
                setTimeout(sendData);//自定义设置超时机制
                if (mSocket != null && sendPacket != null)
                    mSocket.send(sendPacket);
            }

        } else {
            throw new IOException("发送数据包大于限定长度");
        }
    }

    public void receive() throws IOException {
        if (mSocket == null)
            return;
        mSocket.setSoTimeout(0);// 永远等待接受消息！

        if (mSocket != null && receivePacket != null)
            mSocket.receive(receivePacket);

        if (callback != null) {
            currentReceiveByteData = new byte[receivePacket.getLength()];
            try {
                System.arraycopy(receivePacket.getData(), 0, currentReceiveByteData, 0, receivePacket.getLength());
                callback.udp_receive(currentReceiveByteData, receivePacket.getAddress(), receivePacket.getPort());
                //XLog.d(TAG, "UDP底层正常接收到来自IP：" + receivePacket.getAddress() + ":" + receivePacket.getPort() + ",长度为：" + currentReceiveByteData.length + " 的数据：" + ConvertUtil.byte2HexStr(currentReceiveByteData));
            } catch (Exception e) {
                e.printStackTrace();
            }
            //默认是1，需要sleep，在主机升级时候是0，不需要休眠，防止主机升级出现BUG
            if (isReceiveDataNeedThreadSleep) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }
            currentReceiveByteData = null;

        }

    }

    /**
     * 是否需要覆盖超时统计，在场景每一个命令都需要独立的超时统计，插座则需要覆盖上一个超时统计
     *
     * @param data
     */

    private void setTimeout(byte[] data) {
        byte[] sendData = data;
        //XLog.d(TAG, "设置超时：是否需要覆盖超时=" + isNeedCover);
        if (isNeedCover) {// 默认覆盖统计超时,同一个线程
            final byte[] d = sendData;
            if (mThread == null) {
                mThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendTimeout(d);
                    }
                });
                mThread.start();
            } else {
                if (!mThread.isAlive()) {// 线程运行结束,重新开始
                    mThread = null;
                    setTimeout(sendData);
                } else {
                    t1 = System.currentTimeMillis();// 线程在运行中，改变t1的值
                }
            }

        } else {
            final byte[] d = sendData;
            new Thread(new Runnable() {// 每一个命令都有对应的超时,每次不同线程
                @Override
                public void run() {
                    sendTimeout(d);
                }
            }).start();

        }
    }

    private void sendTimeout(byte[] d) {
        byte[] data = d;
        t1 = System.currentTimeMillis();
        long t = System.currentTimeMillis();

        //XLog.d(TAG, "UDP超时" + TIMEOUT + "秒,开始循环计算。");
        while (currentReceiveByteData == null) {//此处根据currentReceiveByteData 进行判断是否是接收数据超时！！
            long curr = (System.currentTimeMillis() - (isNeedCover ? t1 : t));
            if (curr > TIMEOUT * 1000) {
                if (callback != null) {
                    Log.d(TAG, ((InetSocketAddress) sendAddress).getHostName() + ":" + ((InetSocketAddress) sendAddress).getPort() + "  UDP超时" + TIMEOUT + "秒。");
                    callback.udp_timeout(currentReceiveByteData, receivePacket.getAddress(), receivePacket.getPort());
                    break;
                }
            }
        }
    }

    // ==============================================================自定义UDP超时===========TODO end

    public void close() {
        if (mSocket == null)
            return;
        try {
            if (mSocket.isConnected()) {
                mSocket.disconnect();
            }
            if (!mSocket.isClosed()) {
                mSocket.close();
            }
        } catch (Exception ex) {

        } finally {
            mSocket = null;
        }

    }
}
