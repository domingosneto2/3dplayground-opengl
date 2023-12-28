package com.codeinstructions;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.io.IOException;

import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;

public class Main {

    static Scene scene = new Scene();
    static Renderer renderer = new Renderer(800, 600);


    public static void main(String[] args) throws Exception {
        if (!glfwInit())
            throw new AssertionError("Unable to initialize GLFW");
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        long window = glfwCreateWindow(renderer.getWidth(), renderer.getHeight(), "3D Playground!", NULL, NULL);
        if (window == NULL) {
            throw new AssertionError("Failed to create the GLFW window");
        }

        GLFWKeyCallback keyCallback;
        GLFWFramebufferSizeCallback fbCallback;

        glfwSetFramebufferSizeCallback(window, fbCallback = new GLFWFramebufferSizeCallback() {
            public void invoke(long window, int width, int height) {
                if (width > 0 && height > 0 && (renderer.getWidth() != width || renderer.getHeight() != height)) {
                    renderer.setWidth(width);
                    renderer.setHeight(height);
                }
            }
        });

        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                    glfwSetWindowShouldClose(window, true);
                    return;
                }
                if (key == GLFW_KEY_LEFT_BRACKET && action == GLFW_PRESS) {
                    renderer.decreaseDetail();
                }

                if (key == GLFW_KEY_RIGHT_BRACKET && action == GLFW_PRESS) {
                    renderer.increaseDetail();
                }

                if (key == GLFW_KEY_N && action == GLFW_PRESS) {
                    scene.toggleLight(0);
                }

                if (key == GLFW_KEY_M && action == GLFW_PRESS) {
                    scene.toggleLight(1);
                }

                if (key == GLFW_KEY_PERIOD && action == GLFW_PRESS) {
                    scene.increaseLightPower();
                }

                if (key == GLFW_KEY_COMMA && action == GLFW_PRESS) {
                    scene.decreaseLightPower();
                }

            }
        });

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidmode.width() - renderer.getWidth()) / 2, (vidmode.height() - renderer.getHeight()) / 2);

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);

        scene.init();
        renderer.init();


        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();
            scene.setTime(glfwGetTime());
            scene.processInput(window);
            scene.update();

            renderer.render(scene);

            glfwSwapBuffers(window);
        }

        keyCallback.free();
        fbCallback.free();

        renderer.clear();

        glfwDestroyWindow(window);
    }
}

