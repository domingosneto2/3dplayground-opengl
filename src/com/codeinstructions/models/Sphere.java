package com.codeinstructions.models;

import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.PI;

public class Sphere extends BaseModel {

    private int latSteps;
    private int lonSteps;

    public static Sphere model(int latSteps, int lonSteps) {
        return new Sphere(latSteps, lonSteps);
    }

    public Sphere(int latSteps, int lonSteps) {
        this.latSteps = latSteps;
        this.lonSteps = lonSteps;
    }

    @Override
    protected PolygonMesh computeMesh() {
        boolean concave = true;
        PolygonMesh mesh = new PolygonMesh(concave);

        Vector4f north = new Vector4f(0, 0, 1, 1);

        Vector4f lon0 = new Vector4f(north).rotateX((float)PI / latSteps);
        Vector4f lonp = lon0;

        List<Vector4f> previousVertices = new ArrayList<>();
        previousVertices.add(lon0);

        // Build the first layer
        for (int j = 0; j < lonSteps; j++) {
            Vector4f lon1 = j == lonSteps - 1 ? lon0 : new Vector4f(lon0).rotateZ((j + 1) * 2 * (float)PI / lonSteps);
            spherePolygon(mesh, north, lonp, lon1);
            previousVertices.add(lon1);
            lonp = lon1;
        }

        // Build all intermediate layers
        for (int i = 0; i < latSteps - 2; i++) {

            List<Vector4f> currentVertices = new ArrayList<>();
            lon0 = new Vector4f(north).rotateX((i + 2) * (float)PI / latSteps);
            currentVertices.add(lon0);
            lonp = lon0;

            for (int j = 0; j < lonSteps; j++) {
                Vector4f lon1 = j == lonSteps - 1 ? lon0 : new Vector4f(lonp).rotateZ(2 * (float)PI / lonSteps);
                Vector4f north1 = previousVertices.get(j);
                Vector4f north2 = previousVertices.get(j + 1);


                spherePolygon(mesh, north1, lonp, lon1, north2);

                currentVertices.add(lon1);
                lonp = lon1;
            }

            previousVertices = currentVertices;
        }

        Vector4f south = new Vector4f(0, 0, -1, 1);


        // Build the final layer
        for (int i = 0; i < previousVertices.size() - 1; i++) {
            spherePolygon(mesh, previousVertices.get(i + 1), previousVertices.get(i), south);
        }

        Geodesic.computeTextureCoords(mesh);

        return mesh;
    }

    private static void spherePolygon(PolygonMesh mesh, Vector4f ... vertices) {
        List<Vector4f> vertexList = Arrays.asList(vertices);
        mesh.add(new Polygon(vertexList, vertexList));
    }

    @Override
    public void decreaseDetail() {
        int prevLatSteps = latSteps;
        int prevLonSteps = lonSteps;
        latSteps--;
        lonSteps--;

        if (latSteps < 3) {
            latSteps = 3;
        }

        if (lonSteps < 3) {
            lonSteps = 3;
        }

        attributeSet(prevLonSteps, lonSteps);
        attributeSet(prevLatSteps, latSteps);
    }

    @Override
    public void increaseDetail() {
        latSteps++;
        lonSteps++;
        attributeSet(latSteps - 1, latSteps);
    }
}
