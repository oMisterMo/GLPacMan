package com.ds.mo.engine.android;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import com.ds.mo.engine.framework.Music;

import java.io.IOException;


/**
 * Game Music class will handle the streaming of the background music during the game
 * <p/>
 * Created by Mo on 29/09/2016.
 */
public class AndroidMusic implements Music, OnCompletionListener {

    MediaPlayer mediaPlayer;
    boolean isPrepared = false;

    /**
     * Creates a new Music instance from the given value
     *
     * @param assetFileDescriptor provides your own opened FileDescriptor that can be used to
     *                            read the data
     */
    public AndroidMusic(AssetFileDescriptor assetFileDescriptor) {
        mediaPlayer = new MediaPlayer();

        //    It is the caller's responsibility
        //    to close the file descriptor. It is safe to do so as soon as this call returns.
        try {
            mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(),
                    assetFileDescriptor.getLength());
            mediaPlayer.prepare();
            isPrepared = true;
            mediaPlayer.setOnCompletionListener(this);
        } catch (Exception e) {
            throw new RuntimeException("Couldn't load music");
        } finally {
            try {
                assetFileDescriptor.close();
            } catch (IOException e) {
                System.out.println("Can not close asset file descriptor");
                e.printStackTrace();
            }
        }
    }

    /**
     * Plays the loaded song if it has been prepared
     */
    @Override
    public void play() {
        if (mediaPlayer.isPlaying()) {
            //do nothing if media is already playing
            return;
        }
        try {
            synchronized (this) {
                if (!isPrepared) {
                    mediaPlayer.prepare();
                }
                mediaPlayer.start();
            }
        } catch (IllegalStateException e) {
//            System.out.println("Trouble playing : error code 101");
            e.printStackTrace();
        } catch (IOException e) {
//            System.out.println("Trouble playing : error code 202");
            e.printStackTrace();
        }
    }

    /**
     * Stops the current song playing
     */
    @Override
    public void stop() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            synchronized (this) {
                isPrepared = false;
            }
        }
    }

    /**
     * Pauses the current song playing
     */
    @Override
    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    /**
     * Loops the song indefinitely
     *
     * @param b true if should loop
     */
    @Override
    public void setLooping(boolean b) {
        mediaPlayer.setLooping(b);
    }

    /**
     * Sets the volume of the media mo
     *
     * @param volume left/right channel between 0.0f -> 1.0f
     */
    @Override
    public void setVolume(float volume) {
        mediaPlayer.setVolume(volume, volume);
    }

    /**
     * Checks whether the media mo is currently playing
     *
     * @return true if playing
     */
    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    /**
     * Checks if media mo is looping
     *
     * @return true if looping
     */
    @Override
    public boolean isLooping() {
        return mediaPlayer.isLooping();
    }

    /**
     * The isPrepared flag indicates if the MediaPlayer is stopped.
     * This is something MediaPlayer.isPlaying() does not necessarily
     * tell us since it returns false if the MediaPlayer is paused but not stopped.
     *
     * @return true if media mo has been stopped
     */
    @Override
    public boolean isStopped() {
        return !isPrepared;
    }

    /**
     * Stops media mo and releases memory
     */
    @Override
    public void dispose() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
     * @param mediaPlayer
     */
    public void onCompletion(MediaPlayer mediaPlayer) {
        synchronized (this) {
            isPrepared = false;
        }
    }
}
