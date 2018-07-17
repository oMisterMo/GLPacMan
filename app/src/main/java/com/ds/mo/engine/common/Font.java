package com.ds.mo.engine.common;

import com.ds.mo.engine.android.SpriteBatcher;
import com.ds.mo.engine.android.Texture;
import com.ds.mo.engine.android.TextureRegion;

/**
 * Created by Mo on 07/10/2017.
 */

public class Font {
    public final Texture texture;
    public final int glyphWidth;        //32
    public final int glyphHeight;       //32
    public final TextureRegion[] glyphs = new TextureRegion[96];

    /**
     * Constructs a new font object storing the information of the font bitmap within a texture atlas
     * <p>
     * N.B. max no of glyphs (texture region) is 96
     *
     * @param texture      texture atlas containing font bitmap
     * @param offsetX      top left position of font bitmap
     * @param offsetY      top left position of font bitmap
     * @param glyphsPerRow no of characters per row
     * @param glyphWidth   width of a single glyph
     * @param glyphHeight  height of a single glyph
     */
    public Font(Texture texture, int offsetX, int offsetY,
                int glyphsPerRow, int glyphWidth, int glyphHeight) {
        this.texture = texture;
        this.glyphWidth = glyphWidth;
        this.glyphHeight = glyphHeight;
        int x = offsetX;
        int y = offsetY;
        for (int i = 0; i < glyphs.length; i++) {
            glyphs[i] = new TextureRegion(texture, x, y, glyphWidth, glyphHeight);
            x += glyphWidth;
            //If we reach the end of the first row
            if (x == offsetX + glyphsPerRow * glyphWidth) {
                //Reset x pos and increment y width
                x = offsetX;
                y += glyphHeight;
            }
        }
    }

    /**
     * Given a text string, will render the appropriate letter within the play bitmap
     *
     * @param batcher holds the vertex buffer to draw
     * @param text    string to draw
     * @param x       x position (specifies the center)
     * @param y       y position (specifies the center)
     */
    public void drawText(SpriteBatcher batcher, String text, float x, float y) {
        int len = text.length();
        float offsetX = (x + glyphWidth / 2) - (glyphWidth * len) / 2;
        for (int i = 0; i < len; i++) {
//            int c = text.charAt(i) - 48;
            int c = text.charAt(i) - ' ';
            if (c < 0 || c > glyphs.length - 1) {
                continue;
            }
            TextureRegion glyph = glyphs[c];
//            Log.d("FONT", "drawing font at: "+x+", "+y);
//            Log.d("FONT", "width: "+glyphWidth);
            batcher.drawSprite(offsetX, y, glyphWidth, glyphHeight, glyph);
            offsetX += glyphWidth;
        }
    }

    /**
     * Draw scaled version
     *
     * @param batcher
     * @param text
     * @param x
     * @param y
     */
    public void drawText(SpriteBatcher batcher, String text, float x, float y, float width, float height) {
        int len = text.length();
        float offsetX = (x + width / 2) - (width * len) / 2;
        for (int i = 0; i < len; i++) {
//            int c = text.charAt(i) - 48;
            int c = text.charAt(i) - ' ';
            if (c < 0 || c > glyphs.length - 1) {
                continue;
            }
            TextureRegion glyph = glyphs[c];
//            Log.d("FONT", "drawing font at: "+x+", "+y);
//            Log.d("FONT", "width: "+glyphWidth);
            batcher.drawSprite(offsetX, y, width, height, glyph);
            offsetX += width;
        }
    }

    public void drawNumber(SpriteBatcher batcher, String text, float x, float y) {
        int len = text.length();
        for (int i = 0; i < len; i++) {
            int c = text.charAt(i) - 48;
            if (c < 0 || c > glyphs.length - 1) {
                continue;
            }
            TextureRegion glyph = glyphs[c];
//            Log.d("FONT", "drawing font at: "+x+", "+y);
//            Log.d("FONT", "width: "+glyphWidth);
            batcher.drawSprite(x, y, glyphWidth, glyphHeight, glyph);
            x += glyphWidth;
        }
    }

    /**
     * Draw scaled number
     *
     * @param batcher
     * @param text
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void drawNumber(SpriteBatcher batcher, String text, float x, float y,
                           float width, float height) {
        int len = text.length();
        for (int i = 0; i < len; i++) {
            int c = text.charAt(i) - 48;
            if (c < 0 || c > glyphs.length - 1) {
                continue;
            }
            TextureRegion glyph = glyphs[c];
            batcher.drawSprite(x, y, width, height, glyph);
            x += width;
        }
    }
}
