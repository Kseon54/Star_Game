package star_game.game.utils;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import star_game.game.math.Rect;
import star_game.game.math.Rnd;
import star_game.game.pool.EnemyShipPool;
import star_game.game.sprite.EnemyShip;
import star_game.game.sprite.PlayerShip;

public class EnemyEmitter {

    enum Enemy {ENEMY_SMALL, ENEMY_MEDIUM, ENEMY_BIG}

    private static final float GENERATE_INTERVAL = 4f;
    private static final float MIN_GENERATE_INTERVAL = 1f;
    private static final float MIN_RELOAD_INTERVAL = 0.5f;

    private static final float ENEMY_SMALL_HEIGHT = 0.1f;
    private static final float ENEMY_SMALL_BULLET_HEIGHT = 0.01f;
    private static final Vector2 ENEMY_SMALL_BULLET_V = new Vector2(0, -0.3f);
    private static final int ENEMY_SMALL_DAMAGE = 1;
    private static final float ENEMY_SMALL_RELOAD_INTERVAL = 2f;
    private static final int ENEMY_SMALL_HP = 2;

    private static final float ENEMY_MEDIUM_HEIGHT = 0.15f;
    private static final float ENEMY_MEDIUM_BULLET_HEIGHT = 0.02f;
    private static final Vector2 ENEMY_MEDIUM_BULLET_V = new Vector2(0, -0.25f);
    private static final int ENEMY_MEDIUM_DAMAGE = 5;
    private static final float ENEMY_MEDIUM_RELOAD_INTERVAL = 3f;
    private static final int ENEMY_MEDIUM_HP = 7;

    private static final float ENEMY_BIG_HEIGHT = 0.2f;
    private static final float ENEMY_BIG_BULLET_HEIGHT = 0.04f;
    private static final Vector2 ENEMY_BIG_BULLET_V = new Vector2(0, -0.3f);
    private static final int ENEMY_BIG_DAMAGE = 10;
    private static final float ENEMY_BIG_RELOAD_INTERVAL = 4f;
    private static final int ENEMY_BIG_HP = 13;

    private static final float IS_ENEMY_SMALL = 0.5f;
    private static final float IS_ENEMY_MEDIUM = 0.8f;
    private static final float IS_ENEMY_BIG = 1.1f;

    private float generateTimer;

    private final TextureRegion[] enemySmallRegions;
    private final TextureRegion[] enemyMediumRegions;
    private final TextureRegion[] enemyBigRegions;

    private final Vector2 enemySmallV;
    private final Vector2 enemyMediumV;
    private final Vector2 enemyBigV;

    private final TextureRegion bulletRegion;

    private final Rect worldBounds;
    private final EnemyShipPool enemyPool;

    private final PlayerShip playerShip;

    private final Vector2 tmp;

    private Enemy enemySize;

    private int level = 2;

    public EnemyEmitter(Rect worldBounds, EnemyShipPool enemyPool, TextureAtlas atlas, PlayerShip playerShip) {
        this.worldBounds = worldBounds;
        this.enemyPool = enemyPool;
        this.bulletRegion = atlas.findRegion("bulletEnemy");
        enemySmallRegions = Regions.split(atlas.findRegion("enemy0"), 1, 2, 2);
        enemyMediumRegions = Regions.split(atlas.findRegion("enemy1"), 1, 2, 2);
        enemyBigRegions = Regions.split(atlas.findRegion("enemy2"), 1, 2, 2);
        enemySmallV = new Vector2(0, -0.2f);
        enemyMediumV = new Vector2(0, -0.04f);
        enemyBigV = new Vector2();

        this.playerShip = playerShip;
        tmp = new Vector2();
    }

