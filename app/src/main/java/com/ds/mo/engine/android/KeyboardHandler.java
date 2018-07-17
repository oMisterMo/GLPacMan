package com.ds.mo.engine.android;

import android.util.Log;
import android.view.View;
import android.view.View.OnKeyListener;

import com.ds.mo.engine.android.Pool.PoolObjectFactory;
import com.ds.mo.engine.framework.Input.KeyEvent;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mo on 29/05/2017.
 */
public class KeyboardHandler implements OnKeyListener {
    public static final int MAX_KEY_EVENTS = 283;

    boolean pressedKeys[] = new boolean[MAX_KEY_EVENTS - 1];
    Pool<KeyEvent> keyEventPool;
    List<KeyEvent> keyEventsBuffer = new ArrayList<>();
    List<KeyEvent> keyEvents = new ArrayList<>();

    public KeyboardHandler(View view) {
        PoolObjectFactory<KeyEvent> factory = new PoolObjectFactory<KeyEvent>() {
            @Override
            public KeyEvent createObject() {
                return new KeyEvent();
            }
        };
        keyEventPool = new Pool<KeyEvent>(factory, 100);
        view.setOnKeyListener(this);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    @Override
    public boolean onKey(View v, int keyCode, android.view.KeyEvent event) {
        if (event.getAction() == android.view.KeyEvent.ACTION_MULTIPLE) {
            Log.d("KeyboardHandler","event.getAction() == KeyEvent.ACTION_MULTIPLE");
            return false;
        }
        synchronized (this) {
            KeyEvent keyEvent = keyEventPool.newObject();
            keyEvent.keyCode = keyCode;
            keyEvent.keyChar = (char) event.getUnicodeChar();
//            Log.d("KeyboardHandler","key event received");
//            Log.d("KeyboardHandler","key code: "+keyCode);
//            Log.d("KeyboardHandler","event.getKeyCode: "+event.getKeyCode());
            if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
//                Log.d("KeyboardHandler","action down");
                keyEvent.type = android.view.KeyEvent.ACTION_DOWN;
                if (keyCode > 0 && keyCode < MAX_KEY_EVENTS - 1) {
//                    Log.d("KeyboardHandler","PRESSED = TRUE");
                    pressedKeys[keyCode] = true;
                }
            }
            if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
                keyEvent.type = android.view.KeyEvent.ACTION_UP;
                if (keyCode > 0 && keyCode < MAX_KEY_EVENTS - 1) {
                    pressedKeys[keyCode] = false;
                }
            }
            keyEventsBuffer.add(keyEvent);
        }
        return false;
    }

    public boolean isKeyPressed(int keyCode) {
        if (keyCode < 0 || keyCode > MAX_KEY_EVENTS - 1) {
            return false;
        }
        return pressedKeys[keyCode];
    }

    public List<KeyEvent> getKeyEvents() {
        synchronized (this) {
            int len = keyEvents.size();
            for (int i = 0; i < len; i++) {
                keyEventPool.free(keyEvents.get(i));
            }
            keyEvents.clear();
            keyEvents.addAll(keyEventsBuffer);
            keyEventsBuffer.clear();
            return keyEvents;
        }
    }
}
