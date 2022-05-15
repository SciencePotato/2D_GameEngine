package Jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width, height;
    private String title;
    private long glfwWindow;

    private static Window window = null;


    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Engine";
    }

    public static Window get() {
        if(window == null) {
            Window.window = new Window();
        }
        return Window.window;
    }

    public void run() {
        System.out.println("Hello LWJGL" + Version.getVersion() + "!");

        // Engine Loop
        init();
        loop();
    }

    public void init() {
        // Error Call back || Set up an Error output
        GLFWErrorCallback.createPrint(System.err).set();
        // GLFW Init
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to init GLFW");
        }

        //Configure GLFW window
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create the Actual window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW Window");
        }

        // Make OpenGL Context Current
        glfwMakeContextCurrent(glfwWindow);
        // Buffer Swapping and V-sync
        glfwSwapInterval(1);

        glfwShowWindow(glfwWindow);

        // Make sure that you can use the context
        GL.createCapabilities();
    }

    public void loop() {
        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();

            glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            // Swap buffer automatically
            glfwSwapBuffers(glfwWindow);
        }
    }
}
