package star_game.game.screen;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.List;

import star_game.game.base.BaseScreen;
import star_game.game.math.Rect;
import star_game.game.pool.BulletPool;
import star_game.game.pool.EnemyShipPool;
import star_game.game.pool.ExplosionPool;
import star_game.game.sprite.Background;
import star_game.game.sprite.Bullet;
import star_game.game.sprite.ButtonNewGame;
import star_game.game.sprite.EnemyShip;
import star_game.game.sprite.GameOver;
import star_game.game.sprite.PlayerShip;
import star_game.game.sprite.Star;
import star_game.game.utils.EnemyEmitter;

public class GameScreen extends BaseScreen {

    private static final int COUNT_STAR = 24;

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


    private State state;

    public GameScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();

        bg = new Texture("backgrounds/background.jpg");
        background = new Background(bg);

        mainAtlas = new TextureAtlas("textures/mainAtlas.tpack");

        stars = new Star[COUNT_STAR];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(mainAtlas);
        }

        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        explosionPool = new ExplosionPool(mainAtlas, explosionSound);

        bulletPool = new BulletPool();
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        enemyShipPool = new EnemyShipPool(worldBounds, explosionPool, bulletPool, bulletSound);

        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        playerShip = new PlayerShip(mainAtlas, explosionPool, bulletPool, laserSound);
        enemyEmitter = new EnemyEmitter(worldBounds, enemyShipPool, mainAtlas);

        state = State.PLAYING;
        gameOver = new GameOver(mainAtlas);
        newGame = new ButtonNewGame(mainAtlas, game);
    }

    @Override
    public void render(float delta) {
        update(delta);
        activeIsConflict();
        draw();
        freeAllDestroyed();
    }

    public void update(float delta) {
        for (Star star : stars) {
            star.update(delta);
        }
        if (state == State.PLAYING) {
            playerShip.update(delta);
            bulletPool.updateActiveSprites(delta);
            enemyShipPool.updateActiveSprites(delta);
            enemyEmitter.generate(delta);
        } else {
            newGame.update(delta);
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
            explosionPool.drawActiveSprites(batch);
        } else {
            gameOver.draw(batch);
            newGame.draw(batch);
        }
        batch.end();
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
    }

    private void freeAllDestroyed() {
        bulletPool.freeAllDestroyed();
        enemyShipPool.freeAllDestroyed();
        explosionPool.freeAllDestroyed();
    }

    private void activeIsConflict() {
        List<EnemyShip> listEnemy = enemyShipPool.getActiveObjects();
        List<Bullet> bullets = bulletPool.getActiveObjects();

        for (int i = 0; i < listEnemy.size(); i++) {
            if (!listEnemy.get(i).isOutside(playerShip)) {
                playerShip.damage(listEnemy.get(i).getHp() / 2);
                listEnemy.get(i).destroy();
                continue;
            }
            for (int j = 0; j < bullets.size(); j++) {
                if (!listEnemy.get(i).isOutside(bullets.get(j)) &&
                        bullets.get(j).getOwner().equals(playerShip)) {
                    listEnemy.get(i).damage(playerShip.getDamage());
                    bullets.get(j).destroy();
                }
                if (!playerShip.isOutside(bullets.get(j))) {
                    playerShip.damage(bullets.get(j).getDamage());
                    bullets.get(j).destroy();
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
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        newGame.touchDown(touch, pointer, button);
        return false;
    }
}
