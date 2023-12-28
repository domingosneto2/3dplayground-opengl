package com.codeinstructions;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.joml.Vector4fc;

public class GameObject {
    private Vector3f pos;

    private Material material;

    public GameObject(Vector3f pos, Material material) {
        this.pos = pos;
        this.material = material;
    }

    public Vector3f getPos() {
        return pos;
    }

    public Material getMaterial() {
        return material;
    }
}
