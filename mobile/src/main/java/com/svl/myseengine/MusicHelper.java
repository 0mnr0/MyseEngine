package com.svl.myseengine;


import android.content.Context;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;


public class MusicHelper {
    public static boolean isMusicPlaying(Context context) {
        MediaSessionManager mediaSessionManager = (MediaSessionManager) context.getSystemService(Context.MEDIA_SESSION_SERVICE);
        if (mediaSessionManager != null) {
            for (MediaController mediaController : mediaSessionManager.getActiveSessions(null)) {
                String packageName = mediaController.getPackageName();
                if (!packageName.equals(context.getPackageName())) {
                    return true;
                }
            }
        }
        return false;
    }
}