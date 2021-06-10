package star_game.game.screen;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import star_game.game.base.BaseScreen;
import star_game.game.math.Rect;
import star_game.game.pool.BulletPool;
import star_game.game.pool.EnemyShipPool;
import star_game.game.sprite.Background;
import star_game.game.sprite.Bullet;
import star_game.game.sprite.EnemyShip;
import star_game.game.sprite.PlayerShip;
import star_game.game.sprite.Star;
import star_game.game.utils.EnemyEmitter;

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

    private Sound laserSound;
    private Sound bulletSound;

    private EnemyEmitter enemyEmitter;

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
        bulletPool = new BulletPool();
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        enemyShipPool = new EnemyShipPool(worldBounds, bulletPool, bulletSound);
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        playerShip = new PlayerShip(mainAtlas, bulletPool, laserSound);
        enemyEmitter = new EnemyEmitter(worldBounds, enemyShipPool, mainAtlas);
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
        enemyEmitter.generate(delta);

        activeIsConflict();
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
        bulletSound.dispose();
    }

    private void freeAllDestroyed() {
        bulletPool.freeAllDestroyed();
        enemyShipPool.freeAllDestroyed();
    }

    private void activeIsConflict() {
        List<EnemyShip> listEnemy = new ArrayList<>(enemyShipPool.getActiveObjects());
        List<Bullet> bulletPlayer = new ArrayList<>(bulletPool.getActiveObjects());

        for (int i = 0; i < bulletPlayer.size(); i++) {
            if (!bulletPlayer.get(i).getOwner().equals(playerShip)) {
                bulletPlayer.remove(i);
                i--;
            }
        }

        for (int i = 0; i < listEnemy.size(); i++) {
            if (listEnemy.get(i).isConflict(playerShip)) {
                listEnemy.get(i).destroy();
                continue;
            }
            for (int j = 0; j < bulletPlayer.size(); j++) {
                if (listEnemy.get(i).isConflict(bulletPlayer.get(j))) {
                    listEnemy.get(i).destroy();
                    bulletPlayer.get(j).destroy();
                }
            }
        }
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
