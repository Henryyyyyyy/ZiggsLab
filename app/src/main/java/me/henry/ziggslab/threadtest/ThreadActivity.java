package me.henry.ziggslab.threadtest;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.henry.ziggslab.R;
import me.henry.ziggslab.threadtest.syn.TaskOne;
import me.henry.ziggslab.threadtest.syn.TaskThree;
import me.henry.ziggslab.threadtest.syn.TaskTwo;

public class ThreadActivity extends AppCompatActivity {
    @BindView(R.id.start_thread)
    TextView start_thread;
    @BindView(R.id.interrupt_thread)
    TextView interrupt_thread;
    @BindView(R.id.testBtn)
    TextView testBtn;
Object obj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
        ButterKnife.bind(this);
        obj=new Object();

        start_thread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
new Thread(new TaskOne(obj)).start();
new Thread(new TaskTwo(obj)).start();
new Thread(new TaskThree(obj)).start();

            }
        });

        interrupt_thread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
obj.notifyAll();

            }
        });
    }
}
