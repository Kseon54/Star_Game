package star_game.game.base;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class ScaledButton extends Button {

    private static final float SCALE = 0.9f;

    public ScaledButton(TextureRegion region) {
        super(region);
    }

    @Override
    public void touchDownAction(Vector2 touch, int pointer, int button) {
        this.scale = SCALE;
    }

    @Override
    public void touchUpAction(Vector2 touch, int pointer, int button) {
        this.scale = 1f;
    }
}
