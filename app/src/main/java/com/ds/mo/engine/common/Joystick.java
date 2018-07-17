package com.ds.mo.engine.common;

/**
 * Created by Mo on 14/03/2018.
 */

public class Joystick {

    public Vector2D mainPos;
    public float bigWidth;
    public float bigHeight;

    public Vector2D offset;            //analog position
    public float offsetWidth;
    public float offsetHeight;

    public Vector2D center;
    public Rectangle bounds;

    private Vector2D touchPos;
    private Vector2D inputVec;

    public Joystick(float x, float y) {
        //Gets called before assets are loaded
//        TextureRegion lc = Assets.largeCircle;
//        this.bigWidth = lc.texture.width;
//        this.bigHeight = lc.texture.height;
        this.bigWidth = 128;
        this.bigHeight = 128;

        this.center = new Vector2D(x + bigWidth / 2, y + bigHeight / 2);
        this.mainPos = new Vector2D(x, y);
        System.out.println("analog pos: " + mainPos);
        System.out.println("width: " + bigWidth);
        System.out.println("height: " + bigHeight);
        System.out.println("center: " + center);

//        TextureRegion sc = Assets.smallCircle;
//        offsetWidth = sc.texture.width;
//        offsetHeight = sc.texture.height;
        offsetWidth = 32;
        offsetHeight = 32;
//        float tempX, tempY;
//        tempX = x - offsetWidth / 2f;
//        tempY = y - offsetHeight / 2f;
////        tempX = center.x - offsetWidth / 2;
////        tempY = center.y - offsetHeight / 2;
        this.offset = new Vector2D(center.x, center.y);
        System.out.println("offset pos: " + offset);
        float moWidth, moHeight;
        moWidth = 100;
        moHeight = 100;
        bounds = new Rectangle(mainPos.x - moWidth / 2,
                mainPos.y - moHeight / 2, bigWidth + moWidth,
                bigHeight + moHeight);
        touchPos = new Vector2D();
        inputVec = new Vector2D();
    }

    public Joystick(float x, float y, int bigWidth, int bigHeight) {
        this.bigWidth = bigWidth;
        this.bigHeight = bigHeight;
//        this.center = new Vector2D(x + bigWidth / 2, y + bigHeight / 2);
        this.mainPos = new Vector2D(x, y);
        System.out.println("width = " + bigWidth);
        System.out.println("height = " + bigHeight);
//        System.out.println("center: " + center);

//        TextureRegion sc = Assets.smallCircle;
//        offsetWidth = sc.texture.width;
//        offsetHeight = sc.texture.height;
        offsetWidth = 32;
        offsetHeight = 32;

        float tempX, tempY;
        tempX = x - offsetWidth / 2f;
        tempY = y - offsetHeight / 2f;
//        tempX = center.x - offsetWidth / 2f;
//        tempY = center.y - offsetHeight / 2f;
        this.offset = new Vector2D(tempX, tempY);

        float moWidth, moHeight;
        moWidth = 100;
        moHeight = 100;
        bounds = new Rectangle(mainPos.x - moWidth / 2,
                mainPos.y - moHeight / 2, bigWidth + moWidth,
                bigHeight + moHeight);
        touchPos = new Vector2D();
        inputVec = new Vector2D();
    }

    public void touchDown(Vector2D pos) {
//        Log.d("Joystick", "down");
        touchDragged(pos);
    }

    public void touchDragged(Vector2D pos) {
//        Log.d("Joystick", "dragged");

//        Log.d("Joystick", "touch: " + touchPos);
        if (OverlapTester.pointInRectangle(bounds, pos)) {
//            Log.d("Joystick", "INBOUNDS");
            //normalize touch pos between 0 - 1
            touchPos.x = (pos.x - mainPos.x) / bigWidth;
            touchPos.y = (pos.y - mainPos.y) / bigHeight;
//            touchPos.x = (touchPos.x - center.x) / bigWidth;
//            touchPos.y = (touchPos.y - center.y) / bigHeight;
//            Log.d("Joystick", "touchPos: " + touchPos);
//            //mult value between -1 and 1
            inputVec.set(touchPos.x * 2 - 1, touchPos.y * 2 - 1);
            inputVec = (inputVec.length() > 1) ? inputVec.normalize() : inputVec;
//            Log.d("Joystick", "inputVec: " + inputVec);
//
            float size = bigWidth / 2f;  //radius
            offset.set(center.x + (inputVec.x * size),
                    center.y + (inputVec.y * size));
        }
    }

    public void touchUp() {
//        Log.d("Joystick", "up");

//        //Analog let go, reset values
        inputVec.set(0, 0);
        offset.set(center);
    }

    public Vector2D getInputVec() {
        return inputVec;
    }

//    @Override
//    void gameUpdate(float deltaTime) {
//
//    }
//
//    @Override
//    void gameRender(Graphics2D g) {
//        //Draw bounds
//        g.setColor(Color.WHITE);
//        g.drawRect((int) bounds.lowerLeft.x, (int) bounds.lowerLeft.y,
//                (int) bounds.width, (int) bounds.height);
//        //Draw stick
//        g.drawImage(Assets.largeCircle, (int) mainPos.x, (int) mainPos.y,
//                (int) width, (int) height, null);
//        g.drawImage(Assets.smallCircle, (int) offset.x, (int) offset.y,
//                (int) offsetWidth, (int) offsetHeight, null);
//    }

}
