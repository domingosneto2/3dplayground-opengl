package com.codeinstructions;

import com.codeinstructions.models.Mesh;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL;

import java.util.List;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE1;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_SRGB;

public class Renderer {
    private int width;

    private int height;

    private Texture texture1;

    private Texture texture2;

    private ShaderProgram program;

    private ShaderProgram textureProgram;

    private ShaderProgram lightSourceProgram;

    public Renderer(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void init() throws Exception {
        GL.createCapabilities();

        texture1 = new Texture();
        texture1.loadTexture("fabrics_0075_color_2k.jpg", GL_RGB);


        texture2 = new Texture();
        texture2.loadTexture("fabrics_0075_normal_opengl_2k.png", GL_RGBA);


        texture1.bindToUnit(GL_TEXTURE0);
        texture2.bindToUnit(GL_TEXTURE1);

        program = new ShaderProgram();
        program.loadFromResources("shader/lightingp.vs", "shader/lightingp.fs");
        program.useProgram();

        textureProgram = new ShaderProgram();
        textureProgram.loadFromResources("shader/lighting-texture.vs", "shader/lighting-texture.fs");
        textureProgram.useProgram();

        textureProgram.setInt("texture1", 0);
        textureProgram.setInt("texture2", 1);

        lightSourceProgram = new ShaderProgram();
        lightSourceProgram.loadFromResources("shader/lightSource.vs", "shader/lightSource.fs");


        // set global GL state
        glClearColor(0f, 0f, 0f, 1.0f);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glEnable(GL_FRAMEBUFFER_SRGB);
    }

    public void render(Scene scene) {
        glViewport(0, 0, width, height);
        glClearColor(0, 0, 0, 0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        Matrix4f view = scene.getCamera().view();

        Matrix4f projection = new Matrix4f();
        projection.perspective(scene.getCamera().getFov(), (float)width/(float)height, 0.1f, 100f);

        setUniforms(program, scene, projection, view);
        setUniforms(textureProgram, scene, projection, view);
        setUniforms(lightSourceProgram, scene, projection, view);

        for (int i = 0; i < scene.getObjects().size(); i++) {
            GameObject gameObject = scene.getObjects().get(i);
            renderObject(gameObject);
        }

        lightSourceProgram.useProgram();
        List<Light> lights = scene.getLights();
        for (int i = 0; i < lights.size(); i++) {
            Light light = lights.get(i);
            renderPointLight(light, projection, view);
        }
    }

    @NotNull
    private void setUniforms(ShaderProgram program, Scene scene, Matrix4f projection, Matrix4f view) {
        program.useProgram();

        program.setMatrix("projection", projection);
        program.setMatrix("view", view);

        List<Light> lights = scene.getLights();
        for (int i = 0; i < lights.size(); i++) {
            Light light = lights.get(i);
            String prefix = "pointLights[" + i + "].";

            program.setVector3(prefix + "position", light.getPosition());
            program.setVector4(prefix + "diffuse", light.getDiffuseColor());
            program.setVector4(prefix + "ambient", light.getAmbientColor());
            program.setVector4(prefix + "specular", light.getSpecularColor());
            program.setFloat(prefix + "constant", light.getConstant());
            program.setFloat(prefix + "linear", light.getLinear());
            program.setFloat(prefix + "quadratic", light.getQuadratic());
            program.setFloat(prefix + "diffusePower", light.getDiffusePower());
            program.setFloat(prefix + "specularPower", light.getSpecularPower());
            program.setFloat(prefix + "ambientPower", light.getAmbientPower());
        }

        program.setInt("numPointLights", lights.size());

        program.setVector3("cameraPos", scene.getCamera().pos);
    }

    private void renderPointLight(Light light, Matrix4f projection, Matrix4f view) {
        Matrix4f modelTransform = new Matrix4f();
        modelTransform.translate(light.getPosition());
        modelTransform.mul(light.getModelTransform());
        lightSourceProgram.setMatrix("model", modelTransform);
        lightSourceProgram.setVector4("lightColor", light.getDiffuseColor());
        lightSourceProgram.setMatrix("projection", projection);
        lightSourceProgram.setMatrix("view", view);

        Mesh mesh = light.getModel().mesh();
        if (!mesh.initialized()) {
            mesh.initializeBuffers();
        }

        mesh.bind();

        if (mesh.hasIndices()) {
            glDrawElements(GL_TRIANGLES, mesh.getNumIndices(), GL_UNSIGNED_INT, 0);
        } else {
            glDrawArrays(GL_TRIANGLES, 0, mesh.getNumVertices());
        }
    }

    private void renderObject(GameObject gameObject) {
        Matrix4f modelTransform = gameObject.getTransform();

        ShaderProgram program;
        Mesh mesh = gameObject.mesh();
        if (mesh.getTexCoordSize() > 0) {
            program = this.textureProgram;
        } else {
            program = this.program;
        }

        program.useProgram();



        program.setMatrix("model", modelTransform);
        Matrix4f normal = new Matrix4f(modelTransform).normal();
        program.setMatrix("normal", normal);

        Material material = gameObject.getMaterial();

        program.setVector4("color", material.getColor());
        program.setFloat("specularStrength", material.getSpecularStrength());
        program.setFloat("specularFactor", material.getSpecularFactor());

        // debugTBN(mesh, modelTransform);

        mesh.bind();

        if (mesh.hasIndices()) {
            glDrawElements(GL_TRIANGLES, mesh.getNumIndices(), GL_UNSIGNED_INT, 0);
        } else {
            glDrawArrays(GL_TRIANGLES, 0, mesh.getNumVertices());
        }
    }

    private void debugTBN(Mesh mesh, Matrix4f modelTransform) {

        Vector3f normal = mesh.getNormal(0);
        Vector3f tangent = mesh.getTangent(0);
        Vector3f bitangent = mesh.getBitangent(0);

        Vector3f T = new Vector4f(tangent, 0).mul(modelTransform).normalize().xyz(new Vector3f());
        Vector3f B = new Vector4f(bitangent, 0).mul(modelTransform).normalize().xyz(new Vector3f());
        Vector3f N = new Vector4f(normal, 0).mul(modelTransform).normalize().xyz(new Vector3f());

        Matrix3f TBN  = new Matrix3f(T, B, N);
        Vector3f newNormal = new Vector3f(normal).mul(TBN);
        System.out.println(normal + " " + newNormal);
    }

    public void clear() {
        program.deleteProgram();
        lightSourceProgram.deleteProgram();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
