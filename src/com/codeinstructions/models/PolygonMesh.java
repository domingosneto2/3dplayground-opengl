package com.codeinstructions.models;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import java.util.ArrayList;
import java.util.List;

public class PolygonMesh {
    private List<Polygon> polygons = new ArrayList<>();

    boolean concave;

    public PolygonMesh(boolean concave) {
        this.concave = concave;
    }


    public PolygonMesh transform(Matrix4f transformation) {
        Matrix4f normalTransform = new Matrix4f(transformation).normal();
        PolygonMesh newMesh = new PolygonMesh(concave);
        for (Polygon polygon : polygons) {
            newMesh.polygons.add(polygon.transform(transformation, normalTransform));
        }
        return newMesh;
    }

    public void remove(int i) {
        polygons.remove(i);
    }

    public PolygonMesh translate(Vector3f v) {
        PolygonMesh newMesh = new PolygonMesh(concave);
        for (Polygon polygon : polygons) {
            newMesh.polygons.add(polygon.translate(v.x, v.y, v.z));
        }
        return newMesh;
    }

    public PolygonMesh translate(Vector4f v) {
        PolygonMesh newMesh = new PolygonMesh(concave);
        for (Polygon polygon : polygons) {
            newMesh.polygons.add(polygon.translate(v.x, v.y, v.z));
        }
        return newMesh;
    }

    public PolygonMesh copy() {
        PolygonMesh newMesh = new PolygonMesh(concave);
        for (Polygon polygon : polygons) {
            newMesh.polygons.add(polygon.copy());
        }
        return newMesh;
    }

    public void transformInPlace(Matrix4f transform) {
        Matrix4f normalTransform = new Matrix4f(transform).normal();
        for (int i = 0; i < polygons.size(); i++) {
            Polygon polygon = polygons.get(i);
            polygon = polygon.transform(transform, normalTransform);
            polygons.set(i, polygon);
        }
    }

    public void add(Polygon polygon) {
        polygons.add(polygon);
    }

    public List<Polygon> getPolygons() {
        return polygons;
    }

    public PolygonMesh translate(float x, float y, float z) {
        PolygonMesh newMesh = new PolygonMesh(concave);
        for (Polygon polygon : polygons) {
            newMesh.polygons.add(polygon.translate(x, y, z));
        }
        return newMesh;
    }

    public void add(PolygonMesh mesh) {
        for (Polygon polygon : mesh.getPolygons()) {
            add(polygon);
        }
    }

    public void polygon(Vector4fc ... vertices) {
        Polygon polygon = new Polygon(vertices);
        add(polygon);
    }

    public float[] getVerticesArray() {
        List<Polygon> triangles = tesselate();
        int numVertices = triangles.stream().mapToInt(Polygon::getNumVertices).sum();

        float[] vertices = new float[numVertices * 6];
        int count = 0;
        for (Polygon polygon : triangles) {
            List<? extends Vector4fc> polygonVertices = polygon.getVertices();
            List<? extends Vector4fc> vertexNormals = polygon.getVertexNormals();
            Vector4fc normal = null;
            if (vertexNormals == null || vertexNormals.isEmpty()) {
                normal = polygon.normal();
            }

            for (int i = 0; i < polygonVertices.size(); i++) {
                Vector4fc vertex = polygonVertices.get(i);
                vertices[count] = vertex.x();
                vertices[count + 1] = vertex.y();
                vertices[count + 2] = vertex.z();

                Vector4fc vertexNormal = normal != null ? normal : vertexNormals.get(i);

                vertices[count + 3] = vertexNormal.x();
                vertices[count + 4] = vertexNormal.y();
                vertices[count + 5] = vertexNormal.z();
                count += 6;
            }

        }
        return vertices;
    }

    private List<Polygon> tesselate() {
        List<Polygon> result = new ArrayList<>();

        for (Polygon polygon : polygons) {
            if (polygon.getNumVertices() == 3) {
                result.add(polygon);
            } else {
                polygon.tesselate(result);
            }
        }
        return result;
    }
}
