package StateMachine;

import Components.*;
import Game.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class MainMenuState extends State {

    private TextRenderer myFont; // Add this!
    private Button myStartButton; // Add this!
    private Button anotherButton; // Add this!
    private Sound clickSound;

    private Texture myBackground ;
    private Renderer renderer ;

    @Override
    public void init() {
        // Init Font
        myFont = new TextRenderer("C:\\Users\\Acer\\OneDrive\\Desktop\\Jersey10-Regular.ttf", 48);

        // Init Sound...
        clickSound = new Sound("C:\\Users\\Acer\\OneDrive\\Desktop\\edited683098__florianreichelt__click.ogg");

        // Init Button: create a button at X: 500, Y: 300. Width: 200, Height: 100.
        myStartButton = new Button(500, 300, 200, 100, clickSound);
        anotherButton = new Button(0, 600, 200, 100, clickSound);

        myBackground = new Texture("C:\\Users\\Acer\\OneDrive\\Desktop\\miki.png");
        renderer = new Renderer();

    }

    @Override
    public void enter() {
        System.out.println("Entering MainMenuState");
    }

    @Override
    public void update(Window window) {

        renderer.drawImage(myBackground, 0, 0, 1920, 1080);
        renderer.drawImage(myBackground, 500, 500, 320, 320);

        if (myStartButton.isVisible) {
            myStartButton.draw(window.getMouseX(), window.getMouseY());
        }
        if (anotherButton.isVisible) {
            anotherButton.draw(window.getMouseX(), window.getMouseY());
        }



//        System.out.println("Updating MainMenuState");
        GL11.glColor3f(1.0f, 1.0f, 0.0f);                   // 1. Change color to YELLOW
        myFont.drawText("Hello Visual Novel!", 100, 100);   // 2. Draw your text
        GL11.glColor3f(1.0f, 1.0f, 1.0f);                   // 3. THE FIX: Reset the color back to WHITE immediately!
    }

    @Override
    public void exit() {
        super.exit();
    }

    @Override
    public void mouseClickHandler(Window window){
        if (myStartButton.checkClick(window.getMouseX(), window.getMouseY())) {

            System.out.println("Button Clicked! Hiding button now...");
            clickSound.play();

            State.current = State.menu;
            State.current.enter();

            // TURN THE BUTTON OFF!
            myStartButton.isVisible = false;    // This stops it from drawing AND stops the ghost clicks!
        }
        else if (anotherButton.checkClick(window.getMouseX(), window.getMouseY())) {
            System.out.println("Button Clicked! Yay so simple to use hahaha...");
            clickSound.play();
            // State.current = State.game;
            // State.current.enter();
        }
    }

    @Override
    public void keyPressedHandler(Window window, int key, int action) {
        if (key == GLFW.GLFW_KEY_SPACE && action == GLFW.GLFW_PRESS) {
            System.out.println("Spacebar pressed! Ready to advance text.");
        }
        else if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
            System.out.println("Escape pressed!");
        }
        else if (key == GLFW.GLFW_KEY_A && action == GLFW.GLFW_PRESS) {
            System.out.println("The 'A' key was pressed!");
        }
    }
}