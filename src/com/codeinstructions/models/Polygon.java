package com.codeinstructions.models;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Polygon {
    private final List<Vector4f> vertices;

    private final List<Vector4f> vertexNormals;

    private Vector4fc material;

    private Vector4f normal;
    private Vector4f center;
    private float color;
    private List<Float> colors;

    public Polygon(Vector4f... vertices) {
        this.vertices = new ArrayList<>(Arrays.asList(vertices));
        vertexNormals = new ArrayList<>();
        this.material = Color.WHITE;
    }

    public Polygon(List<Vector4f> vertices) {
        this.vertices = new ArrayList<>(vertices);
        vertexNormals = new ArrayList<>();
        this.material = Color.WHITE;
    }

    public Polygon(ArrayList<Vector4f> vertices) {
        this.vertices = vertices;
        vertexNormals = new ArrayList<>();
        this.material = Color.WHITE;
    }

    public Polygon(List<Vector4f> vertices, List<Vector4f> normals) {
        this.vertices = new ArrayList<>(vertices);
        this.vertexNormals = new ArrayList<>(normals);
        this.material = Color.WHITE;
    }

    public Polygon(ArrayList<Vector4f> vertices, ArrayList<Vector4f> normals) {
        this.vertices = vertices;
        this.vertexNormals = normals;
        this.material = Color.WHITE;
    }

    public Polygon(Mesh mesh, int[] vertexIds, int[] normalIds) {
        this.vertices = null;
        this.vertexNormals = null;
        this.material = Color.WHITE;
    }

    public List<Vector4f> getVertices() {
        return vertices;
    }

    public List<Vector4f> getVertexNormals() {
        return vertexNormals;
    }

    public final Polygon transform(Matrix4f transformation, Matrix4f normalTransform) {
        ArrayList<Vector4f> newVertices = new ArrayList<>(vertices.size());
        for (Vector4f vertex : vertices) {
            Vector4f transformed = new Vector4f(vertex);
            transformed.mul(transformation);
            newVertices.add(transformed);
        }
        if (vertexNormals.isEmpty()) {
            Polygon polygon = new Polygon(newVertices);
            polygon.setColor(color);
            if (colors != null) {
                polygon.setColors(colors);
            }
            return polygon;
        } else {
            ArrayList<Vector4f> newNormals = new ArrayList<>(vertexNormals.size());
            for (Vector4f normal : vertexNormals) {
                Vector4f transformed = new Vector4f(normal);
                transformed.mul(normalTransform);
                newNormals.add(transformed);
            }
            Polygon polygon =  new Polygon(newVertices, newNormals);
            polygon.setColor(color);
            if (colors != null) {
                polygon.setColors(colors);
            }
            return polygon;
        }
    }


    public final Polygon copy() {
        Polygon polygon = new Polygon();
        for (Vector4f vertex : vertices) {
            polygon.vertices.add(new Vector4f(vertex));
        }
        polygon.setColor(color);
        if (colors != null) {
            polygon.setColors(colors);
        }
        return polygon;
    }

    private Polygon copyWithVertices(ArrayList<Vector4f> newVertices) {
        Polygon polygon;
        if (vertexNormals == null) {
            polygon = new Polygon(newVertices);
        } else {
            ArrayList<Vector4f> normalsList = new ArrayList<>(vertexNormals.size());
            normalsList.addAll(vertexNormals);
            polygon = new Polygon(newVertices, normalsList);
        }
        polygon.setColor(color);
        if (colors != null) {
            polygon.setColors(colors);
        }
        return polygon;
    }

    public final Polygon translate(float x, float y, float z) {
        ArrayList<Vector4f> newVertices = new ArrayList<>(vertices.size());
        for (Vector4f vertex : vertices) {
            newVertices.add(new Vector4f(vertex).add(x, y, z, 0));
        }
        return copyWithVertices(newVertices);
    }

    public final Vector4f normal() {
        if (normal == null) {
            Vector4f v0 = vertices.get(0);
            Vector4f v1 = vertices.get(1);
            Vector4f v2 = vertices.get(2);
            Vector4f u = new Vector4f(v1).sub(v0);
            Vector4f v = new Vector4f(v2).sub(v0);
            normal = new Vector4f(u.y * v.z - u.z * v.y, u.z * v.x - u.x * v.z, u.x * v.y - u.y * v.x, 0f);
        }
        return normal;
    }

    public final Vector4f center() {
        if (center == null) {
            float x = 0, y = 0, z = 0;
            int numVertices = getNumVertices();
            for (int i = 0; i < numVertices; i++) {
                Vector4f v = vertices.get(i);
                x += v.x;
                y += v.y;
                z += v.z;
            }
            center = new Vector4f(x/numVertices, y/numVertices, z/numVertices, 1f);
        }
        return center;
    }

    public final void setColor(float color) {
        this.color = color;
    }

    public final void setColors(List<Float> colors) {
        this.colors = colors;
    }


    public final void setVertex(int i, Vector4f newVertex) {
        vertices.set(i, newVertex);
    }

    public final void setVertexNormal(int i, Vector4f newVertex) {
        vertexNormals.set(i, newVertex);
    }

    public int getNumVertices() {
        return vertices.size();
    }

    public int getNumNormals() {
        return vertexNormals.size();
    }

    public Vector4fc getMaterial() {
        return material;
    }

    public void setMaterial(Vector4fc material) {
        this.material = material;
    }

    public Polygon[] split() {
        if (getNumVertices() == 3) {
            return new Polygon[] {this};
        } else {
            Vector4f center = center();
            Vector4f normal = normal();
            Polygon[] split = new Polygon[getNumVertices()];
            for (int i = 0; i < getNumVertices(); i++) {
                Vector4f v1 = vertices.get(i);
                Vector4f v2;
                if (i == getNumVertices() - 1) {
                    v2 = vertices.get(0);
                } else {
                    v2 = vertices.get(i + 1);
                }

                if (getNumNormals() > 0) {
                    Vector4f n1 = vertexNormals.get(i);
                    Vector4f n2;
                    if (i == getNumVertices() - 1) {
                        n2 = vertexNormals.get(0);
                    } else {
                        n2 = vertexNormals.get(i + 1);
                    }

                    Polygon child = new Polygon(Arrays.asList(center, v1, v2), Arrays.asList(normal, n1, n2));
                    child.setMaterial(material);
                    split[i] = child;
                } else {
                    Polygon child = new Polygon(center, v1, v2);
                    child.setMaterial(material);
                    split[i] = child;
                }
            }
            return split;
        }
    }

    public Vector4f getVertex(int i) {
        return vertices.get(i);
    }
}
