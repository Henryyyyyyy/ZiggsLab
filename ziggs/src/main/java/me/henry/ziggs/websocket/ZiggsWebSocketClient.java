package me.henry.ziggs.websocket;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Created by zj on 2017/9/29.
 * me.henry.betterme.betterme.demo.websockett
 */

public class ZiggsWebSocketClient extends WebSocketClient {
    private static wsSocketListener mListener;


    private ZiggsWebSocketClient(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders, int connectTimeout) {
        super(serverUri, protocolDraft, httpHeaders, connectTimeout);
    }

    public static ZiggsWebSocketClient create(Map<String, String> headers, String uri, wsSocketListener listener) throws URISyntaxException {
        mListener = listener;
        ZiggsWebSocketClient client = new ZiggsWebSocketClient(new URI(uri), new Draft_6455(), headers, 0);
        return client;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {mListener.onOpen(handshakedata);

    }

    @Override
    public void onMessage(String message) {
        mListener.onMessage(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        mListener.onClose(code, reason, remote);
    }

    @Override
    public void onError(Exception ex) {
        mListener.onError(ex);
    }
}
