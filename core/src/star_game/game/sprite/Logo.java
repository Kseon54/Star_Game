package star_game.game.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import star_game.game.base.Sprite;
import star_game.game.math.Rect;

public class Logo extends Sprite{

    Vector2 nextPos = new Vector2();
    Vector2 tmp = new Vector2();
    Vector2 v = new Vector2();

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
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        tmp.set(nextPos);
        if (tmp.sub(pos).len() <= v.len()) {
            pos.set(nextPos);
            v.setZero();
        } else pos.add(v);
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

    public void setNextPos(Vector2 nextPos) {
        this.nextPos.set(nextPos);
        this.v.set(nextPos.cpy().sub(pos).setLength(speed));
    }
}
