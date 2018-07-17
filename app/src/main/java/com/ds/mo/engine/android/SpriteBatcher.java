package com.ds.mo.engine.android;

/**
 * Created by Mo on 05/11/2017.
 */

import com.ds.mo.engine.common.Color;
import com.ds.mo.engine.common.Vector2D;

import javax.microedition.khronos.opengles.GL10;

public class SpriteBatcher {
    final float[] verticesBuffer;
    int bufferIndex;
    Vertices vertices;
    int numSprites;

//    float color = Color.WHITE.toFloatBits();
//    private Color tempColor = new Color(1,1,1,1);   //WHITE
    private Color color = new Color(Color.WHITE);      //Default color

    public SpriteBatcher(GLGraphics glGraphics, int maxSprites) {
        this.verticesBuffer = new float[maxSprites * (2 + 4 + 2) * 4];    //(2 position + 4 color + 2 texture) * 4 vertices
        this.vertices = new Vertices(glGraphics, maxSprites * 4, maxSprites * 6, true, true);

        this.bufferIndex = 0;
        this.numSprites = 0;

        short[] indices = new short[maxSprites * 6];    //6 indices each (0, 1, 2, 2, 3, 0)...etc
        int len = indices.length;
        short j = 0;
        //Pre compute indices so we only have to set them once for each sprite
        for (int i = 0; i < len; i += 6, j += 4) {
            //0, 1, 2, 2, 3, 0  first round
            //4, 5, 6, 6, 7, 4  next round (j+= 4)
            indices[i + 0] = (short) (j + 0);
            indices[i + 1] = (short) (j + 1);
            indices[i + 2] = (short) (j + 2);
            indices[i + 3] = (short) (j + 2);
            indices[i + 4] = (short) (j + 3);
            indices[i + 5] = (short) (j + 0);
        }
        vertices.setIndices(indices, 0, indices.length);
    }

    public void beginBatch(Texture texture) {
        texture.bind();
        numSprites = 0;
        bufferIndex = 0;
    }

    public void endBatch() {
        vertices.setVertices(verticesBuffer, 0, bufferIndex);
        vertices.bind();
        vertices.draw(GL10.GL_TRIANGLES, 0, numSprites * 6);
        vertices.unbind();
    }

    public void drawSprite(float x, float y, float width, float height, TextureRegion region) {
        float halfWidth = width / 2;
        float halfHeight = height / 2;
        float x1 = x - halfWidth;
        float y1 = y - halfHeight;
        float x2 = x + halfWidth;
        float y2 = y + halfHeight;

//        Log.d("SpriteBatcher", "color: r " + red + ", g " + green + ", b " + blue + ", a " + alpha);
        Color color = this.color;
//        Log.d("SpriteBatcher", "color:  " + color);


        //Bottom left vertex
        verticesBuffer[bufferIndex++] = x1;         //add position
        verticesBuffer[bufferIndex++] = y1;
        verticesBuffer[bufferIndex++] = color.r;
        verticesBuffer[bufferIndex++] = color.g;
        verticesBuffer[bufferIndex++] = color.b;
        verticesBuffer[bufferIndex++] = color.a;
        verticesBuffer[bufferIndex++] = region.u1;  //add texture region
        verticesBuffer[bufferIndex++] = region.v2;

        //Bottom right
        verticesBuffer[bufferIndex++] = x2;
        verticesBuffer[bufferIndex++] = y1;
        verticesBuffer[bufferIndex++] = color.r;
        verticesBuffer[bufferIndex++] = color.g;
        verticesBuffer[bufferIndex++] = color.b;
        verticesBuffer[bufferIndex++] = color.a;
        verticesBuffer[bufferIndex++] = region.u2;
        verticesBuffer[bufferIndex++] = region.v2;

        //Top right
        verticesBuffer[bufferIndex++] = x2;
        verticesBuffer[bufferIndex++] = y2;
        verticesBuffer[bufferIndex++] = color.r;
        verticesBuffer[bufferIndex++] = color.g;
        verticesBuffer[bufferIndex++] = color.b;
        verticesBuffer[bufferIndex++] = color.a;
        verticesBuffer[bufferIndex++] = region.u2;
        verticesBuffer[bufferIndex++] = region.v1;

        //Top left
        verticesBuffer[bufferIndex++] = x1;
        verticesBuffer[bufferIndex++] = y2;
        verticesBuffer[bufferIndex++] = color.r;
        verticesBuffer[bufferIndex++] = color.g;
        verticesBuffer[bufferIndex++] = color.b;
        verticesBuffer[bufferIndex++] = color.a;
        verticesBuffer[bufferIndex++] = region.u1;
        verticesBuffer[bufferIndex++] = region.v1;

        numSprites++;
    }

