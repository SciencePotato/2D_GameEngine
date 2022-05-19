package components;

import Jade.Component;

public class SpriteRenderer extends Component {
    private boolean firstTime = false;
    @Override
    public void start() {
        System.out.println("Sprite Renderer");
    }

    @Override
    public void update(float dt) {
        if (!firstTime) {
            firstTime = true;
            System.out.println("Updating");
        }
    }
}
