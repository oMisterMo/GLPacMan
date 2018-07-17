package com.ds.mo.engine.framework;

import com.ds.mo.engine.android.GLGame;
import com.ds.mo.engine.android.GLGraphics;

/**
 * Created by Mo on 07/10/2017.
 */

public abstract class GLScreen extends Screen {
    protected  final GLGraphics glGraphics;
    protected final GLGame glGame;

    public GLScreen(Game game){
        super(game);
        glGame = (GLGame) game;
        glGraphics = glGame.getGlGraphics();
    }
}
