package Jade;

public abstract class Scene {

    protected Camera camera;
    // Game wrapper basically
    public Scene() {

    }

    public void init(){

    }

    public abstract void update(float dt) ;
}
