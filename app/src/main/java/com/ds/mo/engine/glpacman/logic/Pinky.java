package com.ds.mo.engine.glpacman.logic;

import android.graphics.Point;

import com.ds.mo.engine.common.Color;

import java.util.List;

public class Pinky extends Enemy{
    public Pinky(int id, Tile[][] tiles, Pacman pacman, List<Point> allDots,
                 List<Point> allEnergizers, int x, int y) {
        super(id, tiles, pacman, allDots, allEnergizers, x, y);

        color = new Color(1, 185/255f, 1, 1);
    }

    @Override
    public Tile getTarget(int mode) {
//        return super.getTarget(mode);
//        System.out.println("PINKKKY!!");
//        System.out.println("pacman dir: "+pacmanDir);
//        System.out.println("-------->" + pacman.pacmanDir + "<--------");
        switch (mode) {
//            case Enemy.STATE_CHASE:
//                return tiles[pacmanTile.y][pacmanTile.x];
            case Enemy.STATE_SCATTER:
                return tiles[pinkyScatter.y][pinkyScatter.x];
        }
        int x, y, offset;
        x = pacman.pacmanTile.x;
        y = pacman.pacmanTile.y;
        offset = 4;
        switch (pacman.recentDir) {
            case UP:
                //If it is the same logic as the classic:
                //get 4 tiles up, 4 tiles left
                //due to a subtle error in the logic code that calculates Pinky's offset from Pac-Man
                y += offset;
                y = capTileY(y);
                break;
            case DOWN:
                y -= offset;
                y = capTileY(y);
                break;
            case LEFT:
                x -= offset;
                x = capTileX(x);
                break;
            case RIGHT:
                x += offset;
                x = capTileX(x);
                break;
        }
        return tiles[y][x];
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
//        Assets.pinky.update(deltaTime);
    }

//    @Override
//    public void draw(Graphics2D g) {
//        g.setColor(color);
//        Tile target = getTarget(state);
//        g.fillRect((int) target.bounds.lowerLeft.x, (int) target.bounds.lowerLeft.y,
//                Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
////        g.drawRect(inkyScatter.x, inkyScatter.y, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
//    }
}
