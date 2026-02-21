package StateMachine;

import Game.Window;

public class GameState extends State {

    @Override
    public void enter() {
        System.out.println("Entering GameState");
    }

    @Override
    public void update(Window window) {
        System.out.println("Updating GameState");
    }

}