package star_game.game.base;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class Button extends Sprite {

    protected boolean press;
    protected int pointer;

    public Button(TextureRegion region) {
        super(region);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (this.press || !isMe(touch)) return false;
        this.pointer = pointer;
        this.press = true;
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (this.pointer != pointer || !this.press) return false;
        if (isMe(touch)) {
            action();
        }
        this.press = false;
        return false;
    }

    protected abstract void action();
}
