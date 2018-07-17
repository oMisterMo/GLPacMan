package com.ds.mo.engine.framework;

/**
 * Game interface which ties together all the components
 * <p/>
 * Created by Mo on 26/05/2017.
 */
public interface Game {
    /**
     * Get the Input handler
     *
     * @return input
     */
    Input getInput();

    /**
     * Get the file handler
     *
     * @return fileIO
     */
    FileIO getFileIO();

    /**
     * Get a graphics instance
     *
     * @return graphics
     */
    Graphics getGraphics();

    /**
     * Get the sound handler
     *
     * @return audio
     */
    Audio getAudio();

    /**
     * Set the current screen
     *
     * @param screen game screen
     */
    void setScreen(Screen screen);

    /**
     * Get the current screen
     *
     * @return game screen
     */
    Screen getCurrentScreen();

    /**
     * Get the start screen
     *
     * This is the very first screen that will be updated and rendered
     *
     * @return start screen
     */
    Screen getStartScreen();
}
