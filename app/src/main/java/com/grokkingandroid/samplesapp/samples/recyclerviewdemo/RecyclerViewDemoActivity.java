package com.grokkingandroid.samplesapp.samples.recyclerviewdemo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeTransform;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.widget.ImageButton;

import java.util.Date;
import java.util.List;

import static android.view.GestureDetector.SimpleOnGestureListener;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class RecyclerViewDemoActivity
        extends Activity
        implements RecyclerView.OnItemTouchListener,
        View.OnClickListener,
        ActionMode.Callback {

    RecyclerView recyclerView;
    RecyclerViewDemoAdapter adapter;
    int itemCount;
    GestureDetectorCompat gestureDetector;
    ActionMode actionMode;
    ImageButton fab;
    Context mContext;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        getWindow().setAllowReturnTransitionOverlap(true);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setSharedElementExitTransition(new ChangeTransform());

        setContentView(R.layout.activity_recyclerview_demo);
        fab = (ImageButton) findViewById(R.id.fab_add);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // actually VERTICAL is the default,
        // just remember: LinearLayoutManager
        // supports HORIZONTAL layout out of the box
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // you can set the first visible item like this:
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);


        // allows for optimizations if all items are of the same size:
        recyclerView.setHasFixedSize(true);

        List<DemoModel> items = RecyclerViewDemoApp.getDemoData();
        adapter = new RecyclerViewDemoAdapter(items);
        recyclerView.setAdapter(adapter);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);

        // this is the default; this call is actually only necessary with custom ItemAnimators
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // onClickDetection is done in this Activity's onItemTouchListener
        // with the help of a GestureDetector;
        // Tip by Ian Lake on G+ in a comment to this post:
        // https://plus.google.com/+LucasRocha/posts/37U8GWtYxDE
        recyclerView.addOnItemTouchListener(this);
        gestureDetector =
                new GestureDetectorCompat(this, new RecyclerViewDemoOnGestureListener());

        // fab
        View fab = findViewById(R.id.fab_add);
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
        RecyclerViewDemoApp.addItemToList(model, position);
        adapter.addData(model, position);
    }

    private void removeItemFromList() {
        int position = ((LinearLayoutManager) recyclerView.getLayoutManager()).
                findFirstCompletelyVisibleItemPosition();
        RecyclerViewDemoApp.removeItemFromList(position);
        adapter.removeData(position);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_add) {
            // fab click
            addItemToList();
        } else if (view.getId() == R.id.container_list_item) {
            // item click
            int idx = recyclerView.getChildPosition(view);
            if (actionMode != null) {
                myToggleSelection(idx);
                return;
            }
            DemoModel data = adapter.getItem(idx);
            View innerContainer = view.findViewById(R.id.container_inner_item);
            innerContainer.setTransitionName(Constants.NAME_INNER_CONTAINER + "_" + data.id);
            Intent startIntent = new Intent(this, CardViewDemoActivity.class);
            startIntent.putExtra(Constants.KEY_ID, data.id);
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, innerContainer, Constants.NAME_INNER_CONTAINER);
            this.startActivity(startIntent, options.toBundle());
        }
    }

    private void myToggleSelection(int idx) {
        adapter.toggleSelection(idx);
        String title = getString(R.string.selected_count, adapter.getSelectedItemCount());
        actionMode.setTitle(title);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        // Inflate a menu resource providing context menu items
        MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.menu_cab_recyclerviewdemoactivity, menu);
        fab.setVisibility(View.GONE);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_delete:
                List<Integer> selectedItemPositions = adapter.getSelectedItems();
                int currPos;
                for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
                    currPos = selectedItemPositions.get(i);
                    RecyclerViewDemoApp.removeItemFromList(currPos);
                    adapter.removeData(currPos);
                }
                actionMode.finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        this.actionMode = null;
        adapter.clearSelections();
        fab.setVisibility(View.VISIBLE);
    }

    private class RecyclerViewDemoOnGestureListener extends SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            onClick(view);
            return super.onSingleTapConfirmed(e);
        }

        public void onLongPress(MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (actionMode != null) {
                return;
            }
            // Start the CAB using the ActionMode.Callback defined above
            actionMode = startActionMode(RecyclerViewDemoActivity.this);
            int idx = recyclerView.getChildPosition(view);
            myToggleSelection(idx);
            super.onLongPress(e);
        }
    }
}

