package com.codeinstructions;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33C.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.io.IOException;

import com.codeinstructions.models.Cube;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

public class Main {
    private static int width = 800;
    private static int height = 600;

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

            }
        });

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
        GL.createCapabilities();
        debugProc = GLUtil.setupDebugMessageCallback();

        Mesh mesh = Cube.mesh();

        mesh.bindBuffers();

        Texture texture = new Texture();
        texture.loadTexture("container.jpg", GL_RGB);


        Texture texture2 = new Texture();
        texture2.loadTexture("awesomeface.png", GL_RGBA);

        texture.bindToUnit(GL_TEXTURE0);
        texture2.bindToUnit(GL_TEXTURE1);

        // create shader
        ShaderProgram program = new ShaderProgram();
        program.loadFromResources("shader/vertex.vs", "shader/fragment.fs");
        program.useProgram();

        program.setInt("texture1", 0);
        program.setInt("texture2", 1);

        // set global GL state
        glClearColor(0.01f, 0.03f, 0.05f, 1.0f);
        glEnable(GL_DEPTH_TEST);

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

        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();

            processInput(window);

            glViewport(0, 0, width, height);
            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            Matrix4f view = new Matrix4f().identity();
            view = view.translate(0, 0, -3);

            Matrix4f projection = new Matrix4f();
            projection.perspective((float)Math.toRadians(45), (float)width/(float)height, 0.1f, 100f);


            program.setMatrix("projection", projection);
            program.setMatrix("view", view);

            for (int i = 0; i < cubePositions.length; i++) {
                Matrix4f model = new Matrix4f();
                model = model.translate(cubePositions[i]);
                float angle = 20f * i;
                model = model.rotate((float)Math.toRadians(angle), new Vector3f(1f, 0.3f, 0.5f).normalize());
                program.setMatrix("model", model);

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
        //glDeleteVertexArrays(vao);
        //glDeleteBuffers(vbo);
        program.deleteProgram();
        glfwDestroyWindow(window);
    }

    private static void processInput(long window) {

    }

}

