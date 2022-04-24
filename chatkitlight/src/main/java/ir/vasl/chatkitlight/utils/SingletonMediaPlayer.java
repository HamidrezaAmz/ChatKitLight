package ir.vasl.chatkitlight.utils;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

import ir.vasl.chatkitlight.ui.callback.SingletonMediaPlayerCallback;

public class SingletonMediaPlayer
        implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    private static final String TAG = "SingletonMediaPlayer";

    private static volatile SingletonMediaPlayer instance = null;

    private MediaPlayer mediaPlayer;

    private SingletonMediaPlayerCallback singletonMediaPlayerCallback;

    private SingletonMediaPlayer() {
    }

    public static SingletonMediaPlayer getInstance() {
        if (instance == null) {
            synchronized (SingletonMediaPlayer.class) {
                if (instance == null) {
                    instance = new SingletonMediaPlayer();
                }
            }
        }
        return instance;
    }

    public void setSingletonMediaPlayerCallback(SingletonMediaPlayerCallback singletonMediaPlayerCallback) {
        this.singletonMediaPlayerCallback = singletonMediaPlayerCallback;
    }

    /**
     * @param fileName if sound name is "sound.mp3" then pass fileName as "sound" only.
     */
    public synchronized void playSound(String fileName) {

//        if(instance.mediaPlayer != null)
//            instance.mediaPlayer.stop();
        stopSound();
        instance.mediaPlayer = new MediaPlayer();
        try {
            instance.mediaPlayer.setDataSource(fileName);
            instance.mediaPlayer.prepare();
            instance.mediaPlayer.setVolume(100f, 100f);
            instance.mediaPlayer.setLooping(false);
            instance.mediaPlayer.start();
            instance.mediaPlayer.setOnCompletionListener(this);
            instance.mediaPlayer.setOnErrorListener(this);
            instance.mediaPlayer.setOnPreparedListener(this);
            instance.mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                    Log.e(TAG, "onBufferingUpdate: " + i);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void stopSound() {
        try {
            if (instance.mediaPlayer != null) {
                instance.mediaPlayer.stop();
                instance.mediaPlayer.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void pauseSound() {
        try {
            if (instance.mediaPlayer != null) {
                instance.mediaPlayer.pause();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void restartSound() {
        try {
            if (instance.mediaPlayer != null) {
                instance.mediaPlayer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void playRepeatedSound(String fileName) {
        try {
            if (instance.mediaPlayer == null) {
                instance.mediaPlayer = new MediaPlayer();
            } else {
                instance.mediaPlayer.reset();
            }
            try {
                instance.mediaPlayer.setDataSource(fileName);
                instance.mediaPlayer.prepare();
                instance.mediaPlayer.setVolume(100f, 100f);
                instance.mediaPlayer.setLooping(true);
                instance.mediaPlayer.start();
                instance.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (mp != null) {
                            mp.reset();
                            mp = null;
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized MediaPlayer getMediaPlayer() {
        try {
            if (instance.mediaPlayer == null) {
                instance.mediaPlayer = new MediaPlayer();
            }
            return instance.mediaPlayer;
        } catch (Exception e) {
            e.printStackTrace();
            return new MediaPlayer();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        try {
            if (instance.mediaPlayer != null) {
                instance.mediaPlayer.reset();
                instance.mediaPlayer = null;
            }
            if (singletonMediaPlayerCallback != null)
                singletonMediaPlayerCallback.onCompletion();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        try {
            if (singletonMediaPlayerCallback != null)
                singletonMediaPlayerCallback.onError();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        try {
            if (singletonMediaPlayerCallback != null)
                singletonMediaPlayerCallback.onPrepared();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
