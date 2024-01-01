package com.codeinstructions.models;

import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.Arrays;

public class Rectangle extends BaseModel {

    private final int xRepeats;
    private final int yRepeats;

    public Rectangle(int xRepeats, int yRepeats) {
        this.xRepeats = xRepeats;
        this.yRepeats = yRepeats;
    }

    @Override
    protected PolygonMesh computeMesh() {
        PolygonMesh mesh = new PolygonMesh(true);

        Vector4f v000 = new Vector4f(0, 0, 0, 1);
        Vector4f v001 = new Vector4f(0, 1, 0, 1);
        Vector4f v010 = new Vector4f(1, 0, 0, 1);
        Vector4f v011 = new Vector4f(1, 1, 0, 1);

        Vector2f t00 = new Vector2f(0, 0);
        Vector2f t01 = new Vector2f(0, yRepeats);
        Vector2f t10 = new Vector2f(xRepeats, 0);
        Vector2f t11 = new Vector2f(xRepeats, yRepeats);


        Polygon polygon = new Polygon(v000, v010, v011, v001);
        polygon.setTexCoords(Arrays.asList(t00, t10, t11, t01));
        mesh.add(polygon);

        mesh = mesh.translate(-0.5f, -0.5f, 0);

        return mesh;
    }
}
