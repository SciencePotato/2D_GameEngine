package Jade;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene{

    private boolean changingScene = false;
    private float timeToChangeScene = 2.0f;
    public LevelEditorScene() {
        System.out.println("Inside LevelEditor Scene");
    }

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
}
