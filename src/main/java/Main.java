import Game.Window;
import StateMachine.State;
import StateMachine.GameState;
import StateMachine.MainMenuState;

public class Main {

    public static void main(String[] args) {

        State.game = new GameState();
        State.menu = new MainMenuState();
        State.current = State.menu;

        Window window = Window.get();
        window.run();
    }

}
