package com.codeinstructions;

import com.codeinstructions.models.Model;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.joml.Vector4fc;

public class Light {
    private Vector3f position;

    private Vector4fc diffuseColor;

    private Vector4fc ambientColor;

    private Vector4fc specularColor;

    private float diffusePower;

    private float specularPower;

    private float ambientPower;

    private float constant;

    private float linear;

    private float quadratic;

    private boolean on = true;

    private Model model;

    private Matrix4f modelTransform;

    public Light(Vector3f position, Vector4fc diffuseColor, Vector4fc ambientColor, Vector4fc specularColor,
                 float diffusePower, float specularPower, float ambientPower, float constant, float linear,
                 float quadratic, Model model, Matrix4f modelTransform) {
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
        this.model = model;
        this.modelTransform = modelTransform;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector4fc getDiffuseColor() {
        return diffuseColor;
    }

    public void setDiffuseColor(Vector4fc diffuseColor) {
        this.diffuseColor = diffuseColor;
    }

    public Vector4fc getAmbientColor() {
        return ambientColor;
    }

    public void setAmbientColor(Vector4fc ambientColor) {
        this.ambientColor = ambientColor;
    }

    public Vector4fc getSpecularColor() {
        return specularColor;
    }

    public void setSpecularColor(Vector4fc specularColor) {
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

    public Model getModel() {
        return model;
    }

    public Matrix4f getModelTransform() {
        return modelTransform;
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
