package com.codeinstructions;

import java.io.IOException;

import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE1;

public class TextureSet {
    private String name;
    private Texture color;
    private Texture normal;

    private Texture specular;

    public TextureSet(String name, String colorTexture, int colorFormat, String normalTexture, int normalFormat, String specularTexture, int specularFormat) throws IOException {
        this.name = name;
        color = new Texture();
        color.loadTexture(colorTexture, colorFormat);
        normal = new Texture();
        normal.loadTexture(normalTexture, normalFormat);
        specular = new Texture();
        specular.loadTexture(specularTexture, specularFormat);
    }


    public TextureSet(String name, String colorTexture, int colorFormat, String normalTexture, int normalFormat) throws IOException {
        this.name = name;
        color = new Texture();
        color.loadTexture(colorTexture, colorFormat);
        normal = new Texture();
        normal.loadTexture(normalTexture, normalFormat);
    }

    public TextureSet(String name, String colorTexture, int colorFormat) throws IOException {
        this.name = name;
        color = new Texture();
        color.loadTexture(colorTexture, colorFormat);
    }

    public String getName() {
        return name;
    }

    public Texture getColor() {
        return color;
    }

    public Texture getNormal() {
        return normal;
    }

    public void bind() {
        color.bindToUnit(GL_TEXTURE0);
        if (normal != null) {
            normal.bindToUnit(GL_TEXTURE1);
        }
        if (specular != null) {
            specular.bindToUnit(GL_TEXTURE2);
        }
    }

    public Texture getSpecular() {
        return specular;
    }
}
