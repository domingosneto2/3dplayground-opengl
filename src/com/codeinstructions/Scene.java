package com.codeinstructions;

import com.codeinstructions.models.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

public class Scene {
    private Camera camera;

    private List<Light> lights = new ArrayList<>();
    private List<GameObject> objects = new ArrayList<>();

    private float lastTime;

    private float deltaTime;

    // hacky hacky hacky
    private float spikeLength;

    private ModelCatalog modelCatalog;

    Spiker<Geodesic> model;


    public Scene(ModelCatalog modelCatalog) {
        this.modelCatalog = modelCatalog;
    }

    public void addObject(Vector3f position, Material material, Model model) {
        objects.add(new GameObject(position, material, model));
    }

    public void addObject(Matrix4f transform, Material material, Model model) {
        objects.add(new GameObject(transform, material, model));
    }

    public List<GameObject> getObjects() {
        return objects;
    }

    public Camera getCamera() {
        return camera;
    }

    public void init() {



        Vector3f light1Pos = new Vector3f(10, 10, 10);
        Vector3f light2Pos = new Vector3f(-10, 5, -10);

//        Vector3f light1Pos = new Vector3f(10, 10, 2);
//        Vector3f light2Pos = new Vector3f(-10, 5, 2);

        Vector4fc lightColor = Color.WHITE;
        Vector4fc lightColor2 = Color.YELLOW;
        Vector4f ambientColor = new Vector4f(lightColor).mul(0.1f);
        Vector4f ambientColor2 = new Vector4f(lightColor2).mul(0.1f);

        camera = new Camera();
        camera.moveUp(10);
        camera.lookDown(Angle.toRadians(30));
        camera.back(14);

        createLight(light1Pos, lightColor, ambientColor, lightColor,10f, 1f, 1f, 1f, 0.3f, 0.1f);
        createLight(light2Pos, lightColor2, ambientColor2, lightColor,5f, 1f, 1f, 1f, 0.3f, 0.1f);

        float[] specularStrengths = {0.1f, 0.2f, 0.5f, 0.8f, 1f};
        float[] specularFactors = {2, 4, 8, 16, 32, 64, 128, 256};

        float step = 1.5f;

        Vector4fc[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.WHITE, Color.BLACK};

        model = new Spiker<>(new Geodesic(4), 0f);

        Random random = new Random();
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                float startX = -step * 11 / 2f;
                float startY = -step * 11 / 2f;

                float x = startX + step * i;
                float y = startY + step * j;

                Vector3f pos = new Vector3f(x, 0.5f, y);
                Material material = new Material(colors[random.nextInt(colors.length)], specularStrengths[random.nextInt(specularStrengths.length)], specularFactors[random.nextInt(specularFactors.length)]);
                addObject(pos, material, model);
            }
        }


        Material floorMat = new Material(Color.WHITE, 0, 32);
        Matrix4f floorTransform = new Matrix4f().identity()
                .rotateX(Angle.toRadians(-90))
                .scale(30, 30, 1);

        addObject(floorTransform, floorMat, modelCatalog.rectangle(4, 4));


        for (Light light : lights) {
            light.getPosition().x /= 1.5f;
        }
    }

    private void createLight(Vector3f light1Pos, Vector4fc lightColor, Vector4fc ambientColor, Vector4fc specularColor,
                             float diffusePower, float specularPower, float ambientPower, float attenuationConstant,
                             float attenuationLinear, float attenuationQuadratic) {
        Matrix4f transform = new Matrix4f().identity().scale(0.2f);
        Model model = modelCatalog.geodesic();
        lights.add(new Light(light1Pos, lightColor, ambientColor, specularColor, diffusePower, specularPower,
                ambientPower, attenuationConstant, attenuationLinear, attenuationQuadratic, model, transform));
    }

    public void update() {
        for (Light light : lights) {
            light.getPosition().x *= 1.5f;
            light.getPosition().rotateY(0.5f * deltaTime);
            light.getPosition().x /= 1.5f;
            //light.getPosition().rotateZ(0.5f * deltaTime);
        }

        model.setSpikeDelta(spikeLength);
    }

    public void setTime(double time) {
        deltaTime = (float)time - lastTime;
        lastTime = (float)time;
    }

    // TODO: refactor this
    public void processInput(long window) {
        float speed = 1f;
        float turningSpeed = 1;
        float fovSpeed = 0.5f;

        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
            camera.forward(speed * deltaTime);
        }

        if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
            camera.back(speed * deltaTime);
        }

        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
            camera.moveLeft(speed * deltaTime);
        }

        if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
            camera.moveRight(speed * deltaTime);
        }

        if (glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS) {
            camera.turnLeft(turningSpeed * deltaTime);
        }

        if (glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS) {
            camera.turnRight(turningSpeed * deltaTime);
        }

        if (glfwGetKey(window, GLFW_KEY_PAGE_UP) == GLFW_PRESS) {
            camera.moveUp(speed * deltaTime);
        }

        if (glfwGetKey(window, GLFW_KEY_PAGE_DOWN) == GLFW_PRESS) {
            camera.moveDown(speed * deltaTime);
        }

        if (glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS) {
            camera.lookUp(turningSpeed * deltaTime);
        }

        if (glfwGetKey(window, GLFW_KEY_DOWN) == GLFW_PRESS) {
            camera.lookDown(turningSpeed * deltaTime);
        }

        if (glfwGetKey(window, GLFW_KEY_1) == GLFW_PRESS) {
            camera.increaseFov(fovSpeed * deltaTime);
        }

        if (glfwGetKey(window, GLFW_KEY_2) == GLFW_PRESS) {
            camera.decreaseFov(fovSpeed * deltaTime);
        }

        if (glfwGetKey(window, GLFW_KEY_Q) == GLFW_PRESS) {
            camera.rotateLeft(turningSpeed * deltaTime);
        }

        if (glfwGetKey(window, GLFW_KEY_E) == GLFW_PRESS) {
            camera.rotateRight(turningSpeed * deltaTime);
        }

        if (glfwGetKey(window, GLFW_KEY_P) == GLFW_PRESS) {
            spikeLength += 0.1f * deltaTime;
        }

        if (glfwGetKey(window, GLFW_KEY_O) == GLFW_PRESS) {
            spikeLength -= 0.1f * deltaTime;
        }
    }

    public List<Light> getLights() {
        return lights;
    }

    public void toggleLight(int n) {
        lights.get(n).toogle();
    }

    public void increaseLightPower() {
        for (Light light : lights) {
            light.increasePower();
        }
    }

    public void decreaseLightPower() {
        for (Light light : lights) {
            light.decreasePower();
        }
    }

    public void resetSpikeLength() {
        spikeLength = 0;
    }

    public float getSpikeLength() {
        return spikeLength;
    }

    public void decreaseDetail() {
        model.decreaseDetail();;
    }

    public void increaseDetail() {
        model.increaseDetail();;
    }
}
