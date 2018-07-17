package com.ds.mo.engine.android;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.ds.mo.engine.framework.Audio;
import com.ds.mo.engine.framework.FileIO;
import com.ds.mo.engine.framework.Game;
import com.ds.mo.engine.framework.Graphics;
import com.ds.mo.engine.framework.Input;
import com.ds.mo.engine.framework.Screen;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Mo on 26/09/2017.
 */

public class GLGame extends Activity implements Game, GLSurfaceView.Renderer {
    enum GLGameState {
        Initialized,
        Running,
        Paused,
        Finished,
        Idle
    }

    GLSurfaceView glView;
    GLGraphics glGraphics;
    Audio audio;
    Input input;
    FileIO fileIO;
    Screen screen;
    GLGameState state = GLGameState.Initialized;
    Object stateChanged = new Object();
    long startTime = System.nanoTime();

    /*  ACTIVITY LIFECYCLE */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("AndroidGame", "***************CREATE***************");
        super.onCreate(savedInstanceState);
        //Set flags
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        glView = new GLSurfaceView(this);
        //Tell android that we are using ES 2.0
        //glView.setEGLContextClientVersion(2);

        //Render using our custom renderer
        glView.setRenderer(this);


        Log.d("AndroidGame", "*Initialising graphics...*");
        glGraphics = new GLGraphics(glView);
        Log.d("AndroidGame", "*Initialising fileIO...*");
        fileIO = new AndroidFileIO(this);
        Log.d("AndroidGame", "*Initialising audio...*");
        audio = new AndroidAudio(this);
        Log.d("AndroidGame", "*Initialising input...*");
//        input = new AndroidInput(this, renderView, scaleX, scaleY);
        input = new AndroidInput(this, glView, 1, 1);

        setContentView(glView);
    }

    @Override
    protected void onPause() {
        Log.d("AndroidGame", "***************PAUSE***************");
        synchronized (stateChanged) {
            if (isFinishing()) {
                Log.d("AndroidGame", "state -> Finished");
                state = GLGameState.Finished;
            } else {
                Log.d("AndroidGame", "state -> Paused");
                state = GLGameState.Paused;
            }
            while (true) {
                try {
                    stateChanged.wait();
                    break;
                } catch (InterruptedException e) {
                }
            }
        }
        glView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d("AndroidGame", "***************RESUME***************");
        super.onResume();
        Log.d("AndroidGame", "start the rendering thread...");
        glView.onResume();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.d("AndroidGame", "onSurfaceCreated()...");
        glGraphics.setGL(gl);

        synchronized (stateChanged) {
            if (state == GLGameState.Initialized) {
                Log.d("AndroidGame", "first time application loading start screen...");
                screen = getStartScreen();
            }
            Log.d("AndroidGame", "state -> Running");
            state = GLGameState.Running;
            screen.resume();
            startTime = System.nanoTime();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //nothing to do
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLGameState state = null;

        synchronized (stateChanged) {
            //Gets state from UI thread and sets to Render thread state
            state = this.state;
        }

        switch (state) {
            case Running:
                float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
                startTime = System.nanoTime();

                screen.update(deltaTime);
                screen.draw(deltaTime);
                break;
            case Paused:
                screen.pause();
                synchronized (stateChanged){
                    Log.d("AndroidGame", "state -> Idle");
                    this.state = GLGameState.Idle;
                    stateChanged.notifyAll();
                }
                break;
            case Finished:
                screen.pause();
                screen.dispose();
                synchronized (stateChanged){
                    Log.d("AndroidGame", "state -> Idle");
                    this.state = GLGameState.Idle;
                    stateChanged.notifyAll();
                }
                break;
        }
    }

    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public FileIO getFileIO() {
        return fileIO;
    }

    @Override
    public Graphics getGraphics() {
        throw new IllegalStateException("We are using OpenGL!");
    }

    public GLGraphics getGlGraphics(){
        return glGraphics;
    }

    @Override
    public Audio getAudio() {
        return audio;
    }

    @Override
    public void setScreen(Screen newScreen) {
        if (screen == null) {
            throw new IllegalArgumentException("Screen must not be null");
        }
        this.screen.pause();
        this.screen.dispose();
        newScreen.resume();
        newScreen.update(0);
        this.screen = newScreen;
    }

    @Override
    public Screen getCurrentScreen() {
        return screen;
    }

    @Override
    public Screen getStartScreen() {
        Log.d("TEST", "THIS SHOULD NOT PRINT!!!!");
        return null;
    }
}
