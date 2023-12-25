package com.codeinstructions;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33C.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;

import java.io.IOException;
import java.nio.*;

import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

/**
 * Basic triangle
 */
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

        long window = glfwCreateWindow(width, height, "Hello, Planet!", NULL, NULL);
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
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                    glfwSetWindowShouldClose(window, true);
            }
        });

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);

        try (MemoryStack frame = stackPush()) {
            IntBuffer framebufferSize = frame.mallocInt(2);
            nglfwGetFramebufferSize(window, memAddress(framebufferSize), memAddress(framebufferSize) + 4);
            width = framebufferSize.get(0);
            height = framebufferSize.get(1);
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
        GL.createCapabilities();
        debugProc = GLUtil.setupDebugMessageCallback();

        float[] vertices = {
                // positions          // colors           // texture coords
                0.5f,  0.5f, 0.0f,   1.0f, 0.0f, 0.0f,   1.0f, 1.0f,   // top right
                0.5f, -0.5f, 0.0f,   0.0f, 1.0f, 0.0f,   1.0f, 0.0f,   // bottom right
                -0.5f, -0.5f, 0.0f,   0.0f, 0.0f, 1.0f,   0.0f, 0.0f,   // bottom left
                -0.5f,  0.5f, 0.0f,   1.0f, 1.0f, 0.0f,   0.0f, 1.0f    // top left
        };

        int[] indices = {  // note that we start from 0!
                0, 1, 3,   // first triangle
                1, 2, 3

        };
        Mesh mesh = new Mesh(vertices, indices, 3, 3, 2);
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

        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();

            glViewport(0, 0, width, height);
            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            //glBindVertexArray(vao);
            glDrawElements(GL_TRIANGLES, 6  , GL_UNSIGNED_INT, 0);
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

}

