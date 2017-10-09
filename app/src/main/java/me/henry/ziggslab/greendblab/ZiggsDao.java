package me.henry.ziggslab.greendblab;

import android.util.Log;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

/**
 * Created by zj on 2017/10/9.
 * me.henry.ziggslab.greendblab
 */

public class ZiggsDao<T> {
    private AbstractDao<T, Long> baseDao;

    public ZiggsDao(AbstractDao<T, Long> baseDao) {
        this.baseDao = baseDao;
        String[] allColumns = baseDao.getAllColumns();
        for (int i = 0; i < allColumns.length; i++) {
            Log.e("testgreen", "column=" + allColumns[i]);
        }
    }

    public void searchByWhere( WhereCondition cond) {
        List<T> list2 = baseDao.queryBuilder()
                .where(cond)
                .build().list();
        if (list2 != null) {
            for (int i = 0; i < list2.size(); i++) {
                Log.e("testdao", "i=" + list2.get(i).toString());
            }
        }
    }
}
