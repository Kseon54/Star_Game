package star_game.game.sprite;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import star_game.game.base.ScaledButton;
import star_game.game.math.Rect;
import star_game.game.screen.GameScreen;

public class ButtonPlay extends ScaledButton {

    private static final float HEIGHT = 0.26f;
    private static final float PADDING = 0.03f;

    Game game;

    public ButtonPlay(TextureAtlas atlas, Game game) {
        super(atlas.findRegion("btPlay"));
        this.game = game;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(HEIGHT);
        setLeft(worldBounds.getLeft() + PADDING);
        setBottom(worldBounds.getBottom() + PADDING);
    }

    @Override
    protected void action() {
        game.setScreen(new GameScreen(game));
    }

}
