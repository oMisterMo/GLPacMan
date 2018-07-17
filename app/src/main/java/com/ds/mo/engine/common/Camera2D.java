package com.ds.mo.engine.common;

import android.util.Log;

import com.ds.mo.engine.android.GLGraphics;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Mo on 02/10/2017.
 */

public class Camera2D {
    public final Vector2D position;
    public float zoom;
    public final float frustumWidth;
    public final float frustumHeight;
    final GLGraphics glGraphics;

    public Camera2D(GLGraphics glGraphics, float frustumWidth, float frustumHeight) {
        this.glGraphics = glGraphics;
        this.frustumWidth = frustumWidth;
        this.frustumHeight = frustumHeight;
        this.position = new Vector2D(frustumWidth / 2, frustumHeight / 2);
        Log.d("Camera2D", "Position: " + position);
        this.zoom = 1.0f;
    }

    /**
     * Sets the viewport to span the whole screen
     */
    public void setViewportAndMatrices() {
        GL10 gl = glGraphics.getGL();
        gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(position.x - frustumWidth * zoom / 2,
                position.x + frustumWidth * zoom / 2,
                position.y - frustumHeight * zoom / 2,
                position.y + frustumHeight * zoom / 2,
                1, -1);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    /**
     * Takes a vector containing touch coordinates and transform the vector into world space
     *
     * @param touch screen position
     */
    public void touchToWorld(Vector2D touch) {
        touch.x = (touch.x / (float) glGraphics.getWidth()) * frustumWidth * zoom;
        touch.y = (1 - touch.y / (float) glGraphics.getHeight()) * frustumHeight * zoom;
        touch.add(position).sub(frustumWidth * zoom / 2, frustumHeight * zoom / 2);
    }
}
