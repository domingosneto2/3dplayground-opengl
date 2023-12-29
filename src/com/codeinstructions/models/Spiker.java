package com.codeinstructions.models;


import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import java.util.Arrays;

public class Spiker<T extends Model> extends BaseModel {

    private T delegate;
    private PolygonMesh myMesh;

    private float spikeLength;

    public static <T extends Model> Spiker<T> model(T delegate, float spikeLength) {
        return new Spiker<>(delegate, spikeLength);
    }

    public Spiker(T delegate, float spikeLength) {
        this.delegate = delegate;
        this.spikeLength = spikeLength;
    }


    public T getDelegate() {
        return delegate;
    }

    @Override
    protected PolygonMesh computeMesh() {

        System.out.println("Spiker computing....");
        if (myMesh == null || isModified() || delegate.isModified()) {
            clearModified();
            PolygonMesh sourceMesh = delegate.polygonMesh();
            myMesh = applySpikes(sourceMesh);
            System.out.println("Spiker computed.");
        }
        return myMesh;
    }

    private PolygonMesh applySpikes(PolygonMesh sourceMesh) {
        if (spikeLength == 0) {
            return sourceMesh;
        }
        PolygonMesh mesh = new PolygonMesh(false);

        for (Polygon polygon : sourceMesh.getPolygons()) {
            Vector4fc center = polygon.center();
            Vector4fc normal = polygon.normal();

            Vector4fc v0 = polygon.getVertex(0);
            Vector4fc v1 = polygon.getVertex(1);
            Vector4fc v2 = polygon.getVertex(2);

            Vector4f spike = new Vector4f(center).add(normal.normalize(new Vector4f()).mul(spikeLength));

            Polygon p1 = new Polygon(v0, v1, spike);
            Polygon p2 = new Polygon(v1, v2, spike);
            Polygon p3;
            Polygon p4 = null;

            mesh.add(p1);
            mesh.add(p2);

            if (polygon.getNumVertices() == 3) {
                p3 = new Polygon(v2, v0, spike);
                mesh.add(p3);
            } else {
                Vector4fc v3 = polygon.getVertex(3);
                p3 = new Polygon(v2, v3, spike);
                p4 = new Polygon(v3, v0, spike);
                mesh.add(p3);
                mesh.add(p4);
            }

            if (!polygon.getTexCoords().isEmpty()) {
                Vector2fc t0 = polygon.getTexCoords().get(0);
                Vector2fc t1 = polygon.getTexCoords().get(1);
                Vector2fc t2 = polygon.getTexCoords().get(2);

                Vector2f centerTexCoord = polygon.centerTexCoord();

                p1.setTexCoords(Arrays.asList(t0, t1, centerTexCoord));
                p2.setTexCoords(Arrays.asList(t1, t2, centerTexCoord));

                if (polygon.getNumVertices() == 3) {
                    p3.setTexCoords(Arrays.asList(t2, t0, centerTexCoord));
                } else {
                    Vector2fc t3 = polygon.getTexCoords().get(3);
                    p3.setTexCoords(Arrays.asList(t2, t3, centerTexCoord));
                    p4.setTexCoords(Arrays.asList(t3, t0, centerTexCoord));
                }
            }
        }

        return mesh;
    }

    public void setSpikeDelta(float spikeDelta) {
        attributeSet(spikeLength, spikeDelta);
        spikeLength = spikeDelta;
    }

    public void resetSpikeLength() {
        attributeSet(spikeLength, 0);
        spikeLength = 0;
    }

    @Override
    public void increaseDetail() {
        delegate.increaseDetail();
        setModified();
    }

    @Override
    public void decreaseDetail() {
        delegate.decreaseDetail();
        setModified();
    }
}
