package com.org.apnanews.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Pooja.Patil on 04/04/2018.
 */

public class SampleRecycler extends RecyclerView.Adapter<SampleRecycler.SampleHolder> {
    @Override
    public SampleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(SampleHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class SampleHolder extends RecyclerView.ViewHolder {
        public SampleHolder(View itemView) {
            super(itemView);
        }
    }
}
