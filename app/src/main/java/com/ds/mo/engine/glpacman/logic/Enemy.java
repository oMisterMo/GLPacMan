package com.ds.mo.engine.glpacman.logic;

import android.graphics.Point;

import com.ds.mo.engine.common.Color;
import com.ds.mo.engine.common.Helper;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Enemy {
    public final static int STATE_CHASE = 0;
    public final static int STATE_SCATTER = 1;
    public final static int STATE_EVADE = 2;
    public static int state = STATE_SCATTER;

    protected final List<Point> allDots;
    protected final List<Point> allEnergizers;
    public float enemyStateTime;        //Time ghost is in one state

    //Blinky target tile: Tile t = tiles[0][World.NO_OF_TILES_X-6];
    public static final Point blinkyScatter = new Point(World.NO_OF_TILES_X - 3, World.NO_OF_TILES_Y - 1 - 0);
    public static final Point pinkyScatter = new Point(2, World.NO_OF_TILES_Y - 1 - 0);
    public static final Point inkyScatter = new Point(World.NO_OF_TILES_X - 1, 0);
    public static final Point clydeScatter = new Point(0, 0);

    //-------------------------FROM BLINKY----------------------------------
    public static final int GHOST_WIDTH = 16;
    public static final int GHOST_HEIGHT = 16;
    public static final int X_OFF = 4;
    public static final int Y_OFF = 4;

    protected final Tile[][] tiles;
    private Point centerPoint;          //of a tile

    public enum Direction {

        UP, DOWN, LEFT, RIGHT;//, STOP;// NA

        private static final Direction[] DIRECTION = values();
        private static final int SIZE = DIRECTION.length;
        private static final Random RANDOM = new Random();

        public static Direction getRandomDir() {
            return Direction.DIRECTION[RANDOM.nextInt(SIZE - 1)];// r = 0-3
        }
    }

    //GHOST
    private int id = -1;                //-1 == empty tile
    public Point ghostTile;
    public Point pixel;
    public Direction ghostDir = Direction.LEFT;
    private Direction toTravel = Direction.LEFT;
    public Point temp;                  //stores the pos of a pellet/energizer

    protected final Pacman pacman;      //Pacman's reference -> to get target tile
    public Color color;
    //------------------------------------------------------------------------
    private boolean inHome;
    private int bounce = 1;  //1 or -1 (down or up)
    private float timeToLeave;
    private float elapsedTime;

    private Tile t1, t2, t3;
    private Point[] points;
    private int travel = 0;

    public Enemy(int id, Tile[][] tiles, Pacman pacman, List<Point> allDots, List<Point> allEnergizers,
                 int x, int y) {
        this.allDots = allDots;
        this.allEnergizers = allEnergizers;
        enemyStateTime = 0;

        this.tiles = tiles;
        this.id = id;
        ghostTile = new Point();
        pixel = new Point();
        centerPoint = new Point();
        temp = new Point();
        setGhostPos(x, y, id);

        this.pacman = pacman;
        color = new Color(1, 1, 1, 1);  //default color is white

        inHome = true;  //All ghost start in home except blinky
        elapsedTime = 0;
        timeToLeave = 0;
        initHomePath();
        System.out.println("enemy constructor finished...");
    }

    private void initHomePath() {
        t1 = tiles[World.NO_OF_TILES_Y - 1 - 17][12];
        t2 = tiles[World.NO_OF_TILES_Y - 1 - 17][14];
        t3 = tiles[World.NO_OF_TILES_Y - 1 - 14][14];
        points = new Point[3];
        points[0] = new Point((int) t1.bounds.lowerLeft.x,
                (int) t1.bounds.lowerLeft.y + Y_OFF);
        points[1] = new Point((int) t2.bounds.lowerLeft.x,
                (int) t2.bounds.lowerLeft.y + Y_OFF);
        points[2] = new Point((int) t3.bounds.lowerLeft.x,
                (int) t3.bounds.lowerLeft.y + Y_OFF);
    }

    /**
     * Sets whether a ghost is in home or not
     *
     * @param b
     */
    public void setInHome(boolean b) {
        this.inHome = b;
    }

    public void setGhostHomeInterval(float seconds) {
        timeToLeave = seconds;
    }

    /**
     * Determines whether the ghost will start travelling up or down
     *
     * @param i -1 for up, 1 for down
     */
    public void setBounce(int i) {
        if (i != 1 && i != -1) {
            throw new IllegalArgumentException("You must enter the value 1 or -1");
        }
        bounce = i;
    }

    /**
     * Set ghost to centre of given tile
     *
     * @param x  position
     * @param y  position
     * @param id ghost id
     */
    public void setGhostPos(int x, int y, int id) {
        ghostTile.set(x, y);
        pixel.set(x * Tile.TILE_WIDTH + X_OFF,
                y * Tile.TILE_HEIGHT + Y_OFF);
        centerPoint.set(x * Tile.TILE_WIDTH + X_OFF,
                y * Tile.TILE_HEIGHT + Y_OFF);
        ghostDir = Direction.LEFT;
        tiles[ghostTile.y][ghostTile.x].id = id;

        ghostDir = Direction.LEFT;
        toTravel = Direction.LEFT;
    }

    public void setGhostPos(int x, int y, int xOff, int yOff, int id) {
        //Must be within tile (tile has 8 pixels)
        xOff = Helper.Clamp(xOff, 0, 7);
        yOff = Helper.Clamp(yOff, 0, 7);
        ghostTile.set(x, y);
        pixel.set(x * Tile.TILE_WIDTH + xOff,
                y * Tile.TILE_HEIGHT + yOff);
        centerPoint.set(x * Tile.TILE_WIDTH + xOff,
                y * Tile.TILE_HEIGHT + yOff);
        ghostDir = Direction.LEFT;
        tiles[ghostTile.y][ghostTile.x].id = id;

        //Ghost always travels left at first
        ghostDir = Direction.LEFT;
        toTravel = Direction.LEFT;
    }

    public void setPixel(int x, int y) {
        pixel.set(x, y);
    }

    public void ghostInput() {
        // TODO: 18/07/2018 finish me
//        if (Input.isKeyTyped(KeyEvent.VK_T)) {
////            target = blinkyScatter;
////            pacmanTile.setLocation(World.NO_OF_TILES_X-6, 0);
//            state = STATE_SCATTER;
//            switchDir();
//        }
//        if (Input.isKeyTyped(KeyEvent.VK_Y)) {
////            target = pacmanTile;
//            state = STATE_CHASE;
//            switchDir();
//        }
    }

    private void setDotsBack(Point p) {
        if (allDots.contains(p)) {
            setTile(p.x, p.y, Tile.DOT);
        }
    }

    private void setEnergizersBack(Point p) {
        if (allEnergizers.contains(p)) {
            setTile(p.x, p.y, Tile.ENERGIZER);
        }
    }

    /**
     * Given old location, updates the enemies tile position
     *
     * @param x
     * @param y
     */
    private void updateTileInfo(int x, int y) {
        temp.set(x, y);

        setTile(ghostTile.x, ghostTile.y, id);
        setTile(x, y, Tile.ACTIVE);
        setDotsBack(temp);
        setEnergizersBack(temp);
    }

    public void moveUp() {
//        System.out.println("move tile UP");

        //Store current position
        int x, y;
        x = ghostTile.x;
        y = ghostTile.y;
        ghostTile.y += 1;
        //Update the new tile id then set old tile empty
        updateTileInfo(x, y);
    }

    public void moveDown() {
//        System.out.println("move tile DOWN");
        int x, y;
        x = ghostTile.x;
        y = ghostTile.y;
        ghostTile.y -= 1;
        updateTileInfo(x, y);
    }

    public void moveLeft() {
//        System.out.println("move tile LEFT");
        int x, y;
        x = ghostTile.x;
        y = ghostTile.y;
        ghostTile.x -= 1;
        updateTileInfo(x, y);
    }

    public void moveRight() {
//        System.out.println("move tile RIGHT");
        int x, y;
        x = ghostTile.x;
        y = ghostTile.y;
        ghostTile.x += 1;
        updateTileInfo(x, y);
    }

    private void setTile(int x, int y, int id) {
        tiles[y][x].id = id;
    }

    public Point getCenter(Tile t) {
        centerPoint.x = (int) t.bounds.lowerLeft.x + X_OFF;
        centerPoint.y = (int) t.bounds.lowerLeft.y + Y_OFF;

//        centerPoint.x = (int) (t.bounds.lowerLeft.x / GamePanel.scale + 3);
//        centerPoint.y = (int) (t.bounds.lowerLeft.y / GamePanel.scale + 4);
        return centerPoint;
    }

    private boolean isWall(Tile t) {
        // TODO: 18/07/2018 isWall or not legal????
//        return t.id == Tile.WALL;
        return !t.legal;
    }

    public Tile pixelToTile(int x, int y) {
        //Slightly optimized
        //-> int/int = int, without the decimal. So no need for Math.floor
        x /= Tile.TILE_WIDTH;
        y /= Tile.TILE_HEIGHT;
        x = capTileX(x);
        y = capTileY(y);
//        System.out.println("tile[" + x + "][" + y + "]");
        return tiles[y][x];
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

    private Tile getUp(int x, int y) {
        return tiles[y + 1][x];
    }

    private Tile getDown(int x, int y) {
        return tiles[y - 1][x];
    }

    private Tile getLeft(int x, int y) {
        return tiles[y][x - 1];
    }

    private Tile getRight(int x, int y) {
        return tiles[y][x + 1];
    }

    private Tile getTile(int x, int y) {
        return tiles[y][x];
    }

    public void setCurrentDir(Direction d) {
        this.ghostDir = d;
    }

    public void setDir(Direction d) {
        this.ghostDir = d;
        this.toTravel = d;
//        setNextMove(d);
    }

    public void setRandomDir(Tile tile) {
        // TODO: 20/07/2018 could probably optimize method
        Map<Direction, Tile> adjacent = new HashMap<>();
        //Add tiles: up, left, down, right (if legal)
        if (isTileWithinWorld(tile.grid.x, tile.grid.y + 1)) {
            Tile up = tiles[tile.grid.y + 1][tile.grid.x];
            if (up.legal) {
                adjacent.put(Direction.UP, up);
            }
        }
        if (isTileWithinWorld(tile.grid.x - 1, tile.grid.y)) {
            Tile left = tiles[tile.grid.y][tile.grid.x - 1];
            if (left.legal) {
                adjacent.put(Direction.LEFT, left);
            }
        }
        if (isTileWithinWorld(tile.grid.x, tile.grid.y - 1)) {
            Tile down = tiles[tile.grid.y - 1][tile.grid.x];
            if (down.legal) {
                adjacent.put(Direction.DOWN, down);
            }
        }
        if (isTileWithinWorld(tile.grid.x + 1, tile.grid.y)) {
            Tile right = tiles[tile.grid.y][tile.grid.x + 1];
            if (right.legal) {
                adjacent.put(Direction.RIGHT, right);
            }
        }
        //Get a random tile and set the direction to travel
        for (Map.Entry<Direction, Tile> e : adjacent.entrySet()) {
            Direction testDir = e.getKey();
//            Tile testTile = e.getValue();
//            toTravel = testDir;   //sets only toTravel dir
            setDir(testDir);        //sets both currentDir + toTravel
            setNextMove(testDir);
        }
    }

    public boolean isTileWithinWorld(int x, int y) {
        return (y < World.NO_OF_TILES_Y && y >= 0 && x < World.NO_OF_TILES_X && x >= 0);
    }

    public int capTileX(int x) {
        // TODO: 18/07/2018 could use Helper.Clamp();
        if (x < 0) {
            return 0;
        }
        if (x > World.NO_OF_TILES_X - 1) {
            return World.NO_OF_TILES_X - 1;
        }
        return x;
    }

    public int capTileY(int y) {
        if (y < 0) {
            return 0;
        }
        if (y > World.NO_OF_TILES_Y - 1) {
            return World.NO_OF_TILES_Y - 1;
        }
        return y;
    }

    /* Gets legal adjacent tiles */     //Why pass in current???
    public Map<Direction, Tile> getAdjacent(Tile tile, Direction current) {
        // TODO: 20/07/2018 new LinkedHashMap??? every time
        //To break the tie, the ghost prefers directions in this order: up, left, down, right.
        //Was using HASHSET but order of insertion was not preserved.
        Map<Direction, Tile> adjacent = new LinkedHashMap<>();
        //Up
        if (isTileWithinWorld(tile.grid.x, tile.grid.y + 1)) {
            Tile up = tiles[tile.grid.y + 1][tile.grid.x];
            if (up.legal && current != Direction.DOWN) {
                adjacent.put(Direction.UP, up);
            }
        } else {
//            System.out.println("Cant get tile above, Y < 0");
        }
        //Left
        if (isTileWithinWorld(tile.grid.x - 1, tile.grid.y)) {
            Tile left = tiles[tile.grid.y][tile.grid.x - 1];
            if (left.legal && current != Direction.RIGHT) {
                adjacent.put(Direction.LEFT, left);
            }
        } else {
//            System.out.println("Cant get tile left, X < 0");
        }
        //Down
        if (isTileWithinWorld(tile.grid.x, tile.grid.y - 1)) {
            Tile down = tiles[tile.grid.y - 1][tile.grid.x];
            if (down.legal && current != Direction.UP) {
                adjacent.put(Direction.DOWN, down);
            }
        } else {
//            System.out.println("Cant get tile below, Y > NO_OF_TILES_Y");
        }
        //Right
        if (isTileWithinWorld(tile.grid.x + 1, tile.grid.y)) {
            Tile right = tiles[tile.grid.y][tile.grid.x + 1];
            if (right.legal && current != Direction.LEFT) {
                adjacent.put(Direction.RIGHT, right);
            }
        } else {
//            System.out.println("Cant get tile right, X > NO_OF_TILES_X");
        }
        return adjacent;
    }

    //Test handle new movement
    public void movePixelUp() {
//        System.out.println("up...");
        Tile current = pixelToTile(pixel.x, pixel.y);
        Point c = getCenter(current);
        //If the TILE above pacman is a wall
        if (isWall(pixelToTileAbove(pixel.x, pixel.y))) {
            //Stop pacman at the mid point of the tile
            if (pixel.y < c.y) {
                pixel.y += 1;
            } else {
                ghostDir = toTravel;
            }
        } else {
            if (pixel.y == c.y && ghostDir != toTravel && current.intersection) {
                ghostDir = toTravel;
                return;
            }
            //Otherwise let pacman continue till the end of the tile
            pixel.y += 1;
        }
        Tile next = pixelToTile(pixel.x, pixel.y);
        if (next.position != current.position) {
//            System.out.println("--------------------");
            moveUp();
            setNextMove(Direction.UP);
        }
    }

    public void movePixelRight() {
//        System.out.println("right...");
        Tile current = pixelToTile(pixel.x, pixel.y);

        //-------------------Wrap Player------------------------
        if (pixel.x > World.WORLD_WIDTH - 11) {
            pixel.x = 1;
            //update players tile
            ghostTile.x = pixel.x / Tile.TILE_WIDTH;
            ghostTile.y = pixel.y / Tile.TILE_HEIGHT;
            tiles[ghostTile.y][ghostTile.x].id = id;
            //remove old occupied player tile

            current.id = Tile.ACTIVE;
            return;
        }
        //-------------------------------------------------------
        Point c = getCenter(pixelToTile(pixel.x, pixel.y));
        if (isWall(pixelToTileRight(pixel.x, pixel.y))) {
            if (pixel.x < c.x) {
                pixel.x += 1;
            } else {
                ghostDir = toTravel;
            }
        } else {
            if (current.intersection && pixel.x == c.x && ghostDir != toTravel) {
                ghostDir = toTravel;
            } else {
                pixel.x += 1;
            }
        }
        Tile next = pixelToTile(pixel.x, pixel.y);
        if (next.position != current.position) {
//            System.out.println("--------------------");
            moveRight();
            //Don't get direction if on special tile
            if (next == tiles[World.NO_OF_TILES_Y - 1 - 14][11] ||
                    next == tiles[World.NO_OF_TILES_Y - 1 - 14][14]) {
                return;
            }
            setNextMove(Direction.RIGHT);
        }
    }

    public void movePixelDown() {
//        System.out.println("down...");
        Tile current = pixelToTile(pixel.x, pixel.y);

        Point c = getCenter(pixelToTile(pixel.x, pixel.y));
        if (isWall(pixelToTileBelow(pixel.x, pixel.y))) {
            if (pixel.y > c.y) {
                pixel.y -= 1;
            } else {
                ghostDir = toTravel;
            }
        } else {
            //Otherwise let pacman continue till the end of the tile
            if (current.intersection && pixel.y == c.y && ghostDir != toTravel) {
                //reached the center of an intersection TILE
                ghostDir = toTravel;

            } else {
                pixel.y -= 1;
            }
        }
        Tile next = pixelToTile(pixel.x, pixel.y);
        if (next.position != current.position) {
            moveDown();
            setNextMove(Direction.DOWN);
        }
    }

    public void movePixelLeft() {
//        System.out.println("left...");
        Tile current = pixelToTile(pixel.x, pixel.y);
        //-------------------Wrap Player------------------------
        if (pixel.x < 9) {
            pixel.x = (int) World.WORLD_WIDTH - 1;
            //update players tile
            ghostTile.x = pixel.x / Tile.TILE_WIDTH;
            ghostTile.y = pixel.y / Tile.TILE_HEIGHT;
            tiles[ghostTile.y][ghostTile.x].id = id;   //Should be ghost id here
            //remove old occupied player tile
            current.id = Tile.ACTIVE;
            return;
        }
        //-------------------------------------------------------
        Point c = getCenter(pixelToTile(pixel.x, pixel.y));
        if (isWall(pixelToTileLeft(pixel.x, pixel.y))) {
            if (pixel.x > c.x) {
                pixel.x -= 1;
            } else {
                ghostDir = toTravel;
            }
        } else {
            if (current.intersection && pixel.x == c.x && ghostDir != toTravel) {
                ghostDir = toTravel;
            } else {
                pixel.x -= 1;
            }
        }
        Tile next = pixelToTile(pixel.x, pixel.y);
        //Just entered a new tile
        if (next.position != current.position) {
            moveLeft();
            if (next == tiles[World.NO_OF_TILES_Y - 1 - 14][13] ||
                    next == tiles[World.NO_OF_TILES_Y - 1 - 14][16]) {
                return;
            }
            setNextMove(Direction.LEFT);
        }
    }

    public void setNextMove(Direction dir) {
        Tile test = null;
        switch (dir) {
            case UP:
                test = getUp(ghostTile.x, ghostTile.y);
                break;
            case DOWN:
                test = getDown(ghostTile.x, ghostTile.y);
                break;
            case LEFT:
                test = getLeft(ghostTile.x, ghostTile.y);
                break;
            case RIGHT:
                test = getRight(ghostTile.x, ghostTile.y);
                break;
        }
        //Get adjacent tiles of next tile
        Map<Direction, Tile> adj = getAdjacent(test, ghostDir);
        //If there's only a single adjacent tile -> carry on up
        if (adj.size() == 1) {
            //If blinky reached a corner, set new direction else continue
            for (Map.Entry<Direction, Tile> e : adj.entrySet()) {
                Direction testDir = e.getKey();
//                Tile testTile = e.getValue();
                if (ghostDir != testDir) {
                    toTravel = testDir;
                }
            }
        } else {
            /*
             -> There is a choice to be made next tiles
             -> Choose move for next tile -> based on shortest distance
             -> up, left, down, right.
             */
            int shortest = -1;  //shortest will always be > 10
            for (Map.Entry<Direction, Tile> e : adj.entrySet()) {
                Direction testDir = e.getKey();
                Tile testTile = e.getValue();
                if (shortest == -1) {
                    //If shortest is undefined, set it to the first value
                    shortest = getDistance(testTile, getTarget(state));
                    toTravel = testDir;
//                    System.out.println(testDir + ": " + testTile + ": "
//                            + shortest);
                    continue;
                }
                int distance = getDistance(testTile, getTarget(state));
                if (distance < shortest) {
                    shortest = distance;
                    toTravel = testDir;
                }
//                System.out.println(testDir + ": " + testTile + ": "
//                        + distance);
            }
//            System.out.println("Shortest path: " + toTravel);
//            System.out.println("----------------------------------------");
        }
    }

    public int getDistance(Tile nodeA, Tile goal) {
        int dx = Math.abs(nodeA.grid.x - goal.grid.x);
        int dy = Math.abs(nodeA.grid.y - goal.grid.y);
//        //Attempt 2
//        if (dx > dy) {
//            return 14 * dy + 10 * (dx - dy);
//        }
//        return 14 * dx + 10 * (dy - dx);

        //Attempt 3
        //D = horizontal cost, D2 = Diagonal cost
        //D * (dx + dy) + (D2 - 2 * D) * min(dx, dy) (same thing)
        return 10 * (dx + dy) + (14 - 2 * 10) * Math.min(dx, dy);
    }

    public Tile getTarget(int mode) {
        switch (mode) {
//            case Enemy.STATE_CHASE:
//                return tiles[pacmanTile.y][pacmanTile.x];
            case Enemy.STATE_SCATTER:
                return tiles[blinkyScatter.y][blinkyScatter.x];
        }
        return tiles[pacman.pacmanTile.y][pacman.pacmanTile.x];
    }

    private void switchDir() {
        switch (ghostDir) {
            case UP:
                ghostDir = Direction.DOWN;
//                toTravel = Direction.DOWN;
                setNextMove(Direction.DOWN);
                break;
            case DOWN:
                ghostDir = Direction.UP;
//                toTravel = Direction.UP;
                setNextMove(Direction.UP);
                break;
            case LEFT:
                ghostDir = Direction.RIGHT;
//                toTravel = Direction.RIGHT;
                setNextMove(Direction.RIGHT);
                break;
            case RIGHT:
                ghostDir = Direction.LEFT;
//                toTravel = Direction.LEFT;
                setNextMove(Direction.LEFT);
                break;

        }
    }

//        //Swtich to custom state
//    public void setState(int state) {
//        this.state = state;
//        this.enemyStateTime = 0;
//        switchDir();
//    }

    //Simple swap between two states
    public void switchState() {
        if (state == STATE_CHASE) {
            state = STATE_SCATTER;
            enemyStateTime = 0;
        } else {
            state = STATE_CHASE;
            enemyStateTime = 0;
        }
        switchDir();
    }

    private void moveEnemy() {
        switch (ghostDir) {
            case UP:
                movePixelUp();
                break;
            case DOWN:
                movePixelDown();
                break;
            case LEFT:
                movePixelLeft();
                break;
            case RIGHT:
                movePixelRight();
                break;
        }
    }

    private void travelOutside() {
        if (travel == 0) {
            //Go to point 1
            if (pixel.y != points[0].y) {
                if (pixel.y < points[0].y) {
//                    pixel.y++;
                    pixel.y += 1;
                } else {
//                    pixel.y--;
                    pixel.y -= 1;
                }
            } else {
                travel += 1;
            }
        } else if (travel == 1) {
            //Go to point 2
            if (pixel.x != points[1].x) {
                if (pixel.x < points[1].x) {
//                    pixel.x++;
                    pixel.x += 1;
                } else {
//                    pixel.x--;
                    pixel.x -= 1;
                }
            } else {
                travel += 1;
            }
        } else if (travel == 2) {
            if (pixel.y != points[2].y) {
                if (pixel.y < points[2].y) {
//                    pixel.y++;
                    pixel.y += 1;
                } else {
//                    pixel.y--;
                    pixel.y -= 1;
                }
            } else {
                //TRAVEL IS COMPLETE (reset values)
                System.out.println("Travel is complete -> Ghost is not longet in home");
                inHome = false;
                elapsedTime = 0;
                travel = 0;
                //Set ghost to its new position (center of the tile outside of home)
                setGhostPos(14, World.NO_OF_TILES_Y - 1 - 14, 0, 4, id);
                //Or just update the current tile of the ghost
//                ghostTile.setLocation(pixel.x / Tile.TILE_WIDTH,
//                        pixel.y / Tile.TILE_HEIGHT);
                System.out.println("Current ghost tile is: [" + ghostTile.x + ", "
                        + ghostTile.y + "]");
            }
        }
    }

    //--------------------------------------------------------------------------
    public void update(float deltaTime) {
        //Check if ghost is in home first -> Handle home shit
        if (inHome) {
            // TODO: 18/07/2018 fix me later
            elapsedTime += deltaTime;
//            System.out.println("elapsedTime: " + elapsedTime);
            //Move up and down until its time to come out

            if (elapsedTime >= timeToLeave) {
                //Come out of home
                travelOutside();

            } else {
                //Bounce up and down
//                System.out.println("bounce");
                pixel.y += bounce;
                if (pixel.y <= (World.NO_OF_TILES_Y - 1 - 17) * Tile.TILE_HEIGHT) {
                    bounce *= -1;
                } else if (pixel.y >= ((World.NO_OF_TILES_Y - 1 - 17) * Tile.TILE_HEIGHT) + 7) {
                    bounce *= -1;
                }
            }
            enemyStateTime += deltaTime;    //update animation even when in home
        } else {
            //Now the ghost has escaped
            switch (state) {
                case STATE_CHASE:
                case STATE_SCATTER:
                    enemyStateTime += deltaTime;
                case STATE_EVADE:
                    moveEnemy();
                    break;
            }
        }
    }
}
