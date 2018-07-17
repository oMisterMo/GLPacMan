package com.ds.mo.engine.common;

import android.util.Log;

/**
 * Logs the FPS to the console, simply call from any screen
 * <p/>
 * Android vsync is enabled on all android devices automatically
 * <p/>
 * Created by Mo on 30/05/2017.
 */
public class FPSCounter {
    static long startTime = System.nanoTime();
    static int frames = 0;

    public static void logFrame() {
        frames++;
        if (System.nanoTime() - startTime >= 1000000000) {
            Log.d("FPSCOUNTER", "fps: " + frames);
            frames = 0;
            startTime = System.nanoTime();
        }
    }
}
