package com.ds.mo.engine.android;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.ds.mo.engine.framework.Audio;
import com.ds.mo.engine.framework.Music;
import com.ds.mo.engine.framework.Sound;

import java.io.IOException;


/**
 * Most code from book, couldn't be bothered to revert this section.
 * <p>
 * N.B: instead of just initialising a soundpool instance, it checks the devices version
 * and initialises accordingly.
 * <p>
 * Created by Mo on 28/05/2017.
 */
public class AndroidAudio implements Audio {
    AssetManager assetManager;
    SoundPool soundPool;

    public AndroidAudio(Activity activity) {
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        this.assetManager = activity.getAssets();
        //Create Soundpool based on version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            newSoundPool();
        } else {
            //Create old sound pool without attributes (deprecated)
            oldSoundPool();
        }
    }

    @Override
    public Music newMusic(String filename) {
        try {
            AssetFileDescriptor assetFileDescriptor = assetManager.openFd(filename);
            return new AndroidMusic(assetFileDescriptor);
        } catch (IOException e) {
            throw new RuntimeException("Could not load music: " + filename);
        }
    }

    @Override
    public Sound newSound(String filename) {
        try {
            AssetFileDescriptor assetFileDescriptor = assetManager.openFd(filename);
            int soundId = soundPool.load(assetFileDescriptor, 0);
            return new AndroidSound(soundPool, soundId);
        } catch (IOException e) {
            throw new RuntimeException("Could not load sound: " + filename);
        }
    }

    @SuppressWarnings("deprecation")
    private void oldSoundPool() {
//        System.out.println("Loading OLD soundpool..");
        //                      max simultaneous streams, stream type, no effect
        soundPool = new SoundPool(15, AudioManager.STREAM_MUSIC, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void newSoundPool() {
//        System.out.println("Loading NEW soundpool..");
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .setMaxStreams(15)
                .build();
    }
}
