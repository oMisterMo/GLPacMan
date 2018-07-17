package com.ds.mo.engine.common;

import android.util.Log;

import com.ds.mo.engine.android.GLGraphics;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Mo on 16/03/2018.
 */

public class ShakyCamera{
    public Vector2D position;
    public float zoom;
    public final float frustumWidth;
    public final float frustumHeight;
    final GLGraphics glGraphics;


    //Camera shake
    public float shake; //0-1
    public float maxAngle;
    public float maxOffset;


    public float angle;     //5 deg (+/-)
    public float offsetX;
    public float offsetY;

    public ShakyCamera(GLGraphics glGraphics, float frustumWidth, float frustumHeight) {
        this.glGraphics = glGraphics;
        this.frustumWidth = frustumWidth;
        this.frustumHeight = frustumHeight;
        this.position = new Vector2D(frustumWidth / 2, frustumHeight / 2);
        Log.d("Camera2D", "Position: " + position);
        this.zoom = 1.0f;
//        shake = 0;
//        /* Rotational shake */
//        maxAngle = 0;
//        angle = 0;
//
//        /* Directional shake */
//        maxOffset = 0;
//        offsetX = 0;        //left/right shake
//        offsetY = 0;        //up/down shake
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

    /**
     * Computes the shake amount based on the parameters passed
     *
     * @param shake     between 0 and 1
     * @param maxAngle  angle of shake
     * @param maxOffset space to move
     */
    public void computeShake(float shake, float maxAngle, float maxOffset) {
        angle = maxAngle * shake * Helper.Random(-1, 1);
        offsetX = maxOffset * shake * Helper.Random(-1, 1);
        offsetY = maxOffset * shake * Helper.Random(-1, 1);
    }
}
