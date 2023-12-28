package com.codeinstructions.models;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Polygon {
    private final List<Vector4fc> vertices;

    private final List<Vector4fc> vertexNormals;

    private Vector4fc normal;
    private Vector4fc center;
    private float color;
    private List<Float> colors;

    public Polygon(Vector4fc... vertices) {
        this.vertices = new ArrayList<>(Arrays.asList(vertices));
        vertexNormals = new ArrayList<>();
    }

    public Polygon(List<? extends Vector4fc> vertices) {
        this.vertices = new ArrayList<>(vertices);
        vertexNormals = new ArrayList<>();
    }

    public Polygon(List<? extends Vector4fc> vertices, List<? extends Vector4fc> normals) {
        this.vertices = new ArrayList<>(vertices);
        this.vertexNormals = new ArrayList<>(normals);
    }

    public Polygon(ArrayList<? extends Vector4fc> vertices, ArrayList<? extends Vector4fc> normals) {
        this.vertices = new ArrayList<>(vertices);
        this.vertexNormals = new ArrayList<>(normals);
    }

    public Polygon(Mesh mesh, int[] vertexIds, int[] normalIds) {
        this.vertices = null;
        this.vertexNormals = null;
    }

    public List<? extends Vector4fc> getVertices() {
        return vertices;
    }

    public List<? extends Vector4fc> getVertexNormals() {
        return vertexNormals;
    }

    public final Polygon transform(Matrix4f transformation, Matrix4f normalTransform) {
        ArrayList<Vector4f> newVertices = new ArrayList<>(vertices.size());
        for (Vector4fc vertex : vertices) {
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
            for (Vector4fc normal : vertexNormals) {
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
        for (Vector4fc vertex : vertices) {
            polygon.vertices.add(new Vector4f(vertex));
        }
        polygon.setColor(color);
        if (colors != null) {
            polygon.setColors(colors);
        }
        return polygon;
    }

    private Polygon copyWithVertices(ArrayList<Vector4fc> newVertices) {
        Polygon polygon;
        if (vertexNormals == null) {
            polygon = new Polygon(newVertices);
        } else {
            ArrayList<Vector4fc> normalsList = new ArrayList<>(vertexNormals.size());
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
        ArrayList<Vector4fc> newVertices = new ArrayList<>(vertices.size());
        for (Vector4fc vertex : vertices) {
            newVertices.add(new Vector4f(vertex).add(x, y, z, 0));
        }
        return copyWithVertices(newVertices);
    }

    public final Vector4fc normal() {
        if (normal == null) {
            Vector4fc v0 = vertices.get(0);
            Vector4fc v1 = vertices.get(1);
            Vector4fc v2 = vertices.get(2);
            Vector4fc u = new Vector4f(v1).sub(v0);
            Vector4fc v = new Vector4f(v2).sub(v0);
            normal = new Vector4f(u.y() * v.z() - u.z() * v.y(), u.z() * v.x() - u.x() * v.z(), u.x() * v.y() - u.y() * v.x(), 0f);
        }
        return normal;
    }

    public final Vector4fc center() {
        if (center == null) {
            float x = 0, y = 0, z = 0;
            int numVertices = getNumVertices();
            for (int i = 0; i < numVertices; i++) {
                Vector4fc v = vertices.get(i);
                x += v.x();
                y += v.y();
                z += v.z();
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


    public Polygon[] split() {
        if (getNumVertices() == 3) {
            return new Polygon[] {this};
        } else {
            Vector4fc center = center();
            Vector4fc normal = normal();
            Polygon[] split = new Polygon[getNumVertices()];
            for (int i = 0; i < getNumVertices(); i++) {
                Vector4fc v1 = vertices.get(i);
                Vector4fc v2;
                if (i == getNumVertices() - 1) {
                    v2 = vertices.get(0);
                } else {
                    v2 = vertices.get(i + 1);
                }

                if (getNumNormals() > 0) {
                    Vector4fc n1 = vertexNormals.get(i);
                    Vector4fc n2;
                    if (i == getNumVertices() - 1) {
                        n2 = vertexNormals.get(0);
                    } else {
                        n2 = vertexNormals.get(i + 1);
                    }

                    Polygon child = new Polygon(Arrays.asList(center, v1, v2), Arrays.asList(normal, n1, n2));
                    split[i] = child;
                } else {
                    Polygon child = new Polygon(center, v1, v2);
                    split[i] = child;
                }
            }
            return split;
        }
    }

    public Vector4fc getVertex(int i) {
        return vertices.get(i);
    }

    public void tesselate(List<Polygon> dest) {
        if (vertices.size() == 3) {
            dest.add(this);
            return;
        }
        if (vertices.size() > 4) {
            throw new UnsupportedOperationException();
        }

        Vector4fc v1 = vertices.get(0);
        Vector4fc v2 = vertices.get(1);
        Vector4fc v3 = vertices.get(2);

        if (hasVertexNormals()) {
            Vector4fc n1 = vertexNormals.get(0);
            Vector4fc n2 = vertexNormals.get(1);
            Vector4fc n3 = vertexNormals.get(2);

            dest.add(new Polygon(Arrays.asList(v1, v2, v3), Arrays.asList(n1, n2, n3)));
        } else {
            dest.add(new Polygon(Arrays.asList(v1, v2, v3)));
        }

        v1 = vertices.get(2);
        v2 = vertices.get(3);
        v3 = vertices.get(0);

        if (hasVertexNormals()) {
            Vector4fc n1 = vertexNormals.get(2);
            Vector4fc n2 = vertexNormals.get(3);
            Vector4fc n3 = vertexNormals.get(0);
            dest.add(new Polygon(Arrays.asList(v1, v2, v3), Arrays.asList(n1, n2, n3)));
        } else {
            dest.add(new Polygon(Arrays.asList(v1, v2, v3)));
        }
    }

    private boolean hasVertexNormals() {
        return vertexNormals != null && !vertexNormals.isEmpty();
    }
}
