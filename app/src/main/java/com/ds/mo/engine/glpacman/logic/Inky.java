package com.ds.mo.engine.glpacman.logic;

import android.graphics.Point;

import com.ds.mo.engine.common.Color;
import com.ds.mo.engine.common.Vector2D;

import java.util.List;

public class Inky extends Enemy {
    private final Enemy blinky;
    Vector2D length = new Vector2D();
    Vector2D blinkyPixel = new Vector2D();
    Vector2D offsetPixel = new Vector2D();

    public Inky(int id, Tile[][] tiles, Pacman pacman, List<Point> allDots,
                List<Point> allEnergizers, int x, int y, Enemy blinky) {
        super(id, tiles, pacman, allDots, allEnergizers, x, y);

        this.blinky = blinky;
        color = new Color(0, 1, 1, 1);
    }

    @Override
    public Tile getTarget(int mode) {
        switch (mode) {
            case Enemy.STATE_SCATTER:
                return tiles[inkyScatter.y][inkyScatter.x];
        }
        int x, y, offset;
        x = pacman.pacmanTile.x;
        y = pacman.pacmanTile.y;
//        System.out.println("pacman tile: "+pacman.pacmanTile);
        offset = 2;
        switch (pacman.recentDir) {
            case UP:
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
        /*
         ->imagine an intermediate offset two tiles away from Pac-Man's tile
         in the direction Pac-Man is moving
         ->then draw a line from Blinky's tile to that offset.
         ->Now double the line length by extending the line out just as far again,
         and you will have Inky's target tile
         */
        int tileX, tileY;
        Point c = getCenter(tiles[y][x]);
        blinkyPixel.set(blinky.pixel.x, blinky.pixel.y);
        offsetPixel.set(c.x, c.y);
        length.set(offsetPixel.x - blinkyPixel.x, offsetPixel.y - blinkyPixel.y);
        length.mult(2);
        tileX = (int) (blinky.pixel.x + length.x);
        tileY = (int) (blinky.pixel.y + length.y);

        return pixelToTile(tileX, tileY);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
//        Assets.inky.update(deltaTime);
//        System.out.println("INKY.Y: "+pixel.y);
    }

//    @Override
//    public void draw(Graphics2D g) {
//        g.setColor(color);
//        Tile target = getTarget(state);
//        g.fillRect((int) target.bounds.lowerLeft.x, (int) target.bounds.lowerLeft.y,
//                Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
//        //Draw offset tile (2 tiles from pacman)
//        Tile twoTile = tiles[twoTiles.y][twoTiles.x];
//        g.setColor(color);
//        g.fillRect((int) twoTile.bounds.lowerLeft.x, (int) twoTile.bounds.lowerLeft.y,
//                Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
////        g.drawRect(inkyScatter.x, inkyScatter.y, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
//
//        //Draw line from blinky -> Target
////        g.setColor(color);
////        g.drawLine(blinky.pixel.x, blinky.pixel.y,
////                (int) (blinky.pixel.x + length.x), (int) (blinky.pixel.y + length.y));
////        System.out.println(pos);
////        g.drawImage(Assets.inky.getImage(), (int) (position.x - Blinky.GHOST_WIDTH / 2),
////                (int) (position.y - Blinky.GHOST_HEIGHT / 2),
////                Enemy.GHOST_WIDTH, Enemy.GHOST_HEIGHT, null);
//        //------------------------------------------
//        g.setColor(Color.YELLOW);
//        g.fillRect(points[0].x, points[0].y, scaledNum(1), scaledNum(1));
//        g.fillRect(points[1].x, points[1].y, scaledNum(1), scaledNum(1));
//        g.fillRect(points[2].x, points[2].y, scaledNum(1), scaledNum(1));
//
//        g.drawImage(Assets.inky.getImage(), (int) (pixel.x - Blinky.GHOST_WIDTH / 2),
//                (int) (pixel.y - Blinky.GHOST_HEIGHT / 2),
//                Enemy.GHOST_WIDTH, Enemy.GHOST_HEIGHT, null);
//    }
}
