package Jade;

import components.FontRenderer;
import components.SpriteRenderer;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import renderer.Shader;
import renderer.Texture;
import util.Time;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene{

    private String vertexShaderSrc = "#version 330 core\n" +
            "\n" +
            "layout (location = 0) in vec3 aPos;\n" +
            "layout(location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main() {\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}\n";

    private String fragmentShaderSrc = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main(){\n" +
            "    color = fColor;\n" +
            "}";

    // private int vertexID, fragmentID, shaderProgram;
    // Position, Color, UV Coordinate
    private float[] vertexArray = {
            100.5f, -0.5f, 0.0f,            1.0f, 0.0f, 0.0f, 1.0f,         1, 0, // Bottom Right
            -0.5f, 100.5f, 0.0f,            0.0f, 1.0f, 0.0f, 1.0f,         0, 1, // Top left
            100.5f, 100.5f, 0.0f,           0.0f, 0.0f, 1.0f, 1.0f,         1, 1, // Top right
            -0.5f, -0.5f, 0.0f,             1.0f, 1.0f, 0.0f, 1.0f,         0, 0, // Bottom left
    };

    // IMPORTANT: MUST BE in counter-clock wise order
    private int[] elementArray = {
            2, 1, 0, // Top right triangle
            0, 1, 3 // Bottom left Triangle

    };

    private int vaoID, vboID, eboID;
    private Shader defaultShader;
    private Texture testingTexture;

    private boolean firstTime = false;
    GameObject testObj;

    public LevelEditorScene() {
    }

    @Override
    public void init() {
        System.out.println("Obj creating");
        this.testObj = new GameObject("Test object");
        this.testObj.addComponent(new SpriteRenderer());
        this.testObj.addComponent(new FontRenderer());
        this.addGameObjectToScene(this.testObj);


        this.camera = new Camera(new Vector2f());
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();
        this.testingTexture = new Texture("assets/images/testimg2.png");


        // Generating VAO
        vaoID = glGenVertexArrays();
        // Binds the following processes to this specific vaoID
        glBindVertexArray(vaoID);
        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        // Flips ensures that OpenGL gets the order correctly
        vertexBuffer.put(vertexArray).flip();

        // Generating VBO
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add vertex attribute buffers | Strides and offsets
        int positionSize = 3;
        int colorSize = 4;
        // Since everything is in bytes, and VBO is in 4 bytes, hence everything has to be 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionSize + colorSize + uvSize) * Float.BYTES;

        // Index, Amount of Element, Type, if it's normalized, The size of one vertex, the offsets
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float dt) {
        camera.position.x -= dt * 50.0f;
        camera.position.y -= dt * 50.0f;

        defaultShader.use();

        // Upload uniform Texture to GPU
        defaultShader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        testingTexture.bind();

        // Upload Uniform
        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());


        glBindVertexArray(vaoID);

        // Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);


        defaultShader.detach();

        if (!firstTime) {
            System.out.println("Creating GameObject");
            GameObject go = new GameObject("Game Test 2");
            go.addComponent(new SpriteRenderer());
            this.addGameObjectToScene(go);
            firstTime = true;
        }

        for (GameObject go:
             this.gameObject) {
            go.update(dt);
        }
    }
}
