package com.ds.mo.engine.glpacman.logic;

import android.graphics.Point;

import com.ds.mo.engine.common.Color;

import java.util.List;

public class Blinky extends Enemy{
    public Blinky(int id, Tile[][] tiles, Pacman pacman, List<Point> allDots,
                  List<Point> allEnergizers, int x, int y) {
        super(id, tiles, pacman, allDots, allEnergizers, x, y);
        color = new Color(1, 0, 0, 1);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
//        Assets.blinky.update(deltaTime);
//        System.out.println("BLINKY.Y = "+pixel.y);
    }

    @Override
    public Tile getTarget(int mode) {
        return super.getTarget(mode);
    }
}
