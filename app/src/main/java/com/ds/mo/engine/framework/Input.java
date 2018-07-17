package com.ds.mo.engine.framework;

import java.util.List;

/**
 * Input interface
 * <p/>
 * Implement the input class with any chosen underlying hardware
 * <p/>
 * Created by Mo on 26/05/2017.
 */
public interface Input {

    class KeyEvent {
        public static final int KEY_DOWN = 0;
        public static final int KEY_UP = 1;

        public int type;
        public int keyCode;
        public char keyChar;
    }

    class TouchEvent {
        public static final int TOUCH_DOWN = 0;
        public static final int TOUCH_UP = 1;
        public static final int TOUCH_DRAGGED = 2;

        public int type;
        public int x, y;
        public int pointer;
    }

    boolean isKeyPressed(int keyCode);

    boolean isTouchDown(int pointer);

    int getTouchX(int pointer);

    int getTouchY(int pointer);

    float getAccelX();

    float getAccelY();

    float getAccelZ();

    List<KeyEvent> getKeyEvents();

    List<TouchEvent> getTouchEvents();
}
