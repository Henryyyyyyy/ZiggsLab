package me.henry.ziggslab.websockett;


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

import static android.R.id.message;

/**
 * Created by zj on 2017/9/29.
 * me.henry.betterme.betterme.demo.websockett
 */

public class WsClient extends WebSocketClient {
    private static final String TAG = "WsClient";

    private boolean isget=false;

    private WsClient(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders, int connectTimeout) {
        super(serverUri, protocolDraft, httpHeaders, connectTimeout);

    }

    public static WsClient create() {
        WsClient client = null;
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("sn", "1122334455667788");
            client = new WsClient(new URI("ws://192.168.11.121:8765"), new Draft_6455(), headers, 0);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return client;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.e(TAG, "websocket   onOpen");


    }

    @Override
    public void onMessage(String message) {
        Log.e(TAG, "websocket.onMessage=" + message);
        if (isget)return;
        try {
            JSONObject json = new JSONObject(message);
            if (json.has("EventType")) {
                int eventType = json.getInt("EventType");


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.e(TAG, "websocket.onClose=" + reason);
    }

    @Override
    public void onError(Exception ex) {
        Log.e(TAG, "websocket.onError=" + ex.toString());
    }


}
