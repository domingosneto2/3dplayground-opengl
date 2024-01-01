package com.codeinstructions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11C.GL_RGB;
import static org.lwjgl.opengl.GL11C.GL_RGBA;

public class TextureCatalog {

    private Map<String, TextureSet> textures = new HashMap<>();

    public void loadTextures() throws IOException {
        loadTexture("felt", "fabrics_0075_color_2k.jpg", GL_RGB, "fabrics_0075_normal_opengl_2k.png", GL_RGBA);
        loadTexture("earth", "8081_earthmap10k.jpg", GL_RGB, "EarthNormal.png", GL_RGB, "8081_earthspec10k.png", GL_RGBA);
        loadTexture("sphere-test", "DXCurKn2.png", GL_RGBA);
    }

    private void loadTexture(String name, String colorTexture, int colorFormat, String normalTexture, int normalFormat, String specularTexture, int specularFormat) throws IOException {
        textures.put(name, new TextureSet(name, colorTexture, colorFormat, normalTexture, normalFormat, specularTexture, specularFormat));
    }

    private void loadTexture(String name, String colorTexture, int colorFormat, String normalTexture, int normalFormat) throws IOException {
        textures.put(name, new TextureSet(name, colorTexture, colorFormat, normalTexture, normalFormat));
    }

    private void loadTexture(String name, String colorTexture, int colorFormat) throws IOException {
        textures.put(name, new TextureSet(name, colorTexture, colorFormat));
    }

    public TextureSet getTexture(String textureName) {
        return textures.get(textureName);
    }
}
