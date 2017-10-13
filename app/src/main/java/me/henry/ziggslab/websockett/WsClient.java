package me.henry.ziggslab.websockett;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zj on 2017/9/29.
 * me.henry.betterme.betterme.demo.websockett
 */

public class WsClient extends WebSocketClient {
    private static final String TAG = "WsClient";

    private boolean isget = false;
    private OnCloseListener mCloseListener;
    private MyWebSocketListener mWebSocketListener;
    private Context mContext;

    private WsClient(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders, int connectTimeout) {
        super(serverUri, protocolDraft, httpHeaders, connectTimeout);
    }


    public static WsClient create(OnCloseListener listener,Context context) {
        WsClient client = null;
        try {

            Map<String, String> headers = new HashMap<>();
            headers.put("sn", "1122334455");
            client = new WsClient(new URI("ws://192.168.11.122:8765"), new Draft_6455(), headers, 8000);
            client.setCloseListener(listener);
            client.setContext(context);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return client;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.e(TAG, "websocket   onOpen");
        if (mWebSocketListener!=null){
            mWebSocketListener.onOpen(handshakedata);
        }



    }

    @Override
    public void onMessage(String message) {
        Log.e(TAG, "websocket.onMessage=" + message);
        if (mWebSocketListener!=null){mWebSocketListener.onMessage(message);}

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.e(TAG, "websocket   onClose->code="+code+"            reason="+reason+"      remote="+remote);
        if (mWebSocketListener!=null){mWebSocketListener.onClose(code,reason,remote);}

        mCloseListener.onclose();
    }

    @Override
    public void onError(Exception ex) {
        Log.e(TAG, "websocket   onError->Exception="+ex.toString());
        if (mWebSocketListener!=null){mWebSocketListener.onError(ex);}

    }

    public void setCloseListener(OnCloseListener closeListener) {
        this.mCloseListener = closeListener;
    }

    public void setWebSocketListener(MyWebSocketListener mWebSocketListener) {
        this.mWebSocketListener = mWebSocketListener;
    }


    public void setContext(Context context) {
        this.mContext = context;
    }


}
