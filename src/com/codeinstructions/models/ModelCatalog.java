package com.codeinstructions.models;

public class ModelCatalog {
    public Geodesic geodesic() {
        return new Geodesic(4, false);
    }

    public Rectangle rectangle(int xRepeats, int yRepeats) {
        return new Rectangle(xRepeats, yRepeats);
    }

    public Cube cube() {
        return new Cube();
    }

    public Icosahedron icosahedron() {
        return new Icosahedron();
    }
}
