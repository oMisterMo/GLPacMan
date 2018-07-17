package com.ds.mo.engine.framework;

/**
 * Sound interface, All game sounds comes from the class which implements this
 * Sounds represent 1 - 5 sec clips
 * <p/>
 * Created by Mo on 28/09/2016.
 */
public interface Sound {
    /**
     * Play sound effect
     *
     * @param volume 0.0 (min) - 1.0 (max)
     */
    void play(float volume);

    /**
     * Releases sound instance when application terminates
     */
    void dispose();
}
