package com.ds.mo.engine.common;

/**
 * Created by Mo on 01/10/2017.
 */

public class Rectangle {
    public final Vector2D lowerLeft;
    public float width, height;

    public Rectangle(float x, float y, float width, float height) {
        this.lowerLeft = new Vector2D(x, y);
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return "RECT: x "+ lowerLeft.x + ", y "+this.lowerLeft.y ;
    }
}
