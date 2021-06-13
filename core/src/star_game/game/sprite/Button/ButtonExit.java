package star_game.game.sprite.Button;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import star_game.game.base.ScaledButton;
import star_game.game.math.Rect;

public class ButtonExit extends ScaledButton {

    private static final float HEIGHT = 0.2f;
    private static final float PADDING = 0.03f;

    public ButtonExit(TextureAtlas atlas) {
        super(atlas.findRegion("btExit"));
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(HEIGHT);
        setBottom(worldBounds.getBottom() + PADDING);
        setRight(worldBounds.getRight() - PADDING);
    }

    @Override
    protected void actionOnClick() {
        Gdx.app.exit();
    }
}
