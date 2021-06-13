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
    public final boolean touchDown(Vector2 touch, int pointer, int button) {
        if (this.press || !isMe(touch)) return false;
        this.pointer = pointer;
        this.press = true;
        touchDownAction(touch,pointer,button);
        return false;
    }

    protected void touchDownAction(Vector2 touch, int pointer, int button){

    }

    @Override
    public final boolean touchUp(Vector2 touch, int pointer, int button) {
        if (this.pointer != pointer || !this.press) return false;
        if (isMe(touch)) {
            actionOnClick();
        }
        this.press = false;
        touchUpAction(touch,pointer,button);
        return false;
    }

    protected void touchUpAction (Vector2 touch, int pointer, int button){

    }

    protected abstract void actionOnClick();
}
