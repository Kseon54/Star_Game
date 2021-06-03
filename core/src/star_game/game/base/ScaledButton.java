package star_game.game.base;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class ScaledButton extends Sprite {

    private static final float SCALE = 0.9f;

    private boolean press;
    private int pointer;

    public ScaledButton(TextureRegion region) {
        super(region);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (press || !isMe(touch)) {
            return false;
        }
        this.pointer = pointer;
        this.scale = SCALE;
        press = true;
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (this.pointer != pointer || !press) return false;
        if (isMe(touch)) {
            action();
        }
        this.press = false;
        this.scale = 1f;
        return false;
    }

    protected abstract void action();
}
