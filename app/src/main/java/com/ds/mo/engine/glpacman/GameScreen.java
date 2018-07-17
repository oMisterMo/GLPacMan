package com.ds.mo.engine.glpacman;

import android.util.Log;

import com.ds.mo.engine.android.SpriteBatcher;
import com.ds.mo.engine.android.TextureRegion;
import com.ds.mo.engine.common.Animation;
import com.ds.mo.engine.common.Camera2D;
import com.ds.mo.engine.common.Color;
import com.ds.mo.engine.common.Vector2D;
import com.ds.mo.engine.framework.GLScreen;
import com.ds.mo.engine.framework.Game;
import com.ds.mo.engine.framework.Input.TouchEvent;
import com.ds.mo.engine.glpacman.logic.Tile;
import com.ds.mo.engine.glpacman.logic.World;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Mo on 31/05/2018.
 */

public class GameScreen extends GLScreen {
    public static float WORLD_WIDTH = 360;
    public static float WORLD_HEIGHT = 640;

    private Camera2D camera;
    private SpriteBatcher batcher;
    private Vector2D touchPos = new Vector2D();

//    public static int scale = 1;
    private World world;
    private Color tileCol;
    private Vector2D center;

    public GameScreen(Game game) {
        super(game);
        Log.d("GameScreen", "GameScreen constructor...");
        camera = new Camera2D(glGraphics, WORLD_WIDTH, WORLD_HEIGHT);
        batcher = new SpriteBatcher(glGraphics, 2500);   //max sprites

//        camera.position.x = WORLD_WIDTH / 2 - 68;
//        camera.position.y = WORLD_HEIGHT / 2 - 200;
//        camera.zoom = 0.65f;

        center = new Vector2D((World.NO_OF_TILES_X * Tile.TILE_WIDTH) / 2,
                (World.NO_OF_TILES_Y * Tile.TILE_HEIGHT) / 2);
        camera.position.x = center.x;
        camera.position.y = center.y;  //- 50 to align world top
        camera.zoom = 0.62f;

        tileCol = new Color(139 / 255f, 3 / 255f, 12 / 255f, 1f);
        world = new World();
        world.loadLevel();

        Log.d("GameScreen", "End GameScreen constructor...");
    }

    private void handleInput(List<TouchEvent> events, float deltaTime) {
        //Hand GUI touches
//        int len = events.size();
//        for (int i = 0; i < len; i++) {
//            TouchEvent event = events.get(i);
//            if (event.type == TouchEvent.TOUCH_UP) {
////                world.moveLeft();
//                world.currentDir = World.DIR.LEFT;
//            }
//        }
        world.move(events);
    }

    private void drawWorld(SpriteBatcher batcher) {
        batcher.setColor(Color.WHITE);
        for (int y = 0; y < World.NO_OF_TILES_Y; y++) {
            for (int x = 0; x < World.NO_OF_TILES_X; x++) {
                Tile t = world.tiles[y][x];
                int tileId = t.id;
                switch (tileId) {
                    case Tile.DOUBLE_TL:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x,
                                t.position.y,
                                8, 8,
                                Assets.double_tl);
                        break;
                    case Tile.DOUBLE_TM:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x,
                                t.position.y,
                                8, 8,
                                Assets.double_tm);
                        break;
                    case Tile.DOUBLE_TR:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.double_tr);
                        break;
                    case Tile.DOUBLE_ML:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.double_ml);
                        break;
                    case Tile.DOUBLE_MR:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.double_mr);
                        break;
                    case Tile.DOUBLE_BL:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.double_bl);
                        break;
                    case Tile.DOUBLE_BM:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.double_bm);
                        break;
                    case Tile.DOUBLE_BR:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.double_br);
                        break;
                    //***Line***
                    case Tile.LINE_TL:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.line_tl);
                        break;
                    case Tile.LINE_TM:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.line_tm);
                        break;
                    case Tile.LINE_TR:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.line_tr);
                        break;
                    case Tile.LINE_ML:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.line_ml);
                        break;
                    case Tile.LINE_MR:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.line_mr);
                        break;
                    case Tile.LINE_BL:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.line_bl);
                        break;
                    case Tile.LINE_BM:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.line_bm);
                        break;
                    case Tile.LINE_BR:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.line_br);
                        break;
                    //Hor
                    case Tile.HOR_TL:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.hor_tl);
                        break;
                    case Tile.HOR_TR:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.hor_tr);
                        break;
                    case Tile.HOR_BL:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.hor_bl);
                        break;
                    case Tile.HOR_BR:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.hor_br);
                        break;
                    //Ver
                    case Tile.VER_TL:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.ver_tl);
                        break;
                    case Tile.VER_TR:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.ver_tr);
                        break;
                    case Tile.VER_BL:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.ver_bl);
                        break;
                    case Tile.VER_BR:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.ver_br);
                        break;
                    //Square
                    case Tile.SQUARE_TL:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.square_tl);
                        break;
                    case Tile.SQUARE_TR:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.square_tr);
                        break;
                    case Tile.SQUARE_BL:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.square_bl);
                        break;
                    case Tile.SQUARE_BR:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.square_br);
                        break;
                    case Tile.HOME_L:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.home_l);
                        break;
                    case Tile.HOME_R:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.home_r);
                        break;
                    case Tile.LINE_BIG_TL:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.line_big_tl);
                        break;
                    case Tile.LINE_BIG_TR:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.line_big_tr);
                        break;
                    case Tile.LINE_BIG_BL:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.line_big_bl);
                        break;
                    case Tile.LINE_BIG_BR:
                        batcher.setColor(tileCol);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.line_big_br);
                        break;
                    case Tile.DOT:
                        batcher.setColor(Color.TAN);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.dot);
                        break;
                    case Tile.ENERGIZER:
                        batcher.setColor(Color.TAN);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.energizer);
                        break;
                    case Tile.HOME_DOOR:
