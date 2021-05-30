package star_game.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import star_game.game.base.BaseScreen;
import star_game.game.math.Rect;
import star_game.game.sprite.Background;

public class MenuScreen extends BaseScreen {

    private Texture bg;

    Background background;

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(this);

        bg = new Texture("background.jpg");
        background = new Background(bg);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        background.draw(batch);
        batch.end();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
    }
}
