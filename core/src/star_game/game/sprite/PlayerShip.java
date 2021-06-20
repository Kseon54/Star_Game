package star_game.game.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import star_game.game.base.Ship;
import star_game.game.math.Rect;
import star_game.game.pool.BulletPool;
import star_game.game.pool.ExplosionPool;


public class PlayerShip extends Ship {

    public static final float SPEED = 0.5f;
    private static final float HEIGHT = 0.15f;
    private static final float PADDING = 0.03f;
    private static final float RELOAD_INTERVAL = 0.2f;
    private static final int HP = 100;

    private final Vector2 nextPos;

    public PlayerShip(TextureAtlas atlas, ExplosionPool explosionPool, BulletPool bulletPool, Sound bulletSound) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        this.bulletPool = bulletPool;
        this.bulletRegion = atlas.findRegion("bulletMainShip");
        this.bulletSound = bulletSound;
        this.bulletV.set(0, 0.5f);

        this.explosionPool = explosionPool;

        v0.set(SPEED, 0);
        reloadInterval = RELOAD_INTERVAL;
        bulletHeight = 0.01f;
        damage = 0;
        hp = HP;

        nextPos = new Vector2();
    }

    public void newGame() {
        this.hp = HP;
        this.pos.x = worldBounds.pos.x;
        stop();
        flushDestroy();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        this.worldBounds = worldBounds;
        setHeightProportion(HEIGHT);
        setBottom(worldBounds.getBottom() + PADDING);
        nextPos.set(pos);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        move(delta);

        if (getLeft() < worldBounds.getLeft()) setLeft(worldBounds.getLeft());
        if (getRight() > worldBounds.getRight()) setRight(worldBounds.getRight());
    }

    @Override
    public void calculateBullet() {
        bulletPos.set(pos.x, getTop()).rotateAroundDeg(pos,angle);
    }

    public boolean isBulletCollision(Rect bullet) {
        return !(
                bullet.getRight() < getLeft()
                        || bullet.getLeft() > getRight()
                        || bullet.getBottom() > pos.y
                        || bullet.getTop() < getBottom()
        );
    }

    @Override
    public boolean touchDragged(Vector2 touch, int pointer) {
        nextPos.x = touch.x;
        if (pos.x < touch.x) moveRight();
        if (pos.x > touch.x) moveLeft();
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        return touchDragged(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        stop();
        return false;
    }

    private void move(float delta) {
        tmp.set(nextPos);
        if ((tmp.sub(pos).len() <= 0.01f)) {
            stop();
            pos.set(nextPos);
        }
    }

    private void moveRight() {
        v.set(v0);
    }

    private void moveLeft() {
        v.set(v0).rotateDeg(180);
    }
}
