package star_game.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureArray;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import star_game.game.base.BaseScreen;
import star_game.game.math.Rect;
import star_game.game.math.Rnd;
import star_game.game.sprite.Background;
import star_game.game.sprite.Star;

public class MenuScreen extends BaseScreen {

    private static final int COUNT_STAR = 48;

    private Texture bg;
    private TextureAtlas menuAtlas;

    private Background background;
    Star[] stars;

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(this);

        bg = new Texture("backgrounds/background.jpg");
        background = new Background(bg);
        menuAtlas = new TextureAtlas("textures/menuAtlas.tpack");
        stars = new Star[COUNT_STAR];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(menuAtlas);
        }
    }


    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw(delta);
    }

    public void update(float delta) {
        for (Star star: stars) {
            star.update(delta);
        }
    }

    public void draw(float delta) {
        ScreenUtils.clear(0.33f, 0.47f, 0.68f, 1);
        batch.begin();
        background.draw(batch);
        for (Star star: stars) {
            star.draw(batch);
        }
        batch.end();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star: stars) {
            star.resize(worldBounds);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        menuAtlas.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        return super.touchDown(touch, pointer, button);
    }
}
