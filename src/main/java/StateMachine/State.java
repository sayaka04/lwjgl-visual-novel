package StateMachine;

import Game.Window;

public abstract class State {

    public static State current, menu, game;

    public void init(Window window){}

    public void enter() {}

    public void update(Window window) {}

    public void exit(){}

    public void keyPressedHandler(Window window, int key, int action){}

    public void mouseClickHandler(Window window){}

}