    public void drawSprite(float x, float y, float width, float height, float angle, TextureRegion region) {
        float halfWidth = width / 2;
        float halfHeight = height / 2;

        float rad = angle * Vector2D.TO_RADIANS;
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);

        float x1 = -halfWidth * cos - (-halfHeight) * sin;
        float y1 = -halfWidth * sin + (-halfHeight) * cos;
        float x2 = halfWidth * cos - (-halfHeight) * sin;
        float y2 = halfWidth * sin + (-halfHeight) * cos;
        float x3 = halfWidth * cos - halfHeight * sin;
        float y3 = halfWidth * sin + halfHeight * cos;
        float x4 = -halfWidth * cos - halfHeight * sin;
        float y4 = -halfWidth * sin + halfHeight * cos;

        x1 += x;
        y1 += y;
        x2 += x;
        y2 += y;
        x3 += x;
        y3 += y;
        x4 += x;
        y4 += y;

        //Color between 0 - 1
//        float color = this.color;
        Color color = this.color;

        //Bottom left vertex
        verticesBuffer[bufferIndex++] = x1;         //add position
        verticesBuffer[bufferIndex++] = y1;
        verticesBuffer[bufferIndex++] = color.r;
        verticesBuffer[bufferIndex++] = color.g;
        verticesBuffer[bufferIndex++] = color.b;
        verticesBuffer[bufferIndex++] = color.a;
        verticesBuffer[bufferIndex++] = region.u1;  //add texture region
        verticesBuffer[bufferIndex++] = region.v2;

        //Bottom right
        verticesBuffer[bufferIndex++] = x2;
        verticesBuffer[bufferIndex++] = y2;
        verticesBuffer[bufferIndex++] = color.r;
        verticesBuffer[bufferIndex++] = color.g;
        verticesBuffer[bufferIndex++] = color.b;
        verticesBuffer[bufferIndex++] = color.a;
        verticesBuffer[bufferIndex++] = region.u2;
        verticesBuffer[bufferIndex++] = region.v2;

        //Top right
        verticesBuffer[bufferIndex++] = x3;
        verticesBuffer[bufferIndex++] = y3;
        verticesBuffer[bufferIndex++] = color.r;
        verticesBuffer[bufferIndex++] = color.g;
        verticesBuffer[bufferIndex++] = color.b;
        verticesBuffer[bufferIndex++] = color.a;
        verticesBuffer[bufferIndex++] = region.u2;
        verticesBuffer[bufferIndex++] = region.v1;

        //Top left
        verticesBuffer[bufferIndex++] = x4;
        verticesBuffer[bufferIndex++] = y4;
        verticesBuffer[bufferIndex++] = color.r;
        verticesBuffer[bufferIndex++] = color.g;
        verticesBuffer[bufferIndex++] = color.b;
        verticesBuffer[bufferIndex++] = color.a;
        verticesBuffer[bufferIndex++] = region.u1;
        verticesBuffer[bufferIndex++] = region.v1;

        numSprites++;
    }

    public void setColor(Color tint){
        this.color.set(tint);               //correct
//        this.color = tint;                //wrong
//        this.color = new Color(tint);     //okay-ish
//        Log.d("SB", color.toString());
    }

    public void setColor(float r, float g, float b, float a){
//        int intBits = (int)(255 * a) << 24 | (int)(255 * b) << 16 | (int)(255 * g) << 8 | (int)(255 * r);
//        color = Helper.intToFloatColor(intBits);
        this.color.r = r;
        this.color.g = g;
        this.color.b = b;
        this.color.a = a;
//        Log.d("SB", color.toString());
        this.color.clamp();
    }

//    public Color getColor () {
//        int intBits = Helper.floatToIntColor(color);
//        Color color = tempColor;
//        color.r = (intBits & 0xff) / 255f;
//        color.g = ((intBits >>> 8) & 0xff) / 255f;
//        color.b = ((intBits >>> 16) & 0xff) / 255f;
//        color.a = ((intBits >>> 24) & 0xff) / 255f;
//        return color;
//    }


}
