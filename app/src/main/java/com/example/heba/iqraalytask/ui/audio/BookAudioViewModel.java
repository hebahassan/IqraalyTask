package com.example.heba.iqraalytask.ui.audio;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.heba.iqraalytask.network.model.Episode;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

public class BookAudioViewModel extends AndroidViewModel {
    SimpleExoPlayer player;
    DataSource.Factory dataSourceFactory;
    MediaSource mediaSource;
    DefaultExtractorsFactory extractorsFactory;

    private MutableLiveData<SimpleExoPlayer> playerLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> busy;
    private MutableLiveData<String> speedLD;

    float speed = 1;
    private int lastClick = 0;

    public BookAudioViewModel(@NonNull Application application) {
        super(application);
        getSpeedLD().setValue(speed + "X");
    }

    private void initPlayer(){
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(getApplication().getApplicationContext(), trackSelector);
        extractorsFactory = new DefaultExtractorsFactory();
        dataSourceFactory = new DefaultDataSourceFactory(getApplication().getApplicationContext(),
                Util.getUserAgent(getApplication().getApplicationContext(), getApplication().getApplicationInfo().name),
                defaultBandwidthMeter);
    }

    public SimpleExoPlayer returnPlayer(List<Episode> episodeList){
        initPlayer();

        if(episodeList != null && !episodeList.isEmpty()){
            MediaSource[] mediaSourcesArray = new MediaSource[episodeList.size()];

            for (int i = 0; i < episodeList.size(); i++) {
                mediaSourcesArray[i] = new ExtractorMediaSource.Factory(dataSourceFactory)
                        .setExtractorsFactory(extractorsFactory)
                        .createMediaSource(Uri.parse(episodeList.get(i).getFile()));
            }

            mediaSource = mediaSourcesArray.length == 1 ? mediaSourcesArray[0] : new ConcatenatingMediaSource(mediaSourcesArray);

            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
        }

        return player;
    }

    public void releasePlayer(){
        player.release();
        player = null;
    }

    public MutableLiveData<Integer> getBusy() {
        if(busy == null)
            busy = new MutableLiveData<>();
        return busy;
    }

    public MutableLiveData<String> getSpeedLD() {
        if(speedLD == null)
            speedLD = new MutableLiveData<>();
        return speedLD;
    }

    public void changeAudioSpeed(){
        if(lastClick < 4){
            speed += 0.25;
            lastClick++;
        }
        else {
            speed -= 0.25;
            lastClick++;

            if(lastClick == 8){
                lastClick = 0;
            }
        }

        PlaybackParameters param = new PlaybackParameters(speed);
        player.setPlaybackParameters(param);
        getSpeedLD().setValue(speed + "X");
    }
}
