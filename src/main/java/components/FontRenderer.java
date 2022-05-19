package components;

import Jade.Component;

public class FontRenderer extends Component {
    @Override
    public void start() {
        if (gameObject.getComponents(SpriteRenderer.class) != null)
            System.out.println("Font Renderer");
    }

    @Override
    public void update(float dt) {

    }
}
