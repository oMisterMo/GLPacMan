package com.ds.mo.engine.framework;

/**
 * Abstract screen class representing a single screen
 * <p/>
 * Created by Mo on 26/05/2017.
 */
public abstract class Screen {
    protected final Game game;

    /**
     * Constructor, initialises game to current screen
     *
     * @param game our game
     */
    public Screen(Game game) {
        this.game = game;
    }

    /**
     * Update the current screen
     *
     * @param deltaTime time passed since last frame
     */
    public abstract void update(float deltaTime);

    /**
     * Draw the current screen
     *
     * @param deltaTime time passed since last frame
     */
    public abstract void draw(float deltaTime);

    /**
     * Pause the current screen
     */
    public abstract void pause();

    /**
     * Resume the current screen
     */
    public abstract void resume();

    /**
     * Free up the current screen
     */
    public abstract void dispose();
}
