package star_game.game.pool;

import star_game.game.base.SpritesPool;
import star_game.game.sprite.Bullet;

public class BulletPool extends SpritesPool<Bullet> {

    @Override
    protected Bullet newObject() {
        return new Bullet();
    }
}
