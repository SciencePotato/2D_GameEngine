## [Adding Event Listener with GLFW](https://www.youtube.com/watch?v=88oZT7Aum6s&list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE&index=3)

## Window (Update)
```java
// Memory Free
glfwFreeCallbacks(glfwWindow);
glfwDestroyWindow(glfwWindow);

// Terminate GLFW and free error Callback         
glfwTerminate();
glfwSetErrorCallback(null).free();
```
This is necessary since technically it's GLFW is implemented in C/C++, which means 
user has to manage the memory, this is the same thing as you first `malloc()` some memory
then later you `free()` that say memory.

Afterward, you have to add listeners or Callback; basically attaching the functions that you 
created to GLFW window through their custom input. The syntax of which is like Java Lambda.
```java
glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
```
This has to be attached after the window is created; if you add it before it's created, there
will be an error which is what I experienced.

## Mouse Listener
A Singleton class that is going to be used to attach to GLFW window.

Variables that were used in the Mouse Listener;
- xPos, yPos -> The position of X and Y of your mouse.
- lastX, lastY -> The last position of X and Y of your mouse.
- scrollX, scrollY -> The last position of your scrolling.

Getter and Setters, rather, just Getters.
- getX(), getY() -> Current X/Y;
- getDX(), getDY() -> Change in X/Y;
- getScrollX(), getScrollY() -> Change in the Scroll of DX and DY;
- isDragging() -> If the mouse's current position is dragging or not;
- mouseButtonDown() -> If any of the mouse button is down (We limited it to 3);

Callbacks(Setter basically) that were attached to the window:
- MousePosCallback() -> Change the location of the mouse by updating the position 
X and Y.
- MouseButtonCallback() -> Change which ever button is pressed.
- MouseScrollCallback() -> Change and display the amount of scrolling done by the user.

## Key Listener
Another Singleton Class that is going to be used to attach to GLFW window.

Variables that are used in Key Listener;
- keyPressed -> Basically a boolean that keeps track of everything that was pressed at the 
same time.

Getter 
- `isKeyPressed(int keyCode)` returns whether a button is pressed.

CallBack (Setter)
- keyCallBack -> Basically returns if the button is pressed or not.
## Resource
- [GLFW documentation](https://www.glfw.org/docs/3.3/input_guide.html)