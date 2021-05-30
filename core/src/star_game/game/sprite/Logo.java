package star_game.game.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import star_game.game.actions.IMovement;
import star_game.game.base.Sprite;
import star_game.game.math.Rect;

public class Logo extends Sprite implements IMovement {

    private float speed;

    public Logo(Texture texture) {
        super(new TextureRegion(texture));
        init();
    }

    private void init() {
        setHeightProportion(0.4f);
        this.pos.set(0, 0);
        speed = 0.002f;
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        move();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public void setNextPos(Vector2 nextPos) {
        this.nextPos.set(nextPos);
        this.v.set(nextPos.cpy().sub(pos).nor().scl(speed));
    }

    @Override
    public void move() {
        tmp.set(nextPos);
        if (tmp.sub(pos).len() <= v.len()) {
            pos.set(nextPos);
            v.setZero();
        } else pos.add(v);
    }
}
