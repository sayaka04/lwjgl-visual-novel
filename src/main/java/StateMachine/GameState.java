package StateMachine;

public class GameState extends State {

    @Override
    public void enter() {
        System.out.println("Entering GameState");
    }

    @Override
    public void update() {
        System.out.println("Updating GameState");
    }

}