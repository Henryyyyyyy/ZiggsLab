package me.henry.ziggs.websocket;

import org.java_websocket.handshake.ServerHandshake;

/**
 * Created by zj on 2017/10/9.
 * me.henry.ziggs.websocket
 */

public interface wsSocketListener {
    void onOpen(ServerHandshake handshakedata);
    void onMessage(String message);
    void onClose(int code, String reason, boolean remote);
    void onError(Exception ex);

}
