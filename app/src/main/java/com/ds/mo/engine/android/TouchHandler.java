package com.ds.mo.engine.android;

import android.view.View.OnTouchListener;

import com.ds.mo.engine.framework.Input.TouchEvent;

import java.util.List;


/**
 * Created by Mo on 29/05/2017.
 */
public interface TouchHandler extends OnTouchListener {
    boolean isTouchDown(int pointer);

    int getTouchX(int pointer);

    int getTouchY(int pointer);

    List<TouchEvent> getTouchEvents();
}
