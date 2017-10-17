package me.henry.ziggslab.udp.server;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import static android.R.attr.data;
import static android.R.attr.factor;

/**
 * Created by zj on 2017/10/14.
 * me.henry.ziggslab.udp.server
 */

public class ZiggsUpdServer implements Runnable{
    private DatagramPacket packetSend=null;
    private DatagramPacket packetReceive=null;
    private DatagramSocket mSocket=null;
    private int port;
    private String ip;
    private InetSocketAddress address;
    private static final int TIME_OUT=3000;
    private byte[] revData=new byte[1024];
    public boolean isUdpRun=true;
    public boolean UdpLifeState=true;

    public ZiggsUpdServer(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
public void setUdpRun(boolean isRun){
    isUdpRun=isRun;
}
    public boolean getUdpIsOver(){return UdpLifeState;}
    public void send(String txt)throws IOException{
        //错了!
        packetSend=new DatagramPacket(txt.getBytes(),txt.getBytes().length,packetReceive.getAddress(),packetReceive.getPort());
        mSocket.send(packetSend);

    }
    public void setSocketTimeOut(int time)throws SocketException{
        mSocket.setSoTimeout(time);
    }

    @Override
    public void run() {
        try {
            address=new InetSocketAddress(ip,port);
            mSocket=new DatagramSocket(address);
            setSocketTimeOut(TIME_OUT);
            packetReceive=new DatagramPacket(revData,revData.length);
            while (isUdpRun){
                try {
                    mSocket.receive(packetReceive);
                    //错了！
                    String data=new String(packetReceive.getData(),packetReceive.getOffset(),packetReceive.getLength());
                    Log.e("ZiggsUdpServer","data="+data);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            UdpLifeState=false;
            mSocket.close();

        } catch (SocketException e) {
            e.printStackTrace();
        }


    }
}
