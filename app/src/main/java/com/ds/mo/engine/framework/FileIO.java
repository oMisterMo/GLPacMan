package com.ds.mo.engine.framework;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * File input output interface
 * <p/>
 * Created by Mo on 21/09/2016.
 */
public interface FileIO {

    /**
     * Reads from asset directory
     *
     * @param filename name of file in assets folder
     * @return input stream
     * @throws IOException
     */
    InputStream readAsset(String filename) throws IOException;

    /**
     * Reads from SD card
     *
     * @param filename name of file in assets folder
     * @return input stream
     * @throws IOException
     */
    InputStream readFile(String filename) throws IOException;

    /**
     * Write to the SD card
     *
     * @param filename name of file in assets folder
     * @return output stream
     * @throws IOException
     */
    OutputStream writeFile(String filename) throws IOException;

}
