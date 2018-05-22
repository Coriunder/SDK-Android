package com.coriunder.audio;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.coriunder.R;
import com.coriunder.base.Coriunder;
import com.coriunder.base.common.callbacks.ReceivedBasicCallback;
import com.coriunder.shop.ShopSDKDownloads;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class PlayerActivity extends Activity {
    private CustomPlayer mPlayer;
    public ArrayList<HashMap<String, String>> downloads;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_player);

        mPlayer = (CustomPlayer) findViewById(R.id.mahala_player);
        addMediaPlayerAction();
        init();
    }

    private void addMediaPlayerAction() {
        mPlayer.setOnMediaPlayerActionListener(new CustomPlayer.OnMediaPlayerActionListener() {
            @Override
            public void onTrackChanged(int oldPos, int newPos) { }
            @Override
            public void onMediaChanged(MediaContent newMedia, int pos) { }
            @Override
            public void onEndOfTheListReached(int lastPos) {
                getNextTrack(lastPos);
            }
            @Override
            public void onStreamError(final MediaContent current) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PlayerActivity.this);
                        builder.setMessage("Sorry, the song can\\'t be played right now. Do you want to download it? It may take several minutes.")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        downloadFile(current.getID());
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                    }
                                });
                        builder.create();
                        builder.show();
                    }
                });
            }
            @Override
            public void onPlayerError(final MediaContent current) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PlayerActivity.this);
                        builder.setMessage("Sorry, the song can\\'t be played.")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                    }
                                });
                        builder.create();
                        builder.show();
                    }
                });
            }
            @Override
            public void onForceDownload(final MediaContent current) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PlayerActivity.this, "File is loading", Toast.LENGTH_LONG).show();
                    }
                });
                downloadFile(current.getID());
            }
        });
    }

    public void getNextTrack(int lastPos) {
        if (mPlayer.getPlayListSize() != downloads.size()) {
            if (lastPos < downloads.size() - 1) {
                downloadFile(lastPos + 1);

            } else {
                int firstIDInPlaylist = mPlayer.getFirstMedia().getID();
                downloadFile(firstIDInPlaylist - 1);
            }
        } else {
            mPlayer.repeatPlayList();
        }
    }

    private void init() {
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            final int selected = extras.getInt("itemId");
            downloads = (ArrayList<HashMap<String, String>>)extras.getSerializable("data");

            File mediaDir = FileUtil.getMediaDir();
            String fileName = downloads.get(selected).get(AudioConstants.KEY_PRODUCT_NAME) + ".mp3";
            File mediaFile = new File(mediaDir, fileName);
            if (mediaFile.exists()) downloadFile(selected);
            else streamFile(selected);
        }
    }


    protected static final Handler handler = new Handler();
    private void streamFile(final int selected) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                String uri = getDownloadFileUrl(downloads.get(selected).get(AudioConstants.KEY_PRODUCT_ID));
                MediaContent content = new MediaContent(selected, downloads.get(selected).get(AudioConstants.KEY_PRODUCT_NAME), uri);
                mPlayer.setCurrentMedia(content);
            }
        });
    }

    public static String getDownloadFileUrl(String id) {
        String url = Coriunder.getServiceUrl() + ShopSDKDownloads.SERVICE_URL_PART + "/" + "Download";

        if (!url.endsWith("?")) {
            url += "?";
        }

        List<BasicNameValuePair> params = new LinkedList<>();
/*
        params.add(new BasicNameValuePair(CartInfo.APPLICATION_TOKEN, Constants.Token.APPLICATION_TOKEN_VALUE));
        params.add(new BasicNameValuePair(CartInfo.WALLETCREDENTIALS, App.getSession().getCredentialsToken()));
*/
        params.add(new BasicNameValuePair("itemId", id));
        params.add(new BasicNameValuePair("asPlainData", Boolean.toString(true)));

        String paramString = URLEncodedUtils.format(params, "utf-8");

        url += paramString;

        return url;
    }

    private void downloadFile(final int pos) {
        downloadFile(pos, false);
    }

    private void downloadFile(final int position, final boolean needInsert) {
        new ShopSDKDownloads().download(Long.valueOf(downloads.get(position).get(AudioConstants.KEY_PRODUCT_ID)), true, new ReceivedBasicCallback() {
            @Override
            public void onResultReceived(boolean isSuccess, String message) {
                if (isSuccess) {
                    /*final HttpResponse response = (HttpResponse) result.data;
                    FileUtil.writeFile(PlayerActivity.this, downloads.get(position).get(AudioConstants.KEY_PRODUCT_NAME),
                            response, new FileUtil.OnFileReadyListener() {

                        @Override
                        public void onFileReady(final File file) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (file != null) {
                                        MediaContent content = new MediaContent(position,
                                                downloads.get(position).get(AudioConstants.KEY_PRODUCT_NAME), file.getPath());

                                        if (needInsert) mPlayer.setCurrentMedia(content, position);
                                        else mPlayer.setCurrentMedia(content);

                                    } else {
                                        mPlayer.repeatPlayList();
                                    }
                                }
                            });
                        }
                    });*/

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PlayerActivity.this);
                    builder.setMessage(message)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            });
                    builder.create();
                    builder.show();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        if (mPlayer != null) mPlayer.onStopCalled();
        super.onStop();
    }

    @Override
    protected void onPause() {
        if (mPlayer != null) mPlayer.onPauseCalled();
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mPlayer != null) mPlayer.onResumeCalled();
        super.onResume();
    }
}