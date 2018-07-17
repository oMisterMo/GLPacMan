package com.ds.mo.engine.framework;

/**
 * Graphics interface
 * <p/>
 * Created by Mo on 26/05/2017.
 */
public interface Graphics {
    enum PixmapFormat {
        ARGB8888, ARGB4444, RGB565
    }

    /**
     * Loads a new image from the assets folder
     *
     * @param filename name of file in assets folder
     * @param format   the color model to use
     * @return new pixmap
     */
    Pixmap newPixmap(String filename, PixmapFormat format);

    /**
     * Clears the whole screen with the given color
     *
     * @param color color in hex form (0xff0000ff)
     */
    void clear(int color);

    /**
     * Draws a pixel at the given location
     *
     * @param x     position
     * @param y     position
     * @param color color of pixel
     */
    void drawPixel(int x, int y, int color);

    /**
     * Draws a line at the given location
     *
     * @param x     start x
     * @param y     start y
     * @param x2    end x
     * @param y2    end y
     * @param color color of line
     */
    void drawLine(int x, int y, int x2, int y2, int color);

    /**
     * Draws a rectangle at the given location
     *
     * @param x      start x
     * @param y      start y
     * @param width  width of rect
     * @param height height of rect
     * @param color  color of rect
     */
    void drawRect(int x, int y, int width, int height, int color);

    /**
     * Draws an image at the given location bounded by the rect
     * <p/>
     * The image will scale up or down depending on the size of the rect
     *
     * @param pixmap    image to draw
     * @param x         x position
     * @param y         y position
     * @param srcX      rect x
     * @param srcY      rect y
     * @param srcWidth  rect width
     * @param srcHeight rect height
     */
    void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY,
                    int srcWidth, int srcHeight);

    /**
     * Draws an image at the given location bounded by the rect
     * <p/>
     * The image will scale up or down depending on the size of the rect
     *
     * @param pixmap image to draw
     * @param x      x position
     * @param y      y position
     */
    void drawPixmap(Pixmap pixmap, int x, int y);

    /**
     * Gets the width of the framebuffer
     *
     * @return width
     */
    int getWidth();

    /**
     * Gets the height of the framebuffer
     *
     * @return height
     */
    int getHeight();
}
