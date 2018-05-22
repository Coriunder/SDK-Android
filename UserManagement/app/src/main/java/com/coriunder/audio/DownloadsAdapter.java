package com.coriunder.audio;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.coriunder.R;

import java.util.ArrayList;
import java.util.HashMap;

public class DownloadsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public static final int TYPE_ITEM = 0;
    public static final int TYPE_INDICATOR = 1;

    private ArrayList<HashMap<String, String>> downloads;
    private boolean showProgressIndicator;

    public DownloadsAdapter(ArrayList<HashMap<String, String>> downloads){
        this.downloads = downloads;
        showProgressIndicator = true;
    }

    @Override
    public int getItemCount() {
        return downloads.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < downloads.size()) return TYPE_ITEM;
        return TYPE_INDICATOR;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.downloads_item_cell, viewGroup, false);
            return new HistoryViewHolder(view);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.downloads_indicator_cell, viewGroup, false);
            return new ActivityIndicatorHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        if (position < downloads.size()) {
            ((HistoryViewHolder) viewHolder).productName.setText(downloads.get(position).get(AudioConstants.KEY_PRODUCT_NAME));
            ((HistoryViewHolder) viewHolder).productDate.setText(downloads.get(position).get(AudioConstants.KEY_DATE));

            ((HistoryViewHolder) viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playerIntent = new Intent(((HistoryViewHolder) viewHolder).itemView.getContext(), PlayerActivity.class);
                    playerIntent.putExtra("data", downloads);
                    playerIntent.putExtra("itemId", position);
                    ((HistoryViewHolder) viewHolder).itemView.getContext().startActivity(playerIntent);
                }
            });

        } else {
            if (showProgressIndicator) ((ActivityIndicatorHolder)viewHolder).indicator.setVisibility(View.VISIBLE);
            else ((ActivityIndicatorHolder)viewHolder).indicator.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void addData(ArrayList<HashMap<String, String>> data, boolean reloadFullList) {
        int alreadyShown = getRealDataSize();
        downloads.addAll(data);
        if (reloadFullList) notifyDataSetChanged();
        else {
            for (int i=0; i<data.size(); i++) {
                notifyItemInserted(alreadyShown+i);
            }
        }
    }

    public void hideSpinner() {
        this.showProgressIndicator = false;
        notifyItemChanged(getItemCount()-1);
    }

    public int getRealDataSize() {
        if (downloads == null) return 0;
        return downloads.size();
    }

    public boolean isProgressIndicatorVisible() {
        return showProgressIndicator;
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productDate;

        HistoryViewHolder(View itemView) {
            super(itemView);
            productName = (TextView) itemView.findViewById(R.id.product_name);
            productDate = (TextView) itemView.findViewById(R.id.product_date);
        }
    }

    public static class ActivityIndicatorHolder extends RecyclerView.ViewHolder {
        ProgressBar indicator;

        ActivityIndicatorHolder(View itemView) {
            super(itemView);
            indicator = (ProgressBar)itemView.findViewById(R.id.indicator);
        }
    }
}