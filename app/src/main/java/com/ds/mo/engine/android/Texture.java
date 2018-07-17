package com.ds.mo.engine.android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

import com.ds.mo.engine.framework.FileIO;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Mo on 27/09/2017.
 */

public class Texture {
    private GLGraphics glGraphics;
    private FileIO fileIO;
    private String fileName;
    private int textureId;
    private int minFilter;
    private int magFilter;
    public int width;
    public int height;

    public Texture(GLGame glGame, String fileName) {
        this.glGraphics = glGame.glGraphics;
        this.fileIO = glGame.fileIO;
        this.fileName = fileName;
        load();
    }

    private void load() {
        GL10 gl = glGraphics.getGL();
        int textureIds[] = new int[1];          //List of textures ids
        gl.glGenTextures(1, textureIds, 0);
        textureId = textureIds[0];

        InputStream in = null;
        try {
            in = fileIO.readAsset(fileName);
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            Log.d("Texture", "texture config: "+bitmap.getConfig());
            Log.d("Texture", "has alpha?: "+bitmap.hasAlpha());
            width = bitmap.getWidth();
            height = bitmap.getHeight();

            //bind texture
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
            setFilters(GL10.GL_NEAREST, GL10.GL_NEAREST);
            //unbind
            gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
        } catch (IOException e) {
            Log.d("Texture", "couldn't load asset " + fileName);
            throw new RuntimeException("couldn't load asset " + fileName + e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public void reload() {
        load();
        bind();
        setFilters(minFilter, magFilter);
        glGraphics.getGL().glBindTexture(GL10.GL_TEXTURE_2D, 0);
    }

    public void setFilters(int minFilter, int magFilter) {
        this.minFilter = minFilter;
        this.magFilter = magFilter;
        GL10 gl = glGraphics.getGL();
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, minFilter);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, magFilter);
    }

    public void bind() {
        GL10 gl = glGraphics.getGL();
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
    }

    public void dispose() {
        GL10 gl = glGraphics.getGL();
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
        int[] textureIds = {textureId};
        gl.glDeleteTextures(1, textureIds, 0);
    }
}
