package com.ds.mo.engine.glpacman;

import android.util.Log;

import com.ds.mo.engine.android.GLGame;
import com.ds.mo.engine.framework.Screen;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Main activity
 * <p>
 * Created by Mo on 26/09/2017.
 */

public class DrawActivity extends GLGame {
    boolean firstTimeCreated = true;

    @Override
    public Screen getStartScreen() {
        Log.d("DrawActivity", "new ReadyScreen()...");
        return new ReadyScreen(this);
//        return new GameScreen(this);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        if (firstTimeCreated) {
            Log.d("DrawActivity", "First time loading assets...");
            //Check if file exists first
//            Settings.load(getFileIO());
            Assets.load(this);
            firstTimeCreated = false;
        } else {
            Log.d("DrawActivity", "Reloading assets...");
            Assets.reload();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Settings.soundEnabled) {
            Assets.music.pause();
        }
    }
}
