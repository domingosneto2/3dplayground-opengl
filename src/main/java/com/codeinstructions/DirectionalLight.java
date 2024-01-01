package com.codeinstructions;

import org.joml.Vector3fc;
import org.joml.Vector4fc;

public class DirectionalLight {
    private Vector3fc direction;

    private Vector4fc diffuseColor;

    private Vector4fc ambientColor;

    private Vector4fc specularColor;

    private float diffusePower;

    private float specularPower;

    private float ambientPower;

    private boolean on = true;

    public DirectionalLight(Vector3fc direction, Vector4fc diffuseColor, Vector4fc ambientColor, Vector4fc specularColor, float diffusePower, float specularPower, float ambientPower, boolean on) {
        this.direction = direction;
        this.diffuseColor = diffuseColor;
        this.ambientColor = ambientColor;
        this.specularColor = specularColor;
        this.diffusePower = diffusePower;
        this.specularPower = specularPower;
        this.ambientPower = ambientPower;
        this.on = on;
    }

    public Vector3fc getDirection() {
        return direction;
    }

    public Vector4fc getDiffuseColor() {
        return diffuseColor;
    }

    public Vector4fc getAmbientColor() {
        return ambientColor;
    }

    public Vector4fc getSpecularColor() {
        return specularColor;
    }

    public float getDiffusePower() {
        return diffusePower;
    }

    public float getSpecularPower() {
        return specularPower;
    }

    public float getAmbientPower() {
        return ambientPower;
    }

    public boolean isOn() {
        return on;
    }
}
