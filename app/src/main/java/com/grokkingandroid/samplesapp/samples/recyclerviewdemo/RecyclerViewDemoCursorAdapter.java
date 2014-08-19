package com.grokkingandroid.samplesapp.samples.recyclerviewdemo;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple implementation that vaguely resembles SimpleCursorAdapter of old days
 */
public class RecyclerViewDemoCursorAdapter extends RecyclerView.Adapter<RecyclerViewDemoCursorAdapter.ListItemViewHolder> {

    Cursor cursor;
    int[] layoutIds;
    String[] columnNames;
    SparseArray<String> columnMapping;

    RecyclerViewDemoCursorAdapter(Cursor c, int[] layoutIds, String[] columnNames) {
        if (c == null || layoutIds == null || columnNames == null) {
            throw new IllegalArgumentException("All objects passed into the constructor must not be null");
        }
        this.cursor = c;
        columnMapping = new SparseArray<String>(layoutIds.length);
        for (int i = 0; i < layoutIds.length; i++) {
            columnMapping.append(c.getColumnIndex(columnNames[i]), columnNames[i]);
        }
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_demo_01, viewGroup, false);
        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder viewHolder, int position) {
        cursor.moveToPosition(position);

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder {
        TextView label;
        TextView dateTime;
        ImageView icon;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            label = (TextView)itemView.findViewById(R.id.txt_label_item);
            dateTime = (TextView)itemView.findViewById(R.id.txt_date_time);
//            icon = (ImageView)itemView.findViewById(R.id.img_icon);
        }
    }
}
