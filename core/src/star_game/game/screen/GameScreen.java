package star_game.game.screen;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import star_game.game.base.BaseScreen;
import star_game.game.math.Rect;
import star_game.game.math.Rnd;
import star_game.game.pool.BulletPool;
import star_game.game.pool.EnemyShipPool;
import star_game.game.sprite.Background;
import star_game.game.sprite.EnemyShip;
import star_game.game.sprite.PlayerShip;
import star_game.game.sprite.Star;
import star_game.game.utils.Timer;

public class GameScreen extends BaseScreen {

    private static final int COUNT_STAR = 24;

    private Game game;

    private Texture bg;
    private TextureAtlas mainAtlas;

    private Background background;
    private Star[] stars;
    private PlayerShip playerShip;
    private BulletPool bulletPool;
    private EnemyShipPool enemyShipPool;

    private Sound sound;

    private Timer timerAddEnemyShip;

    Rect worldBounds;

    public GameScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(this);

        bg = new Texture("backgrounds/background.jpg");
        background = new Background(bg);
        mainAtlas = new TextureAtlas("textures/mainAtlas.tpack");
        stars = new Star[COUNT_STAR];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(mainAtlas);
        }
        bulletPool = new BulletPool();
        enemyShipPool = new EnemyShipPool(mainAtlas);
        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        playerShip = new PlayerShip(mainAtlas, bulletPool, sound);

        timerAddEnemyShip = new Timer(1);

        worldBounds = this.getWorldBounds();
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
        freeAllDestroyed();
    }

    public void update(float delta) {
        for (Star star : stars) {
            star.update(delta);
        }
        playerShip.update(delta);
        bulletPool.updateActiveSprites(delta);
        enemyShipPool.updateActiveSprites(delta);
        if (timerAddEnemyShip.isItTime()) addEnemyShip();
    }

    public void draw() {
        ScreenUtils.clear(0.33f, 0.47f, 0.68f, 1);
        batch.begin();
        background.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        playerShip.draw(batch);
        bulletPool.drawActiveSprites(batch);
        enemyShipPool.drawActiveSprites(batch);
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
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        mainAtlas.dispose();
        bulletPool.dispose();
        playerShip.destroy();
        enemyShipPool.dispose();
    }

    public void addEnemyShip() {

        EnemyShip enemyShip = enemyShipPool.obtain();
        Vector2 enemyShipPos = new Vector2().set(
                Rnd.nextFloat(worldBounds.getLeft(),
                        worldBounds.getRight()),
                0.6f);
        enemyShip.set(enemyShipPos, new Vector2(0, -0.1f), worldBounds, 1, 0.2f);
    }

    private void freeAllDestroyed() {
        bulletPool.freeAllDestroyed();
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        playerShip.touchUp(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchDragged(Vector2 touch, int pointer) {
        playerShip.touchDragged(touch, pointer);
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        return false;
    }
}
