package star_game.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import star_game.game.base.BaseScreen;
import star_game.game.math.Rect;
import star_game.game.sprite.Background;
import star_game.game.sprite.Logo;

public class MenuScreen extends BaseScreen {

    private Texture bg;
    private Texture logoImg;

    private Background background;
    private Logo logo;

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(this);

        bg = new Texture("background.jpg");
        background = new Background(bg);

        logoImg = new Texture("badlogic.jpg");
        logo = new Logo(logoImg);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        background.draw(batch);
        logo.draw(batch);
        batch.end();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        logo.resize(worldBounds);
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        logoImg.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        logo.setNextPos(touch);
        return super.touchDown(touch, pointer, button);
    }
}
