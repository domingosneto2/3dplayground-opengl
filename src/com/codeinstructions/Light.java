package com.codeinstructions;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class Light {
    private Vector3f position;

    private Vector4f diffuseColor;

    private Vector4f ambientColor;

    private Vector4f specularColor;

    private float diffusePower;

    private float specularPower;

    private float ambientPower;

    private float constant;

    private float linear;

    private float quadratic;

    private boolean on = true;

    public Light(Vector3f position, Vector4f diffuseColor, Vector4f ambientColor, Vector4f specularColor, float diffusePower, float specularPower, float ambientPower, float constant, float linear, float quadratic) {
        this.position = position;
        this.diffuseColor = diffuseColor;
        this.ambientColor = ambientColor;
        this.specularColor = specularColor;
        this.diffusePower = diffusePower;
        this.specularPower = specularPower;
        this.ambientPower = ambientPower;
        this.constant = constant;
        this.linear = linear;
        this.quadratic = quadratic;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector4f getDiffuseColor() {
        return diffuseColor;
    }

    public void setDiffuseColor(Vector4f diffuseColor) {
        this.diffuseColor = diffuseColor;
    }

    public Vector4f getAmbientColor() {
        return ambientColor;
    }

    public void setAmbientColor(Vector4f ambientColor) {
        this.ambientColor = ambientColor;
    }

    public Vector4f getSpecularColor() {
        return specularColor;
    }

    public void setSpecularColor(Vector4f specularColor) {
        this.specularColor = specularColor;
    }

    public float getDiffusePower() {
        return on ? diffusePower : 0f;
    }

    public void setDiffusePower(float diffusePower) {
        this.diffusePower = diffusePower;
    }

    public float getSpecularPower() {
        return on ? specularPower : 0f;
    }

    public void setSpecularPower(float specularPower) {
        this.specularPower = specularPower;
    }

    public float getAmbientPower() {
        return on ? ambientPower : 0f;
    }

    public void setAmbientPower(float ambientPower) {
        this.ambientPower = ambientPower;
    }

    public float getConstant() {
        return constant;
    }

    public void setConstant(float constant) {
        this.constant = constant;
    }

    public float getLinear() {
        return linear;
    }

    public void setLinear(float linear) {
        this.linear = linear;
    }

    public float getQuadratic() {
        return quadratic;
    }

    public void setQuadratic(float quadratic) {
        this.quadratic = quadratic;
    }

    public void toogle() {
        on = !on;
    }

    public void increasePower() {
        diffusePower *= 1.2f;
        specularPower *= 1.2f;
        ambientPower *= 1.2f;
    }

    public void decreasePower() {
        diffusePower /= 1.2f;
        specularPower /= 1.2f;
        ambientPower /= 1.2f;
    }
}
