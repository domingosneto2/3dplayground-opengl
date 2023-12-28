package com.codeinstructions;

import com.codeinstructions.models.Color;
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


    public Scene() {

    }

    public void addObject(Vector3f position, Material material) {
        objects.add(new GameObject(position, material));
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
        Vector4f lightColor = new Vector4f(1, 1, 1, 1);
        Vector4f ambientColor = new Vector4f(lightColor).mul(0.02f);

        camera = new Camera();
        camera.forward(-20f);
        lights.add(new Light(light1Pos, lightColor, ambientColor, lightColor, 1f, 1f, 1f, 1f, 0.09f, 0.032f));
        lights.add(new Light(light2Pos, lightColor, new Vector4f(0, 0, 0, 0), lightColor, 1, 1, 1f, 1,0.09f, 0.032f));

        float[] specularStrengths = {0.1f, 0.2f, 0.5f, 0.8f, 1f};
        float[] specularFactors = {2, 4, 8, 16, 32, 64, 128, 256};

        float step = 3f;

        Vector4fc[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.WHITE, Color.BLACK};

        Random random = new Random();
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                float startX = -step * 11 / 2f;
                float startY = step * 11 / 2f;

                float x = startX + step * i;
                float y = startY - step * j;

                Vector3f pos = new Vector3f(y, -5, x - 12);
                Material material = new Material(colors[random.nextInt(colors.length)], specularStrengths[random.nextInt(specularStrengths.length)], specularFactors[random.nextInt(specularFactors.length)]);
                addObject(pos, material);
            }
        }

        for (Light light : lights) {
            light.getPosition().x /= 3f;
        }
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
    }

    public void update() {
        for (Light light : lights) {
            light.getPosition().x *= 3f;
            light.getPosition().rotateY(0.5f * deltaTime);
            light.getPosition().x /= 3f;
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
}
