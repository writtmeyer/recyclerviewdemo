package com.grokkingandroid.samplesapp.samples.recyclerviewdemo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.format.DateUtils;
import android.transition.ChangeTransform;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class CardViewDemoActivity extends Activity {

    CardView cardView;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setAllowEnterTransitionOverlap(true);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setSharedElementEnterTransition(new ChangeTransform());

        Bundle extras = getIntent().getExtras();
        int id = extras.getInt(Constants.KEY_ID);
        DemoModel model = RecyclerViewDemoApp.findById(id);

        setContentView(R.layout.activity_cardview_demo);
        cardView = (CardView) findViewById(R.id.card_detail);
        View innerContainer = cardView.findViewById(R.id.container_inner_item);
        innerContainer.setTransitionName(Constants.NAME_INNER_CONTAINER);
        TextView label = (TextView) cardView.findViewById(R.id.txt_label_item);
        TextView dateTime = (TextView) cardView.findViewById(R.id.txt_date_time);
        label.setText(model.label);
        String dateStr = DateUtils.formatDateTime(
                this,
                model.dateTime.getTime(),
                DateUtils.FORMAT_ABBREV_ALL);
        dateTime.setText(dateStr);
    }
}
