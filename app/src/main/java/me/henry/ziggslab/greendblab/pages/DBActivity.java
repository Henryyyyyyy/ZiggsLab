package me.henry.ziggslab.greendblab.pages;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

import me.henry.ziggslab.R;
import me.henry.ziggslab.greendblab.DataBaseManager;
import me.henry.ziggslab.greendblab.ZiggsDao;
import me.henry.ziggslab.greendblab.entities.Heros;
import me.henry.ziggslab.greendblab.entities.HerosDao;
import me.henry.ziggslab.websockett.WsClient;



public class DBActivity extends AppCompatActivity implements View.OnClickListener {
    Button add;
    Button alter;
    Button delete;
    Button search;
    TextView show;
    HerosDao herosDao;
    Random r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);
        herosDao = DataBaseManager.getInstance().getDaoSession().getHerosDao();
        add = (Button) findViewById(R.id.add);
        alter = (Button) findViewById(R.id.alter);
        delete = (Button) findViewById(R.id.delete);
        search = (Button) findViewById(R.id.search);
        show = (TextView) findViewById(R.id.show);
        add.setOnClickListener(this);
        alter.setOnClickListener(this);
        delete.setOnClickListener(this);
        search.setOnClickListener(this);

    }
    WsClient wsClient;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
//                Heros heros = new Heros("hero-" + randomCommon(10, 1000), "21", true, 1234);
//                Toast.makeText(DBActivity.this, "添加了" + herosDao.insert(heros), Toast.LENGTH_LONG).show();
               wsClient.send("{'EventType':10000}");
                break;
            case R.id.alter:
                Heros unique = herosDao.queryBuilder()
                        .where(HerosDao.Properties.Name.eq("hero-997"))
                        .build()
                        .unique();
                if (unique != null) {
                    unique.setSex(false);
                    unique.setName("cao-gaile");
                    herosDao.update(unique);
                }

                break;
            case R.id.delete:
                List<Heros> list1 = herosDao.queryBuilder()
                        .whereOr(HerosDao.Properties.Name.eq("hero-911"), HerosDao.Properties.Name.eq("hero-373"))
                        .build()
                        .list();
                herosDao.deleteInTx(list1);
                break;
            case R.id.search:
//                ZiggsDao<Heros> lev = new ZiggsDao(herosDao);
//                lev.searchByWhere(HerosDao.Properties.Sex.eq(false));
                  wsClient=WsClient.create();
                  wsClient.connect();

                break;

        }
    }

    public static int randomCommon(int min, int max) {
        int num = (int) (Math.random() * (max - min)) + min;
        return num;
    }
    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        //由高位到低位
        result[0] = (byte)((i >> 24) & 0xFF);
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
        result[3] = (byte)(i & 0xFF);
        return result;
    }
}
