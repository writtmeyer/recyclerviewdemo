package com.grokkingandroid.samplesapp.samples.recyclerviewdemo;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewDemoAdapter
        extends RecyclerView.Adapter
                <RecyclerViewDemoAdapter.ListItemViewHolder> {

    private List<DemoModel> items;

    RecyclerViewDemoAdapter(List<DemoModel> modelData) {
        if (modelData == null) {
            throw new IllegalArgumentException("modelData must not be null");
        }
        items = modelData;
    }

    /**
     * Adds and item into the underlying data set
     * at the position passed into the method.
     *
     * @param newModelData The item to add to the data set.
     * @param position The index of the item to remove.
     */
    public void addData(DemoModel newModelData, int position) {
        items.add(position, newModelData);
        notifyItemInserted(position);
    }

    /**
     * Removes the item that currently is at the passed in position from the
     * underlying data set.
     *
     * @param position The index of the item to remove.
     */
    public void removeData(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public DemoModel getItem(int position) {
        return items.get(position);
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_demo_01, viewGroup, false);
        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder viewHolder, int position) {
        DemoModel model = items.get(position);
        viewHolder.label.setText(model.label);
        String dateStr = DateUtils.formatDateTime(
                viewHolder.label.getContext(),
                model.dateTime.getTime(),
                DateUtils.FORMAT_ABBREV_ALL);
        viewHolder.dateTime.setText(dateStr);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder {
        TextView label;
        TextView dateTime;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.txt_label_item);
            dateTime = (TextView) itemView.findViewById(R.id.txt_date_time);
        }
    }
}
