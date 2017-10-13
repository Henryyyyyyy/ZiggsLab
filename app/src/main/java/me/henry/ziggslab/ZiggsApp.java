package me.henry.ziggslab;

import android.app.Application;

import com.facebook.stetho.Stetho;

import me.henry.ziggslab.greendblab.DataBaseManager;
import me.henry.ziggslab.websockett.SocketManager;

/**
 * Created by zj on 2017/10/9.
 * me.henry.ziggslab
 */

public class ZiggsApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        initDataBase();
        initStetho();
        SocketManager.getInstance().init(this);
    }
    private void initDataBase() {
        DataBaseManager.getInstance().init(this);
    }
    private void initStetho() {
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());
    }
}
