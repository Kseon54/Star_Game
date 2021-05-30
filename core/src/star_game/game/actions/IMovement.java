package star_game.game.actions;

import com.badlogic.gdx.math.Vector2;

public interface IMovement {

    Vector2 nextPos = new Vector2();
    Vector2 tmp = new Vector2();
    Vector2 v = new Vector2();

    void setNextPos(Vector2 nextPos);

    void move();
}
