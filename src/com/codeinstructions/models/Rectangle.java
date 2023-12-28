package com.codeinstructions.models;

import org.joml.Vector4f;

public class Rectangle extends BaseModel {
    @Override
    protected PolygonMesh computeMesh() {
        PolygonMesh mesh = new PolygonMesh(true);

        Vector4f v000 = new Vector4f(0, 0, 0, 1);
        Vector4f v001 = new Vector4f(0, 1, 0, 1);
        Vector4f v010 = new Vector4f(1, 1, 0, 1);
        Vector4f v011 = new Vector4f(1, 0, 0, 1);

        //mesh.add(new Polygon(v000, v001, v010, v011));
        mesh.add(new Polygon(v011, v010, v001, v000));

        mesh = mesh.translate(-0.5f, -0.5f, 0);

        return mesh;
    }
}
