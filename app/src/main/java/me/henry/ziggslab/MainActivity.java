package me.henry.ziggslab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.java_websocket.handshake.ServerHandshake;

import me.henry.ziggslab.websockett.MyWebSocketListener;
import me.henry.ziggslab.websockett.SocketManager;

public class MainActivity extends AppCompatActivity implements MyWebSocketListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SocketManager.getInstance().setWebSocketListener(this);
        SocketManager.getInstance().connect();
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {

    }

    @Override
    public void onMessage(String message) {

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onError(Exception ex) {

    }
}
