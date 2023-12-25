package com.codeinstructions;

import java.io.IOException;

import static com.codeinstructions.GLUtils.createShader;
import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL20C.glGetProgramInfoLog;

public class ShaderProgram {
    int program;
    int vshader;
    int fshader;

    public void loadFromResources(String vertexShader, String fragmentShader) throws IOException {
        program = glCreateProgram();
        vshader = createShader("shader/vertex.vs", GL_VERTEX_SHADER);
        fshader = createShader("shader/fragment.fs", GL_FRAGMENT_SHADER);
        glAttachShader(program, vshader);
        glAttachShader(program, fshader);
        glLinkProgram(program);
        int linked = glGetProgrami(program, GL_LINK_STATUS);
        String programLog = glGetProgramInfoLog(program);
        if (programLog.trim().length() > 0) {
            System.err.println(programLog);
        }
        if (linked == 0) {
            throw new AssertionError("Could not link program");
        }
    }

    public void useProgram() {
        glUseProgram(program);
    }

    public void deleteProgram() {
        glDeleteProgram(program);
    }

    public void setInt(String name, int value) {
        glUniform1i(glGetUniformLocation(program, name), value);
    }
}
