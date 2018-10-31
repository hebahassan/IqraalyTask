package com.example.heba.iqraalytask.ui.audio;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.ConnectivityManager;
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
import com.example.heba.iqraalytask.controller.ConnectivityChangeReceiver;
import com.example.heba.iqraalytask.databinding.ActivityBookAudioBinding;
import com.example.heba.iqraalytask.databinding.BottomSheetEpisodesBinding;
import com.example.heba.iqraalytask.interfaces.ConnectivityInterface;
import com.example.heba.iqraalytask.network.model.Book;
import com.example.heba.iqraalytask.network.model.Episode;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;

import java.util.ArrayList;
import java.util.List;

public class BookAudioActivity extends AppCompatActivity implements ConnectivityInterface {
    ActivityBookAudioBinding binding;
    BookAudioViewModel viewModel;

    SimpleExoPlayer player;
    List<Episode> episodeList = new ArrayList<>();
    Episode selectedEpisode;

    int selectedPos = 0;
    long posMs = 0;
    boolean isError = false;

    ConnectivityChangeReceiver connectivityChangeReceiver;
    ConnectivityInterface connectivityInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_audio);
        viewModel = ViewModelProviders.of(this).get(BookAudioViewModel.class);
        binding.setAudioVM(viewModel);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();

        Book book = (Book) getIntent().getSerializableExtra("Book");
        episodeList = book.getEpisodes();
        selectedEpisode = episodeList.get(0);

        setReceiver();
        onStartActivity();
        setPlayerListener();

        binding.IBOpenSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEpisodesSheet();
            }
        });
        binding.IBShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShare();
            }
        });
    }

    private void onStartActivity(){
        player = viewModel.returnPlayer(episodeList);
        binding.playerView.setPlayer(player);
        binding.setEpisode(selectedEpisode);
        selectedEpisode.setPlaying(true);
    }

    private void setReceiver(){
        connectivityChangeReceiver = new ConnectivityChangeReceiver();
        connectivityInterface = this;
        connectivityChangeReceiver.setNetworkListener(connectivityInterface);
        registerReceiver(connectivityChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void setPlayerListener(){
        player.addListener(new Player.DefaultEventListener() {
            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                super.onTracksChanged(trackGroups, trackSelections);
                selectedEpisode.setPlaying(false);
                selectedPos = player.getCurrentWindowIndex();
                selectedEpisode = episodeList.get(selectedPos);
                selectedEpisode.setPlaying(true);
                binding.setEpisode(selectedEpisode);
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                super.onPlayerStateChanged(playWhenReady, playbackState);
                viewModel.getBusy().setValue(playbackState == Player.STATE_BUFFERING ? 0 : 8);

                selectedEpisode.setPlaying(playWhenReady);
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                super.onPlayerError(error);
                isError = true;
                player.setPlayWhenReady(false);
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
                if(binding.getEpisode().getId().equals(episodeList.get(pos).getId())){
                    if(!player.getPlayWhenReady()){
                        player.setPlayWhenReady(true);
                        episodeList.get(pos).setPlaying(true);
                    }else {
                        player.setPlayWhenReady(false);
                        episodeList.get(pos).setPlaying(false);
                    }
                }
                else {
                    selectedEpisode.setPlaying(false);
                    binding.setEpisode(episodeList.get(pos));
                    player.seekTo(pos, 0);
                    player.setPlayWhenReady(true);
                    episodeList.get(pos).setPlaying(true);
                }

                bottomSheetDialog.dismiss();
            }
        });
        sheetBinding.RVEpisodes.setLayoutManager(layoutManager);
        sheetBinding.RVEpisodes.setAdapter(adapter);

        bottomSheetDialog.show();
    }

    private void onShare(){
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, "Listen to " + binding.getEpisode().getTitle() + " "
                + binding.getEpisode().getFile());
        startActivity(Intent.createChooser(share, "Share File"));
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(connectivityChangeReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.releasePlayer();
        unregisterNetworkChanges();
    }

    @Override
    public void onNetworkConnection() {
        if(isError){
            posMs = player.getContentPosition();
            selectedPos = player.getCurrentWindowIndex();
            selectedEpisode = episodeList.get(selectedPos);

            viewModel.releasePlayer();
            onStartActivity();
            player.seekTo(selectedPos, posMs);
            setPlayerListener();
        }
    }
}
