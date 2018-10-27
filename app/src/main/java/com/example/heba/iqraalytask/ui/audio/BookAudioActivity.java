package com.example.heba.iqraalytask.ui.audio;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.QuickContactBadge;
import android.widget.Toast;

import com.example.heba.iqraalytask.R;
import com.example.heba.iqraalytask.databinding.ActivityBookAudioBinding;
import com.example.heba.iqraalytask.generated.callback.OnClickListener;
import com.example.heba.iqraalytask.network.model.Book;
import com.example.heba.iqraalytask.network.model.Episode;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

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
    }

    private void setPlayerListener(){
        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                int pos = player.getCurrentWindowIndex();
                binding.setEpisode(episodeList.get(pos));
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                viewModel.getBusy().setValue(playbackState == Player.STATE_BUFFERING ? 0 : 8);
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        releasePlayer();
        viewModel.releasePlayer();
    }
}
