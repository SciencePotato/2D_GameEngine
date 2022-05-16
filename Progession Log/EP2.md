## [Adding Event Listener with GLFW]()

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

## Mouse Listener
- dx, dy: Change in X and Y position for the mouse
- EndMouse Listener must be called at the end since you want to change in the position changed.
- 




## Resource
- [GLFW documentation](https://www.glfw.org/docs/3.3/input_guide.html)