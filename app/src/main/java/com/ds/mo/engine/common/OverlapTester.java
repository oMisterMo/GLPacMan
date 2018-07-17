package com.ds.mo.engine.common;

/**
 * Created by Mo on 01/10/2017.
 */

public final class OverlapTester {

    private OverlapTester(){
        //Can not instantiate class
    }

    public static boolean overlapCircles(Circle c1, Circle c2) {
        float distance = c1.center.distanceSqr(c2.center);
        float radiusSum = c1.radius + c2.radius;
        return distance <= radiusSum * radiusSum;
    }

    public static boolean overlapRectangles(Rectangle r1, Rectangle r2) {
        return r1.lowerLeft.x < r2.lowerLeft.x + r2.width &&
                r1.lowerLeft.x + r1.width > r2.lowerLeft.x &&
                r1.lowerLeft.y < r2.lowerLeft.y + r2.height &&
                r1.lowerLeft.y + r1.height > r2.lowerLeft.y;
    }

    public static boolean overlapCircleRectangle(Circle c, Rectangle r) {
        float nearestX = c.center.x;
        float nearestY = c.center.y;

        //circle is on the left
        if (c.center.x < r.lowerLeft.x) {
            nearestX = r.lowerLeft.x;
        } else if (c.center.x > r.lowerLeft.x + r.width) {
            //circle is on the right
            nearestX = r.lowerLeft.x + r.width;
        }
        //circle is above
        if (c.center.y < r.lowerLeft.y) {
            nearestY = r.lowerLeft.y;
        } else if (c.center.y > r.lowerLeft.y + r.height) {
            //circle in below
            nearestY = r.lowerLeft.y + r.height;
        }

        return c.center.distanceSqr(nearestX, nearestY) < c.radius * c.radius;
    }

    public static boolean pointInCircle(Circle c, Vector2D p) {
        return c.center.distanceSqr(p) < c.radius * c.radius;
    }

    public static boolean pointInCircle(Circle c, float x, float y) {
        return c.center.distanceSqr(x, y) < c.radius * c.radius;
    }

    public static boolean pointInRectangle(Rectangle r, Vector2D p) {
        return r.lowerLeft.x <= p.x && r.lowerLeft.x + r.width >= p.x &&
                r.lowerLeft.y <= p.y && r.lowerLeft.y + r.height >= p.y;
    }

    public static boolean pointInRectangle(Rectangle r, float x, float y) {
        return r.lowerLeft.x <= x && r.lowerLeft.x + r.width >= x &&
                r.lowerLeft.y <= y && r.lowerLeft.y + r.height >= y;
    }
}
