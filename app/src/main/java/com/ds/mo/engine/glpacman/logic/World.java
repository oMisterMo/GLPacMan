package com.ds.mo.engine.glpacman.logic;

import android.graphics.Point;
import android.util.Log;

import com.ds.mo.engine.common.Vector2D;
import com.ds.mo.engine.framework.Input;
import com.ds.mo.engine.framework.Input.TouchEvent;
import com.ds.mo.engine.glpacman.Assets;
import com.ds.mo.engine.glpacman.GameScreen;
import com.ds.mo.engine.glpacman.ReadyScreen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class World {

    //Size of the original pacman arcade world
    public static final float WORLD_WIDTH = 224;
    public static final float WORLD_HEIGHT = 288;

    public static final int NO_OF_TILES_X = (int) (WORLD_WIDTH / Tile.TILE_WIDTH);      //28
    public static final int NO_OF_TILES_Y = (int) (WORLD_HEIGHT / Tile.TILE_HEIGHT);    //36
    public Tile[][] tiles;

    //---------------------------------------------------------------------------------------------
    private static final float TIME_TO_MOVE = 0f;

    public Pacman pacman;

    private float elapsedTime;

    private static final float SWIPE_DISTANCE = 170;
    private Vector2D initial = new Vector2D();

    private int numDots, numEnergizers;
    private List<Point> allDots;    //When blinky comes off a tile, it knows if it is a dot
    private List<Point> allEnergizers;
    private final WorldListener listener;

    public interface WorldListener {
        void wa();

        void ka();

        void start();

        void die();
    }

    public World(WorldListener listener) {
        Log.d("World", "Loading world...");
        this.listener = listener;
        init();

        System.out.println("-----------------------------");
        System.out.println("*WORLD INFO*");
        System.out.println("Tile Width: " + Tile.TILE_WIDTH);
        System.out.println("Tile Height: " + Tile.TILE_HEIGHT);
        System.out.println("No of X Tiles: " + NO_OF_TILES_X);
        System.out.println("No of Y Tiles: " + NO_OF_TILES_Y);
        System.out.println("Total tiles: " + (NO_OF_TILES_X * NO_OF_TILES_Y));
        System.out.println("Num of Dots: " + numDots);
        System.out.println("Num of Energizers: " + numEnergizers);
//        System.out.println("Num of Food: " + numFood);
//        System.out.println("Num of Energizers: " + numEnergizers);
        System.out.println("-----------------------------");
        System.out.println("World loaded...");

    }

    private void init() {
        tiles = new Tile[NO_OF_TILES_Y][NO_OF_TILES_X];
        nullTiles();    //sets to null
        initTiles();    //create empty tiles
//        setEmpty();     //sets to empty (not needed here)

        setId(Tile.EMPTY);
//        setWallType(Tile.DOUBLE_BL);
//        loadLevel();
//        loadWalls();

        //-----------------------------------------------------------------------------------------
        numDots = 0;
        numEnergizers = 0;
        allEnergizers = new ArrayList<>();
        allDots = new ArrayList<>();

        pacman = new Pacman(tiles, allDots, allEnergizers, listener);
        pacman.setPacmanPos(14, NO_OF_TILES_Y - 26);    //flip y position
        elapsedTime = 0;

        tiles[18][0].teleportTile = true;
        tiles[18][NO_OF_TILES_X - 1].teleportTile = true;
    }

    /**
     * Sets all tiles to null
     */
    private void nullTiles() {
        System.out.println("Setting all tiles to null...");
        for (int y = 0; y < NO_OF_TILES_Y; y++) {
            for (int x = 0; x < NO_OF_TILES_X; x++) {
                tiles[y][x] = null;
            }
        }
    }

    /**
     * Called from the constructor, sets the position of all tiles
     */
    private void initTiles() {
        System.out.println("Initializing tiles...");
        for (int y = 0; y < NO_OF_TILES_Y; y++) {
            for (int x = 0; x < NO_OF_TILES_X; x++) {
//                tiles[y][x] = new Tile(x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT,
//                        Tile.TILE_WIDTH, Tile.TILE_HEIGHT);

                tiles[y][x] = new Tile(
                        x * Tile.TILE_WIDTH + Tile.TILE_WIDTH / 2,
                        y * Tile.TILE_HEIGHT + Tile.TILE_HEIGHT / 2,
                        new Point(x, y));
            }
        }
    }

    /**
     * Sets all tiles to empty
     */
    private void setEmpty() {
        System.out.println("Setting the id of all tiles to EMPTY");
        for (int y = 0; y < NO_OF_TILES_Y; y++) {
            for (int x = 0; x < NO_OF_TILES_X; x++) {
                tiles[y][x].id = Tile.EMPTY;
            }
        }
    }

    private void setId(int id) {
        System.out.println("Setting the id of all tiles to " + id);
        for (int y = 0; y < NO_OF_TILES_Y; y++) {
            for (int x = 0; x < NO_OF_TILES_X; x++) {
                tiles[y][x].id = id;
            }
        }
    }

    /**
     * Loads the level data from the json object
     * <p>
     * This method is called after the completion of the World constructor
     */
    public void loadLevel() {
        Log.d("World", "Loading level...");
        JSONObject pacWorld = Assets.pacWorld;
        try {
            Log.d("Object", "******************************************************************");
            //Get data from JSON file
            JSONArray data = pacWorld.getJSONArray("layers").getJSONObject(0)
                    .getJSONArray("data");
            Log.d("World", data.toString());
            //Load world data into an array
            int len = data.length();
            Log.d("World", "data.length() -> " + len);
            int[] worldData = new int[len];
            for (int i = 0; i < len; i++) {
                worldData[i] = (int) data.get(i);
            }
            //Init tiles based on id
            for (int i = 0; i < len; i++) {
                int x = i % NO_OF_TILES_X;
                int y = NO_OF_TILES_Y - (i / NO_OF_TILES_X) - 1;

                //Tiled uses global id to determine the run time id (so - 1 to get actual id)
                tiles[y][x].id = worldData[i] - 1;

                //Set active tiles based on id
                int id = tiles[y][x].id;
                if (id == Tile.DOT || id == Tile.ENERGIZER || id == Tile.PACMAN ||
                        id == Tile.ACTIVE) {
                    tiles[y][x].legal = true;
                }
                if (id == Tile.DOT) {
                    allDots.add(new Point(x, y));
                    numDots += 1;
                }
                if (id == Tile.ENERGIZER) {
                    allEnergizers.add(new Point(x, y));
                    numEnergizers += 1;
                }

                if (id == Tile.PACMAN) {
                    //Tile is already legal, set pacman's position
                    pacman.setPacmanPos(x, y);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

//        tiles[1][1].id = Tile.HOME_L;
//        printTiles();

//        for (int y = 0; y < NO_OF_TILES_Y; y++) {
//            for (int x = 0; x < NO_OF_TILES_X; x++) {
//                int pixel = y;
//                //Get color of pixel in testLevel array
//                int a = ((pixel & 0xff000000) >>> 24);
//                int r = ((pixel & 0x00ff0000) >>> 16);
//                int g = ((pixel & 0x0000ff00) >>> 8);
//                int b = ((pixel & 0x000000ff));
//                Tile t = tiles[y][x];
//
//                //Depending on the color of a pixel, set the tile
//                if (r == 0 && g == 0 && b == 0) {
//                    //System.out.println("Black block at: " + x + " " + y);
//                    t.id = Tile.EMPTY;
//                    continue;   //We found a tile, do not need to check others
//                }
//                //Active (white tile)
//                if (r == 255 && g == 255 && b == 255) {
//                    //System.out.println("Wall block at: " + x + " " + y);
//                    t.id = Tile.ACTIVE;
//                    t.legal = true;
//                    continue;
//                }
//                //Food (gray tile)
//                if (r == 120 && g == 120 && b == 120) {
//                    t.id = Tile.FOOD;
//                    t.legal = true;
////                    numFood++;
//                    continue;
//                }
//                //Power up (green tile)
//                if (r == 0 && g == 255 && b == 0) {
//                    t.id = Tile.POWER_UP;
//                    t.legal = true;
////                    numEnergizers++;
//                    continue;
//                }
//                //Player (yellow tile)
//                if (r == 255 && g == 255 && b == 0) {
//                    t.id = Tile.PLAYER;
//                    t.legal = true;
//
////                    System.out.println("player position: " + x + ", " + y);
////                    pacmanTile.setLocation(x, y);
////                    temp.setLocation(x, y);
////
////                    this.pixel.setLocation(pacmanTile.x * Tile.TILE_WIDTH + scaledNum(3),
////                            pacmanTile.y * Tile.TILE_HEIGHT + scaledNum(4));
////                    this.centerPoint.setLocation(pacmanTile.x * Tile.TILE_WIDTH + scaledNum(3),
////                            pacmanTile.y * Tile.TILE_HEIGHT + scaledNum(4));
//                    continue;
//                }
//            }
//        }
    }

    public void printTiles() {
        for (int y = 0; y < NO_OF_TILES_Y; y++) {
            for (int x = 0; x < NO_OF_TILES_X; x++) {
                Tile t = tiles[y][x];
                Log.d("World", t.toString());
            }
        }
    }

    public void move(List<TouchEvent> events) {
        int len = events.size();
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = events.get(i);
            if (event.type == Input.TouchEvent.TOUCH_DOWN) {
                initial.set(event.x, event.y);
            }
            if (event.type == Input.TouchEvent.TOUCH_DRAGGED) {

            }
            if (event.type == Input.TouchEvent.TOUCH_UP) {
                float finalX = event.x;
                float finalY = event.y;
                float dx = finalX - initial.x;
                float dy = finalY - initial.y;

                if (dx > SWIPE_DISTANCE) {
                    System.out.println("Right");
                    if (initial.x == 0) return;//Stops it from moving right at the beginning (HACK)

                    pacman.swipeRight();
                } else if (dx < -SWIPE_DISTANCE) {
                    System.out.println("Left");

                    pacman.swipeLeft();
                }
                //Remove the else to allow for diagonal movements
                else if (dy > SWIPE_DISTANCE) {
                    System.out.println("Down");

                    pacman.swipeDown();
                } else if (dy < -SWIPE_DISTANCE) {
                    System.out.println("Up");

                    pacman.swipeUp();
                }
            }
        }
    }

    //---------------------------------------------------------------------------------------------
//    private void updateScore() {
//        Tile t = tiles[pacmanTile.y][pacmanTile.x];
//        switch (t.id) {
//            case Tile.DOT:
////                System.out.println("FOOD!");
//                Assets.playSound(Assets.waka);
//                score += FOOD_SCORE;
//                break;
//            case Tile.ENERGIZER:
////                System.out.println("Energizer!");
//                score += ENERGIZER_SCORE;
//                break;
//        }
//    }

    public void update(float deltaTime) {
        /*slow down pacman*/
        elapsedTime += deltaTime;   //in seconds
        if (elapsedTime >= TIME_TO_MOVE) {
//            System.out.println("move");
//            movePacman(deltaTime);
            pacman.update(deltaTime);
            elapsedTime = 0;
        }
    }
}
