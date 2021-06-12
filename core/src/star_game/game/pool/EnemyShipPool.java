package star_game.game.pool;

import com.badlogic.gdx.audio.Sound;

import star_game.game.base.SpritesPool;
import star_game.game.math.Rect;
import star_game.game.sprite.EnemyShip;

public class EnemyShipPool extends SpritesPool<EnemyShip> {

    private final Rect worldBounds;
    private final BulletPool bulletPool;
    private final ExplosionPool explosionPool;
    private final Sound bulletSound;

    public EnemyShipPool(Rect worldBounds,ExplosionPool explosionPool, BulletPool bulletPool, Sound bulletSound) {
        this.worldBounds = worldBounds;
        this.explosionPool = explosionPool;
        this.bulletPool = bulletPool;
        this.bulletSound = bulletSound;
    }

    @Override
    protected EnemyShip newObject() {
        return new EnemyShip(worldBounds,explosionPool, bulletPool, bulletSound);
    }
}
