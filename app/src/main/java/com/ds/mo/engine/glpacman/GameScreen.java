package com.ds.mo.engine.glpacman;

import android.util.Log;

import com.ds.mo.engine.android.SpriteBatcher;
import com.ds.mo.engine.android.TextureRegion;
import com.ds.mo.engine.common.Animation;
import com.ds.mo.engine.common.Camera2D;
import com.ds.mo.engine.common.Color;
import com.ds.mo.engine.common.Helper;
import com.ds.mo.engine.common.Vector2D;
import com.ds.mo.engine.framework.GLScreen;
import com.ds.mo.engine.framework.Game;
import com.ds.mo.engine.framework.Input.TouchEvent;
import com.ds.mo.engine.glpacman.logic.Enemy;
import com.ds.mo.engine.glpacman.logic.Pacman;
import com.ds.mo.engine.glpacman.logic.Tile;
import com.ds.mo.engine.glpacman.logic.World;
import com.ds.mo.engine.glpacman.logic.World.WorldListener;

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

    private WorldListener worldListener;
    private World world;
    private Color tileColor;
    private final Vector2D center;

    private Vector2D readyPos;
    private Vector2D scorePos;

    private float time = 0;

    public GameScreen(Game game) {
        super(game);
        Log.d("GameScreen", "GameScreen constructor...");
        camera = new Camera2D(glGraphics, WORLD_WIDTH, WORLD_HEIGHT);
        batcher = new SpriteBatcher(glGraphics, 2500);   //max sprites

        center = new Vector2D((World.NO_OF_TILES_X * Tile.TILE_WIDTH) / 2,
                (World.NO_OF_TILES_Y * Tile.TILE_HEIGHT) / 2);
        camera.position.x = center.x;
        camera.position.y = center.y;  //- 50 to align world top
        camera.zoom = 0.62f;

        tileColor = new Color(139 / 255f, 3 / 255f, 12 / 255f, 1f);
        worldListener = new WorldListener() {
            @Override
            public void wa() {
                Assets.playSound(Assets.wa);
            }

            @Override
            public void ka() {
                Assets.playSound(Assets.ka);
            }

            @Override
            public void start() {

            }

            @Override
            public void death() {
                Assets.playSound(Assets.deathSfx);
            }
        };
        world = new World(worldListener);
        world.loadLevel();
        world.loadIntersection();

        //Get desired tile so we can set the position y position of the text
        Tile t = world.tiles[World.NO_OF_TILES_Y - 1 - 20][13];
        readyPos = new Vector2D(center.x, t.position.y);
        t = world.tiles[World.NO_OF_TILES_Y - 1 - 2][22];
        scorePos = new Vector2D(center.x, t.position.y);

        Log.d("GameScreen", "End GameScreen constructor...");
    }

    private void doLerp(float deltaTime) {
        if (world.specialEnemies.size() >= 1) {
            time += deltaTime * 0.01f;
            if (time >= 1) {
                time = 0;   //restart cycle
//            time = 1;     //cap
            }
            Log.d("GS", "time: " + time);
            //time cycles between 0 - 1
            tileColor.lerp(Helper.Random(), Helper.Random(), Helper.Random(), 1f, time);
        } else {
            //Red tiles if no special ghosts
            tileColor.set(139 / 255f, 3 / 255f, 12 / 255f, 1f);
        }
//        tileColor.lerp(0.8f, 0.5f, 0.1f, 1f, time);
    }

    private void handleInput(List<TouchEvent> events, float deltaTime) {
        //Hand GUI touches
        int len = events.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = events.get(i);
            touchPos.set(event.x, event.y);
            camera.touchToWorld(touchPos);
            if (event.type == TouchEvent.TOUCH_UP) {
                //Touch is greater than top of world
                if (touchPos.y > 8 * World.NO_OF_TILES_Y) {
                    world.blinky.switchState();
                    world.pinky.switchState();
                    world.inky.switchState();
                    world.clyde.switchState();
                }
            }
        }
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
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x,
                                t.position.y,
                                8, 8,
                                Assets.double_tl);
                        break;
                    case Tile.DOUBLE_TM:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x,
                                t.position.y,
                                8, 8,
                                Assets.double_tm);
                        break;
                    case Tile.DOUBLE_TR:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.double_tr);
                        break;
                    case Tile.DOUBLE_ML:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.double_ml);
                        break;
                    case Tile.DOUBLE_MR:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.double_mr);
                        break;
                    case Tile.DOUBLE_BL:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.double_bl);
                        break;
                    case Tile.DOUBLE_BM:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.double_bm);
                        break;
                    case Tile.DOUBLE_BR:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.double_br);
                        break;
                    //***Line***
                    case Tile.LINE_TL:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.line_tl);
                        break;
                    case Tile.LINE_TM:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.line_tm);
                        break;
                    case Tile.LINE_TR:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.line_tr);
                        break;
                    case Tile.LINE_ML:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.line_ml);
                        break;
                    case Tile.LINE_MR:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.line_mr);
                        break;
                    case Tile.LINE_BL:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.line_bl);
                        break;
                    case Tile.LINE_BM:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.line_bm);
                        break;
                    case Tile.LINE_BR:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.line_br);
                        break;
                    //Hor
                    case Tile.HOR_TL:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.hor_tl);
                        break;
                    case Tile.HOR_TR:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.hor_tr);
                        break;
                    case Tile.HOR_BL:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.hor_bl);
                        break;
                    case Tile.HOR_BR:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.hor_br);
                        break;
                    //Ver
                    case Tile.VER_TL:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.ver_tl);
                        break;
                    case Tile.VER_TR:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.ver_tr);
                        break;
                    case Tile.VER_BL:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.ver_bl);
                        break;
                    case Tile.VER_BR:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.ver_br);
                        break;
                    //Square
                    case Tile.SQUARE_TL:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.square_tl);
                        break;
                    case Tile.SQUARE_TR:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.square_tr);
                        break;
                    case Tile.SQUARE_BL:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.square_bl);
                        break;
                    case Tile.SQUARE_BR:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.square_br);
                        break;
                    case Tile.HOME_L:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.home_l);
                        break;
                    case Tile.HOME_R:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.home_r);
                        break;
                    case Tile.LINE_BIG_TL:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.line_big_tl);
                        break;
                    case Tile.LINE_BIG_TR:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.line_big_tr);
                        break;
                    case Tile.LINE_BIG_BL:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.line_big_bl);
                        break;
                    case Tile.LINE_BIG_BR:
                        batcher.setColor(tileColor);
                        batcher.drawSprite(t.position.x, t.position.y,
                                8, 8,
                                Assets.line_big_br);
                        break;
                    case Tile.DOT:
                        batcher.setColor(Color.TAN);
