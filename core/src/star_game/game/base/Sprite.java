package star_game.game.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import star_game.game.math.Rect;
import star_game.game.utils.Regions;


public class Sprite extends Rect {

    protected float angle;
    protected float scale = 1;
    protected TextureRegion[] regions;
    protected int frame;
    protected boolean destroyed;

    public Sprite() {
    }

    public Sprite(TextureRegion region) {
        regions = new TextureRegion[1];
        regions[0] = region;
    }

    public Sprite(TextureRegion region, int rows, int cols, int frames) {
        regions = Regions.split(region, rows, cols, frames);
    }

    /**
     * Установка размера изображения по высоте
     *
     * @param height высота
     */
    public void setHeightProportion(float height) {
        setHeight(height);
        float aspect = regions[frame].getRegionWidth() / (float) regions[frame].getRegionHeight();
        setWidth(height * aspect);
    }

    /**
     * Логика спрайта
     *
     * @param delta отрезок времени
     */
    public void update(float delta) {

    }

    public void draw(SpriteBatch batch) {
        batch.draw(
                regions[frame],
                getLeft(), getBottom(),
                halfWidth, halfHeight,
                getWidth(), getHeight(),
                scale, scale,
                angle
        );
    }

    public void resize(Rect worldBounds) {

    }

    @Override
    public boolean isOutside(Rect sprite) {
        return (this.pos.y < sprite.getBottom() ||
                this.pos.y > sprite.getTop() ||
                this.getRight() < sprite.getLeft() ||
                this.getLeft() > sprite.getRight());
    }

    public boolean touchDown(Vector2 touch, int pointer, int button) {
        return false;
    }

    public boolean touchUp(Vector2 touch, int pointer, int button) {
        return false;
    }

    public boolean touchDragged(Vector2 touch, int pointer) {
        return false;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void destroy() {
        destroyed = true;
    }

    public void flushDestroy() {
        destroyed = false;
    }
}
