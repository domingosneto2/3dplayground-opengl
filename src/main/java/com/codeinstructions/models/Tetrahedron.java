package com.codeinstructions.models;

import org.joml.Vector4f;

public class Tetrahedron extends BaseModel {

    public static Tetrahedron model() {
        return new Tetrahedron();
    }

    @Override
    protected PolygonMesh computeMesh() {
        PolygonMesh mesh = new PolygonMesh(true);

        Vector4f v0 = new Vector4f(0, 0, 0, 1);
        Vector4f v1 = new Vector4f(1, 0, 0, 1);
        Vector4f v2 = new Vector4f(v1).rotateY(Angle.toRadians(-60));
        Vector4f v3 = new Vector4f(v2).rotateX((float)-Math.atan(2 * Math.sqrt(2)));

        mesh.polygon( v0, v3, v1);
        mesh.polygon(v1, v3, v2);
        mesh.polygon(v2, v3, v0);
        mesh.polygon(v0, v1, v2);

        return mesh;
    }

    public static Vector4f centroid() {
        Vector4f v0 = new Vector4f(0, 0, 0, 1);
        Vector4f v1 = new Vector4f(1, 0, 0, 1);
        Vector4f v2 = new Vector4f(v1).rotateY(Angle.toRadians(-60));
        Vector4f v3 = new Vector4f(v2).rotateX((float)-Math.atan(2 * Math.sqrt(2)));

        return Vertex.sum(v0, v1, v2, v3).mul(1.0f / 4);
    }
}
