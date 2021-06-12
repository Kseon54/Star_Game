package star_game.game.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import star_game.game.base.Sprite;

public class Explosion extends Sprite {

    private static final float ANIMATION_INTERVAL = 0.017f;

    private float animationInterval;
    private Sound explosionSound;

    public Explosion(TextureAtlas atlas, Sound explosionSound) {
        super(atlas.findRegion("explosion"), 9, 9, 74);
        this.explosionSound = explosionSound;
    }

    public void set(Vector2 pos, float height){
        this.pos.set(pos);
        setHeightProportion(height);
        explosionSound.play(0.2f);
    }

    @Override
    public void update(float delta) {
        animationInterval+=delta;
        if (animationInterval>=ANIMATION_INTERVAL){
            animationInterval = 0;
            if (++frame>=regions.length) destroy();
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        frame = 0;
    }
}
