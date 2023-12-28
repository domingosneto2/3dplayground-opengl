package com.codeinstructions.models;

public class ModelCatalog {
    public Geodesic geodesic() {
        return new Geodesic(4);
    }

    public Rectangle rectangle() {
        return new Rectangle();
    }

    public Cube cube() {
        return new Cube();
    }

    public Icosahedron icosahedron() {
        return new Icosahedron();
    }
}
