package com.codeinstructions;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    Vector3f pos = new Vector3f(0f, 0f, 3f);
    Vector3f front = new Vector3f(0f, 0f, -1);
    Vector3f up = new Vector3f(0f, 1f, 0f);

    private float fov = (float) Math.toRadians(45);

    void forward(float delta) {
        pos.add(new Vector3f(front).mul(delta));
    }

    Matrix4f view() {
        return new Matrix4f().lookAt(pos, new Vector3f(pos).add(front), up);
    }

    public void back(float delta) {
        forward(-delta);
    }

    public void moveLeft(float delta) {
        Vector3f direction = new Vector3f(up).cross(front).mul(delta);
        pos.add(direction);
    }

    public void moveRight(float delta) {
        Vector3f direction = new Vector3f(front).cross(up).mul(delta);
        pos.add(direction);
    }

    public void turnLeft(float radians) {
        front.rotateAxis(radians, up.x, up.y, up.z).normalize();
    }

    public void turnRight(float radians) {
        front.rotateAxis(-radians, up.x, up.y, up.z).normalize();
    }

    public void moveUp(float delta) {
        Vector3f direction = new Vector3f(up).mul(delta);
        pos.add(direction);
    }

    public void moveDown(float delta) {
        Vector3f direction = new Vector3f(up).mul(-delta);
        pos.add(direction);
    }

    public void lookDown(float radians) {
        Vector3f axis = new Vector3f(up).cross(front);
        front.rotateAxis(radians, axis.x, axis.y, axis.z).normalize();
        up = new Vector3f(front).cross(axis).normalize();
    }

    public void lookUp(float radians) {
        Vector3f axis = new Vector3f(up).cross(front);
        front.rotateAxis(-radians, axis.x, axis.y, axis.z).normalize();
        up = new Vector3f(front).cross(axis).normalize();
    }

    public void increaseFov(float radians) {
        fov += radians;
    }

    public void decreaseFov(float radians) {
        fov -= radians;
    }

    public float getFov() {
        return fov;
    }

    public void rotateLeft(float radians) {
        up.rotateAxis(-radians, front.x, front.y, front.z).normalize();
    }

    public void rotateRight(float radians) {
        up.rotateAxis(radians, front.x, front.y, front.z).normalize();
    }
}
