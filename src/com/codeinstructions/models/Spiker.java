package com.codeinstructions.models;


import org.joml.Vector4f;
import org.joml.Vector4fc;

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

        if (myMesh == null || isModified() || delegate.isModified()) {
            PolygonMesh sourceMesh = delegate.polygonMesh();
            myMesh = applySpikes(sourceMesh);
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

            mesh.polygon(v0, v1, spike);
            mesh.polygon(v1, v2, spike);

            if (polygon.getNumVertices() == 3) {
                mesh.polygon(v2, v0, spike);
            } else {
                Vector4fc v3 = polygon.getVertex(3);
                mesh.polygon(v2, v3, spike);
                mesh.polygon(v3, v0, spike);
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
