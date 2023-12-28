package com.codeinstructions.models;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Transform {
    public static Matrix4f rotationX(double radians) {
        return new Matrix4f().identity().rotationX((float)radians);
    }

    public static Matrix4f rotationY(double radians) {
        return new Matrix4f().identity().rotationY((float)radians);
    }

    public static Matrix4f rotationZ(double radians) {
        return new Matrix4f().identity().rotationZ((float)radians);
    }

    public static Matrix4f translate(float x, float y, float z) {
        return new Matrix4f().identity().translate(x, y, z);
    }

    public static Matrix4f translate(Vector4f offset) {
        return new Matrix4f().identity().translate(offset.x, offset.y, offset.z);
    }

    public static Matrix4f scale(float factor) {
        return new Matrix4f().identity().scale(factor);
    }
}
