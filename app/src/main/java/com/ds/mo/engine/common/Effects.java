package com.ds.mo.engine.common;

/**
 * Created by Mo on 29/03/2017.
 */
public class Effects {

    private Effects(){

    }

    /**
     *
     * @param pulseSpeed speed at which the value pulsates (between 0.0 and 1)
     * @param pulseMin min pulse size
     * @param pulseMax max pulse size
     * @param elapsedTime must increment
     * @return A value within the given range
     */
    public static float pulse(float pulseSpeed, float pulseMin, float pulseMax, int elapsedTime) {
//        System.out.println("elapsed: " + elapsedTime);
//        float sinX;
//        float speed = 0.08f; //between 0 + 1;

//        return (float) Math.abs(
//                Math.sin(Vector2D.TO_RADIANS * elapsedTime * pulseSpeed)
//        ) * (pulseMax-pulseMin) + pulseMin;
        return (float) (((Math.sin(Vector2D.TO_RADIANS * elapsedTime * pulseSpeed)
                + 1) * 0.5f) * (pulseMax - pulseMin)) + pulseMin;
    }
}
