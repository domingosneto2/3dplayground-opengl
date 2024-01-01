package com.codeinstructions;

import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static com.codeinstructions.GLUtils.ioResourceToByteBuffer;
import static org.lwjgl.opengl.GL13C.*;
import static org.lwjgl.opengl.GL30C.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;

public class Texture {
    int texture;

    int width;
    int height;

    public Texture() {
    }

    void loadTexture(String resourceName, int format) throws IOException {
        IntBuffer texWidth = BufferUtils.createIntBuffer(1);
        IntBuffer texHeight = BufferUtils.createIntBuffer(1);
        IntBuffer components = BufferUtils.createIntBuffer(1);
        ByteBuffer data = stbi_load_from_memory(ioResourceToByteBuffer(resourceName, 1024), texWidth, texHeight, components, 0);

        texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        width = texWidth.get();
        height = texHeight.get();
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, format, GL_UNSIGNED_BYTE, data);
        glGenerateMipmap(GL_TEXTURE_2D);
    }

    public void bindToUnit(int unit) {
        glActiveTexture(unit);
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
