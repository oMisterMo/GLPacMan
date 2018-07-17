package com.ds.mo.engine.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mo on 30/09/2017.
 */

public class SpatialHashGrid {

    List<GameObject>[] dynamicCells;
    List<GameObject>[] staticCells;
    int cellsPerRow;    //ceil(WORLD.WIDTH / cellSize)
    int cellsPerCol;
    float cellSize;     //2.5
    int[] cellIds = new int[4]; //Cell an object is currently contained in
    List<GameObject> foundObjects;

    @SuppressWarnings("unchecked")
    public SpatialHashGrid(float worldWidth, float worldHeight, float cellSize) {
        this.cellSize = cellSize;
        this.cellsPerRow = (int) Math.ceil(worldWidth / cellSize);      //round up
        this.cellsPerCol = (int) Math.ceil(worldHeight / cellSize);
        int numOfCells = cellsPerRow * cellsPerCol;                     //12

        dynamicCells = new List[numOfCells];
        staticCells = new List[numOfCells];
        for (int i = 0; i < numOfCells; i++) {
            //Assumption that a single cell will not contain more than 10 gameObjects at once so
            //array doesn't have to be resized
            dynamicCells[i] = new ArrayList<>(10);
            staticCells[i] = new ArrayList<>(10);
        }
        foundObjects = new ArrayList<>(10);

////        //Debugging
//        System.out.println("*********************************");
//        System.out.println("cells size: " + cellSize);          //250
//        System.out.println("cells per row: " + cellsPerRow);    //6
//        System.out.println("cells per col: " + cellsPerCol);    //3
//        System.out.println("num of cells: " + numOfCells);      //18
//        System.out.println("*********************************");
    }

    public void insertStaticObject(GameObject obj) {
        int[] cellIds = getCellIds(obj);
        int i = 0;
        int cellId = -1;

//        int j = 0;
//        for (j = 0; j <= 3; j++) {
//            System.out.println("j: " + j + ", cellId: " + cellIds[j]);
//        }
//        System.out.println("---------------------------------------");
        while (i <= 3 && (cellId = cellIds[i++]) != -1) {
            //cellId should be between 0 -> 12 (totalCells)
            staticCells[cellId].add(obj);
        }
    }

    public void insertDynamicObject(GameObject obj) {
        int[] cellIds = getCellIds(obj);
        int i = 0;
        int cellId = -1;
        while (i <= 3 && (cellId = cellIds[i++]) != -1) {
            dynamicCells[cellId].add(obj);
        }
    }

    public void removeObject(GameObject obj) {
        int[] cellIds = getCellIds(obj);
        int i = 0;
        int cellId = -1;
        while (i <= 3 && (cellId = cellIds[i++]) != -1) {
            dynamicCells[cellId].remove(obj);
            staticCells[cellId].remove(obj);
        }
    }

    public void clearDynamicCells(GameObject obj) {
        int len = dynamicCells.length;
        for (int i = 0; i < len; i++) {
            dynamicCells[i].clear();
        }
    }

    /**
     * Given a game object, will return a list of game objects contained in the
     * same cell
     *
     * @param obj a game object
     * @return a list of neighbouring objects in same cell as 'obj'
     */
    public List<GameObject> getPotentialColliders(GameObject obj) {
        foundObjects.clear();
        int[] cellIds = getCellIds(obj);
        int i = 0;
        int cellId = -1;

        while (i <= 3 && (cellId = cellIds[i++]) != -1) {
            //go through dynamic list
            int len = dynamicCells[cellId].size();
            for (int j = 0; j < len; j++) {
                GameObject collider = dynamicCells[cellId].get(j);
                if (!foundObjects.contains(collider)) {
                    foundObjects.add(collider);
                }
            }

            //go through static list
            len = staticCells[cellId].size();
            for (int j = 0; j < len; j++) {
                GameObject collider = staticCells[cellId].get(j);
                if (!foundObjects.contains(collider)) {
                    foundObjects.add(collider);
                }
            }
        }
        return foundObjects;
    }

