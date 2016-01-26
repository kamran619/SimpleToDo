package com.codepatch.simpletodo.persistance;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.codepatch.simpletodo.model.OpenModel;

import java.util.Iterator;
import java.util.List;

/**
 * Created by kpirwani on 1/23/16.
 */
public class PersistenceCoordinator {

    private PersistenceCoordinator() {
    }

    public static List<? extends OpenModel> selectEntries(Class fromClass, String orderBy, String whereClause, String... whereArgs) {
        Select select = new Select();
        From from = select.all().from(fromClass);
        if (whereClause != null && whereArgs != null) {
            from.where(whereClause, whereArgs);
        }
        if (orderBy != null) {
            from.orderBy(orderBy);
        }
        List<? extends OpenModel> models = from.execute();
        return models;
    }

    public static boolean saveEntries(List<? extends Model> items) {
        boolean success = false;
        ActiveAndroid.beginTransaction();
        Iterator itr = items.iterator();
        try {
            while (itr.hasNext()) {
                Model model = (Model) itr.next();
                model.save();
            }
            ActiveAndroid.setTransactionSuccessful();
            success = true;
        } finally {
            ActiveAndroid.endTransaction();
        }
        return success;
    }
}

