package com.ds.mo.engine.android;

import android.content.Context;
import android.view.View;

import com.ds.mo.engine.framework.Input;

import java.util.List;

/**
 * Created by Mo on 29/05/2017.
 */
public class AndroidInput implements Input {
    private AccelerometerHandler accelHandler;
    private KeyboardHandler keyHandler;
    private TouchHandler touchHandler;

    public AndroidInput(Context context, View view, float scaleX, float scaleY){
//        accelHandler = new AccelerometerHandler(context);
        keyHandler = new KeyboardHandler(view);
//        touchHandler = new SingleTouchHandler(view, scaleX, scaleY);
        touchHandler = new MultiTouchHandler(view, scaleX, scaleY);
    }

    public void pause(){

    }

    public void resume(){
    }

    @Override
    public boolean isKeyPressed(int keyCode) {
        return keyHandler.isKeyPressed(keyCode);
    }

    @Override
    public boolean isTouchDown(int pointer) {
        return touchHandler.isTouchDown(pointer);
    }

    @Override
    public int getTouchX(int pointer) {
        return touchHandler.getTouchX(pointer);
    }

    @Override
    public int getTouchY(int pointer) {
        return touchHandler.getTouchY(pointer);
    }

    @Override
    public float getAccelX() {
        return accelHandler.getAccelX();
    }

    @Override
    public float getAccelY() {
        return accelHandler.getAccelY();
    }

    @Override
    public float getAccelZ() {
        return accelHandler.accelZ;
    }

    @Override
    public List<KeyEvent> getKeyEvents() {
        return keyHandler.getKeyEvents();
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }
}
