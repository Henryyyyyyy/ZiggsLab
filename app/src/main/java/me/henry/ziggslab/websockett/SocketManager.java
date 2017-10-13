package me.henry.ziggslab.websockett;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;



/**
 * Created by zj on 2017/10/12.
 * com.weicontrol.net
 */

public class SocketManager implements OnCloseListener {
    private static final String TAG = "WsClient";
    private static Context mContext;
    private WsClient mWsClient;
    private static final int RECONNECT_INTERVAL = 3000;

    /**
     * 首先要在app获取context
     *
     * @param context
     */
    public void init(Context context) {
        Log.e(TAG, "init======");
        mContext = context;

    }

    private SocketManager(Context context) {
        this.mContext = context;
    }

    private static final class HOLDER {
        private static final SocketManager INSTANCE = new SocketManager(mContext);
    }

    public static SocketManager getInstance() {
        return HOLDER.INSTANCE;
    }

    public WsClient getWebSocket() {
        return mWsClient;
    }

    public void send(byte[] data) {
        if (mWsClient != null) {
            mWsClient.send(data);
        } else {
            Log.e(TAG, "方法send,websocket=null");

        }
    }

    public void send(String txt) {
        if (mWsClient != null) {
            mWsClient.send(txt);
        } else {
            Log.e(TAG, "方法send,websocket=null");
        }
    }

    public void connect() {

        mWsClient = WsClient.create(this, mContext);
        mWsClient.connect();
//        OpenTask task = new OpenTask();
//        new Thread(task).start();
    }

    public void reConnect() {

            boolean isconnecting=mWsClient.isConnecting();
            boolean isclose=mWsClient.isClosed();
            boolean isopen=mWsClient.isOpen();
            Log.e(TAG,"isconnecting="+isconnecting+"  isclose="+isclose+"     isopen="+isopen);
            if (!isconnecting&&!isopen)
            {
                Looper.prepare();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "reconnecting............");
                        mWsClient = WsClient.create(SocketManager.this, mContext);
                        mWsClient.connect();
                    }
                }, RECONNECT_INTERVAL);
         Looper.loop();

            }



    }

    public void onDestory() {
        if (mWsClient != null) {
            mWsClient.close();
        }
    }

    public void close() {
        if (mWsClient != null && !mWsClient.isClosed()) {
            mWsClient.close();

        }
    }

    public void setWebSocketListener(MyWebSocketListener listener) {
        if (mWsClient != null) {
            mWsClient.setWebSocketListener(listener);
        }
    }

    @Override
    public void onclose() {
        Log.e(TAG, "onclose之后进行重连操作!");
        reConnect();

    }

    public class OpenTask implements Runnable {

        @Override
        public void run() {
            while (!mWsClient.isOpen()) {
                if (!mWsClient.isConnecting()) {//因为有connecttimeout的存在，所以不用sleep了吧
                    mWsClient.connect();
                }
            }
        }
    }
}
