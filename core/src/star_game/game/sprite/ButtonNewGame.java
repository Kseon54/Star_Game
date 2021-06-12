package star_game.game.sprite;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import star_game.game.base.Button;
import star_game.game.math.Rect;
import star_game.game.screen.GameScreen;

public class ButtonNewGame extends Button {

    private static final float HEIGHT = 0.06f;
    private static final float MAX_HEIGHT = 0.07f;
    private static final float UPDATE_HEIGHT = 0.0002f;

    private float height;
    private boolean isMaxHeight;
    private float updateHeight;

    Game game;

    public ButtonNewGame(TextureAtlas atlas, Game game) {
        super(atlas.findRegion("button_new_game"));
        this.game = game;
        height = HEIGHT;
        updateHeight = UPDATE_HEIGHT;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(height);
        setTop(worldBounds.pos.y - 0.2f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (!isMaxHeight && height >= MAX_HEIGHT) {
            isMaxHeight = true;
            updateHeight *= -1;
        }
        if (isMaxHeight && height <= HEIGHT) {
            isMaxHeight = false;
            updateHeight *= -1;
        }
        height += updateHeight;
        setHeightProportion(height);
    }

    @Override
    protected void action() {
        game.setScreen(new GameScreen(game));
    }
}
