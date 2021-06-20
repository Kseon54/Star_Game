package star_game.game.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import star_game.game.math.Rect;
import star_game.game.pool.BulletPool;
import star_game.game.pool.ExplosionPool;
import star_game.game.sprite.Bullet;
import star_game.game.sprite.Explosion;

public class Ship extends Sprite {

    public static final float DAMAGE_ANIMATE_TIMER = 0.1f;
    protected Vector2 v0;
    protected Vector2 v;
    protected final Vector2 tmp;

    protected Rect worldBounds;
    protected BulletPool bulletPool;
    protected TextureRegion bulletRegion;
    protected Vector2 bulletV;
    protected Vector2 bulletPos;
    protected Sound bulletSound;
    protected float bulletHeight;
    protected int damage;
    protected int hp;

    private Vector2 purpose;

    protected ExplosionPool explosionPool;

    protected float reloadInterval;
    protected float reloadTimer;

    protected float damageAnimateTimer = DAMAGE_ANIMATE_TIMER;

    public Ship() {
        tmp = new Vector2();
    }

    public Ship(TextureRegion region, int rows, int cols, int frames) {
        super(region, rows, cols, frames);
        v = new Vector2();
        v0 = new Vector2();
        bulletV = new Vector2();
        bulletPos = new Vector2();
        tmp = new Vector2();
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        reloadTimer += delta;
        if (reloadTimer >= reloadInterval && !isDestroyed()) {
            reloadTimer = 0;
            calculateBullet();
            shoot();
        }

        damageAnimateTimer += delta;
        if (damageAnimateTimer >= DAMAGE_ANIMATE_TIMER) frame = 0;
    }

    public void calculateBullet() {
        bulletPos.set(pos.x, getBottom()).rotateAroundDeg(pos, angle);
        if (purpose != null) {
            float angele = tmp.set(purpose).sub(bulletPos).angleDeg();
            bulletV.setAngleDeg(angele);
        }
    }

    public void damage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            hp = 0;
            destroy();
        }
        frame = 1;
        damageAnimateTimer = 0;
    }

    private void shoot() {
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, bulletPos, bulletV, worldBounds, damage, bulletHeight);
        bulletSound.play();
    }

    @Override
    public void destroy() {
        super.destroy();
        boom();
    }

    private void boom() {
        Explosion explosion = explosionPool.obtain();
        explosion.set(pos, getHeight());
    }

    public void stop() {
        v.setZero();
    }

    public int getDamage() {
        return damage;
    }

    public int getHp() {
        return hp;
    }

    public void setBulletPos(Vector2 bulletPos) {
        this.bulletPos.set(bulletPos);
    }

    public void setBulletPos(float x, float y) {
        this.bulletPos.set(x, y);
    }

    public Vector2 getV() {
        return v;
    }

    public void setV(Vector2 v) {
        this.v.set(v);
    }

    public void setV(float x, float y) {
        v.x = x;
        v.y = y;
    }

    public Vector2 getBulletV() {
        return bulletV;
    }

    public void setBulletV(Vector2 bulletV) {
        this.bulletV.set(bulletV);
    }

    public Vector2 getV0() {
        return v0;
    }

    public void setV0(Vector2 v0) {
        this.v0.set(v0);
    }

    public void setV0(float x, float y) {
        v0.x = x;
        v0.y = y;
    }

    public float getReloadInterval() {
        return reloadInterval;
    }

    public void setReloadInterval(float reloadInterval) {
        this.reloadInterval = reloadInterval;
    }

    public Vector2 getPurpose() {
        return purpose;
    }

    public void setPurpose(Vector2 purpose) {
        this.purpose = purpose;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
