package me.henry.ziggslab.websockett;

import org.java_websocket.handshake.ServerHandshake;

/**
 * Created by zj on 2017/10/13.
 * com.weicontrol.net.websocket
 */

public interface MyWebSocketListener {
    void onOpen(ServerHandshake handshakedata);
    void onMessage(String message);
     void onClose(int code, String reason, boolean remote);
    void onError(Exception ex);
}
