package star_game.game.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import star_game.game.base.Sprite;
import star_game.game.math.Rect;
import star_game.game.pool.BulletPool;


public class PlayerShip extends Sprite {

    public static final float SPEED = 0.02f;
    public static final float PADDING = 0.02f;

    private final Vector2 v;
    private final Vector2 tmp;
    private final Vector2 nextPos;

    Rect worldBounds;
    private BulletPool bulletPool;
    private TextureRegion bulletRegion;
    private Vector2 bulletV;
    private Vector2 bulletPos;

    public PlayerShip(TextureAtlas atlas, BulletPool bulletPool) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        this.bulletPool = bulletPool;
        this.bulletRegion = atlas.findRegion("bulletMainShip");
        this.bulletV = new Vector2(0, 0.5f);
        this.bulletPos = new Vector2();
        v = new Vector2();
        tmp = new Vector2();
        nextPos = new Vector2();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        this.worldBounds = worldBounds;
        setHeightProportion(0.2f);
        setBottom(worldBounds.getBottom() + PADDING);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        tmp.set(nextPos.x, 0);
        if (tmp.sub(pos.x, 0).len() <= v.len()) {
            pos.set(nextPos.x, pos.y);
            v.setZero();
        } else pos.add(v);
        if (getLeft() < worldBounds.getLeft()) setLeft(worldBounds.getLeft());
        if (getRight() > worldBounds.getRight()) setRight(worldBounds.getRight());

        shoot();
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

    private void shoot() {
        Bullet bullet = bulletPool.obtain();
        bulletPos.set(pos.x, pos.y + getHalfHeight());
        bullet.set(this, bulletRegion, bulletPos, bulletV, worldBounds, 1, 0.01f);
    }
}
