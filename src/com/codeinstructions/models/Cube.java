package com.codeinstructions.models;

import org.joml.Vector4f;

public class Cube extends BaseModel {

    public static Cube model() {
        return new Cube();
    }

    @Override
    protected PolygonMesh computeMesh() {

        PolygonMesh mesh = new PolygonMesh(true);

        Vector4f v000 = new Vector4f(0, 0, 0, 1);
        Vector4f v001 = new Vector4f(0, 0, 1, 1);
        Vector4f v010 = new Vector4f(0, 1, 0, 1);
        Vector4f v011 = new Vector4f(0, 1, 1, 1);
        Vector4f v100 = new Vector4f(1, 0, 0, 1);
        Vector4f v101 = new Vector4f(1, 0, 1, 1);
        Vector4f v110 = new Vector4f(1, 1, 0, 1);
        Vector4f v111 = new Vector4f(1, 1, 1, 1);



        mesh.polygon(v000, v010, v110, v100); // south
        mesh.polygon(v100, v110, v111, v101); // east
        mesh.polygon(v001, v011, v010, v000); // west
        mesh.polygon(v101, v111, v011, v001); // north
        mesh.polygon(v001, v000, v100, v101); // bottom
        mesh.polygon(v010, v011, v111, v110); // top


        mesh = mesh.translate(-0.5f, -0.5f, -0.5f);

        return mesh;
    }
}
