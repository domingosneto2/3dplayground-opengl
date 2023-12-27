package com.codeinstructions;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    Vector3f pos = new Vector3f(0f, 0f, 3f);
    Vector3f front = new Vector3f(0f, 0f, -1);
    Vector3f up = new Vector3f(0f, 1f, 0f);

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
}
