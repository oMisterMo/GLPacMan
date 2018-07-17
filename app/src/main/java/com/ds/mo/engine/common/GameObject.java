package com.ds.mo.engine.common;

/**
 * Created by Mo on 30/09/2017.
 */

public class GameObject {
    public final Vector2D position;
    public final Rectangle bounds;

    public GameObject(float x, float y, float width, float height) {
        this.position = new Vector2D(x, y);
        this.bounds = new Rectangle(x - width / 2, y - height / 2, width, height);
    }
}
