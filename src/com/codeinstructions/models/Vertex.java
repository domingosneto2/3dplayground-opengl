package com.codeinstructions.models;

import org.joml.Matrix4d;
import org.joml.Vector4f;
import org.joml.Vector4fc;

public class Vertex {

    public static final Vector4fc I = new Vector4f(1f, 0f, 0f, 1f);
    public static final Vector4fc J = new Vector4f(0f, 1f, 0f, 1f);
    public static final Vector4fc K = new Vector4f(0f, 0f, 1f, 1f);

    public static final Vector4fc IN = new Vector4f(-1f, 0f, 0f, 1f);
    public static final Vector4fc JN = new Vector4f(0f, -1f, 0f, 1f);
    public static final Vector4fc KN = new Vector4f(0f, 0f, -1f, 1f);

    public static Vector4f sum(Vector4f ... vectors) {
        Vector4f result = new Vector4f();

        for (Vector4f vector : vectors) {
            result.add(vector);
        }
        result.w = 1;
        return result;
    }
}
