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

    protected Rect worldBounds;
    protected BulletPool bulletPool;
    protected TextureRegion bulletRegion;
    protected Vector2 bulletV;
    protected Vector2 bulletPos;
    protected Sound bulletSound;
    protected float bulletHeight;
    protected int damage;
    protected int hp;

    protected ExplosionPool explosionPool;

    protected float reloadInterval;
    protected float reloadTimer;

    protected float damageAnimateTimer = DAMAGE_ANIMATE_TIMER;

    public Ship() {
    }

    public Ship(TextureRegion region, int rows, int cols, int frames) {
        super(region, rows, cols, frames);
        v = new Vector2();
        v0 = new Vector2();
        bulletV = new Vector2();
        bulletPos = new Vector2();
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        reloadTimer += delta;
        if (reloadTimer >= reloadInterval) {
            reloadTimer = 0;
            shoot();
        }

        damageAnimateTimer += delta;
        if (damageAnimateTimer >= DAMAGE_ANIMATE_TIMER) frame = 0;
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

    public int getDamage() {
        return damage;
    }

    public int getHp() {
        return hp;
    }

    public void setBulletPos(Vector2 bulletPos) {
        this.bulletPos.set(bulletPos);
    }

    public Vector2 getV() {
        return v;
    }
}
