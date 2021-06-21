package star_game.game.screen;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.List;

import star_game.game.base.BaseScreen;
import star_game.game.base.Font;
import star_game.game.math.Rect;
import star_game.game.pool.BulletPool;
import star_game.game.pool.EnemyShipPool;
import star_game.game.pool.ExplosionPool;
import star_game.game.sprite.Background;
import star_game.game.sprite.Bullet;
import star_game.game.sprite.Button.ButtonNewGame;
import star_game.game.sprite.EnemyShip;
import star_game.game.sprite.GameOver;
import star_game.game.sprite.PlayerShip;
import star_game.game.sprite.Star;
import star_game.game.utils.EnemyEmitter;

public class GameScreen extends BaseScreen {

    private static final int COUNT_STAR = 24;

    private static final float FONT_SIZE = 0.02f;
    private static final float PADDING = 0.01f;

    private static final String FRAGS = "Frags: ";
    private static final String HP = "HP: ";
    private static final String LEVEL = "Level: ";

    private final Game game;

    private enum State {GAME_OVER, PLAYING}

    private Texture bg;
    private TextureAtlas mainAtlas;

    private Background background;
    private Star[] stars;
    private PlayerShip playerShip;
    private BulletPool bulletPool;
    private EnemyShipPool enemyShipPool;
    private ExplosionPool explosionPool;
    private GameOver gameOver;

    private ButtonNewGame newGame;

    private EnemyEmitter enemyEmitter;

    private Sound laserSound;
    private Sound bulletSound;
    private Sound explosionSound;

    private int frags;

    private Font font;
    private StringBuilder sbFrags;
    private StringBuilder sbHP;
    private StringBuilder sbLevel;

    private State state;

    public GameScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();

        bg = new Texture("backgrounds/bg.png");
        background = new Background(bg);

        mainAtlas = new TextureAtlas("textures/mainAtlas.tpack");

        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        explosionPool = new ExplosionPool(mainAtlas, explosionSound);

        bulletPool = new BulletPool();
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        enemyShipPool = new EnemyShipPool(worldBounds, explosionPool, bulletPool, bulletSound);

        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        playerShip = new PlayerShip(mainAtlas, explosionPool, bulletPool, laserSound);
        enemyEmitter = new EnemyEmitter(worldBounds, enemyShipPool, mainAtlas, playerShip);

        state = State.PLAYING;
        gameOver = new GameOver(mainAtlas);
        newGame = new ButtonNewGame(mainAtlas, this);

        stars = new Star[COUNT_STAR];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(mainAtlas);
        }

        font = new Font("font/font.fnt", "font/font.png");
        font.setSize(FONT_SIZE);
        sbFrags = new StringBuilder();
        sbHP = new StringBuilder();
        sbLevel = new StringBuilder();

        frags = 0;
    }

    public void newGame() {
        frags = 0;
        playerShip.newGame();
        explosionPool.freeAllActiveObjects();
        bulletPool.freeAllActiveObjects();
        enemyShipPool.freeAllActiveObjects();
        state = State.PLAYING;
    }

    @Override
    public void render(float delta) {
        update(delta);
        activeIsConflict();
        draw();
        freeAllDestroyed();
    }

    public void update(float delta) {
        if (state.equals(State.PLAYING)) {
            playerShip.update(delta);
            bulletPool.updateActiveSprites(delta);
            enemyShipPool.updateActiveSprites(delta);
            enemyEmitter.generate(delta, frags);
        } else {
            newGame.update(delta);
        }

        for (Star star : stars) {
            star.update(delta);
        }
        explosionPool.updateActiveSprites(delta);
    }

    public void draw() {
        ScreenUtils.clear(0.33f, 0.47f, 0.68f, 1);
        batch.begin();
        background.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        if (state == State.PLAYING) {
            playerShip.draw(batch);
            bulletPool.drawActiveSprites(batch);
            enemyShipPool.drawActiveSprites(batch);
        } else {
            gameOver.draw(batch);
            newGame.draw(batch);
        }
        printInfo();
        explosionPool.drawActiveSprites(batch);
        batch.end();
    }

    private void printInfo() {
        sbFrags.setLength(0);
        sbHP.setLength(0);
        sbLevel.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(frags), worldBounds.getLeft() + PADDING, worldBounds.getTop() - PADDING);
        font.draw(batch, sbHP.append(HP).append(playerShip.getHp()), worldBounds.pos.x, worldBounds.getTop() - PADDING, Align.center);
        font.draw(batch, sbLevel.append(LEVEL).append(enemyEmitter.getLevel()), worldBounds.getRight() - PADDING, worldBounds.getTop() - PADDING, Align.right);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star : stars) {
            star.resize(worldBounds);
        }
        playerShip.resize(worldBounds);
        gameOver.resize(worldBounds);
        newGame.resize(worldBounds);
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        mainAtlas.dispose();

        playerShip.destroy();

        bulletPool.dispose();
        enemyShipPool.dispose();

        bulletSound.dispose();
        laserSound.dispose();
        explosionSound.dispose();

        font.dispose();
    }

    private void freeAllDestroyed() {
        bulletPool.freeAllDestroyed();
        enemyShipPool.freeAllDestroyed();
        explosionPool.freeAllDestroyed();
    }

    private void activeIsConflict() {
        List<EnemyShip> enemyList = enemyShipPool.getActiveObjects();
        List<Bullet> bulletList = bulletPool.getActiveObjects();

        for (EnemyShip enemyShip : enemyList) {
            float minDist = enemyShip.getHalfWidth() + playerShip.getHalfWidth();
            if (enemyShip.pos.dst(playerShip.pos) < minDist) {
                playerShip.damage(enemyShip.getHp() / 2);
                enemyShip.destroy();
                frags++;
                continue;
            }
            for (Bullet bullet : bulletList) {
                if (enemyShip.isBulletCollision(bullet) &&
                        bullet.getOwner().equals(playerShip)) {
                    enemyShip.damage(playerShip.getDamage());
                    bullet.destroy();
                    if (enemyShip.isDestroyed()) {
                        frags++;
                    }
                }
                if (playerShip.isBulletCollision(bullet)
                        && !bullet.getOwner().equals(playerShip)) {
                    playerShip.damage(bullet.getDamage());
                    bullet.destroy();
                }
            }

        }
        if (playerShip.isDestroyed()) state = State.GAME_OVER;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        playerShip.touchUp(touch, pointer, button);
        newGame.touchUp(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchDragged(Vector2 touch, int pointer) {
        playerShip.touchDragged(touch, pointer);
        return false;
    }

    @Override
    public boolean mouseMoved(Vector2 touch) {
        return touchDragged(touch, 1);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        newGame.touchDown(touch, pointer, button);
        playerShip.touchDown(touch, pointer, button);
        return false;
    }
}
