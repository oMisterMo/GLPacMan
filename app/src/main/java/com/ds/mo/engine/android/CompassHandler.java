package com.ds.mo.engine.android;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Mo on 29/05/2017.
 */
public class CompassHandler implements SensorEventListener {
    float yaw;
    float pitch;
    float roll;
    Sensor mAccelerometer;
    Sensor mMagnetometer;
    float[] mLastAccelerometer = new float[3];
    float[] mLastMagnetometer = new float[3];

    boolean mLastAcceleromterSet = false;
    boolean mLastMagetometerSet = false;
    float[] mR = new float[9];
    float[] mOrientation = new float[3];

    public CompassHandler(Context context){
        SensorManager manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor == mAccelerometer){
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAcceleromterSet = true;
        }else if(event.sensor == mMagnetometer){
            System.arraycopy(event.values, 0, mLastMagetometerSet, 0, event.values.length);
            mLastMagetometerSet = true;
        }
        if(mLastAcceleromterSet && mLastMagetometerSet){
            SensorManager.getRotationMatrix(mR, null,
                    mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);
            yaw = mOrientation[0];
            pitch = mOrientation[1];
            roll = mOrientation[2];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //nothing to do here
    }
}
