## [Scene Manager & Delta Time](https://www.youtube.com/watch?v=gYhEknnKFJY&list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE&index=4)

## Delta Time [util/Time]
Delta time in video game is to ensure that everything is correct, time wise. This will ensure
the game engine to be correct no matter how fast or how slow your FPS is regarding everything
else.

`nanotime()` Use to get nanoseconds, and 1 nanosecond is 10^-9 seconds. Then you want to find 
delta Time using FPS (Frames per second). From `nanotime()`, we know it's S/F, meaning to get
delta time, it's the same as 1/dt = F/S.

```java

public class Time {
    
    public static float timeStarted = System.nanoTime();

    public static float getTime() {
        return (float) ((System.nanoTime() - timeStarted) * 1E-9);
    }
}
```

## Window Update

### Time Update
```java
public void loop() {
        float beginTime = Time.getTime();
        float endTime = Time.getTime();

        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();

            glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            if(KeyListener.isKeyPressed(GLFW_KEY_SPACE))
                System.out.println("Pressed");

            // Swap buffer automatically
            glfwSwapBuffers(glfwWindow);

            // dt = Delta time, time elapsed, and swap endTime with BeginTime
            endTime = Time.getTime();
            float dt = endTime - beginTime;
            beginTime = endTime;
        }
}
```

Added A tracker for Time loop, counting for Frames elapsed for every operation. 

### Changing Scene Update

```java

public static void changeScene(int newScene) {
    switch (newScene){
        case 0:
            currentScene = new LevelEditorScene();
            break;
        case 1:
            currentScene = new LevelScene();
            break;
        default:
            assert false: "Unknown Scene" + newScene + "!";
            break;
        }
}
```

This ensures that we are changing scenes by looping through the scenes that we made and change 
the current scene being displayed into the scene we wanted to it to display.

## Scene
An abstraction of a class to implement future scenes and to allow functions to be inherited; 
making the process of making new scenes easier and allow the users to add more specific and
different scenes.
### LevelEditorScene
This is the first scene we initialize to, and this is where we're going to switch from. 
The LevelEditorScene has an `update(float dt)` function, in which dt, represents the time passed.
The transition between scene is taken care of by the variable `timetoChangeScene()`

The following is the Update function from Video 3 so far:
```java

@Override
public void update(float dt) {
        // Frames per Second
        System.out.println("Frames" + (1.0f / dt) + "Per Second");

        if (!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
           changingScene = true;
        }

        if (changingScene && timeToChangeScene > 0) {
            timeToChangeScene -= dt;
            System.out.println(timeToChangeScene);
        } else if(changingScene) {
            Window.changeScene(1);
        }
}
```
### LevelScene
There's nothing much to this so far, it's a placeholder so far for us to place a scene down.