//                        batcher.setColor(tileCol);
                        batcher.setColor(Color.PINK);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.home_door);
                        break;
                    case Tile.PACMAN:
//                        batcher.setCo
////                        batcher.drawSprite(t.position.x, t.position.y,
////                                16, 16,
////                                Assets.pacman.getKeyFrame(0,
////                                        Animation.ANIMATION_NON_LOOPING));lor(Color.YELLOW);
                }
            }
        }
    }

    private void drawLegal(SpriteBatcher batcher) {
        batcher.setColor(Color.BLUE);
        for (int y = 0; y < World.NO_OF_TILES_Y; y++) {
            for (int x = 0; x < World.NO_OF_TILES_X; x++) {
                Tile t = world.tiles[y][x];
                if (t.legal) {
                    batcher.drawSprite(t.position.x, t.position.y, 8, 8, Assets.dot);
                }
            }
        }
    }

    private void drawBoarder(SpriteBatcher batcher) {
        batcher.setColor(Color.CHARTREUSE);
        for (int i = 0; i < World.NO_OF_TILES_X; i++) {
            Tile top = world.tiles[0][i];
            Tile bot = world.tiles[World.NO_OF_TILES_Y - 1][i];
            batcher.drawSprite(top.position.x, top.position.y, 8, 8, Assets.double_bm);
            batcher.drawSprite(bot.position.x, bot.position.y, 8, 8, Assets.double_tm);
        }
        for (int i = 0; i < World.NO_OF_TILES_Y; i++) {
            Tile left = world.tiles[i][0];
            Tile right = world.tiles[i][World.NO_OF_TILES_X - 1];
            batcher.drawSprite(left.position.x, left.position.y, 8, 8, Assets.double_ml);
            batcher.drawSprite(right.position.x, right.position.y, 8, 8, Assets.double_mr);
        }
    }

    private void drawGame() {
        batcher.beginBatch(Assets.pacSheet);
        drawWorld(batcher);
//        drawBoarder(batcher);
//        drawLegal(batcher);

        /* Draw fluid pac-man */
        batcher.setColor(Color.YELLOW);
        TextureRegion keyFrame = Assets.pacman.getKeyFrame(world.stateTime,
                Animation.ANIMATION_LOOPING);
        batcher.drawSprite(world.pixel.x, world.pixel.y,
                16, 16, world.rotation, keyFrame);

        /* Draw score */
        batcher.setColor(Color.WHITE);
        Assets.font.drawText(batcher, "SCORE:" + world.score,
                center.x, WORLD_HEIGHT - 350, 8, 8);
        batcher.endBatch();
    }

    @Override
    public void update(float deltaTime) {
        game.getInput().getKeyEvents();
        List<TouchEvent> events = game.getInput().getTouchEvents();

        handleInput(events, deltaTime);
        world.update(deltaTime);
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
        drawGame();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        Assets.pacSheet.dispose();
    }
}
