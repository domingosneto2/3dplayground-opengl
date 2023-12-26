package com.codeinstructions;

import static org.lwjgl.opengl.GL11C.GL_FLOAT;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20C.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;

public class Mesh {
    private float[] vertices;
    private int[] indices;

    int vertexSize;

    int colorSize;

    int texCoordSize;

    int vao;
    int vbo;
    int ebo;

    int colorIndex = -1;

    int texCoordIndex = -1;


    public Mesh(float[] vertices, int[] indices, int vertexSize, int colorSize, int texCoordSize) {
        this.vertices = vertices;
        this.indices = indices;
        this.vertexSize = vertexSize;
        this.colorSize = colorSize;
        this.texCoordSize = texCoordSize;
    }

    public Mesh(float[] vertices, int vertexSize, int colorSize, int texCoordSize) {
        this.vertices = vertices;
        this.vertexSize = vertexSize;
        this.colorSize = colorSize;
        this.texCoordSize = texCoordSize;
    }

    public void bindBuffers() {
        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        vbo = glGenBuffers();

        if (indices != null) {
            ebo = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        }

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        int stride = vertexSize + colorSize + texCoordSize;
        glVertexAttribPointer(0, vertexSize, GL_FLOAT, false, stride * 4, 0L);
        glEnableVertexAttribArray(0);
        int nextIndex = 1;
        if (colorSize != 0) {
            glVertexAttribPointer(1, colorSize, GL_FLOAT, false, stride * 4, vertexSize * 4L);
            glEnableVertexAttribArray(1);
            colorIndex = 1;
            nextIndex++;
        }
        if (texCoordSize != 0) {
            glVertexAttribPointer(nextIndex, texCoordSize, GL_FLOAT, false, stride * 4, (vertexSize + colorSize) * 4L);
            glEnableVertexAttribArray(nextIndex);
            texCoordIndex = nextIndex;
        }
    }
}
