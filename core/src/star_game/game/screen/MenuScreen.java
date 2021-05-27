package star_game.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import star_game.game.base.BaseScreen;

public class MenuScreen extends BaseScreen {

    private Texture img;
    private TextureRegion backgroundTexture;
    private Vector2 pos;
    private Vector2 v;
    private Vector2 posNext;

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(this);

        img = new Texture("badlogic.jpg");
        backgroundTexture = new TextureRegion(
                new Texture("background.jpg")
                , 0, 0, 1920, 1180
        );

        pos = new Vector2();
        posNext = new Vector2();
        v = new Vector2();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (Math.abs(pos.x - posNext.x) > 3 || Math.abs(pos.y - posNext.y) > 3) pos.add(v);

        ScreenUtils.clear(1, 0, 0, 1);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0);
        batch.draw(img, pos.x, pos.y);
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        posNext.set(screenX - img.getWidth()/2, Gdx.graphics.getHeight() - screenY - img.getHeight()/2);
        v.set(posNext.cpy().sub(pos).nor());
        return false;
    }
}
