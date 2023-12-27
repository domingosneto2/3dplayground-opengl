package com.codeinstructions.models;

public class Square {
    public static Mesh mesh() {
        float[] vertices = {
                // positions          // colors           // texture coords
                0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,   // top right
                0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,   // bottom right
                -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,   // bottom left
                -0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f    // top left
        };

        int[] indices = {  // note that we start from 0!
                0, 1, 3,   // first triangle
                1, 2, 3

        };
        Mesh mesh = new Mesh(vertices, indices, 3, 0, 3, 2, true);
        return mesh;
    }
}
