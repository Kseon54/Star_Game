package star_game.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;

import star_game.game.screen.MenuScreen;

public class StarGame extends Game {


    @Override
    public void create() {
        setScreen(new MenuScreen());
    }

}
