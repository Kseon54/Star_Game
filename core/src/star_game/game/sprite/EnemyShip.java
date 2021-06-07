package star_game.game.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import star_game.game.base.Sprite;
import star_game.game.math.Rect;

public class EnemyShip extends Sprite {

    private Rect worldBounds;
    private Vector2 v;
    private int healthPoints;
    private int damage;

    public EnemyShip(TextureAtlas atlas) {
        super(atlas.findRegion("enemy0"), 1, 2, 2);
        v = new Vector2();
    }

    public void set(
            Vector2 pos0,
            Vector2 v0,
            Rect worldBounds,
            int damage,
            float height
    ) {
        this.damage = damage;
        this.pos.set(pos0);
        this.v.set(v0);
        this.worldBounds = worldBounds;
        this.healthPoints = damage;
        setHeightProportion(height);
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        if (isOutside(worldBounds) || healthPoints<=0) {
            destroy();
        }
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getDamage() {
        return damage;
    }
}
