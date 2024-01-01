package com.codeinstructions;

import com.codeinstructions.models.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Scene {
    private Camera camera;

    private List<Light> lights = new ArrayList<>();
    private List<GameObject> objects = new ArrayList<>();

    private Light playerLight;

    private float lastTime;

    private float deltaTime;

    // hacky hacky hacky
    private float spikeLength;

    private ModelCatalog modelCatalog;

    Spiker<Model> model;


    public Scene(ModelCatalog modelCatalog) {
        this.modelCatalog = modelCatalog;
    }

    public void addObject(Matrix4f transform, Material material, Model model, String texture) {
        GameObject gameObject = new GameObject(transform, material, model);
        gameObject.setTexture(texture);
        objects.add(gameObject);
    }

    public List<GameObject> getObjects() {
        return objects;
    }

    public Camera getCamera() {
        return camera;
    }

    public void init() {
        Vector3f light1Pos = new Vector3f(10, 2, 10);
        //Vector3f light1Pos = new Vector3f(0, 2, -10);
        Vector3f light2Pos = new Vector3f(-10, 5, -10);

        Vector4fc lightColor = Color.WHITE;
        Vector4fc lightColor2 = Color.WHITE;
        Vector4f ambientColor = new Vector4f(lightColor).mul(0.1f);
        Vector4f ambientColor2 = new Vector4f(lightColor2).mul(0.1f);

        camera = new Camera();

        camera.moveUp(2);
        camera.back(3);

        createLight(light1Pos, lightColor, ambientColor, lightColor,10f, 10f, 0f, 1f, 0.3f, 0.3f, 30f, 5);

        playerLight = new Light(camera.getPos(), Color.WHITE, Color.BLACK, Color.WHITE, 0.5f, 0.5f, 0, 1, 0f, 0.6f, 10, 3,null, null);

        model = new Spiker<>(new Geodesic(4, false), 0f);


        Matrix4f objectTransform = new Matrix4f().identity()
                .translate(new Vector3f(0, 2, 0))
                .rotateY((float)Math.toRadians(90))
                .rotateX(-(float)Math.toRadians(90 - 23))
        ;

        Material material = new Material(Color.WHITE, 1, 128);
        addObject(objectTransform, material, model, "earth");

        Material floorMaterial = new Material(Color.WHITE, 0, 32);
        Matrix4f floorTransform = new Matrix4f().identity()
                .rotateX(Angle.toRadians(-90))
                .scale(120, 120, 1);

        addObject(floorTransform, floorMaterial, modelCatalog.rectangle(16, 16), "felt");
    }

    private void createLight(Vector3f light1Pos, Vector4fc lightColor, Vector4fc ambientColor, Vector4fc specularColor,
                             float diffusePower, float specularPower, float ambientPower, float attenuationConstant,
                             float attenuationLinear, float attenuationQuadratic, float cutoff, float cutOffSmoothing) {
        Matrix4f transform = new Matrix4f().identity().scale(0.2f);
        Model model = modelCatalog.geodesic();
        lights.add(new Light(light1Pos, lightColor, ambientColor, specularColor, diffusePower, specularPower,
                ambientPower, attenuationConstant, attenuationLinear, attenuationQuadratic, cutoff, cutOffSmoothing, model, transform));
    }

    public void update() {
//        for (Light light : lights) {
//            light.getPosition().rotateY(0.5f * deltaTime);
//        }

        objects.get(0).getTransform().rotateZ((float)Math.toRadians(deltaTime * 5));

        model.setSpikeDelta(spikeLength);
    }

    public void setTime(double time) {
        deltaTime = (float)time - lastTime;
        lastTime = (float)time;
    }

    float acceleration = 0.5f;
    float maxSpeed = 5f;

    float vSpeed = 0;
    float hSpeed = 0;

    // TODO: refactor this
    public void processInput(long window) {
        float speed = 1f;
        float turningSpeed = camera.getFov();
        float fovSpeed = 0.1f;

        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
            if (glfwGetKey(window, GLFW_KEY_S) != GLFW_PRESS) {
                if (hSpeed < 0) {
                    hSpeed = 0;
                }
                hSpeed += acceleration * deltaTime;
                if (hSpeed > maxSpeed) {
                    hSpeed = maxSpeed;
                }
                camera.forward(hSpeed * deltaTime);
            }
        } else if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
            if (hSpeed > 0) {
                hSpeed = 0;
            }
            hSpeed -= acceleration * deltaTime;
            if (hSpeed < -maxSpeed) {
                hSpeed = -maxSpeed;
            }
            camera.back(-hSpeed * deltaTime);
        } else {
            hSpeed = 0;
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

        if (glfwGetKey(window, GLFW_KEY_R) == GLFW_PRESS) {
            getObjects().get(0).getTransform().rotateZ((float)Math.toRadians(30) * deltaTime);
        }

        if (glfwGetKey(window, GLFW_KEY_T) == GLFW_PRESS) {
            getObjects().get(0).getTransform().rotateZ(-(float)Math.toRadians(30) * deltaTime);
        }
    }

    public List<Light> getLights() {
        ArrayList result = new ArrayList(lights);
        result.add(playerLight);
        return result;
    }

    public void toggleLight(int n) {
        lights.get(n).toggle();
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
