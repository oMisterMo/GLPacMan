package com.ds.mo.engine.framework;

/**
 * Audio interface, gets new music or sound ference
 * <p/>
 * Created by Mo on 28/09/2016.
 */
public interface Audio {

    /**
     * Gets new music instance
     *
     * @param filename name of file in assets folder
     * @return new music
     */
    Music newMusic(String filename);

    /**
     * Gets new Sound instance
     *
     * @param filename name of file in assets folder
     * @return new sound
     */
    Sound newSound(String filename);

}
