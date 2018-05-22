package com.coriunder.audio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.coriunder.R;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class CustomPlayer extends FrameLayout implements OnClickListener {
    public static final String HTTP = "http://";
    public static final String HTTPS = "https://";

    private enum MediaPlayerState {
        Idle, Preparing, Prepared, None
    }

    private enum PlayingState {
        Playing, Paused, Stoped, None
    }

    public interface OnMediaPlayerActionListener {
        /**
         * Called when the new MediaContent is prepared for been played
         *
         * @param newMedia - new MediaContent
         * @param pos      - position of new MediaContent in playlist
         */
        public void onMediaChanged(MediaContent newMedia, int pos);

        public void onTrackChanged(int oldPos, int newPos);

        /**
         * Called when the end of the playlist is reached
         *
         * @param lastPos - Position of last played item
         */
        public void onEndOfTheListReached(int lastPos);

        public void onStreamError(MediaContent current);

        public void onPlayerError(MediaContent current);

        public void onForceDownload(MediaContent current);
    }

    private static final String TAG = "MahalaPlayer";

    private final String undefinedTime = "--:--";

    private LinearLayout rootView;

    private List<MediaContent> playList = new ArrayList<MediaContent>();

    private int currentTrack = 0;

    private MediaPlayer mPlayer;

    private View mPlayerMessage;
    private TextView mTimePassed;
    private TextView mAvarageTime;

    private SeekBar mTimeLine;

    private MediaPlayerState currentState = MediaPlayerState.Idle;
    private PlayingState currentPlayState = PlayingState.None;

    private ImageView playButton;
    private ImageView prevButton;
    private ImageView nextButton;
    private ImageView downloadButton;

    private Timer playerTimer;

    private SurfaceView videoView;
    private ImageView imagePreview;

    private List<View> touchableControls = new ArrayList<View>();

    private OnMediaPlayerActionListener actionListener;

    private Handler handler;

    public CustomPlayer(Context context) {
        super(context);

        initLayout();
    }

    public CustomPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);

        initLayout();
    }

    public CustomPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initLayout();
    }

    private void initLayout() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_custom_player, this);

        rootView = (LinearLayout) getChildAt(0);

        videoView = (SurfaceView) rootView.findViewById(R.id.player_videoview);
        imagePreview = (ImageView) rootView.findViewById(R.id.player_preview);

        mPlayerMessage = rootView.findViewById(R.id.player_mess);

        // Setting up controls
        playButton = (ImageView) rootView.findViewById(R.id.player_playbutton);
        prevButton = (ImageView) rootView.findViewById(R.id.player_prevbutton);
        nextButton = (ImageView) rootView.findViewById(R.id.player_nextbutton);
        downloadButton = (ImageView) rootView.findViewById(R.id.downloadButton);
        playButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        downloadButton.setOnClickListener(this);

        /*
         * Setting other interface
         */
        mTimeLine = (SeekBar) rootView.findViewById(R.id.player_seekbar);

        mAvarageTime = (TextView) rootView.findViewById(R.id.player_alltime);
        mTimePassed = (TextView) rootView.findViewById(R.id.player_pasttime);

        handler = new Handler();

        ProgressBar pb = (ProgressBar) rootView.findViewById(R.id.player_buffering_progress);
        pb.getIndeterminateDrawable().setColorFilter(
                Color.parseColor("#f73c3c"), PorterDuff.Mode.MULTIPLY);

        touchableControls.add(playButton);
        touchableControls.add(nextButton);
        touchableControls.add(prevButton);
        touchableControls.add(mTimeLine);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.player_playbutton:
                prepare(true);
                if (currentState == MediaPlayerState.Prepared) {
                    toggle();
                }
                break;
            case R.id.player_prevbutton:
                previousTrack();
                break;
            case R.id.player_nextbutton:
                nextTrack();
                break;
            case R.id.downloadButton:
                onStopCalled();
                actionListener.onForceDownload(getCurrent());
                break;
        }
    }

    private void prepare(final boolean playOnPrepared) {
        if (currentState == MediaPlayerState.Idle) {
            currentState = MediaPlayerState.Preparing;

            blockUI(false);
            try {
                if (mPlayer == null) {
                    mPlayer = new MediaPlayer();
                }
                mPlayer.reset();
                mPlayer.setDataSource(getCurrent().getUri());
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mPlayer.prepareAsync();
                setAdditionalListeners();
                mPlayer.setOnCompletionListener(new OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        nextTrack();
                    }
                });
                mPlayer.setOnPreparedListener(new OnPreparedListener() {

                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        currentState = MediaPlayerState.Prepared;

                        int duaration = mPlayer.getDuration();
                        // Set the duration
                        mAvarageTime.setText(formatTime(duaration));

                        mTimeLine.setMax(duaration);
                        mTimeLine.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                mPlayer.seekTo(seekBar.getProgress());
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {
                            }

                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                if (fromUser) {
                                    mTimePassed.setText(formatTime(progress));
                                }
                            }
                        });
                        mPlayer.setOnSeekCompleteListener(new OnSeekCompleteListener() {

                            @Override
                            public void onSeekComplete(MediaPlayer mp) {
                                mTimePassed.setText(formatTime(mp.getCurrentPosition()));
                                mTimeLine.setProgress(mp.getCurrentPosition());
                            }
                        });

                        if (playOnPrepared) {
                            play();
                        }

                        releaseUI();
                    }
                });
            } catch (Exception e) {
                Log.d(TAG, "Can't prepare player: "+e);
                releaseUI();
            }
        }
    }

    private void setAdditionalListeners() {
        mPlayer.setOnErrorListener(new OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                        Log.d(TAG, "Unspecified media player error");
                        break;

                    case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                        Log.d(TAG, "Media server died");
                        break;
                }

                switch (extra) {
                    case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                        Log.d(TAG, "Some operation takes too long to complete, usually more than 3-5 seconds");
                        break;
                }

                releaseUI();

                Toast.makeText(getContext(), "En error has occured while buffering content", Toast.LENGTH_SHORT).show();

                if (mPlayer != null) {
                    if (mPlayer.isPlaying()) {
                        mPlayer.stop();
                    }

                    mPlayer.release();
                }

                return true;
            }
        });

        mPlayer.setOnInfoListener(new OnInfoListener() {

            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                        Log.d(TAG, "Unable to seek");
                        break;

                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        blockUI(true);
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        releaseUI();
                }

                return true;
            }
        });
    }

    @SuppressLint("NewApi")
    private void getMediaInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = getCurrent().getUri();

                MediaMetadataRetriever mmr = new MediaMetadataRetriever();

                try {
                    mmr.setDataSource(url);
                } catch (Exception e) {
                    Log.d(TAG, "Player exception: "+e);
                }
                if (url.startsWith(HTTP) || url.startsWith(HTTPS)) {
                    // In case of streaming
                    if (Build.VERSION.SDK_INT >= 14) {
                        try {
                            mmr.setDataSource(url, new HashMap<String, String>());
                        } catch (Exception e) {
                            Log.d(TAG, "Can't stream media: "+e);
                            actionListener.onStreamError(getCurrent());
                        }
                    }
                }

                // Get album cover
                final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                Bitmap tmpBitmap = ImageUtils.getCachedImage(url, false);
                if (tmpBitmap == null) {
                    tmpBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.preview_mp3);

                    int width = (int)dpToPx(130);
                    int height = (int)dpToPx(100);
                    layoutParams.width = width;
                    layoutParams.height = height;

                    byte[] embeddedPicture = mmr.getEmbeddedPicture();
                    if (embeddedPicture != null) {
                        InputStream inputStream = new ByteArrayInputStream(embeddedPicture);
                        tmpBitmap = BitmapFactory.decodeStream(inputStream);
                        if (tmpBitmap != null) {
                            ImageUtils.addBitmapToCache(url, tmpBitmap);

                            layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                            layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
                        }
                    }
                }

                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                final Bitmap cover = Bitmap.createBitmap(tmpBitmap);

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        imagePreview.setLayoutParams(layoutParams);

                        imagePreview.setImageBitmap(cover);
                    }
                });

                mmr.release();
            }
        }).start();
    }

    public void toggle() {
        switch (currentPlayState) {
            case Playing:
                pause();
                break;

            case Paused:
                play();
                break;
            case Stoped:
                prepare(true);
                break;
        }
    }

    public void play() {
        playButton.setImageResource(R.drawable.player_pausebutton_selector);

        mPlayer.start();

        createTimer();

        currentPlayState = PlayingState.Playing;
    }

    public void pause() {
        playButton.setImageResource(R.drawable.player_playbutton_selector);

        mPlayer.pause();

        killTimer();

        currentPlayState = PlayingState.Paused;
    }

    public void stop() {
        playButton.setImageResource(R.drawable.player_playbutton_selector);

        mTimeLine.setProgress(0);
        mTimePassed.setText(undefinedTime);

        killTimer();

        if (mPlayer != null) {
            try {
                mPlayer.stop();
            } catch (Exception e) {
                Log.d(TAG, "Can't stop player: "+e);
            }
        }
        currentState = MediaPlayerState.Idle;
        currentPlayState = PlayingState.Stoped;
    }

    public void nextTrack() {
        clearUI();

        stop();

        if (currentTrack == playList.size() - 1) {
            actionListener.onEndOfTheListReached(getCurrent().getID());
        } else {
            currentTrack++;

            setCurrent(currentTrack);

            prepare(true);
        }
    }

    public void previousTrack() {
        clearUI();

        stop();

        if (currentTrack == 0) {
            actionListener.onEndOfTheListReached(getCurrent().getID());
        } else {
            currentTrack--;

            setCurrent(currentTrack);

            prepare(true);
        }
    }

    public void repeatPlayList() {
        currentTrack = 0;
        setCurrent(currentTrack);
        prepare(true);
    }

    public int getPlayListSize() {
        return playList.size();
    }

    public MediaContent getFirstMedia() {
        return playList.get(0);
    }

    /*
     * Returns the current MediaContent
     */
    public MediaContent getCurrent() {
        return playList.get(currentTrack);
    }

    public void setCurrentMedia(MediaContent media) {
        setCurrentMedia(media, -1);
    }

    public void setCurrentMedia(MediaContent media, int pos) {
        if (media != null) {
            if (playList != null && !playList.isEmpty()) {
                if (!playList.contains(media)) {
                    if (pos >= 0) {
                        playList.add(pos, media);
                    } else {
                        playList.add(media);
                    }
                }
            } else {
                playList = new ArrayList<MediaContent>();
                playList.add(media);
            }
            setCurrent(playList.indexOf(media));
            prepare(true);
        }
    }

    public void setCurrent(int pos) {
        if (pos < playList.size()) {
            clearUI();

            getMediaInfo();

            currentTrack = pos;

            if (mPlayer != null && currentState == MediaPlayerState.Prepared) {
                stop();
            }

            if (actionListener != null) {
                actionListener.onMediaChanged(getCurrent(), pos);
            }
        }
    }

    public void setOnMediaPlayerActionListener(OnMediaPlayerActionListener listener) {
        this.actionListener = listener;
    }

    private void blockUI(boolean justBuffering) {
        mPlayerMessage.setVisibility(View.VISIBLE);

        if (!justBuffering) {
            for (View view : touchableControls) {
                view.setEnabled(false);
            }
        }
    }

    private void releaseUI() {
        mPlayerMessage.setVisibility(View.INVISIBLE);

        for (View view : touchableControls) {
            view.setEnabled(true);
        }
    }

    private void clearUI() {
        // Clear the cover
        int width = (int)dpToPx(130);
        int height = (int)dpToPx(100);
        final android.widget.RelativeLayout.LayoutParams layoutParams = new android.widget.RelativeLayout.LayoutParams(
                width, height);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        imagePreview.setLayoutParams(layoutParams);

        imagePreview.setImageResource(R.drawable.preview_mp3);

        // Clear times
        mAvarageTime.setText(undefinedTime);
        mTimePassed.setText(undefinedTime);
        mTimeLine.setProgress(0);
    }

    private String formatTime(int milis) {

        int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(milis);
        int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(milis);
        seconds -= minutes * 60;
        int[] timeValues = new int[]{minutes, seconds};

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < timeValues.length; i++) {

            if (timeValues[i] <= 0) {
                builder.append("00");
            } else {
                if (timeValues[i] > 9) {
                    builder.append(timeValues[i]);
                } else {
                    builder.append(0);
                    builder.append(timeValues[i]);
                }
            }
            if (i < timeValues.length - 1) {
                builder.append(":");
            }
        }

        return builder.toString();
    }

    private void killTimer() {
        if (playerTimer != null) {
            playerTimer.cancel();
            playerTimer.purge();
            playerTimer = null;
        }
    }

    private void createTimer() {
        killTimer();
        playerTimer = new Timer();
        playerTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                final int progress = mPlayer.getCurrentPosition();

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        mTimePassed.setText(formatTime(progress));
                        mTimeLine.setProgress(progress);
                    }
                });
            }
        }, 0, 500);
    }

    public void onStopCalled() {
        try {
            stop();
            if (mPlayer != null) {
                mPlayer.release();
            }
            currentPlayState = PlayingState.None;
        } catch (Exception e) {
            Log.d(TAG, "Can't stop player:"+e);
        }
    }

    public void onPauseCalled() {
        try {
            if (mPlayer != null && mPlayer.isPlaying()) {
                mPlayer.pause();
            }
        } catch (Exception e) {
            actionListener.onPlayerError(getCurrent());
        }
    }

    public void onResumeCalled() {
    }

    public static float dpToPx(float dp) {
        return dp * Resources.getSystem().getDisplayMetrics().density;
    }
}
