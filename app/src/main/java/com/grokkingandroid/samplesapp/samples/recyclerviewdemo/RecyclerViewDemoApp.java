package com.grokkingandroid.samplesapp.samples.recyclerviewdemo;

import android.app.Application;
import android.util.SparseArray;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RecyclerViewDemoApp extends Application {

    private static List<DemoModel> demoData;
    private static SparseArray<DemoModel> demoMap;

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        Random r = new Random();
        demoData = new ArrayList<DemoModel>();
        demoMap = new SparseArray<DemoModel>();
        for (int i = 0; i < 20; i++) {
            DemoModel model = new DemoModel();
            DateTime dateTime = new DateTime();
            dateTime = dateTime.minusDays(r.nextInt(30));
            model.dateTime = dateTime.toDate();
            model.label = "Test Label No. " + i;
            demoData.add(model);
            demoMap.put(model.id, model);
        }
    }

    public static final List<DemoModel> getDemoData() {
        return new ArrayList<DemoModel>(demoData);
    }

    public static final List<DemoModel> addItemToList(DemoModel model, int position) {
        demoData.add(position, model);
        demoMap.put(model.id, model);
        return new ArrayList<DemoModel>(demoData);
    }

    public static final List<DemoModel> removeItemFromList(int position) {
        demoData.remove(position);
        demoMap.remove(demoData.get(position).id);
        return new ArrayList<DemoModel>(demoData);
    }

    public static DemoModel findById(int id) {
        return demoMap.get(id);
    }

}
