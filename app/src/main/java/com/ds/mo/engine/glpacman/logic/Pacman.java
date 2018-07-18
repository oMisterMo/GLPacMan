package com.ds.mo.engine.glpacman.logic;

import android.graphics.Point;
import android.util.Log;

import com.ds.mo.engine.common.Color;
import com.ds.mo.engine.framework.Input;
import com.ds.mo.engine.glpacman.Assets;
import com.ds.mo.engine.glpacman.logic.World.WorldListener;

import java.util.List;

public class Pacman {

    //Pacman
    public static final int PACMAN_WIDTH = 16;
    public static final int PACMAN_HEIGHT = 16;

    private final Tile[][] tiles;

    public enum DIR {
        UP, DOWN, LEFT, RIGHT, STOP, NA
    }

    public float stateTime;
    public int rotation;
    public Point pacmanTile;
    public Point pixel;
    public DIR pacmanDir = DIR.STOP;
    private DIR turnBuffer = DIR.NA;
    public DIR recentDir = DIR.STOP;
    private Point centerPoint;  //used as a temp variable (center of a tile

    public static final int FOOD_SCORE = 10;
    public static final int ENERGIZER_SCORE = 50;
    public static int score;

    //Reference to the location of all pellets in the world
    private final List<Point> allDots;
    private final List<Point> allEnergizers;
    public Color color = new Color(Color.YELLOW);

    private boolean wa = true;
    private final WorldListener listener;


    public Pacman(Tile[][] tiles, List<Point> allDots, List<Point> allEnergizers, WorldListener listener) {
        this.tiles = tiles;
        this.allDots = allDots;
        this.allEnergizers = allEnergizers;
        this.listener = listener;
        init();
        Log.d("Pacman", "pacman constructor finished...");
    }

    private void init() {
        stateTime = 0;
        rotation = 0;               //facing right
        pacmanTile = new Point();
        pixel = new Point();
        centerPoint = new Point();
        Log.d("Pacman", "pacman's direction = " + pacmanDir);

        score = 0;
    }

    public void setPacmanPos(int x, int y) {
        this.pacmanTile.set(x, y);
        this.pixel.set(x * Tile.TILE_WIDTH + 3,
                y * Tile.TILE_HEIGHT + 4);
        this.centerPoint.set(x * Tile.TILE_WIDTH + 3,
                y * Tile.TILE_HEIGHT + 4);
        this.pacmanDir = DIR.STOP;
        this.turnBuffer = DIR.NA;
        this.recentDir = DIR.STOP;
//        Assets.pacman.resetAnimation();
    }

    private void playSound(){
        if(wa){
//            Assets.wa.play(1);
            listener.wa();
        }else{
//            Assets.ka.play(1);
            listener.ka();
        }
    }

    private void updateScore() {
        Tile t = tiles[pacmanTile.y][pacmanTile.x];
        switch (t.id) {
            case Tile.DOT:
                score += FOOD_SCORE;
                if (allDots.contains(t.grid)) {
                    allDots.remove(t.grid);
                }
                playSound();
                wa = !wa;
                break;
            case Tile.ENERGIZER:
                score += ENERGIZER_SCORE;
                if (allEnergizers.contains(t.grid)) {
                    allEnergizers.remove(t.grid);
                }
                playSound();
                wa = !wa;
                break;
        }
    }

    private void setTile(int x, int y, int id) {
        tiles[y][x].id = id;
    }

    /* Handle pacman's movement */
    public void swipeUp() {
        turnBuffer = DIR.UP;
        //Get tile above -> if its not active ignore movement
        if ((pixelToTileAbove(pixel.x, pixel.y).legal)) {
            pacmanDir = DIR.UP;
            System.out.println("pacmanDir: " + pacmanDir);
        } else {
            System.out.println("cant press up, tile above blocked");
        }
    }

    public void swipeDown() {
        turnBuffer = DIR.DOWN;
        if ((pixelToTileBelow(pixel.x, pixel.y).legal)) {
            pacmanDir = DIR.DOWN;
            System.out.println("pacmanDir: " + pacmanDir);
        } else {

        }
    }

    public void swipeLeft() {
        if (pixelToTile(pixel.x, pixel.y).teleportTile) {
            System.out.println("We are on left telport tile");
            return;
        }
        turnBuffer = DIR.LEFT;
        if ((pixelToTileLeft(pixel.x, pixel.y).legal)) {
            pacmanDir = DIR.LEFT;
            System.out.println("pacmanDir: " + pacmanDir);
        } else {

        }
    }

    public void swipeRight() {
        if (pixelToTile(pixel.x, pixel.y).teleportTile) {
            System.out.println("We are on left telport tile");
            return;
        }
        turnBuffer = DIR.RIGHT;
        if ((pixelToTileRight(pixel.x, pixel.y).legal)) {
            pacmanDir = DIR.RIGHT;
            System.out.println("pacmanDir: " + pacmanDir);
        } else {

        }
    }

    /* Moves pacman's tile position */
    private void moveUp() {
        //Store current position
        int x, y;
        x = pacmanTile.x;
        y = pacmanTile.y;
        pacmanTile.y += 1;

        //Handle collision with item
        updateScore();

        //Update the new tile id then set old tile empty
        setTile(pacmanTile.x, pacmanTile.y, Tile.PACMAN);
        setTile(x, y, Tile.ACTIVE);
    }

