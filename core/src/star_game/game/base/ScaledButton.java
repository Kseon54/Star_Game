package star_game.game.base;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class ScaledButton extends Button {

    private static final float SCALE = 0.9f;

    public ScaledButton(TextureRegion region) {
        super(region);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (press || !isMe(touch)) return false;
        super.touchDown(touch,pointer,button);
        this.scale = SCALE;
        press = true;
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (this.pointer != pointer || !press) return false;
        super.touchUp(touch,pointer,button);
        this.scale = 1f;
        return false;
    }
}
