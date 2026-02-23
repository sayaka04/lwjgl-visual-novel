package StateMachine;

import Game.Window;

public abstract class State {

    public static State current, menu, game, save, menu_load, ingame_load;

    public void init(Window window){}

    public void enter(Window window) {}

    public void update(Window window) {}

    public void exit(){}

    public void keyPressedHandler(Window window, int key, int action){}

    public void mouseClickHandler(Window window){}

}