    public void generate(float delta, int frags) {
        level = frags / 10 + 1;

        if (playerShip.isDestroyed()) return;

        generateTimer += delta;
        float generateInterval = GENERATE_INTERVAL - (level - 1) * 0.3f;
        generateInterval = Math.max(generateInterval, MIN_GENERATE_INTERVAL);
        if (generateTimer >= generateInterval) {
            generateTimer = 0f;
            float type = (float) Math.random();

            EnemyShip enemyShip = generationLevel1();

            float enemyHalfWidth = enemyShip.getHalfWidth();
            enemyShip.pos.x = Rnd.nextFloat(worldBounds.getLeft() + enemyHalfWidth, worldBounds.getRight() - enemyHalfWidth);
            enemyShip.setBottom(worldBounds.getTop());

            if (enemySize == Enemy.ENEMY_BIG)
                enemyShip.setV0((enemyShip.pos.x < 0 ? 1 : -1) * Rnd.nextFloat(0.025f, 0.055f), 0);

            if (level > 1) {
                if (enemySize.equals(Enemy.ENEMY_SMALL)) {
                    tmp.set(playerShip.pos).sub(enemyShip.pos);

                    enemyShip.setV0(tmp);
                    enemyShip.getV0().setLength(enemySmallV.len());
                    enemyShip.setBulletV(tmp);
                    enemyShip.getBulletV().setLength(ENEMY_SMALL_BULLET_V.len());
                    float angele = tmp.angleDeg() + 90;
                    enemyShip.setAngle(angele);
                }
                enemyShip.setReloadInterval(Math.max(MIN_RELOAD_INTERVAL, enemyShip.getReloadInterval() - level * 0.08f));
            }
        }
    }

    private EnemyShip generationLevel1() {
        float type = (float) Math.random();
//        float type = 0.3f;
        if (type < IS_ENEMY_SMALL) {
            enemySize = Enemy.ENEMY_SMALL;
            return getEnemySmall();
        }
        if (type < IS_ENEMY_MEDIUM) {
            enemySize = Enemy.ENEMY_MEDIUM;
            return getEnemyMedium();
        }
        if (type < IS_ENEMY_BIG) {
            enemySize = Enemy.ENEMY_BIG;
            return getEnemyBig();
        }
        return getEnemyBig();
    }

    public EnemyShip getEnemyBig() {
        EnemyShip enemyShip = enemyPool.obtain();
        enemyShip.clear();
        enemyShip.set(
                enemyBigRegions,
                enemyBigV,
                bulletRegion,
                ENEMY_BIG_BULLET_HEIGHT,
                ENEMY_BIG_BULLET_V,
                ENEMY_BIG_DAMAGE,
                ENEMY_BIG_RELOAD_INTERVAL,
                ENEMY_BIG_HEIGHT,
                ENEMY_BIG_HP
        );
        return enemyShip;
    }

    public EnemyShip getEnemyMedium() {
        EnemyShip enemyShip = enemyPool.obtain();
        enemyShip.clear();
        enemyShip.set(
                enemyMediumRegions,
                enemyMediumV,
                bulletRegion,
                ENEMY_MEDIUM_BULLET_HEIGHT,
                ENEMY_MEDIUM_BULLET_V,
                ENEMY_MEDIUM_DAMAGE,
                ENEMY_MEDIUM_RELOAD_INTERVAL,
                ENEMY_MEDIUM_HEIGHT,
                ENEMY_MEDIUM_HP
        );
        return enemyShip;
    }

    public EnemyShip getEnemySmall() {
        EnemyShip enemyShip = enemyPool.obtain();
        enemyShip.clear();
        enemyShip.set(
                enemySmallRegions,
                enemySmallV,
                bulletRegion,
                ENEMY_SMALL_BULLET_HEIGHT,
                ENEMY_SMALL_BULLET_V,
                ENEMY_SMALL_DAMAGE,
                ENEMY_SMALL_RELOAD_INTERVAL,
                ENEMY_SMALL_HEIGHT,
                ENEMY_SMALL_HP
        );
        return enemyShip;
    }

    public int getLevel() {
        return level;
    }
}
