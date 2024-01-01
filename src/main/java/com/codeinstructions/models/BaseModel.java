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
            boolean hasTexCoord = polygonMesh.hasTexCoords();
            int texCoordSize = 0;
            int tangentSize = 0;
            int bitangentSize = 0;

            if (hasTexCoord) {
                texCoordSize = 2;
                tangentSize = 3;
                bitangentSize = 3;
            }
            mesh = new Mesh(vertices, 3, 3, tangentSize, bitangentSize, 0, texCoordSize, polygonMesh.concave);
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
