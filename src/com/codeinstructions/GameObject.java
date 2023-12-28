package com.codeinstructions;

import com.codeinstructions.models.Mesh;
import com.codeinstructions.models.Model;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.joml.Vector4fc;

public class GameObject {
    private Matrix4f transform;

    private Material material;

    private Model model;

    private Mesh mesh;

    public GameObject(Vector3f pos, Material material, Model model) {
        this.transform = new Matrix4f().identity().translate(pos);
        this.material = material;
        this.model = model;
    }

    public GameObject(Matrix4f transform, Material material, Model model) {
        this.transform = transform;
        this.material = material;
        this.model = model;
    }

    public Matrix4f getTransform() {
        return transform;
    }

    public Material getMaterial() {
        return material;
    }

    public Model getModel() {
        return model;
    }

    public Mesh mesh() {
        Mesh newMesh = model.mesh();
        if (newMesh != mesh) {
            if (mesh != null) {
                mesh.releaseBuffers();
            }
            mesh = newMesh;
            mesh.initializeBuffers();
        }
        return mesh;
    }
}
