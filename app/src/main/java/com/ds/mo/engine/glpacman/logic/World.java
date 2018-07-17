package com.ds.mo.engine.glpacman.logic;

import android.graphics.Point;
import android.util.Log;

import com.ds.mo.engine.common.Vector2D;
import com.ds.mo.engine.framework.Input.TouchEvent;
import com.ds.mo.engine.glpacman.Assets;
import com.ds.mo.engine.glpacman.GameScreen;
import com.ds.mo.engine.glpacman.ReadyScreen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class World {

    //Size of the original pacman arcade world
    public static final float WORLD_WIDTH = 224;
    public static final float WORLD_HEIGHT = 288;

    public static final int NO_OF_TILES_X = (int) (WORLD_WIDTH / Tile.TILE_WIDTH);      //28
    public static final int NO_OF_TILES_Y = (int) (WORLD_HEIGHT / Tile.TILE_HEIGHT);    //36
    public Tile[][] tiles;

    //---------------------------------------------------------------------------------------------
    public static final int FOOD_SCORE = 10;
    public static final int ENERGIZER_SCORE = 50;

    public int score;
    private String totalScore;
    private int scoreX, scoreY;

    //Pacman
    public enum DIR {
        UP, DOWN, LEFT, RIGHT, STOP, NA
    }

    public float stateTime;
    public Point pixel;
    private Point pacmanTile;
    private Point temp; //old position (temp variable)
    private DIR currentDir = DIR.STOP;
    private DIR turnBuffer = DIR.NA;
    private Point centerPoint;  //used as a temp variable
    private float TIME_TO_MOVE = 0f;   // TODO: 07/06/2018 make final float
    public int rotation;

    private float elapsedTime;

    private static final float SWIPE_DISTANCE = 170;
    private Vector2D initial = new Vector2D();


    public World() {
        Log.d("World", "Loading world...");
        init();

        System.out.println("-----------------------------");
        System.out.println("*WORLD INFO*");
        System.out.println("Tile Width: " + Tile.TILE_WIDTH);
        System.out.println("Tile Height: " + Tile.TILE_HEIGHT);
        System.out.println("No of X Tiles: " + NO_OF_TILES_X);
        System.out.println("No of Y Tiles: " + NO_OF_TILES_Y);
        System.out.println("Total tiles: " + (NO_OF_TILES_X * NO_OF_TILES_Y));
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
        stateTime = 0;
        pixel = new Point();
        pacmanTile = new Point();
        temp = new Point();
        centerPoint = new Point();
        rotation = 0;       //facing right
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
                        Tile.TILE_WIDTH / 2 + x * Tile.TILE_WIDTH,
                        Tile.TILE_HEIGHT / 2 + y * Tile.TILE_HEIGHT);
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
                //int y = (i / NO_OF_TILES_X);
//                System.out.println("Y -> "+y);

                //Tiled uses global id to determine the run time id
                tiles[y][x].id = worldData[i] - 1;

                //Set active tiles based on id
                int id = tiles[y][x].id;
                if (id == Tile.DOT || id == Tile.ENERGIZER || id == Tile.PACMAN ||
                        id == Tile.ACTIVE) {
                    tiles[y][x].legal = true;
                }
                if (id == Tile.PACMAN) {
                    pacmanTile.set(x, y);
                    temp.set(x, y);

                    this.pixel.set(pacmanTile.x * Tile.TILE_WIDTH + 3,
                            pacmanTile.y * Tile.TILE_HEIGHT + 4);
                    this.centerPoint.set(pacmanTile.x * Tile.TILE_WIDTH + 3,
                            pacmanTile.y * Tile.TILE_HEIGHT + 4);
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

    //---------------------------------------------------------------------------------------------
    private void updateScore() {
        Tile t = tiles[pacmanTile.y][pacmanTile.x];
        switch (t.id) {
            case Tile.DOT:
//                System.out.println("FOOD!");
                Assets.playSound(Assets.waka);
                score += FOOD_SCORE;
                break;
            case Tile.ENERGIZER:
//                System.out.println("Energizer!");
                score += ENERGIZER_SCORE;
                break;
        }
    }

    private void setTile(int x, int y, int id) {
        tiles[y][x].id = id;
    }

    public void moveUp() {
        //Store current position
        temp.set(pacmanTile.x, pacmanTile.y);
        pacmanTile.y += 1;

        //Handle collision with item
        updateScore();

        //Update the new tile id then set old tile empty
        setTile(pacmanTile.x, pacmanTile.y, Tile.PACMAN);
        setTile(temp.x, temp.y, Tile.ACTIVE);
    }

    private void moveDown() {
        temp.set(pacmanTile.x, pacmanTile.y);
        pacmanTile.y -= 1;
        updateScore();
        setTile(pacmanTile.x, pacmanTile.y, Tile.PACMAN);
        setTile(temp.x, temp.y, Tile.ACTIVE);
    }

    public void moveLeft() {
        temp.set(pacmanTile.x, pacmanTile.y);
        pacmanTile.x -= 1;
        updateScore();
        setTile(pacmanTile.x, pacmanTile.y, Tile.PACMAN);
        setTile(temp.x, temp.y, Tile.ACTIVE);
    }

    public void moveRight() {
        temp.set(pacmanTile.x, pacmanTile.y);
        pacmanTile.x += 1;
        updateScore();
        setTile(pacmanTile.x, pacmanTile.y, Tile.PACMAN);
        setTile(temp.x, temp.y, Tile.ACTIVE);
    }

    private void handleTurnBuffer() {
        if (turnBuffer != DIR.NA) {
            switch (turnBuffer) {
                case UP:
                    if (pixelToTileAbove(pixel.x, pixel.y).legal) {
//                        System.out.println("consuming UP turn");
                        currentDir = DIR.UP;
                        turnBuffer = DIR.NA;
                    }
                    break;
                case DOWN:
                    if (pixelToTileBelow(pixel.x, pixel.y).legal) {
//                        System.out.println("consuming DOWN turn");
                        currentDir = DIR.DOWN;
                        turnBuffer = DIR.NA;
                    }
                    break;
                case LEFT:
                    if (pixelToTileLeft(pixel.x, pixel.y).legal) {
//                        System.out.println("consuming LEFT turn");
                        currentDir = DIR.LEFT;
                        turnBuffer = DIR.NA;
                    }
                    break;
                case RIGHT:
                    if (pixelToTileRight(pixel.x, pixel.y).legal) {
//                        System.out.println("consuming RIGHT turn");
                        currentDir = DIR.RIGHT;
                        turnBuffer = DIR.NA;
                    }
                    break;
            }
        }
    }

    // TODO: 08/06/2018 make swipe up/down/left/right method, move rest to GameScreen
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

                //SWIPE RIGHT
                if (dx > SWIPE_DISTANCE) {
                    System.out.println("Right");
                    if (initial.x == 0) return;//Stops it from moving right at the beginning (HACK)

                    if (pixelToTile(pixel.x, pixel.y).teleportTile) {
                        System.out.println("We are on left telport tile");
                        return;
                    }
                    turnBuffer = DIR.RIGHT;
                    if ((pixelToTileRight(pixel.x, pixel.y).legal)) {
                        currentDir = DIR.RIGHT;
                        System.out.println("currentDir: " + currentDir);
                    } else {

                    }
                    //SWIPE LEFT
                } else if (dx < -SWIPE_DISTANCE) {
                    System.out.println("Left");

                    if (pixelToTile(pixel.x, pixel.y).teleportTile) {
                        System.out.println("We are on left telport tile");
                        return;
                    }
                    turnBuffer = DIR.LEFT;
                    if ((pixelToTileLeft(pixel.x, pixel.y).legal)) {
                        currentDir = DIR.LEFT;
                        System.out.println("currentDir: " + currentDir);
                    } else {

                    }
                }
                //Remove the else to allow for diagonal movements
                else if (dy > SWIPE_DISTANCE) {
                    System.out.println("Down");

                    turnBuffer = DIR.DOWN;
                    if ((pixelToTileBelow(pixel.x, pixel.y).legal)) {
                        currentDir = DIR.DOWN;
                        System.out.println("currentDir: " + currentDir);
                    } else {

                    }
                } else if (dy < -SWIPE_DISTANCE) {
                    System.out.println("Up");

                    turnBuffer = DIR.UP;
                    //Get tile above -> if its not active ignore movement
                    if ((pixelToTileAbove(pixel.x, pixel.y).legal)) {
                        currentDir = DIR.UP;
                        System.out.println("currentDir: " + currentDir);
                    } else {
                        System.out.println("cant press up, tile above blocked");
                    }
                }
            }
        }
    }

    private Tile pixelToTile(int x, int y) {
//        x = (int) Math.floor(x / Tile.TILE_WIDTH);
//        y = (int) Math.floor(y / Tile.TILE_HEIGHT);

        //Slightly optamized
        //-> int/int = int, without the decimal. So no need for Math.floor
        x /= Tile.TILE_WIDTH;
        y /= Tile.TILE_HEIGHT;
//        System.out.println("tile[" + x + "][" + y + "]");
        return tiles[y][x];
    }

    private boolean isLegal(int x, int y) {
//        System.out.println("isLegal");
        return tiles[y][x].legal;
    }

    private boolean isLegal(Tile t) {
//        System.out.println("isLegal");
        return t.legal;
    }

    private boolean isActive(int x, int y) {
        System.out.println("isActive");
        return tiles[y][x].id == Tile.ACTIVE;
    }

    private boolean isActive(Tile t) {
        System.out.println("isActive");
        return t.id == Tile.ACTIVE;
    }

    private boolean isFood(int x, int y) {
        System.out.println("isFood");
        return tiles[y][x].id == Tile.DOT;
    }

    private boolean isPowerUp(int x, int y) {
        System.out.println("isPowerUp");
        return tiles[y][x].id == Tile.ENERGIZER;
    }

    private boolean isEmpty(int x, int y) {
        System.out.println("isEmpty");
        return tiles[y][x].id == Tile.EMPTY;
    }

