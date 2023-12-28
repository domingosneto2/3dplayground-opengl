package com.codeinstructions;

import org.joml.Vector4fc;

public class Material {
    private Vector4fc color;

    private float specularStrength;

    private float specularFactor;

    public Material(Vector4fc color, float specularStrength, float specularFactor) {
        this.color = color;
        this.specularStrength = specularStrength;
        this.specularFactor = specularFactor;
    }

    public Vector4fc getColor() {
        return color;
    }

    public float getSpecularStrength() {
        return specularStrength;
    }

    public float getSpecularFactor() {
        return specularFactor;
    }
}
