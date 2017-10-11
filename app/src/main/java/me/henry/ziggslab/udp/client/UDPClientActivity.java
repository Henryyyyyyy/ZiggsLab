package me.henry.ziggslab.udp.client;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.InetAddress;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.henry.ziggs.udputils.UDPSocketCallback;
import me.henry.ziggs.udputils.UDPSocketConnect;
import me.henry.ziggslab.R;



public class UDPClientActivity extends AppCompatActivity {
@BindView(R.id.send)
    Button send;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.show)
    TextView show;
    private UDPSocketConnect udp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udpclient);
        ButterKnife.bind(this);
        udpRun();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            udp.send("好开心".getBytes());
            }
        });
    }
    private void udpRun() {
        if (udp != null) {
            //Tools.send2ServiceInterruptUDP(getActivity(), udp);
            udp.stop();
            udp = null;
        }
        udp = new UDPSocketConnect();
        udp.setSendAddress(this, "10.97.84.68", 8084,7);
        udp.setUDPSocketCallback(new UDPSocketCallback() {
            @Override
            public void udp_receive( byte[] buffer, InetAddress inetAddress, int port) {
//                Message msg = new Message();
//                msg.obj = buffer;
//                if (mHandler != null)
//                    mHandler.sendMessage(msg);
                final byte[] b=buffer;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        show.setText(new String(b));
                    }
                });
            }

            @Override
            public void udp_timeout(byte[] buffer, InetAddress inetAddress, int port) {

            }

        });
        new Thread(udp).start();

    }
}
