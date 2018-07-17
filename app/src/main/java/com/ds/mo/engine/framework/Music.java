package com.ds.mo.engine.framework;

/**
 * Music interface
 * <p/>
 * Created by Mo on 28/09/2016.
 */
public interface Music {

    /**
     * Streams music
     */
    void play();

    /**
     * Stops stream
     */
    void stop();

    /**
     * Pauses stream
     */
    void pause();

    /**
     * Loops music stream
     *
     * @param b true if looping
     */
    void setLooping(boolean b);

    /**
     * Sets the volume of the music stream
     *
     * @param volume range from 0.0 - 1.0 (max)
     */
    void setVolume(float volume);

    /**
     * Returns true is music is playing
     *
     * @return true if playing
     */
    boolean isPlaying();

    /**
     * Checks if music is looping
     *
     * @return true if looping
     */
    boolean isLooping();

    /**
     * Check to see if music has stopped
     *
     * @return true if stopped
     */
    boolean isStopped();

    /**
     * Clears music instance
     */
    void dispose();
}