//                        if (t.special) {
////                            batcher.setColor(Helper.Random(), Helper.Random(), Helper.Random(), 1);
//                            batcher.setColor(Color.PINK);
//                        }
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
//                        batcher.setColor(tileColor);
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

    private void drawScore(SpriteBatcher batcher) {
        /* Draw score */
        // TODO: 17/07/2018 Format position 0000+score
        batcher.setColor(Color.WHITE);
        Assets.font.drawText(batcher, "SCORE:" + world.pacman.score,
                scorePos.x, scorePos.y, 8, 8);
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

    private void drawReady(SpriteBatcher batcher) {
        batcher.setColor(Color.YELLOW);
        Assets.font.drawText(batcher, "READY!", readyPos.x, readyPos.y, 10, 10);
    }

    private void drawPacman(SpriteBatcher batcher) {
        /* Draw fluid pac-man */
        batcher.setColor(Color.YELLOW);
        TextureRegion keyFrame = Assets.pacman.getKeyFrame(world.pacman.stateTime,
                Animation.ANIMATION_LOOPING);
        batcher.drawSprite(world.pacman.pixel.x, world.pacman.pixel.y,
                Pacman.PACMAN_WIDTH, Pacman.PACMAN_HEIGHT, world.pacman.rotation, keyFrame);
    }

    private void drawGhosts(SpriteBatcher batcher) {
        /* BLINKY */
        //draw body
        batcher.setColor(world.blinky.color);
        batcher.drawSprite(world.blinky.pixel.x, world.blinky.pixel.y,
                Enemy.GHOST_WIDTH, Enemy.GHOST_HEIGHT,
                Assets.ghost.getKeyFrame(world.blinky.enemyStateTime, Animation.ANIMATION_LOOPING));
        //draw eyes
        batcher.setColor(Color.WHITE);
        switch (world.blinky.ghostDir) {
            case UP:
                batcher.drawSprite(world.blinky.pixel.x, world.blinky.pixel.y,
                        Enemy.GHOST_WIDTH, Enemy.GHOST_HEIGHT, Assets.eyesU);
                break;
            case DOWN:
                batcher.drawSprite(world.blinky.pixel.x, world.blinky.pixel.y,
                        Enemy.GHOST_WIDTH, Enemy.GHOST_HEIGHT, Assets.eyesD);
                break;
            case LEFT:
                batcher.drawSprite(world.blinky.pixel.x, world.blinky.pixel.y,
                        Enemy.GHOST_WIDTH, Enemy.GHOST_HEIGHT, Assets.eyesL);
                break;
            case RIGHT:
                batcher.drawSprite(world.blinky.pixel.x, world.blinky.pixel.y,
                        Enemy.GHOST_WIDTH, Enemy.GHOST_HEIGHT, Assets.eyesR);
                break;
        }
        /* PINKY */
        batcher.setColor(world.pinky.color);
        batcher.drawSprite(world.pinky.pixel.x, world.pinky.pixel.y,
                Enemy.GHOST_WIDTH, Enemy.GHOST_HEIGHT,
                Assets.ghost.getKeyFrame(world.pinky.enemyStateTime, Animation.ANIMATION_LOOPING));
        batcher.setColor(Color.WHITE);
        switch (world.pinky.ghostDir) {
            case UP:
                batcher.drawSprite(world.pinky.pixel.x, world.pinky.pixel.y,
                        Enemy.GHOST_WIDTH, Enemy.GHOST_HEIGHT, Assets.eyesU);
                break;
            case DOWN:
                batcher.drawSprite(world.pinky.pixel.x, world.pinky.pixel.y,
                        Enemy.GHOST_WIDTH, Enemy.GHOST_HEIGHT, Assets.eyesD);
                break;
            case LEFT:
                batcher.drawSprite(world.pinky.pixel.x, world.pinky.pixel.y,
                        Enemy.GHOST_WIDTH, Enemy.GHOST_HEIGHT, Assets.eyesL);
                break;
            case RIGHT:
                batcher.drawSprite(world.pinky.pixel.x, world.pinky.pixel.y,
                        Enemy.GHOST_WIDTH, Enemy.GHOST_HEIGHT, Assets.eyesR);
                break;
        }
        /* INKY */
        batcher.setColor(world.inky.color);
        batcher.drawSprite(world.inky.pixel.x, world.inky.pixel.y,
                Enemy.GHOST_WIDTH, Enemy.GHOST_HEIGHT,
                Assets.ghost.getKeyFrame(world.inky.enemyStateTime, Animation.ANIMATION_LOOPING));
        batcher.setColor(Color.WHITE);
        switch (world.inky.ghostDir) {
            case UP:
                batcher.drawSprite(world.inky.pixel.x, world.inky.pixel.y,
                        Enemy.GHOST_WIDTH, Enemy.GHOST_HEIGHT, Assets.eyesU);
                break;
            case DOWN:
                batcher.drawSprite(world.inky.pixel.x, world.inky.pixel.y,
                        Enemy.GHOST_WIDTH, Enemy.GHOST_HEIGHT, Assets.eyesD);
                break;
            case LEFT:
                batcher.drawSprite(world.inky.pixel.x, world.inky.pixel.y,
                        Enemy.GHOST_WIDTH, Enemy.GHOST_HEIGHT, Assets.eyesL);
                break;
            case RIGHT:
                batcher.drawSprite(world.inky.pixel.x, world.inky.pixel.y,
                        Enemy.GHOST_WIDTH, Enemy.GHOST_HEIGHT, Assets.eyesR);
                break;
        }
        /* CLYDE */
        batcher.setColor(world.clyde.color);
        batcher.drawSprite(world.clyde.pixel.x, world.clyde.pixel.y,
                Enemy.GHOST_WIDTH, Enemy.GHOST_HEIGHT,
                Assets.ghost.getKeyFrame(world.clyde.enemyStateTime, Animation.ANIMATION_LOOPING));
        batcher.setColor(Color.WHITE);
        switch (world.clyde.ghostDir) {
            case UP:
                batcher.drawSprite(world.clyde.pixel.x, world.clyde.pixel.y,
                        Enemy.GHOST_WIDTH, Enemy.GHOST_HEIGHT, Assets.eyesU);
                break;
            case DOWN:
                batcher.drawSprite(world.clyde.pixel.x, world.clyde.pixel.y,
                        Enemy.GHOST_WIDTH, Enemy.GHOST_HEIGHT, Assets.eyesD);
                break;
            case LEFT:
                batcher.drawSprite(world.clyde.pixel.x, world.clyde.pixel.y,
                        Enemy.GHOST_WIDTH, Enemy.GHOST_HEIGHT, Assets.eyesL);
                break;
            case RIGHT:
                batcher.drawSprite(world.clyde.pixel.x, world.clyde.pixel.y,
                        Enemy.GHOST_WIDTH, Enemy.GHOST_HEIGHT, Assets.eyesR);
                break;
        }
    }

    private void drawSpecial(SpriteBatcher batcher) {
        for (Enemy e : world.specialEnemies) {
            batcher.setColor(e.color);
            batcher.drawSprite(e.pixel.x, e.pixel.y,
                    Enemy.GHOST_WIDTH, Enemy.GHOST_HEIGHT,
                    Assets.ghost.getKeyFrame(e.enemyStateTime, Animation.ANIMATION_LOOPING));
            //draw eyes
            batcher.setColor(Color.WHITE);
            switch (e.ghostDir) {
                case UP:
                    batcher.drawSprite(e.pixel.x, e.pixel.y,
                            Enemy.GHOST_WIDTH, Enemy.GHOST_HEIGHT, Assets.eyesU);
                    break;
                case DOWN:
                    batcher.drawSprite(e.pixel.x, e.pixel.y,
                            Enemy.GHOST_WIDTH, Enemy.GHOST_HEIGHT, Assets.eyesD);
                    break;
                case LEFT:
                    batcher.drawSprite(e.pixel.x, e.pixel.y,
                            Enemy.GHOST_WIDTH, Enemy.GHOST_HEIGHT, Assets.eyesL);
                    break;
                case RIGHT:
                    batcher.drawSprite(e.pixel.x, e.pixel.y,
                            Enemy.GHOST_WIDTH, Enemy.GHOST_HEIGHT, Assets.eyesR);
                    break;
            }
        }
    }

    private void drawDeath(SpriteBatcher batcher) {
        batcher.setColor(Color.YELLOW);
        TextureRegion keyFrame = Assets.pacmanDeath.getKeyFrame(world.stateTime,
                Animation.ANIMATION_NON_LOOPING);
        batcher.drawSprite(world.pacman.pixel.x, world.pacman.pixel.y,
                Pacman.PACMAN_WIDTH, Pacman.PACMAN_HEIGHT, keyFrame);
    }

    private void drawTest(SpriteBatcher batcher) {
        batcher.setColor(Color.ORANGE);
        for (int y = 0; y < World.NO_OF_TILES_Y; y++) {
            for (int x = 0; x < World.NO_OF_TILES_X; x++) {
                Tile t = world.tiles[y][x];
                if (t.intersection) {
                    batcher.drawSprite(t.position.x, t.position.y,
                            8, 8,
                            Assets.energizer);
                }
            }
        }
    }

    private void drawGame() {
        batcher.beginBatch(Assets.pacSheet);
        drawWorld(batcher);
        drawScore(batcher);
//        drawBoarder(batcher);
//        drawLegal(batcher);

        switch (world.state) {
            case World.WORLD_STATE_READY:
                drawReady(batcher);
            case World.WORLD_STATE_RUNNING:
            case World.WORLD_STATE_WAIT:
                drawPacman(batcher);
                drawGhosts(batcher);
//                drawSpecial(batcher);
                break;
            case World.WORLD_STATE_DEAD:
                drawDeath(batcher);
                break;
            case World.WORLD_STATE_COMPLETE:
                break;
            case World.WORLD_STATE_GAME_OVER:
                break;
        }
//        drawTest(batcher);
        batcher.endBatch();
    }

    @Override
    public void update(float deltaTime) {
        game.getInput().getKeyEvents();
        List<TouchEvent> events = game.getInput().getTouchEvents();

        doLerp(deltaTime);

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
