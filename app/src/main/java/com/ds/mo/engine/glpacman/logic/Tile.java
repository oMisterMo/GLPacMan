package com.ds.mo.engine.glpacman.logic;

import android.graphics.Point;

import com.ds.mo.engine.common.GameObject;
import com.ds.mo.engine.glpacman.GameScreen;

public class Tile extends GameObject {

    public static final int TILE_WIDTH = 8;
    public static final int TILE_HEIGHT = 8;

    //ids
//    public final static int EMPTY = 0;
//    public final static int ACTIVE = 1;
//    public final static int FOOD = 2;
//    public final static int POWER_UP = 3;
//    public final static int PLAYER = 4;
//    public final static int WALL = 5;
//    public final static int BLINKY = 6;
//    public final static int PINKY = 7;
//    public final static int INKY = 8;
//    public final static int CLYDE = 9;

    //New Tile ID
    public final static int EMPTY = -1;
    public final static int DOUBLE_BL = 0;
    public final static int DOUBLE_BM = 1;
    public final static int DOUBLE_BR = 2;
    public final static int DOUBLE_ML = 3;
    public final static int DOUBLE_MR = 4;
    public final static int DOUBLE_TL = 5;
    public final static int DOUBLE_TM = 6;
    public final static int DOUBLE_TR = 7;

    public final static int HOME_L = 8;
    public final static int HOME_R = 9;

    public final static int HOR_BL = 10;
    public final static int HOR_BR = 11;
    public final static int HOR_TL = 12;
    public final static int HOR_TR = 13;

    public final static int LINE_BL = 14;
    public final static int LINE_BM = 15;
    public final static int LINE_BR = 16;
    public final static int LINE_ML = 17;
    public final static int LINE_MR = 18;
    public final static int LINE_TL = 19;
    public final static int LINE_TM = 20;
    public final static int LINE_TR = 21;

    public final static int SQUARE_BL = 22;
    public final static int SQUARE_BR = 23;
    public final static int SQUARE_TL = 24;
    public final static int SQUARE_TR = 25;

    public final static int VER_BL = 26;
    public final static int VER_BR = 27;
    public final static int VER_TL = 28;
    public final static int VER_TR = 29;

    public final static int LINE_BIG_BL = 30;
    public final static int LINE_BIG_BR = 31;
    public final static int LINE_BIG_TL = 32;
    public final static int LINE_BIG_TR = 33;

    public final static int DOT = 34;
    public final static int ENERGIZER = 35;
    public final static int HOME_DOOR = 36;      //door to enemies home
    public final static int PACMAN = 37;
    public final static int ACTIVE = 38;
    public final static int INTERSECTION = 39;
    public final static int BLINKY = 40;
    public final static int PINKY = 41;
    public final static int INKY = 42;
    public final static int CLYDE = 43;

    public int id = EMPTY;
    public boolean legal;           //dictates whether the tile is active or not
    public boolean teleportTile;     //dictates whether the pacman can turn on this tile
    public boolean intersection;

    public boolean special;

    public Point grid;

    /**
     * Creates a tile at x,y screen position
     *
     * @param x the position along the x axis
     * @param y the position along the y axis
     */
    public Tile(float x, float y, Point grid) {
        super(x, y, TILE_WIDTH, TILE_HEIGHT);
        legal = false;
        teleportTile = false;
        intersection = false;
        special = false;

        id = EMPTY;
        this.grid = grid;
    }

    @Override
    public String toString() {
        String type = "(" + (int) Math.floor(position.x / TILE_WIDTH) + ", "
                + (int) Math.floor(position.y / TILE_HEIGHT) + ")";
        return type + ": " + id;
    }

}