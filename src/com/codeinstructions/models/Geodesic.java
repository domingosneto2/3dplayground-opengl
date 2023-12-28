package com.codeinstructions.models;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import java.util.Arrays;

public class Geodesic extends BaseModel {

    private int level;

    public static Geodesic model(int level) {
        return new Geodesic(level);
    }

    public Geodesic(int level) {
        this.level = level;
    }

    public void setLevel(int level) {
        attributeSet(this.level, level);
        this.level = level;
    }

    @Override
    public PolygonMesh computeMesh() {
        clearModified();
        PolygonMesh icosahedron = Icosahedron.model().polygonMesh();
        PolygonMesh geodesic = new PolygonMesh(true);

        for (Polygon polygon : icosahedron.getPolygons()) {
            splitGeodesicFace(polygon, geodesic, level);
        }

        return geodesic;

    }

    private static void splitGeodesicFace(Polygon triangle, PolygonMesh mesh, int levels) {
        if (levels < 0) {
            throw new IllegalArgumentException();
        }
        Vector4fc p1 = triangle.getVertex(0);
        Vector4fc p2 = triangle.getVertex(1);
        Vector4fc p3 = triangle.getVertex(2);

        splitGeodesicFace(p1, p2, p3, mesh, levels);
    }

    private static void splitGeodesicFace(Vector4fc p1, Vector4fc p2, Vector4fc p3, PolygonMesh mesh, int levels) {

        if (levels == 0) {
            Vector4f n1 = new Vector4f(p1);
            Vector4f n2 = new Vector4f(p2);
            Vector4f n3 = new Vector4f(p3);

            n1.w = 0;
            n2.w = 0;
            n3.w = 0;

            n1.normalize();
            n2.normalize();
            n3.normalize();

            n1.w = 1;
            n2.w = 1;
            n3.w = 1;

            Polygon polygon = new Polygon(Arrays.asList(p1, p2, p3), Arrays.asList(n1, n2, n3));
            mesh.add(polygon);
        } else {
            Vector3f p13f = new Vector3f(p1.x(), p1.y(), p1.z());
            Vector3f p23f = new Vector3f(p2.x(), p2.y(), p2.z());
            Vector3f p33f = new Vector3f(p3.x(), p3.y(), p3.z());

            float len = p13f.length();

            Vector3f m13f = new Vector3f(p13f).add(p23f).mul(0.5f).normalize(len);
            Vector3f m23f = new Vector3f(p23f).add(p33f).mul(0.5f).normalize(len);;
            Vector3f m33f = new Vector3f(p33f).add(p13f).mul(0.5f).normalize(len);;
            Vector4f m1 = new Vector4f(m13f, 1f);
            Vector4f m2 = new Vector4f(m23f, 1f);
            Vector4f m3 = new Vector4f(m33f, 1f);
            splitGeodesicFace(p1, m1, m3, mesh, levels - 1);
            splitGeodesicFace(m1, p2, m2, mesh, levels - 1);
            splitGeodesicFace(m3, m2, p3, mesh, levels - 1);
            splitGeodesicFace(m3, m1, m2, mesh, levels - 1);
        }
    }

    @Override
    public void decreaseDetail() {
        level--;
        if (level < 0) {
            level = 0;
        }
        attributeSet(level, level - 1);
    }

    @Override
    public void increaseDetail() {
        level++;
        attributeSet(level - 1, level);
    }
}
