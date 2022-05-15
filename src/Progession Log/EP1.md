# [Setting Up the Window with LWJGL](https://www.youtube.com/watch?v=_UYxTtJQuuw&list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE&index=2)

## Singleton 
Used to prevent a creation of another instances of the class. For example, the Window.java Class.
The implementation mention is through singleton. With the implementation
```java
public static Window get() {
        if(window == null) {
            Window.window = new Window();
        }
        return Window.window;
}
```

## GLFW Initialization
For a game engine, or just a game in general you need two things. A game initialization
and a game loop. Game Initialization creates the hint along with other information necessary
for a GLFW window to appear
```java

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
```

The Game Loop, or the looping function ensures the game doesn't close until the user close it
or if there exists a context in which the game will break.

```java
    public void loop() {
        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();
            
            glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            // Swap buffer automatically
            glfwSwapBuffers(glfwWindow);
        }
}
```

## Buffer
GLFW uses a Double-buffering technique, in which the front-buffer is displayed to the user
and the back-buffer is queue to be rendered.

## Resource
- [LearnOpenGL - Hello World](https://learnopengl.com/Getting-started/Hello-Window)
- [GLFW: Getting Started](https://www.glfw.org/docs/3.0/quick.html)