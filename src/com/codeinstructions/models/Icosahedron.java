package com.codeinstructions.models;

import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.PI;

public class Icosahedron extends BaseModel {

    public static Icosahedron model() {
        return new Icosahedron();
    }


    @Override
    protected PolygonMesh computeMesh() {
        PolygonMesh mesh = new PolygonMesh(true);

        Vector4f north = new Vector4f(Vertex.K).mul(0.5f);
        Vector4f south = new Vector4f(north).mul(-1);

        List<Vector4f> layer1 = new ArrayList<>();

        Vector4f layer1Head = new Vector4f(north).rotateX((float)PI / 2 - (float)Math.atan(1/2d));
        layer1.add(new Vector4f(layer1Head));

        for (int i = 1; i < 5; i++) {
            Vector4f next = new Vector4f(layer1Head).rotateZ((float)Math.toRadians(i * 72));
            layer1.add(next);
        }
        layer1.add(new Vector4f(layer1Head));

        List<Vector4f> layer2 = new ArrayList<>();
        Vector4f layer2Head = new Vector4f(south).rotateX((float)(Math.atan(1/2d) - PI/2)).rotateZ((float)Math.toRadians(36));
        layer2.add(new Vector4f(layer2Head));
        for (int i = 1; i < 5; i++) {
            Vector4f next = new Vector4f(layer2Head).rotateZ((float)Math.toRadians(i * 72));
            layer2.add(next);
        }
        layer2.add(new Vector4f(layer2Head));

        for (int i = 0; i < layer1.size() - 1; i++) {
            mesh.add(new Polygon(new Vector4f(north), layer1.get(i), layer1.get(i + 1)));
        }

        for (int i = 0; i < layer1.size() - 1; i++) {
            mesh.add(new Polygon(layer1.get(i +1), layer1.get(i), layer2.get(i)));
        }

        for (int i = 0; i < layer2.size() - 1; i++) {
            mesh.add(new Polygon(layer2.get(i), layer2.get(i + 1), layer1.get(i + 1)));
        }

        for (int i = 0; i < layer2.size() - 1; i++) {
            mesh.add(new Polygon(south, layer2.get(i + 1), layer2.get(i)));
        }


        return mesh;

    }
}
