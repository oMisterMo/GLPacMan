package com.ds.mo.engine.android;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Mo on 27/09/2017.
 */

public class Vertices {
    final GLGraphics glGraphics;
    final boolean hasColor;
    final boolean hasTextCoords;
    final int vertexSize;
    final FloatBuffer vertices;
    final ShortBuffer indices;

    public Vertices(GLGraphics glGraphics, int maxVertices, int maxIndices,
                    boolean hasColor, boolean hasTextCoords) {
        this.glGraphics = glGraphics;
        this.hasColor = hasColor;
        this.hasTextCoords = hasTextCoords;
        this.vertexSize = (2 + (hasColor ? 4 : 0) + (hasTextCoords ? 2 : 0)) * 4;

        ByteBuffer buffer = ByteBuffer.allocateDirect(maxVertices * vertexSize);
        buffer.order(ByteOrder.nativeOrder());
        vertices = buffer.asFloatBuffer();

        if (maxIndices > 0) {
            buffer = ByteBuffer.allocateDirect(maxIndices * Short.SIZE / 8);
            buffer.order(ByteOrder.nativeOrder());
            indices = buffer.asShortBuffer();
        } else {
            indices = null;
        }
    }

    public void setVertices(float[] vertices, int offset, int length) {
        this.vertices.clear();
        this.vertices.put(vertices, offset, length);
        this.vertices.flip();
    }

    public void setIndices(short[] indices, int offset, int length) {
        this.indices.clear();
        this.indices.put(indices, offset, length);
        this.indices.flip();
    }

    public void bind(){
        GL10 gl = glGraphics.getGL();

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        vertices.position(0);
        gl.glVertexPointer(2, GL10.GL_FLOAT, vertexSize, vertices);

        if (hasColor) {
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            vertices.position(2);
            gl.glColorPointer(4, GL10.GL_FLOAT, vertexSize, vertices);
        }
        if (hasTextCoords) {
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            vertices.position(hasColor ? 6 : 2);
            gl.glTexCoordPointer(2, GL10.GL_FLOAT, vertexSize, vertices);
        }
    }

    public void unbind(){
        GL10 gl = glGraphics.getGL();
        //I switched hasColor and hasTextCoor
        if (hasColor) {
            gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        }
        if (hasTextCoords) {
            gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        }
    }

    public void draw(int primitiveType, int offset, int numVertices) {
        GL10 gl = glGraphics.getGL();
        //bind

        //draw
        if (indices != null) {
            indices.position(offset);
            gl.glDrawElements(primitiveType, numVertices, GL10.GL_UNSIGNED_SHORT, indices);
        } else {
            gl.glDrawArrays(primitiveType, offset, numVertices);
        }
        //unbind
    }
}
