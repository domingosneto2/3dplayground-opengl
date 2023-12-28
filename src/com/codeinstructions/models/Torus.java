package com.codeinstructions.models;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.PI;

public class Torus extends BaseModel{
    private int outerSteps;
    private int innerSteps;
    private float innerRadius;

    public static Torus model(int outerSteps, int innerSteps, float innerRadius) {
        return new Torus(outerSteps, innerSteps, innerRadius);
    }

    public Torus(int outerSteps, int innerSteps, float innerRadius) {
        this.outerSteps = outerSteps;
        this.innerSteps = innerSteps;
        this.innerRadius = innerRadius;
    }

    public void setOuterSteps(int outerSteps) {
        attributeSet(this.outerSteps, outerSteps);
        this.outerSteps = outerSteps;
    }

    public void setInnerSteps(int innerSteps) {
        attributeSet(this.innerSteps, innerSteps);
        this.innerSteps = innerSteps;
    }

    public void setInnerRadius(float innerRadius) {
        attributeSet(this.innerRadius, innerRadius);
        this.innerRadius = innerRadius;
    }

    @Override
    protected PolygonMesh computeMesh() {
        float outerRadius = 1 - innerRadius;

        Vector4f outerStart = new Vector4f(0, outerRadius, 0, 1);
        List<Vector4f> initialRing = new ArrayList<>();
        Vector4f ringBuilder = new Vector4f(0, 0, innerRadius, 1);
        initialRing.add(new Vector4f(ringBuilder).add(outerStart));
        for (int j = 1; j < innerSteps; j++) {
            Vector4f ringNext = new Vector4f(ringBuilder).rotateX(j * 2 * (float)PI / innerSteps);
            initialRing.add(ringNext.add(outerStart));
        }
        initialRing.add(new Vector4f(ringBuilder).add(outerStart));
        List<Vector4f> previousRing = initialRing;

        PolygonMesh mesh = new PolygonMesh(false);
        Vector4f prevCenter = outerStart;

        for (int i = 0; i < outerSteps; i++) {
            List<Vector4f> currentRing;
            Vector4f currentCenter;
            if (i == outerSteps - 1) {
                currentRing = initialRing;
                currentCenter = outerStart;
            } else {
                Matrix4f rotate = Transform.rotationZ(- (i + 1) * 2 * PI / outerSteps);
                currentRing = initialRing.stream().map(v -> new Vector4f(v).mul(rotate)).collect(Collectors.toList());
                currentCenter = new Vector4f(outerStart).mul(rotate);
            }
            for (int j = 0; j < currentRing.size() - 1; j++) {
                Vector4f p1 = previousRing.get(j);
                Vector4f p2 = previousRing.get(j + 1);
                Vector4f c1 = currentRing.get(j);
                Vector4f c2 = currentRing.get(j + 1);


                Vector4f np1 = new Vector4f(p1).sub(prevCenter).normalize();
                Vector4f np2 = new Vector4f(p2).sub(prevCenter).normalize();
                Vector4f nc2 = new Vector4f(c2).sub(currentCenter).normalize();
                Vector4f nc1 = new Vector4f(c1).sub(currentCenter).normalize();

                //polygon(mesh, p1, p2, c2, c1);
                List<Vector4f> vertices = Arrays.asList(p1, p2, c2, c1);
                List<Vector4f> normals = Arrays.asList(np1, np2, nc2, nc1);

//                mesh.add(new Polygon(Arrays.asList(p1, p2, c2), Arrays.asList(np1, np2, nc2)));
//                mesh.add(new Polygon(Arrays.asList(c2, c1, p1), Arrays.asList(nc2, nc1, np1)));
                mesh.add(new Polygon(vertices, normals));

            }
            previousRing = currentRing;
            prevCenter = currentCenter;
        }

        return mesh;
    }

    @Override
    public void decreaseDetail() {
        innerSteps--;
        outerSteps--;
        if (innerSteps < 4) {
            innerSteps = 4;
        }
        if (outerSteps < 4) {
            outerSteps = 4;
        }
        attributeSet(innerSteps, innerSteps - 1);
    }

    @Override
    public void increaseDetail() {
        innerSteps++;
        outerSteps++;
        attributeSet(innerSteps - 1, innerSteps);
    }
}
