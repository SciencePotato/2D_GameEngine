package Jade;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObject = new ArrayList();

    public Scene() {

    }

    public void init(){

    }

    public void start() {
        for (GameObject go :
                gameObject) {
            go.start();
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject go) {
        if (!isRunning) {
            gameObject.add(go);
        } else {
            gameObject.add(go);
            go.start();
        }
    }

    public abstract void update(float dt) ;
}
