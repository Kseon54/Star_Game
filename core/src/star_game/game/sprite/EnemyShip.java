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

    private static final float MIN_SPEED = 0.3f;

    private float angeleStart = 0;

    public EnemyShip(Rect worldBounds, ExplosionPool explosionPool, BulletPool bulletPool, Sound bulletSound) {
        this.worldBounds = worldBounds;
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.bulletSound = bulletSound;
        v0 = new Vector2();
        v = new Vector2();
        this.bulletV = new Vector2();
        this.bulletPos = new Vector2();
        angle = 0;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (getTop() + PADDING < worldBounds.getTop() || angle != 0 && v.len() >= MIN_SPEED) {
            if (isRotation()) {
                stop();
                reloadTimer = reloadInterval * 0.8f;
            } else v.set(v0);
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

    private boolean isRotation() {
        angle = angle % 360 < 0 ? angle + 360 : angle;
        if (angle == angeleStart) return false;
        if (angeleStart - 180 > 180) angle++;
        else angle--;
        if (Math.abs(angle - angeleStart) < 2)
            angle = angeleStart;
        return angle != angeleStart;
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

    public void setAngeleStart(float angeleStart) {
        this.angeleStart = angeleStart;
    }

    @Override
    public void setAngle(float angle) {
        this.angle = angle;
        angeleStart = angle;
    }

    public void clear() {
        angle = 0;
        v.setZero();
        v0.setZero();
        bulletV.setZero();
        bulletPos.setZero();
        angeleStart = 0;
    }
}
