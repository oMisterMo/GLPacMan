package com.ds.mo.engine.android;

import android.media.SoundPool;

import com.ds.mo.engine.framework.Sound;

/**
 * Lets the user playback a single sound instance
 * Created by Mo on 29/09/2016.
 */
public class AndroidSound implements Sound {
    /* A SoundPool is a collection of samples that can be loaded into memory
       from a resource inside the APK or from a file in the file system. */
    SoundPool soundPool;
    int id;

    /**
     * Create new instance from the given values
     *
     * @param soundPool reference to soundpool holding all sound
     * @param id        id of the sound to play
     */
    public AndroidSound(SoundPool soundPool, int id) {
        this.soundPool = soundPool;
        this.id = id;
    }

    /**
     * Sets the volume of the sound object
     *
     * @param volume between 0.0 - 1.0
     */
    @Override
    public void play(float volume) {
        soundPool.play(id, volume, volume, 0, 0, 1);
    }

    /**
     * Release sound resources
     */
    @Override
    public void dispose() {
        soundPool.unload(id);
    }
}
