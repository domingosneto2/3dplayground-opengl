package com.codeinstructions;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33C.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.io.IOException;

import com.codeinstructions.models.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

public class Main {
    private static int width = 800;
    private static int height = 600;

    private static Camera camera = new Camera();

    private static Model model = new Geodesic(1);
    private static Mesh newMesh = model.mesh();

    private static Mesh mesh;

    public static void main(String[] args) throws IOException {
        if (!glfwInit())
            throw new AssertionError("Unable to initialize GLFW");
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        long window = glfwCreateWindow(width, height, "3D Playground!", NULL, NULL);
        if (window == NULL) {
            throw new AssertionError("Failed to create the GLFW window");
        }

        GLFWKeyCallback keyCallback;
        GLFWFramebufferSizeCallback fbCallback;
        Callback debugProc;

        glfwSetFramebufferSizeCallback(window, fbCallback = new GLFWFramebufferSizeCallback() {
            public void invoke(long window, int width, int height) {
                if (width > 0 && height > 0 && (Main.width != width || Main.height != height)) {
                    Main.width = width;
                    Main.height = height;
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
                    model.decreaseDetail();
                    newMesh = model.mesh();
                }

                if (key == GLFW_KEY_RIGHT_BRACKET && action == GLFW_PRESS) {
                    model.increaseDetail();
                    newMesh = model.mesh();
                }

            }
        });

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
        GL.createCapabilities();
        debugProc = GLUtil.setupDebugMessageCallback();

        Texture texture = new Texture();
        texture.loadTexture("container.jpg", GL_RGB);


        Texture texture2 = new Texture();
        texture2.loadTexture("awesomeface.png", GL_RGBA);

        texture.bindToUnit(GL_TEXTURE0);
        texture2.bindToUnit(GL_TEXTURE1);

        // create shader
        ShaderProgram program = new ShaderProgram();
        //program.loadFromResources("shader/lightingp.vs", "shader/lightingp.fs");
        program.loadFromResources("shader/lighting.vs", "shader/lighting.fs");
        program.useProgram();

        program.setInt("texture1", 0);
        program.setInt("texture2", 1);

        // set global GL state
        glClearColor(0f, 0f, 0f, 1.0f);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);

        Vector3f[] cubePositions = {
                new Vector3f( 0.0f,  0.0f,  0.0f),
                new Vector3f( 2.0f,  5.0f, -15.0f),
                new Vector3f(-1.5f, -2.2f, -2.5f),
                new Vector3f(-3.8f, -2.0f, -12.3f),
                new Vector3f( 2.4f, -0.4f, -3.5f),
                new Vector3f(-1.7f,  3.0f, -7.5f),
                new Vector3f( 1.3f, -2.0f, -2.5f),
                new Vector3f( 1.5f,  2.0f, -2.5f),
                new Vector3f( 1.5f,  0.2f, -1.5f),
                new Vector3f(-1.3f,  1.0f, -1.5f)
        };

        Vector3f color = new Vector3f(1, 1, 1);
        Vector3f lightPos = new Vector3f(5, 5, 5);
        Vector3f lightColor = new Vector3f(1, 1, 1);
        Vector3f ambientColor = new Vector3f(lightColor).mul(0.2f);

        program.setVector3("color", color);
        program.setVector3("lightPos", lightPos);
        program.setVector3("lightColor", lightColor);
        program.setVector3("ambientColor", ambientColor);


        while (!glfwWindowShouldClose(window)) {
            if (mesh != null && mesh != newMesh) {
                mesh.releaseBuffers();
            }

            mesh = newMesh;
            if (!mesh.bound()) {
                mesh.bindBuffers();
                System.out.println("Vertices: " + mesh.getNumVertices());
            }

            glfwPollEvents();

            processInput(window);

            glViewport(0, 0, width, height);
            glClearColor(0f, 0f, 0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            Matrix4f view = camera.view();

            Matrix4f projection = new Matrix4f();
            projection.perspective((float)Math.toRadians(45), (float)width/(float)height, 0.1f, 100f);


            program.setMatrix("projection", projection);
            program.setMatrix("view", view);

            for (int i = 0; i < cubePositions.length; i++) {
                Matrix4f modelTransform = new Matrix4f();
                modelTransform = modelTransform.translate(cubePositions[i]);
                float angle = 20f * i;
                modelTransform = modelTransform.rotate((float)Math.toRadians(angle), new Vector3f(1f, 0.3f, 0.5f).normalize());
                program.setMatrix("model", modelTransform);
                Matrix4f normal = new Matrix4f(modelTransform).normal();
                program.setMatrix("normal", normal);

                if (mesh.hasIndices()) {
                    glDrawElements(GL_TRIANGLES, mesh.getNumIndices(), GL_UNSIGNED_INT, 0);
                } else {
                    glDrawArrays(GL_TRIANGLES, 0, mesh.getNumVertices());
                }

            }
            glfwSwapBuffers(window);
        }
        if (debugProc != null) {
            debugProc.free();
        }
        keyCallback.free();
        fbCallback.free();
        mesh.releaseBuffers();
        program.deleteProgram();
        glfwDestroyWindow(window);
    }

    private static Vector3f from(Vector4f v) {
        return new Vector3f(v.x, v.y, v.z);
    }

    static float lastTime = 0;

    private static void processInput(long window) {
        float speed = 0.4f;
        float time = (float)glfwGetTime();
        float deltaTime = time - lastTime;
        lastTime = time;

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
    }

}