//    private boolean isWall(int x, int y) {
//        System.out.println("isWall");
//        return tiles[y][x].id == Tile.WALL;
//    }
//
//    private boolean isWall(Tile t) {
//        return t.id == Tile.WALL;
//    }

    private Point getCenter(Tile t) {
        centerPoint.x = (int) t.bounds.lowerLeft.x + 3;
        centerPoint.y = (int) t.bounds.lowerLeft.y + 4;

//        centerPoint.x = (int) (t.bounds.lowerLeft.x / GamePanel.scale + 3);
//        centerPoint.y = (int) (t.bounds.lowerLeft.y / GamePanel.scale + 4);
        return centerPoint;
    }

    private void alignX(Point c) {
        //If pacman is not position on center.x
        if (pixel.x < c.x) {
            pixel.x += 1;
        } else if (pixel.x > c.x) {
            pixel.x -= 1;
        }
    }

    private void alignY(Point c) {
        if (pixel.y < c.y) {
            pixel.y += 1;
        } else if (pixel.y > c.y) {
            pixel.y -= 1;
        }
    }

    private Tile pixelToTileAbove(int x, int y) {
        x /= Tile.TILE_WIDTH;
        y /= Tile.TILE_HEIGHT;
        return tiles[y + 1][x];
    }

    private Tile pixelToTileBelow(int x, int y) {
        x /= Tile.TILE_WIDTH;
        y /= Tile.TILE_HEIGHT;
        return tiles[y - 1][x];
    }

    private Tile pixelToTileLeft(int x, int y) {
        x /= Tile.TILE_WIDTH;
        y /= Tile.TILE_HEIGHT;
        return tiles[y][x - 1];
    }

    private Tile pixelToTileRight(int x, int y) {
        x /= Tile.TILE_WIDTH;
        y /= Tile.TILE_HEIGHT;
        return tiles[y][x + 1];
    }

    private void movePixelDown() {
//        System.out.println("up...");
        Tile previous = pixelToTile(pixel.x, pixel.y);  //TEST

        //Is the PIXEL above pacman is not a wall
        if ((pixelToTile(pixel.x, pixel.y - 1).legal)) {
            Point c = getCenter(pixelToTile(pixel.x, pixel.y));
            //If the TILE above pacman is a wall
            if (!(pixelToTileBelow(pixel.x, pixel.y).legal)) {
                //Stop pacman at the mid point of the tile
                if (pixel.y > c.y) {
                    pixel.y -= 1;
                } else {
                    System.out.println("Reach BOTTOM wall");
                    currentDir = DIR.STOP;
                    System.out.println("currentDir: " + currentDir);
                }
            } else {
                //Otherwise let pacman continue till the end of the tile
                pixel.y -= 1;
            }
            alignX(c);
            //------------------TEST---------------------------
            Tile curent = pixelToTile(pixel.x, pixel.y);
            if (curent.position != previous.position) {
                moveDown();
            }
            //--------------------------------------------------
        } else {
            //The pixel above is a wall, stop moving
            System.out.println("can't move DOWN");
            currentDir = DIR.STOP;
            System.out.println("currentDir: " + currentDir);
        }
    }

    private void movePixelUp() {
//        System.out.println("down...");
        Tile previous = pixelToTile(pixel.x, pixel.y);

        if ((pixelToTile(pixel.x, pixel.y + 1)).legal) {
            Point c = getCenter(pixelToTile(pixel.x, pixel.y));
            if (!(pixelToTileAbove(pixel.x, pixel.y).legal)) {
                if (pixel.y < c.y) {
                    pixel.y += 1;
                } else {
                    System.out.println("Reach TOP wall");
                    currentDir = DIR.STOP;
                    System.out.println("currentDir: " + currentDir);
                }
            } else {
                pixel.y += 1;
            }
            alignX(c);
            //------------------TEST---------------------------
            Tile curent = pixelToTile(pixel.x, pixel.y);
            if (curent.position != previous.position) {
                moveUp();
            }
            //--------------------------------------------------
        } else {
            System.out.println("can't move UP");
            currentDir = DIR.STOP;
            System.out.println("currentDir: " + currentDir);
        }
    }

    private void movePixelLeft() {
//        System.out.println("left...");
        Tile previous = pixelToTile(pixel.x, pixel.y);
        if ((pixelToTile(pixel.x - 1, pixel.y)).legal) {
            //-------------------Wrap Player------------------------
            //0.1.2.3.4.5.6.7 = tile size if pixel < 8 we get an error due to pixelToTileLeft()
            if (pixel.x < 9) {
                pixel.x = (int) WORLD_WIDTH - 1;
                //update players tile
                pacmanTile.x = pixel.x / Tile.TILE_WIDTH;
                pacmanTile.y = pixel.y / Tile.TILE_HEIGHT;
                tiles[pacmanTile.y][pacmanTile.x].id = Tile.PACMAN;
                //remove old occupied player tile
                previous.id = Tile.ACTIVE;
                return;
            }
            //-------------------------------------------------------
//            System.out.println("pixel to left: " + (pixel.x - scaledNum(1)));
            Point c = getCenter(pixelToTile(pixel.x, pixel.y));
            if (!(pixelToTileLeft(pixel.x, pixel.y).legal)) {
                if (pixel.x > c.x) {
                    pixel.x -= 1;
                } else {
                    System.out.println("Reach LEFT wall");
                    currentDir = DIR.STOP;
                    System.out.println("currentDir: " + currentDir);
                }
            } else {
                pixel.x -= 1;
            }
            alignY(c);
            //------------------TEST---------------------------
            Tile curent = pixelToTile(pixel.x, pixel.y);
            if (curent.position != previous.position) {
                moveLeft();
            }
            //--------------------------------------------------
        } else {
            System.out.println("can't move LEFT");
            currentDir = DIR.STOP;
            System.out.println("currentDir: " + currentDir);
        }
    }

    private void movePixelRight() {
//        System.out.println("right...");
        Tile previous = pixelToTile(pixel.x, pixel.y);

        if ((pixelToTile(pixel.x + 1, pixel.y).legal)) {
            //-------------------Wrap Player------------------------
            if (pixel.x > WORLD_WIDTH - 10) {
                pixel.x = 1;
                //update players tile
                pacmanTile.x = pixel.x / Tile.TILE_WIDTH;
                pacmanTile.y = pixel.y / Tile.TILE_HEIGHT;
                tiles[pacmanTile.y][pacmanTile.x].id = Tile.PACMAN;
                //remove old occupied player tile

                previous.id = Tile.ACTIVE;
                return;
            }
            //-------------------------------------------------------
            Point c = getCenter(pixelToTile(pixel.x, pixel.y));
            if (!(pixelToTileRight(pixel.x, pixel.y).legal)) {
                if (pixel.x < c.x) {
                    pixel.x += 1;
                } else {
                    System.out.println("Reach RIGHT wall");
                    currentDir = DIR.STOP;
                    System.out.println("currentDir: " + currentDir);
                }
            } else {
                pixel.x += 1;
            }
            alignY(c);
            //------------------TEST---------------------------
            Tile curent = pixelToTile(pixel.x, pixel.y);
            if (curent.position != previous.position) {
                moveRight();
            }
            //--------------------------------------------------
        } else {
            System.out.println("can't move RIGHT");
            currentDir = DIR.STOP;
            System.out.println("currentDir: " + currentDir);
        }
    }

    private void movePacman(float deltaTime) {
        handleTurnBuffer();
        switch (currentDir) {
            case UP:
                rotation = 90;
                stateTime += deltaTime;
                movePixelUp();
                break;
            case DOWN:
                rotation = 270;
                stateTime += deltaTime;
                movePixelDown();
                break;
            case LEFT:
                rotation = 180;
                stateTime += deltaTime;
                movePixelLeft();
                break;
            case RIGHT:
                rotation = 0;
                stateTime += deltaTime;
                movePixelRight();
                break;
            case STOP:
                //pacOffset.set(0, 0);
                break;
        }
    }

    public void update(float deltaTime) {
        /*slow down pacman*/
        elapsedTime += deltaTime;   //in seconds
        if (elapsedTime >= TIME_TO_MOVE) {
//            System.out.println("move");
            movePacman(deltaTime);
            elapsedTime = 0;
        }
    }
}
