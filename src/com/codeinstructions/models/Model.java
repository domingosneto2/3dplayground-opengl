package com.codeinstructions.models;

public interface Model {

    boolean isModified();

    PolygonMesh polygonMesh();

    public Mesh mesh();

    public void increaseDetail();

    public void decreaseDetail();
}
