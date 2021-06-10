package star_game.game.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import star_game.game.base.Ship;
import star_game.game.math.Rect;
import star_game.game.pool.BulletPool;


public class PlayerShip extends Ship {

    public static final float SPEED = 0.02f;
    private static final float HEIGHT = 0.15f;
    private static final float PADDING = 0.03f;
    private static final float RELOAD_INTERVAL = 0.2f;

    private final Vector2 tmp;
    private final Vector2 nextPos;

    public PlayerShip(TextureAtlas atlas, BulletPool bulletPool, Sound bulletSound) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        this.bulletPool = bulletPool;
        this.bulletRegion = atlas.findRegion("bulletMainShip");
        this.bulletSound = bulletSound;
        this.bulletV = new Vector2(0, 0.5f);
        this.bulletPos = new Vector2();
        v0 = new Vector2(0.5f, 0);
        v = new Vector2(SPEED,0);
        reloadInterval = RELOAD_INTERVAL;
        bulletHeight = 0.01f;
        damage = 1;
        hp = 100;

        tmp = new Vector2();
        nextPos = new Vector2();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        this.worldBounds = worldBounds;
        setHeightProportion(HEIGHT);
        setBottom(worldBounds.getBottom() + PADDING);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        bulletPos.set(pos.x, pos.y + getHalfHeight());
        tmp.set(nextPos.x, 0);
        if (tmp.sub(pos.x, 0).len() <= v.len()) {
            pos.set(nextPos.x, pos.y);
            v.setZero();
        } else pos.add(v);
        if (getLeft() < worldBounds.getLeft()) setLeft(worldBounds.getLeft());
        if (getRight() > worldBounds.getRight()) setRight(worldBounds.getRight());
    }

    @Override
    public boolean touchDragged(Vector2 touch, int pointer) {
        nextPos.set(touch.x, 0);
        this.v.set(nextPos.cpy().sub(pos).setLength(SPEED).x, 0);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        v.setZero();
        return false;
    }
}
