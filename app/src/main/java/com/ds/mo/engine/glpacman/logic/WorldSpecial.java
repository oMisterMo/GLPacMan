package com.ds.mo.engine.glpacman.logic;

import android.graphics.Point;
import android.util.Log;

import com.ds.mo.engine.common.Color;
import com.ds.mo.engine.common.Helper;
import com.ds.mo.engine.common.Vector2D;
import com.ds.mo.engine.framework.Input.TouchEvent;
import com.ds.mo.engine.glpacman.Assets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WorldSpecial {

    //Size of the original pacman arcade world
    public static final float WORLD_WIDTH = 224;
    public static final float WORLD_HEIGHT = 288;

    public static final int NO_OF_TILES_X = (int) (WORLD_WIDTH / Tile.TILE_WIDTH);      //28
    public static final int NO_OF_TILES_Y = (int) (WORLD_HEIGHT / Tile.TILE_HEIGHT);    //36

    public static final int WORLD_STATE_READY = 0;
    public static final int WORLD_STATE_RUNNING = 1;
    public static final int WORLD_STATE_WAIT = 2;
    public static final int WORLD_STATE_DEAD = 3;
    public static final int WORLD_STATE_COMPLETE = 4;
    public static final int WORLD_STATE_GAME_OVER = 5;
    public int state;                                   //Current state
    public float stateTime;                             //Time in current state

    public Tile[][] tiles;

    //---------------------------------------------------------------------------------------------
    private static final float READY_TIME = 3f;         //Time (seconds) during the ready screen
    public static final float DEATH_WAIT = 1f;          //seconds to wait after death
    private /*static final*/ float TIME_TO_MOVE = 0f;
//    private /*static final*/ float TIME_TO_MOVE = 1f;

    public Pacman pacman;
    public Enemy blinky;
    public Enemy pinky;
    public Enemy inky;
    public Enemy clyde;

    public ArrayList<Enemy> specialEnemies;

    private float elapsedTime;

    private static final float SWIPE_DISTANCE = 170;
    private Vector2D initial = new Vector2D();

    private int numDots, numEnergizers;
    private List<Point> allDots;    //When blinky comes off a tile, it knows if it is a dot
    private List<Point> allEnergizers;
    private List<Point> special;
    private final WorldListener listener;

    public interface WorldListener {
        void wa();

        void ka();

        void start();

        void death();
    }

    public WorldSpecial(WorldListener listener) {
        Log.d("World", "Loading world...");
        this.listener = listener;
        init();

        state = WORLD_STATE_READY;
//        state = WORLD_STATE_RUNNING;
        System.out.println("-----------------------------");
        System.out.println("*WORLD INFO*");
        System.out.println("Tile Width: " + Tile.TILE_WIDTH);
        System.out.println("Tile Height: " + Tile.TILE_HEIGHT);
        System.out.println("No of X Tiles: " + NO_OF_TILES_X);
        System.out.println("No of Y Tiles: " + NO_OF_TILES_Y);
        System.out.println("Total tiles: " + (NO_OF_TILES_X * NO_OF_TILES_Y));
        // TODO: 20/07/2018 numDots == 0 && numEnergizers == 0, until world.loadLevel();
        System.out.println("Num of Dots: " + numDots);
        System.out.println("Num of Energizers: " + numEnergizers);
        System.out.println("Max points: " + (numDots * Pacman.FOOD_SCORE
                + numEnergizers * Pacman.ENERGIZER_SCORE));
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
//        loadLevel();
//        loadWalls();

        //-----------------------------------------------------------------------------------------
        numDots = 0;
        numEnergizers = 0;
        allEnergizers = new ArrayList<>();
        allDots = new ArrayList<>();
        special = new ArrayList<>();
        specialEnemies = new ArrayList<>();

        //set pacman
//        pacman = new Pacman(tiles, allDots, allEnergizers, listener);
//        pacman.setPacmanPos(14, NO_OF_TILES_Y - 1 - 26);    //inverse y position
        //set blinky
        blinky = new Blinky(Tile.BLINKY, tiles, pacman, allDots, allEnergizers,
                14, NO_OF_TILES_Y - 1 - 14);
        blinky.setInHome(false);
        //set pinky
        pinky = new Pinky(Tile.PINKY, tiles, pacman, allDots, allEnergizers,
                14, NO_OF_TILES_Y - 1 - 17);
        pinky.setGhostPos(14, NO_OF_TILES_Y - 1 - 17, 0, 4, Tile.PINKY);
        pinky.setGhostHomeInterval(5);
        pinky.setBounce(-1);

        //set inky
        inky = new Inky(Tile.INKY, tiles, pacman, allDots, allEnergizers,
                12, NO_OF_TILES_Y - 1 - 17, blinky);
        inky.setGhostPos(12, NO_OF_TILES_Y - 1 - 17, 0, 4, Tile.INKY);
        inky.setGhostHomeInterval(10);
        //set clyde
        clyde = new Clyde(Tile.CLYDE, tiles, pacman, allDots, allEnergizers,
                16, NO_OF_TILES_Y - 1 - 17);
        clyde.setGhostPos(16, NO_OF_TILES_Y - 1 - 17, 0, 4, Tile.CLYDE);
        clyde.setGhostHomeInterval(15);

        stateTime = 0;
        elapsedTime = 0;

        tiles[WorldSpecial.NO_OF_TILES_Y - 1 - 17][0].teleportTile = true;
        tiles[WorldSpecial.NO_OF_TILES_Y - 1 - 17][NO_OF_TILES_X - 1].teleportTile = true;
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

                    //Make a few special tiles (must be a dot)
                    float num = Helper.Random();
                    if (num < 0.04) {
                        special.add(new Point(x, y));
                        tiles[y][x].special = true;
                    }
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

    public void loadIntersection() {
        Log.d("World", "Loading intersection...");
        JSONObject pacWorld = Assets.pacIntersection;
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

                int id = worldData[i] - 1;
                if (id == Tile.INTERSECTION) {
                    tiles[y][x].intersection = true;
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void printTiles() {
        for (int y = 0; y < NO_OF_TILES_Y; y++) {
            for (int x = 0; x < NO_OF_TILES_X; x++) {
                Tile t = tiles[y][x];
                Log.d("World", t.toString());
            }
        }
    }

    private boolean levelComplete() {
        return pacman.dotsEaten >= numDots && pacman.energizersEaten >= numEnergizers;
    }

    private boolean hitGhost() {
        //Could do pacman.pacmanTile.equals(blinky.ghost.Tile) however this is slightly optimized
        return (pacman.pacmanTile.equals(blinky.ghostTile.x, blinky.ghostTile.y) ||
                pacman.pacmanTile.equals(pinky.ghostTile.x, pinky.ghostTile.y) ||
                pacman.pacmanTile.equals(inky.ghostTile.x, inky.ghostTile.y) ||
                pacman.pacmanTile.equals(clyde.ghostTile.x, clyde.ghostTile.y)
        );
//        return false;
    }

    private boolean hitSpecial() {
        for (int i = special.size() - 1; i >= 0; i--) {
            Point p = special.get(i);
            if (pacman.pacmanTile.equals(p.x, p.y)) {
                //remove special tile from list of special since its a ghost now
                special.remove(p);
                return true;
            }
        }
        return false;
    }

    private boolean hitEnergizer() {
        // TODO: 20/07/2018 refactor hitEnergizer and create hitDots
        for (int i = allEnergizers.size() - 1; i >= 0; i--) {
            Point p = allEnergizers.get(i);
            if (pacman.pacmanTile.equals(p.x, p.y)) {
                pacman.score += Pacman.ENERGIZER_SCORE;
                pacman.energizersEaten++;
                allEnergizers.remove(p);
                return true;
            }
        }
        return false;
    }

    private void spawnRandomEnemy() {
        Enemy e = new Clyde(Tile.CLYDE, tiles, pacman, allDots, allEnergizers,
                pacman.pacmanTile.x, pacman.pacmanTile.y);
        e.setInHome(false);
        e.setRandomDir(tiles[pacman.pacmanTile.y][pacman.pacmanTile.x]);
        int num = Helper.Random(0, 3);
        switch (num) {
            case 0:
                e.color = new Color(Color.RED);
                break;
            case 1:
                e.color = new Color(1, 185 / 255f, 1, 1);
                break;
            case 2:
                e.color = new Color(0, 1, 1, 1);
                break;
            case 3:
                e.color = new Color(1, 185 / 255f, 80 / 255f, 1);
                break;

        }
//                e.setDir(pacman);

        specialEnemies.add(e);
    }

    private void reset() {
        System.out.println("reset");
        state = WORLD_STATE_READY;
        stateTime = 0;
        pacman.setPacmanPos(13, NO_OF_TILES_Y - 1 - 26);
//        pacman.rotation = 180;
        blinky.setGhostPos(14, NO_OF_TILES_Y - 1 - 14, Tile.BLINKY);
        blinky.setInHome(false);

        pinky.setGhostPos(14, NO_OF_TILES_Y - 1 - 17, 0, 4, Tile.PINKY);
        pinky.setInHome(true);
        pinky.setBounce(-1);
        pinky.setGhostHomeInterval(5);

        inky.setGhostPos(12, NO_OF_TILES_Y - 1 - 17, 0, 4, Tile.INKY);
        inky.setInHome(true);
        inky.setBounce(1);
        inky.setGhostHomeInterval(10);

        clyde.setGhostPos(16, NO_OF_TILES_Y - 1 - 17, 0, 4, Tile.CLYDE);
        clyde.setInHome(true);
        clyde.setBounce(1);
        clyde.setGhostHomeInterval(15);
    }

    public void move(List<TouchEvent> events) {
        int len = events.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = events.get(i);
            if (event.type == TouchEvent.TOUCH_DOWN) {
                initial.set(event.x, event.y);
            }
            if (event.type == TouchEvent.TOUCH_DRAGGED) {

            }
            if (event.type == TouchEvent.TOUCH_UP) {
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

    private void updatePacman(float deltaTime) {
        pacman.update(deltaTime);
    }

    private void updateGhosts(float deltaTime) {
        blinky.update(deltaTime);
        pinky.update(deltaTime);
        inky.update(deltaTime);
        clyde.update(deltaTime);
    }

    private void setBack() {
        int len = allDots.size();
        for (int i = 0; i < len; i++) {
            Point p = allDots.get(i);
            tiles[p.y][p.x].id = Tile.DOT;
        }
        len = allEnergizers.size();
        for (int i = 0; i < len; i++) {
            Point p = allEnergizers.get(i);
            tiles[p.y][p.x].id = Tile.ENERGIZER;
        }
    }

    private void handleCollision() {
//        if (hitSpecial()) {
//            System.out.println("HIT SPECIAL DOT");
//            spawnRandomEnemy();
//        }

        if (hitEnergizer()) {
            specialEnemies.clear();
//            setBack();
        }

//        if(hitDot()){
//
//        }
    }

    private void updateRunning(float deltaTime) {
//        if (levelComplete()) {
//            Log.d("World", "LEVEL COMPLETE");
//            return;
//        }
        if (hitGhost()) {
            Log.d("World", "Enemy HIT");
            state = WORLD_STATE_WAIT;
            stateTime = 0;
            return;
        }

        //Update
//        for (Enemy e : specialEnemies) {
//            e.update(deltaTime);
//        }

        /*slow down pacman*/
        elapsedTime += deltaTime;   //in seconds
        if (elapsedTime >= TIME_TO_MOVE) {
            updatePacman(deltaTime);
            updateGhosts(deltaTime);
            elapsedTime = 0;
        }

        //Handle collisions
        handleCollision();
    }

    public void update(float deltaTime) {
        switch (state) {
            case WORLD_STATE_READY:
                stateTime += deltaTime;
                //countdown from 3
                if (stateTime >= READY_TIME) {
                    state = WORLD_STATE_RUNNING;
                    stateTime = 0;
                }
                break;
            case WORLD_STATE_RUNNING:
                stateTime += deltaTime;
                updateRunning(deltaTime);
                break;
            case WORLD_STATE_WAIT:
                stateTime += deltaTime;
                if (stateTime >= DEATH_WAIT) {
                    state = WORLD_STATE_DEAD;
                    listener.death();
                    stateTime = 0;
                }
                break;
            case WORLD_STATE_DEAD:
                stateTime += deltaTime;
                //Play death animation
                if (stateTime > 2f) {
                    //Restart after animation
                    reset();
                }
                break;
            case WORLD_STATE_COMPLETE:
                stateTime += deltaTime;

                break;
            case WORLD_STATE_GAME_OVER:
                stateTime += deltaTime;
                break;
        }
    }
}
