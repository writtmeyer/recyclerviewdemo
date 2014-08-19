package com.grokkingandroid.samplesapp.samples.recyclerviewdemo;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Outline;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeTransform;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import java.util.Date;
import java.util.List;

import static android.view.GestureDetector.SimpleOnGestureListener;

public class RecyclerViewDemoActivity
        extends Activity
        implements RecyclerView.OnItemTouchListener,
        View.OnClickListener {

    RecyclerView recyclerView;
    RecyclerViewDemoAdapter adapter;
    int itemCount;
    GestureDetectorCompat gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setAllowExitTransitionOverlap(true);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setSharedElementExitTransition(new ChangeTransform());

        setContentView(R.layout.activity_recyclerview_demo);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // actually VERTICAL is the default,
        // just remember: LinearLayoutManager
        // supports HORIZONTAL layout out of the box
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // you can set the first visible item like this:
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);


        // allows for optimizations:
        recyclerView.setHasFixedSize(true);

        List<DemoModel> items = RecyclerViewDemoApp.getDemoData();
        adapter = new RecyclerViewDemoAdapter(items);
        recyclerView.setAdapter(adapter);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);

        // that's the deault, you can of course use a custom ItemAnimator, if you like
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // onClickDetection done in this Activity's onItemTouchListener
        // with the help of a GestureDetector;
        // Tip by Ian Lake on G+ in a comment to this post:
        // https://plus.google.com/+LucasRocha/posts/37U8GWtYxDE
        recyclerView.addOnItemTouchListener(this);
        gestureDetector =
                new GestureDetectorCompat(this, new RecyclerViewDemoOnGestureListener());

        // fab
        int size = getResources().getDimensionPixelSize(R.dimen.fab_size);
        Outline outline = new Outline();
        outline.setOval(0, 0, size, size);
        View fab = findViewById(R.id.fab_add);
        fab.setOutline(outline);
        fab.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_cardview_demo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.action_remove) {
            removeItemFromList();
        }
        return true;
    }

    private void addItemToList() {
        DemoModel model = new DemoModel();
        model.label = "New Item " + itemCount;
        itemCount++;
        model.dateTime = new Date();
        int position = ((LinearLayoutManager) recyclerView.getLayoutManager()).
                findFirstVisibleItemPosition();
        // needed to be able to show the animation
        // otherwise the view would be inserted before the first
        // visible item; that is outside of the viewable area
        position++;
        // PseuodDAO
        RecyclerViewDemoApp.addItemToList(model, position);
        adapter.addData(model, position);
    }

    private void removeItemFromList() {
        int position = ((LinearLayoutManager) recyclerView.getLayoutManager()).
                findFirstCompletelyVisibleItemPosition();
        RecyclerViewDemoApp.removeItemFromList(position);
        adapter.removeData(position);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_add) {
            // fab click
            addItemToList();
        }
        else if (view.getId() == R.id.container_list_item) {
            // item click
            int idx = recyclerView.getChildPosition(view);
            DemoModel data = adapter.getItem(idx);
            View innerContainer = view.findViewById(R.id.container_inner_item);
            innerContainer.setViewName(Constants.NAME_INNER_CONTAINER + "_" + data.id);
            Intent startIntent = new Intent(this, CardViewDemoActivity.class);
            startIntent.putExtra(Constants.KEY_ID, data.id);
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, innerContainer, Constants.NAME_INNER_CONTAINER);
            this.startActivity(startIntent, options.toBundle());
        }
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    private class RecyclerViewDemoOnGestureListener extends SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            onClick(view);
            return super.onSingleTapConfirmed(e);
        }
    }
}

