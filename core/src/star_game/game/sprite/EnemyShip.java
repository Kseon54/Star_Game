package star_game.game.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import star_game.game.base.Ship;
import star_game.game.math.Rect;
import star_game.game.pool.BulletPool;
import star_game.game.pool.ExplosionPool;

public class EnemyShip extends Ship {

    private static final float PADDING = 0.03f;

    public EnemyShip(Rect worldBounds, ExplosionPool explosionPool, BulletPool bulletPool, Sound bulletSound) {
        this.worldBounds = worldBounds;
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.bulletSound = bulletSound;
        v0 = new Vector2();
        v = new Vector2();
        this.bulletV = new Vector2();
        this.bulletPos = new Vector2();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        bulletPos.set(pos.x, pos.y - getHalfHeight());

        if (getTop() + PADDING < worldBounds.getTop()) {
            v.set(v0);
        } else {
            reloadTimer = reloadInterval * 0.8f;
        }

        if (getRight() > worldBounds.getRight() || getLeft() < worldBounds.getLeft()) {
            v0.x *= -1;
            v.x *= -1;
        }

        if (worldBounds.isOutside(this)) {
            destroy();
        }
    }

    public void set(
            TextureRegion[] regions,
            Vector2 v0,
            TextureRegion bulletRegion,
            float bulletHeight,
            Vector2 bulletV,
            int damage,
            float reloadInterval,
            float height,
            int hp
    ) {
        this.regions = regions;
        this.v0.set(v0);
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletV.set(bulletV);
        this.damage = damage;
        this.reloadInterval = reloadInterval;
        this.hp = hp;
        v.set(0, -0.4f);
        setHeightProportion(height);
    }

    public boolean isBulletCollision(Rect bullet) {
        return !(
                bullet.getRight() < getLeft()
                        || bullet.getLeft() > getRight()
                        || bullet.getBottom() > getTop()
                        || bullet.getTop() < pos.y
        );
    }
}
