package com.example.heba.iqraalytask.ui.audio;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.example.heba.iqraalytask.R;
import com.example.heba.iqraalytask.databinding.ActivityBookAudioBinding;
import com.example.heba.iqraalytask.databinding.BottomSheetEpisodesBinding;
import com.example.heba.iqraalytask.network.model.Book;
import com.example.heba.iqraalytask.network.model.Episode;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;

import java.util.ArrayList;
import java.util.List;

public class BookAudioActivity extends AppCompatActivity {
    ActivityBookAudioBinding binding;
    BookAudioViewModel viewModel;

    SimpleExoPlayer player;
    List<Episode> episodeList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Book book = (Book) getIntent().getSerializableExtra("Book");

        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_audio);
        viewModel = ViewModelProviders.of(this).get(BookAudioViewModel.class);
        binding.setAudioVM(viewModel);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();

        episodeList = book.getEpisodes();

        player = viewModel.returnPlayer(episodeList);
        binding.playerView.setPlayer(player);
        setPlayerListener();

        binding.IBOpenSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEpisodesSheet();
            }
        });
    }

    private void setPlayerListener(){
        player.addListener(new Player.DefaultEventListener() {
            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                super.onTracksChanged(trackGroups, trackSelections);
                int pos = player.getCurrentWindowIndex();
                binding.setEpisode(episodeList.get(pos));
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                super.onPlayerStateChanged(playWhenReady, playbackState);
                viewModel.getBusy().setValue(playbackState == Player.STATE_BUFFERING ? 0 : 8);
            }
        });
    }

    public void openEpisodesSheet(){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        BottomSheetEpisodesBinding sheetBinding = DataBindingUtil.inflate(LayoutInflater.from(this),
                R.layout.bottom_sheet_episodes, null, false);
        bottomSheetDialog.setContentView(sheetBinding.getRoot());

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMarginStart(20);
        params.setMarginEnd(20);
        ((View) sheetBinding.LLContent.getParent()).setBackgroundColor(Color.TRANSPARENT);
        sheetBinding.LLContent.setLayoutParams(params);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        EpisodesAdapter adapter = new EpisodesAdapter(episodeList, new EpisodesAdapter.ClickListener() {
            @Override
            public void onClick(View view, int pos) {
                //Todo: check network connection & add toast for else condition
                binding.setEpisode(episodeList.get(pos));
                player.seekTo(pos, 0);
                player.setPlayWhenReady(true);
                bottomSheetDialog.dismiss();
            }
        });
        sheetBinding.RVEpisodes.setLayoutManager(layoutManager);
        sheetBinding.RVEpisodes.setAdapter(adapter);

        bottomSheetDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        releasePlayer();
        viewModel.releasePlayer();
    }
}
