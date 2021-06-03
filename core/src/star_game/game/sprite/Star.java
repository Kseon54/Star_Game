package star_game.game.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import star_game.game.base.Sprite;
import star_game.game.math.Rect;
import star_game.game.math.Rnd;

public class Star extends Sprite {

    private final Vector2 v;
    private Rect worldBounds;

    public Star(TextureAtlas atlas) {
        super(atlas.findRegion("star"));
        this.v = new Vector2();
        speedChange();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(v,delta);
        if (getRight() < worldBounds.getLeft()) {
            setLeft(worldBounds.getRight());
            RandomParameters();
        }
        if (getLeft() >  worldBounds.getRight()) {
            setRight(worldBounds.getLeft());
            RandomParameters();
        }
        if (getTop() < worldBounds.getBottom()) {
            setBottom(worldBounds.getTop());
            RandomParameters();
        }
        if (getBottom() > worldBounds.getTop()) {
            setTop(worldBounds.getBottom());
            RandomParameters();
        }
        float height = getHeight();
        height += 0.0001f;
        if (height >= 0.012f) {
            height = 0.008f;
        }
        setHeightProportion(height);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        this.worldBounds = worldBounds;
        setHeightProportion(Rnd.nextFloat(0.005f,0.014f));
        posChange();
    }

    private void speedChange(){
        float vx = Rnd.nextFloat(-0.005f,0.005f);
        float vy = Rnd.nextFloat(-0.1f,-0.05f);
        v.set(vx,vy);
    }
    private void posChange(){
        float x = Rnd.nextFloat(worldBounds.getLeft(),worldBounds.getRight());
        float y = Rnd.nextFloat(worldBounds.getBottom(),worldBounds.getTop());
        pos.set(x,y);
    }

    private void RandomParameters(){
        speedChange();
        posChange();
        setHeightProportion(Rnd.nextFloat(0.005f,0.014f));
    }
}
