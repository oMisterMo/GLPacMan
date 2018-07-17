package com.ds.mo.engine.framework;

import com.ds.mo.engine.framework.Graphics.PixmapFormat;

/**
 * Bitmap wrapper
 * <p/>
 * Created by Mo on 26/05/2017.
 */
public interface Pixmap {

    /**
     * Gets the width of the image
     *
     * @return width
     */
    int getWidth();

    /**
     * Gets the height of the image
     *
     * @return height
     */
    int getHeight();

    /**
     * Gets the format of the image
     * <p/>
     * ARGB 8888/RBG 888/ARGB 4444 etc
     *
     * @return color model of image
     */
    PixmapFormat getFormat();

    /**
     * Free up image resource
     */
    void dispose();
}
