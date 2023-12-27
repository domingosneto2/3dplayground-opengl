package com.codeinstructions.models;

import java.util.Objects;

public abstract class BaseModel implements Model {

    private PolygonMesh polygonMesh;

    private Mesh mesh;

    private boolean modified = true;

    @Override
    public boolean isModified() {
        return modified;
    }

    void clearModified() {
        this.modified = false;
    }

    void setModified() {
        modified = true;
    }

    void attributeSet(Object oldValue, Object newValue) {
        this.modified = !Objects.equals(oldValue, newValue) || this.modified;
    }

    @Override
    public void increaseDetail() {

    }

    @Override
    public void decreaseDetail() {

    }

    @Override
    public Mesh mesh() {
        if (modified) {
            polygonMesh = computeMesh();
            float[] vertices = polygonMesh.getVerticesArray();
            mesh = new Mesh(vertices, 3, 3, 0, 0, polygonMesh.concave);
        }

        modified = false;
        return mesh;
    }

    @Override
    public PolygonMesh polygonMesh() {
        mesh();
        return polygonMesh;
    }

    abstract protected PolygonMesh computeMesh();
}
