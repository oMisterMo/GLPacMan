package com.ds.mo.engine.glpacman;

import android.util.Log;

import com.ds.mo.engine.android.SpriteBatcher;
import com.ds.mo.engine.common.Animation;
import com.ds.mo.engine.common.Camera2D;
import com.ds.mo.engine.common.Color;
import com.ds.mo.engine.common.Helper;
import com.ds.mo.engine.common.Vector2D;
import com.ds.mo.engine.framework.GLScreen;
import com.ds.mo.engine.framework.Game;
import com.ds.mo.engine.framework.Input.TouchEvent;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class ReadyScreen extends GLScreen {
    private static final float TIME_TO_WAIT = 0f;
//    private static final float TIME_TO_WAIT = 6f;
    private static final float QUICK_PULSE = 0.2f;
    private static final float PULSE = 1f;

    private static final int ANIMATION = 0;
    private static final int COMPLETE = 1;
    private static int state = ANIMATION;

    public static final float WORLD_WIDTH = 360;
    public static final float WORLD_HEIGHT = 640;

    private Camera2D camera;
    private SpriteBatcher batcher;
    private Vector2D touchPos = new Vector2D();

    private Vector2D center;

    private Vector2D blinkyPos;
    private Vector2D pinkyPos;
    private Vector2D inkyPos;
    private Vector2D clydePos;

//    private String shadow, speedy, bashful, pokey;
//    private String blinky, pinky, inky, clyde;

    private String[] enemies;

    //Touch to start pulse
    private boolean pulse;
    private float touchToStartElapsed;
    //Energizer pulse
    private boolean quickPulse;
    private float energizerElapsed;
    private float elapsed;

    public ReadyScreen(Game game) {
        super(game);
        Log.d("ReadyScreen", "ReadyScreen constructor...");
        camera = new Camera2D(glGraphics, WORLD_WIDTH, WORLD_HEIGHT);
        batcher = new SpriteBatcher(glGraphics, 100);   //max sprites

        center = new Vector2D(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);

        blinkyPos = new Vector2D(50, WORLD_HEIGHT - 130);
        pinkyPos = new Vector2D(blinkyPos.x, blinkyPos.y - (32 * 2));
        inkyPos = new Vector2D(blinkyPos.x, pinkyPos.y - (32 * 2));
        clydePos = new Vector2D(blinkyPos.x, inkyPos.y - (32 * 2));
//        shadow = "SHADOW";
//        speedy = "SPEEDY";
//        bashful = "BASHFUL";
//        pokey = "POKEY";
//        blinky = "BLINKY";
//        pinky = "PINKY";
//        inky = "INKY";
//        clyde = "CLYDE";

        String[] enemiesA, enemiesB, enemiesC, enemiesD;
        enemiesA = new String[]{"SHADOW", "SPEEDY", "BASHFUL", "POKEY"};
        enemiesB = new String[]{"BLINKY", "PINKY", "INKY", "CLYDE"};
        enemiesC = new String[]{"CHASER", "AMBUSHER", "FICKLE", "STUPID"};
        enemiesD = new String[]{"RED GUY", "PINK GUY", "BLUE GUY", "SLOW GUY"};
        int num = Helper.Random(0, 3);
        Log.d("RS", "RANDOM = "+num);
        switch (num){
            case 0:
                enemies = enemiesA;
                break;
            case 1:
                enemies = enemiesB;
                break;
            case 2:
                enemies = enemiesC;
                break;
            case 3:
                enemies = enemiesD;
                break;
        }

        pulse = true;
        touchToStartElapsed = 0;
        elapsed = 0;
    }

    private void updateReady(List<TouchEvent> events, float deltaTime) {
        int len = events.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = events.get(i);
            touchPos.set(event.x, event.y);     //0, 0, 2560, 1440
            camera.touchToWorld(touchPos);      //0, 0, 360, 640
            if (event.type == TouchEvent.TOUCH_UP) {
                Log.d("GameScreen", "Touch up, loading level!");
                if (elapsed > TIME_TO_WAIT) {
                    game.setScreen(new GameScreen(game));
                }
                return;
            }
        }

        touchToStartElapsed += deltaTime;
        //Flash every 1 second
        if (touchToStartElapsed >= PULSE) {
            pulse = !pulse;
            touchToStartElapsed = 0;
        }

        //Only happens after initial animation is complete
        switch (state) {
            case COMPLETE:
                energizerElapsed += deltaTime;
                if (energizerElapsed >= QUICK_PULSE) {
                    quickPulse = !quickPulse;
                    energizerElapsed = 0;
                }
                break;
        }
    }

    private void drawReady() {
        batcher.beginBatch(Assets.pacSheet);
        batcher.setColor(Color.WHITE);
        Assets.font.drawText(batcher, "CHARACTER / NICKNAME", center.x, WORLD_HEIGHT - 50);
        switch (state) {
            case ANIMATION:
                drawAnimation();
                break;
            case COMPLETE:
                drawComplete();
                break;
        }
        if (pulse) {
            batcher.setColor(Color.WHITE);
            Assets.font.drawText(batcher, "TOUCH TO START", center.x, 50);
        }
        batcher.endBatch();
    }

    private void drawAnimation() {
        if (elapsed > 2f) {
            //BLINKY
            batcher.setColor(Color.RED);
            batcher.drawSprite(blinkyPos.x, blinkyPos.y, 32, 32,
                    Assets.ghost.getKeyFrame(1f, Animation.ANIMATION_LOOPING));
            batcher.setColor(Color.WHITE);
            batcher.drawSprite(blinkyPos.x, blinkyPos.y, 32, 32, Assets.eyesR);
        }
        if (elapsed > 3f) {
            batcher.setColor(Color.RED);
            Assets.font.drawText(batcher, enemies[0], center.x, blinkyPos.y);
        }
        if (elapsed > 4f) {
            //PINKY
            batcher.setColor(Color.PINK);
            batcher.drawSprite(pinkyPos.x, pinkyPos.y, 32, 32,
                    Assets.ghost.getKeyFrame(1f, Animation.ANIMATION_LOOPING));
            batcher.setColor(Color.WHITE);
            batcher.drawSprite(pinkyPos.x, pinkyPos.y, 32, 32, Assets.eyesR);
        }
        if (elapsed > 5f) {
            batcher.setColor(Color.PINK);
            Assets.font.drawText(batcher, enemies[1], center.x, pinkyPos.y);
        }
        if (elapsed > 6f) {
            //INKY
            batcher.setColor(Color.SKY);
            batcher.drawSprite(inkyPos.x, inkyPos.y, 32, 32,
                    Assets.ghost.getKeyFrame(1f, Animation.ANIMATION_LOOPING));
            batcher.setColor(Color.WHITE);
            batcher.drawSprite(inkyPos.x, inkyPos.y, 32, 32, Assets.eyesR);
        }
        if (elapsed > 7f) {
            batcher.setColor(Color.SKY);
            Assets.font.drawText(batcher, enemies[2], center.x, inkyPos.y);
        }
        if (elapsed > 8f) {
            //CLYDE
            batcher.setColor(Color.ORANGE);
            batcher.drawSprite(clydePos.x, clydePos.y, 32, 32,
                    Assets.ghost.getKeyFrame(1f, Animation.ANIMATION_LOOPING));
            batcher.setColor(Color.WHITE);
            batcher.drawSprite(clydePos.x, clydePos.y, 32, 32, Assets.eyesR);
        }
        if (elapsed > 9f) {
            batcher.setColor(Color.ORANGE);
            Assets.font.drawText(batcher, enemies[3], center.x, clydePos.y);
        }
        if (elapsed > 10f) {
            batcher.setColor(Color.TAN);
            batcher.drawSprite(center.x - 50, clydePos.y - 32 * 2.5f, 25, 25, Assets.dot);
            batcher.drawSprite(center.x - 50, clydePos.y - 32 * 3.5f, 20, 20, Assets.energizer);
            batcher.setColor(Color.WHITE);
            Assets.font.drawText(batcher, "10PTS", WORLD_WIDTH / 2, clydePos.y - 32 * 2.5f, 10, 10);
            Assets.font.drawText(batcher, "50PTS", WORLD_WIDTH / 2, clydePos.y - 32 * 3.5f, 10, 10);
        }

        if (elapsed > 12) {
            state = COMPLETE;
            Log.d("ReadyScreen", "STATE = COMPLETE");
        }
    }

    private void drawComplete() {
        //BLINKY
        batcher.setColor(Color.RED);
        batcher.drawSprite(blinkyPos.x, blinkyPos.y, 32, 32,
                Assets.ghost.getKeyFrame(1f, Animation.ANIMATION_LOOPING));
        Assets.font.drawText(batcher, enemies[0], center.x, blinkyPos.y);
        batcher.setColor(Color.WHITE);
        batcher.drawSprite(blinkyPos.x, blinkyPos.y, 32, 32, Assets.eyesR);

        //PINKY
        batcher.setColor(Color.PINK);
        batcher.drawSprite(pinkyPos.x, pinkyPos.y, 32, 32,
                Assets.ghost.getKeyFrame(1f, Animation.ANIMATION_LOOPING));
        Assets.font.drawText(batcher, enemies[1], center.x, pinkyPos.y);
        batcher.setColor(Color.WHITE);
        batcher.drawSprite(pinkyPos.x, pinkyPos.y, 32, 32, Assets.eyesR);

        //INKY
        batcher.setColor(Color.SKY);
        batcher.drawSprite(inkyPos.x, inkyPos.y, 32, 32,
                Assets.ghost.getKeyFrame(1f, Animation.ANIMATION_LOOPING));
        Assets.font.drawText(batcher, enemies[2], center.x, inkyPos.y);
        batcher.setColor(Color.WHITE);
        batcher.drawSprite(inkyPos.x, inkyPos.y, 32, 32, Assets.eyesR);

        //CLYDE
        batcher.setColor(Color.ORANGE);
        batcher.drawSprite(clydePos.x, clydePos.y, 32, 32,
                Assets.ghost.getKeyFrame(1f, Animation.ANIMATION_LOOPING));
        Assets.font.drawText(batcher, enemies[3], center.x, clydePos.y);
        batcher.setColor(Color.WHITE);
        batcher.drawSprite(clydePos.x, clydePos.y, 32, 32, Assets.eyesR);

//        Assets.font.drawText(batcher, "SCORE 1234567890", WORLD_WIDTH / 2 - 100, WORLD_HEIGHT - 100);


        batcher.setColor(Color.TAN);
        batcher.drawSprite(center.x - 50, clydePos.y - 32 * 2.5f, 25, 25, Assets.dot);
        if (quickPulse) {
            batcher.drawSprite(center.x - 50, clydePos.y - 32 * 3.5f, 20, 20, Assets.energizer);
        }
        batcher.setColor(Color.WHITE);
        Assets.font.drawText(batcher, "10PTS", WORLD_WIDTH / 2, clydePos.y - 32 * 2.5f, 10, 10);
        Assets.font.drawText(batcher, "50PTS", WORLD_WIDTH / 2, clydePos.y - 32 * 3.5f, 10, 10);
    }

    @Override
    public void update(float deltaTime) {
        game.getInput().getKeyEvents();
        List<TouchEvent> events = game.getInput().getTouchEvents();
        elapsed += deltaTime;

        updateReady(events, deltaTime);
    }

    @Override
    public void draw(float deltaTime) {
        GL10 gl = glGraphics.getGL();
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);               //Specifies which buffer to clear
        gl.glClearColor(0, 0, 0, 1);
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        camera.setViewportAndMatrices();    //640 : 360
        drawReady();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
    }
}
