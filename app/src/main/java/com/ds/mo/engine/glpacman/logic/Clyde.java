package com.ds.mo.engine.glpacman.logic;

import android.graphics.Point;

import com.ds.mo.engine.common.Circle;
import com.ds.mo.engine.common.Color;
import com.ds.mo.engine.common.OverlapTester;

import java.util.List;

public class Clyde extends Enemy {
    private Circle scatterBounds;

    public Clyde(int id, Tile[][] tiles, Pacman pacman, List<Point> allDots,
                 List<Point> allEnergizers, int x, int y) {
        super(id, tiles, pacman, allDots, allEnergizers, x, y);
        color = new Color(1, 185 / 255f, 80 / 255f, 1);

        //Create a circle 8 tiles wide around pacman
        scatterBounds = new Circle(pacman.pixel.x,
                pacman.pixel.y, 8 * Tile.TILE_WIDTH);
        System.out.println("clyde constructor finished...");
    }

    @Override
    public Tile getTarget(int mode) {
        switch (mode) {
            case Enemy.STATE_SCATTER:
                return tiles[clydeScatter.y][clydeScatter.x];
        }
        //If clyde is 8 tiles from pacman -> go hide in scatter corner
        if (OverlapTester.pointInCircle(scatterBounds, pixel.x, pixel.y)) {
            return tiles[clydeScatter.y][clydeScatter.x];
        }
        //Otherwise chase pacman
        return tiles[pacman.pacmanTile.y][pacman.pacmanTile.x];
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
//        Assets.clyde.update(deltaTime);
        scatterBounds.center.set(pacman.pixel.x, pacman.pixel.y);
    }

//    @Override
//    public void draw(Graphics2D g) {
//        g.setColor(color);
//        if (OverlapTester.pointInCircle(scatterBounds, pixel.x, pixel.y)) {
//            g.setColor(Color.RED);
//        }
//        g.drawOval((int) (scatterBounds.center.x - scatterBounds.radius),
//                (int) (scatterBounds.center.y - scatterBounds.radius),
//                (int) scatterBounds.radius * 2, (int) scatterBounds.radius * 2);
//    }
}
