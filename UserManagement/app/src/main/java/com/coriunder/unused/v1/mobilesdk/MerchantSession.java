package com.coriunder.unused.v1.mobilesdk;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.coriunder.unused.v1.basesdk.callbacks.ReceivedBasicCallback;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 1 on 25.02.2016.
 */
public class MerchantSession {
    private static MerchantSession instance;
    private String credentialsToken = null;
    private int keepAliveIntervalInMinutes = 11;
    private Timer timer = null;
    private ArrayList<SessionListener> sessionListeners = new ArrayList<>();

    public static MerchantSession getInstance() {
        if (instance == null) instance = new MerchantSession();
        return instance;
    }

    public MerchantSession() {
    }

    public String getCredentialsToken() {
        return credentialsToken;
    }

    public void start(final Context context, String credentialsToken) {
        if (TextUtils.isEmpty(credentialsToken)) {
            Log.d("SdkUserSession", "Session not started. Received empty credentials token");
            return;
        }

        this.credentialsToken = credentialsToken;
        //keep alive
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                MobileManagementSDK.keepAliveSession(context, new ReceivedBasicCallback() {
                    @Override
                    public void onResultReceived(boolean isSuccess, String message) {
                        if (!isSuccess) stop();
                    }
                });
            }
        }, keepAliveIntervalInMinutes * 60000, keepAliveIntervalInMinutes * 60000);
    }

    public void stop() {
        resetTimer();
        fireSessionExpired();
        this.credentialsToken = null;
        if (sessionListeners != null) sessionListeners.clear();
        Log.d("SdkUserSession", "Session stopped");
    }

    private void resetTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    public synchronized void addSessionExpiredListener(SessionListener listener) {
        sessionListeners.add(listener);
    }

    public synchronized void removeSessionExpiredListener(SessionListener listener) {
        sessionListeners.remove(listener);
    }

    private synchronized void fireSessionExpired() {
        if (sessionListeners == null) return;
        for (SessionListener listener : sessionListeners) {
            listener.onSessionExpired();
        }
    }

    public interface SessionListener {
        void onSessionExpired();
    }
}