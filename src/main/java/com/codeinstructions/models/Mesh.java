package com.codeinstructions.models;

import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11C.GL_FLOAT;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20C.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.*;

public class Mesh {
    private float[] vertices;
    private int[] indices;

    int vertexSize;

    int normalSize;

    int normalOffset;

    int colorSize;

    int colorOffset;

    int texCoordSize;

    int texCoordOffset;

    int tangentSize;

    int tangentOffset;

    int bitangentSize;

    int bitangentOffset;

    int vao;
    int vbo;
    int ebo;

    int normalIndex;

    int colorIndex = -1;

    int texCoordIndex = -1;

    int tangentIndex = -1;

    int bitangentIndex = -1;

    boolean concave;

    boolean initialized;


    public Mesh(float[] vertices, int vertexSize, int normalSize, int tangentSize, int bitangentSize, int colorSize, int texCoordSize, boolean concave) {
        this.vertices = vertices;
        this.vertexSize = vertexSize;
        this.normalSize = normalSize;
        this.tangentSize = tangentSize;
        this.bitangentSize = bitangentSize;
        this.colorSize = colorSize;
        this.texCoordSize = texCoordSize;
        this.concave = concave;
    }

    public void initializeBuffers() {
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
        int stride = getStride();
        glVertexAttribPointer(0, vertexSize, GL_FLOAT, false, stride * 4, 0L);
        glEnableVertexAttribArray(0);
        int nextIndex = 1;
        int runningSize = vertexSize;

        if (normalSize != 0) {
            glVertexAttribPointer(nextIndex, normalSize, GL_FLOAT, false, stride * 4, runningSize * 4L);
            glEnableVertexAttribArray(nextIndex);
            normalOffset = runningSize;
            normalIndex = nextIndex;
            runningSize += normalSize;
            nextIndex++;
        }

        if (colorSize != 0) {
            glVertexAttribPointer(nextIndex, colorSize, GL_FLOAT, false, stride * 4, runningSize  * 4L);
            glEnableVertexAttribArray(nextIndex);
            colorOffset = runningSize;
            colorIndex = nextIndex;
            runningSize += colorSize;
            nextIndex++;
        }
        if (texCoordSize != 0) {
            glVertexAttribPointer(nextIndex, texCoordSize, GL_FLOAT, false, stride * 4, runningSize * 4L);
            glEnableVertexAttribArray(nextIndex);
            texCoordOffset = runningSize;
            texCoordIndex = nextIndex;
            runningSize += texCoordIndex;
            nextIndex++;
        }

        if (tangentSize != 0) {
            glVertexAttribPointer(nextIndex, tangentSize, GL_FLOAT, false, stride * 4, runningSize * 4L);
            glEnableVertexAttribArray(nextIndex);
            tangentOffset = runningSize;
            tangentIndex = nextIndex;
            runningSize += tangentSize;
            nextIndex++;
        }

        if (bitangentSize != 0) {
            glVertexAttribPointer(nextIndex, bitangentSize, GL_FLOAT, false, stride * 4, runningSize * 4L);
            glEnableVertexAttribArray(nextIndex);
            bitangentOffset = runningSize;
            bitangentIndex = nextIndex;
        }

        initialized = true;
    }

    public void releaseBuffers() {
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        if (indices != null) {
            glDeleteBuffers(ebo);
        }
    }

    public void bind() {
        glBindVertexArray(vao);
    }

    public boolean hasIndices() {
        return indices != null;
    }

    public int getNumVertices() {
        return vertices.length / (getStride());
    }

    public int getNumIndices() {
        return indices == null ? 0 : indices.length;
    }

    public int getStride() {
        return vertexSize + normalSize + colorSize + texCoordSize + tangentSize + bitangentSize;
    }

    public int getNormalOffset() {
        return normalOffset;
    }

    public int getColorOffset() {
        return colorOffset;
    }

    public int getTexCoordOffset() {
        return texCoordOffset;
    }

    public int getTangentOffset() {
        return tangentOffset;
    }

    public int getBitangentOffset() {
        return bitangentOffset;
    }

    public int getTexCoordSize() {
        return texCoordSize;
    }

    public Vector3f getTangent(int index) {
        int pos = getStride() * index + tangentOffset;
        return new Vector3f(vertices[pos], vertices[pos + 1], vertices[pos + 2]);
    }

    public Vector3f getBitangent(int index) {
        int pos = getStride() * index + bitangentOffset;
        return new Vector3f(vertices[pos], vertices[pos + 1], vertices[pos + 2]);
    }

    public Vector3f getNormal(int index) {
        if (normalSize == 0) {
            return new Vector3f();
        } else {
            int x = getStride() * index + vertexSize;
            return new Vector3f(vertices[x], vertices[x+1], vertices[x+2]);
        }
    }

    public Vector3f getVertex(int index) {
        if (normalSize == 0) {
            return new Vector3f();
        } else {
            int x = getStride() * index;
            return new Vector3f(vertices[x], vertices[x+1], vertices[x+2]);
        }
    }

    public boolean initialized() {
        return initialized;
    }
}