    public int[] getCellIds(GameObject obj) {
        //Bottom left -> Grid coordinates e.g (2, 1)
        int x1 = (int) Math.floor(obj.bounds.lowerLeft.x / cellSize);
        int y1 = (int) Math.floor(obj.bounds.lowerLeft.y / cellSize);
        //Top right
        int x2 = (int) Math.floor((obj.bounds.lowerLeft.x + obj.bounds.width) / cellSize);
        int y2 = (int) Math.floor((obj.bounds.lowerLeft.y + obj.bounds.height) / cellSize);

//        System.out.println(obj.bounds);
//        System.out.println("bottom left -> (" + x1 + ", " + y1 + ")");
//        System.out.println("top right   -> (" + x2 + ", " + y2 + ")");

        //Single cell
        //If bottom left cell and top right cell have the same coordinates
        if (x1 == x2 && y1 == y2) {
//            System.out.println("SINGLE CELL");
            if (x1 >= 0 && x1 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol) {
//                System.out.println("*****In Cell: " + (x1 + y1 * cellsPerRow) + "*****");
                cellIds[0] = x1 + y1 * cellsPerRow;
                cellIds[1] = -1;
                cellIds[2] = -1;
                cellIds[3] = -1;
            } else {
                cellIds[0] = -1;
                cellIds[1] = -1;
                cellIds[2] = -1;
                cellIds[3] = -1;
            }
        } else if (x1 == x2) {
//            System.out.println("VERTICAL CELLS");
            //Two cell (Vertical)
            int i = 0;
            if (x1 >= 0 && x1 < cellsPerRow) {
                if (y1 >= 0 && y1 < cellsPerCol) {
//                    System.out.println("*****In Cell: " + (x1 + y1 * cellsPerRow) + "*****");
                    cellIds[i++] = x1 + y1 * cellsPerRow;
                }
                if (y2 >= 0 && y2 < cellsPerCol) {
//                    System.out.println("*****In Cell: " + (x1 + y2 * cellsPerRow) + "*****");
                    cellIds[i++] = x1 + y2 * cellsPerRow;
                }
            }
            while (i <= 3) {
                cellIds[i++] = -1;
            }
        } else if (y1 == y2) {
//            System.out.println("HORIZONTAL CELLS");
            //Two cell (Horizontal)
            int i = 0;
            if (y1 >= 0 && y1 < cellsPerCol) {
                if (x1 >= 0 && x1 < cellsPerRow) {
//                    System.out.println("*****In Cell: " + (x1 + y1 * cellsPerRow) + "*****");
                    cellIds[i++] = x1 + y1 * cellsPerRow;
                }
                if (x2 >= 0 && x2 < cellsPerRow) {
//                    System.out.println("*****In Cell: " + (x2 + y1 * cellsPerRow) + "*****");
                    cellIds[i++] = x2 + y1 * cellsPerRow;
                }
            }
            while (i <= 3) {
                cellIds[i++] = -1;
            }
        } else {
//            System.out.println("ALL CELLS");
            //Four cell
            int i = 0;
            int y1CellsPerRow = y1 * cellsPerRow;
            int y2CellsPerRow = y2 * cellsPerRow;
            if (x1 >= 0 && x1 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol) {
//                System.out.println("*****In Cell: " + (x1 + y1CellsPerRow) + "*****");
                cellIds[i++] = x1 + y1CellsPerRow;
            }
            if (x2 >= 0 && x2 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol) {
//                System.out.println("*****In Cell: " + (x2 + y1CellsPerRow) + "*****");
                cellIds[i++] = x2 + y1CellsPerRow;
            }
            if (x2 >= 0 && x2 < cellsPerRow && y2 >= 0 && y2 < cellsPerCol) {
//                System.out.println("*****In Cell: " + (x2 + y2CellsPerRow) + "*****");
                cellIds[i++] = x2 + y2CellsPerRow;
            }
            if (x1 >= 0 && x1 < cellsPerRow && y2 >= 0 && y2 < cellsPerCol) {
//                System.out.println("*****In Cell: " + (x1 + y2CellsPerRow) + "*****");
                cellIds[i++] = x1 + y2CellsPerRow;
            }
            while (i <= 3) {
                cellIds[i++] = -1;
            }
        }
        return cellIds;
    }

    // TODO: 15/11/2017 Remove me, debugging only
    public List<GameObject>[] getStaticCells(){
        return staticCells;
    }
}
