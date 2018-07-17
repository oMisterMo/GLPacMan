package com.ds.mo.engine.android;

/**
 * Created by Mo on 02/10/2017.
 */

public class TextureRegion {
    public final float u1, v1;      //Top left coordinates of texture within Texture atlas
    public final float u2, v2;      //Bottom right coordinates
    public final Texture texture;

    public TextureRegion(Texture texture, float x, float y, float width, float height) {
        this.u1 = x / texture.width;
        this.v1 = y / texture.height;
        this.u2 = this.u1 + width / texture.width;
        this.v2 = this.v1 + height / texture.height;
        this.texture = texture;
    }
}
