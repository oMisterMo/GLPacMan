package com.ds.mo.engine.common;

/**
 * Created by Mo on 30/09/2017.
 */

public class DynamicGameObject extends GameObject {
    public final Vector2D velocity;
    public final Vector2D accel;

    public DynamicGameObject(float x, float y, float width, float height) {
        super(x, y, width, height);
        velocity = new Vector2D();
        accel = new Vector2D();
    }
}
