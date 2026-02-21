package StateMachine;

import Components.*;
import Game.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class GameState extends State {

    private TextRenderer buttonTextFont; // Add this!
    private Button newGameButton; // Add this!
    private Button loadGameButton; // Add this!
    private Button settingButton; // Add this!
    private Button exitGameButton; // Add this!

    private BackgroundMusic backgroundMusic;
    private Sound hoverSound;
    private Sound clickSound;

    private Texture background;
    private Texture character;

    private Dialogue dialogue;

    private Renderer renderer ;

    @Override
    public void init(Window window) {
        
        // Init Font
        buttonTextFont = new TextRenderer("C:\\Users\\Acer\\OneDrive\\Desktop\\Jersey10-Regular.ttf", 48);

        // Init Sound...
        backgroundMusic = new BackgroundMusic("C:\\Users\\Acer\\OneDrive\\Desktop\\bgm\\hope.ogg");
        hoverSound = new Sound("C:\\Users\\Acer\\OneDrive\\Desktop\\bertsz__game-ui-sounds.ogg");
        clickSound = new Sound("C:\\Users\\Acer\\OneDrive\\Desktop\\edited683098__florianreichelt__click.ogg");

        // Init Button: create a button at X: 500, Y: 300. Width: 200, Heisght: 100.
        newGameButton = new Button((1920/2) - 250, 250, 500, 50, hoverSound, "Option 1", buttonTextFont);
        loadGameButton = new Button((1920/2) - 250, 320, 500, 50, hoverSound, "Option 2", buttonTextFont);
        settingButton = new Button((1920/2) - 250, 390, 500, 50, hoverSound, "Option 3", buttonTextFont);
        exitGameButton = new Button((1920/2) - 250, 460, 500, 50, hoverSound, "Option 4", buttonTextFont);

        dialogue = new Dialogue(0, 780, 1920, 300, hoverSound, "C:\\Users\\Acer\\OneDrive\\Desktop\\Jersey10-Regular.ttf", 48);

        background = new Texture("C:\\Users\\Acer\\OneDrive\\Desktop\\lounge.jpg");
        character = new Texture("C:\\Users\\Acer\\OneDrive\\Desktop\\miki.png");

        renderer = new Renderer();

    }

    @Override
    public void enter() {
        backgroundMusic.play();
        System.out.println("Entering MainMenuState");
    }

    @Override
    public void update(Window window) {

        renderer.drawImage(background, 0, 0, 1920, 1080);
        renderer.drawImage(character, 0, 0, 1920, 1080);


        if (newGameButton.isVisible) {
            newGameButton.draw(window.getMouseX(), window.getMouseY());
        }
        if (loadGameButton.isVisible) {
            loadGameButton.draw(window.getMouseX(), window.getMouseY());
        }
        if (settingButton.isVisible) {
            settingButton.draw(window.getMouseX(), window.getMouseY());
        }
        if (exitGameButton.isVisible) {
            exitGameButton.draw(window.getMouseX(), window.getMouseY());
        }

        if (dialogue.isVisible) {
            dialogue.draw(window.getMouseX(), window.getMouseY());
        }
        //Assures that whats painte
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
    }

    @Override
    public void exit() {
        super.exit();
    }

    @Override
    public void mouseClickHandler(Window window){
        if (newGameButton.checkClick(window.getMouseX(), window.getMouseY())) {

            System.out.println("Button Clicked! Hiding button now...");
            backgroundMusic.stop();
            clickSound.play();

            State.current = State.menu;

            State.current.enter();

            // TURN THE BUTTON OFF!
//            newGameButton.isVisible = false;    // This stops it from drawing AND stops the ghost clicks!
        }
        else if (loadGameButton.checkClick(window.getMouseX(), window.getMouseY())) {
            System.out.println("Button Clicked! Yay so simple to use hahaha...");
            clickSound.play();
            // State.current = State.game;
            // State.current.enter();
        }
        else if (settingButton.checkClick(window.getMouseX(), window.getMouseY())) {
            System.out.println("Button Clicked! Yay so simple to use hahaha...");
            clickSound.play();
        }
        else if (exitGameButton.checkClick(window.getMouseX(), window.getMouseY())) {
            System.out.println("Button Clicked! Yay so simple to use hahaha...");
            clickSound.play();
        }
    }

    @Override
    public void keyPressedHandler(Window window, int key, int action) {
        if (key == GLFW.GLFW_KEY_SPACE && action == GLFW.GLFW_PRESS) {
            System.out.println("Spacebar pressed! Ready to advance text.");
            dialogue.setName("Spacebar");
            dialogue.setDialogueText("Spacebar");
        }
        else if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
            System.out.println("Escape pressed!");
            dialogue.setName("ESC");
            dialogue.setDialogueText("ESC");
        }
        else if (key == GLFW.GLFW_KEY_A && action == GLFW.GLFW_PRESS) {
            System.out.println("The 'A' key was pressed!");
            dialogue.setName("A");
            dialogue.setDialogueText("A");
        }
    }
}