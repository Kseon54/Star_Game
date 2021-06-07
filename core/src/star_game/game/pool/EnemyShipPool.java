package star_game.game.pool;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import star_game.game.base.SpritesPool;
import star_game.game.sprite.EnemyShip;

public class EnemyShipPool extends SpritesPool<EnemyShip> {

    TextureAtlas atlas;

    public EnemyShipPool(TextureAtlas atlas) {
        this.atlas = atlas;
    }

    @Override
    protected EnemyShip newObject() {
        return new EnemyShip(atlas);
    }
}
