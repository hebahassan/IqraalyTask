package com.example.heba.iqraalytask.ui.audio;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.heba.iqraalytask.R;
import com.example.heba.iqraalytask.databinding.RowEpisodeBinding;
import com.example.heba.iqraalytask.network.model.Episode;

import java.util.List;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.EpisodeItemView> {
//    private Context context;
    private List<Episode> episodesList;
    ClickListener clickListener;

    public EpisodesAdapter(List<Episode> episodesList, ClickListener clickListener){
//        this.context = context;
        this.episodesList = episodesList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public EpisodeItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowEpisodeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_episode, parent, false);
        return new EpisodeItemView(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeItemView holder, int position) {
        Episode episode = episodesList.get(position);
        holder.binding.setEpisode(episode);
        holder.binding.executePendingBindings();
        Log.d("Episodes", episode.getTitle());
    }

    @Override
    public int getItemCount() {
        return episodesList != null && episodesList.size() > 0 ? episodesList.size() : 0;
    }

    class EpisodeItemView extends RecyclerView.ViewHolder implements View.OnClickListener{
        RowEpisodeBinding binding;

        public EpisodeItemView(RowEpisodeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.IBPlay.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getAdapterPosition());
        }
    }

    interface ClickListener{
        void onClick(View view, int pos);
    }
}