    private void moveDown() {
        int x, y;
        x = pacmanTile.x;
        y = pacmanTile.y;
        pacmanTile.y -= 1;
        updateScore();
        setTile(pacmanTile.x, pacmanTile.y, Tile.PACMAN);
        setTile(x, y, Tile.ACTIVE);
    }

    private void moveLeft() {
        int x, y;
        x = pacmanTile.x;
        y = pacmanTile.y;
        pacmanTile.x -= 1;
        updateScore();
        setTile(pacmanTile.x, pacmanTile.y, Tile.PACMAN);
        setTile(x, y, Tile.ACTIVE);
    }

    private void moveRight() {
        int x, y;
        x = pacmanTile.x;
        y = pacmanTile.y;
        pacmanTile.x += 1;
        updateScore();
        setTile(pacmanTile.x, pacmanTile.y, Tile.PACMAN);
        setTile(x, y, Tile.ACTIVE);
    }

    private void handleTurnBuffer() {
        if (turnBuffer != DIR.NA) {
            switch (turnBuffer) {
                case UP:
                    if (pixelToTileAbove(pixel.x, pixel.y).legal) {
//                        System.out.println("consuming UP turn");
                        pacmanDir = DIR.UP;
                        turnBuffer = DIR.NA;
                    }
                    break;
                case DOWN:
                    if (pixelToTileBelow(pixel.x, pixel.y).legal) {
//                        System.out.println("consuming DOWN turn");
                        pacmanDir = DIR.DOWN;
                        turnBuffer = DIR.NA;
                    }
                    break;
                case LEFT:
                    if (pixelToTileLeft(pixel.x, pixel.y).legal) {
//                        System.out.println("consuming LEFT turn");
                        pacmanDir = DIR.LEFT;
                        turnBuffer = DIR.NA;
                    }
                    break;
                case RIGHT:
                    if (pixelToTileRight(pixel.x, pixel.y).legal) {
//                        System.out.println("consuming RIGHT turn");
                        pacmanDir = DIR.RIGHT;
                        turnBuffer = DIR.NA;
                    }
                    break;
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
                    pacmanDir = DIR.STOP;
                    System.out.println("pacmanDir: " + pacmanDir);
                }
            } else {
                //Otherwise let pacman continue till the end of the tile
                pixel.y -= 1;
            }
            alignX(c);
            //------------------TEST---------------------------
            Tile current = pixelToTile(pixel.x, pixel.y);
            if (current.position != previous.position) {
                moveDown();
            }
            //--------------------------------------------------
        } else {
            //The pixel above is a wall, stop moving
            System.out.println("can't move DOWN");
            pacmanDir = DIR.STOP;
            System.out.println("pacmanDir: " + pacmanDir);
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
                    pacmanDir = DIR.STOP;
                    System.out.println("pacmanDir: " + pacmanDir);
                }
            } else {
                pixel.y += 1;
            }
            alignX(c);
            //------------------TEST---------------------------
            Tile current = pixelToTile(pixel.x, pixel.y);
            if (current.position != previous.position) {
                moveUp();
            }
            //--------------------------------------------------
        } else {
            System.out.println("can't move UP");
            pacmanDir = DIR.STOP;
            System.out.println("pacmanDir: " + pacmanDir);
        }
    }

    private void movePixelLeft() {
//        System.out.println("left...");
        Tile previous = pixelToTile(pixel.x, pixel.y);
        if ((pixelToTile(pixel.x - 1, pixel.y)).legal) {
            //-------------------Wrap Player------------------------
            //0.1.2.3.4.5.6.7 = tile size if pixel < 8 we get an error due to pixelToTileLeft()
            if (pixel.x < 9) {
                pixel.x = (int) World.WORLD_WIDTH - 1;
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
                    pacmanDir = DIR.STOP;
                    System.out.println("pacmanDir: " + pacmanDir);
                }
            } else {
                pixel.x -= 1;
            }
            alignY(c);
            //------------------TEST---------------------------
            Tile current = pixelToTile(pixel.x, pixel.y);
            if (current.position != previous.position) {
                moveLeft();
            }
            //--------------------------------------------------
        } else {
            System.out.println("can't move LEFT");
            pacmanDir = DIR.STOP;
            System.out.println("pacmanDir: " + pacmanDir);
        }
    }

    private void movePixelRight() {
//        System.out.println("right...");
        Tile previous = pixelToTile(pixel.x, pixel.y);

        if ((pixelToTile(pixel.x + 1, pixel.y).legal)) {
            //-------------------Wrap Player------------------------
            if (pixel.x > World.WORLD_WIDTH - 10) {
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
                    pacmanDir = DIR.STOP;
                    System.out.println("pacmanDir: " + pacmanDir);
                }
            } else {
                pixel.x += 1;
            }
            alignY(c);
            //------------------TEST---------------------------
            Tile current = pixelToTile(pixel.x, pixel.y);
            if (current.position != previous.position) {
                moveRight();
            }
            //--------------------------------------------------
        } else {
            System.out.println("can't move RIGHT");
            pacmanDir = DIR.STOP;
            System.out.println("pacmanDir: " + pacmanDir);
        }
    }

    public void update(float deltaTime) {
        handleTurnBuffer();
        switch (pacmanDir) {
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
}
