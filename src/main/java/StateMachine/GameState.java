package StateMachine;

import Components.*;
import Game.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class GameState extends State {

    private TextRenderer buttonTextFont; // Add this!
    private Button option1Button; // Add this!
    private Button option2Button; // Add this!
    private Button option3Button; // Add this!
    private Button option4Button; // Add this!


    private boolean tabIsVisible = false;
    private Button tabMenuButton; // Add this!
    private Button tabSaveButton; // Add this!
    private Button tabLoadButton; // Add this!
    private Button tabExitButton; // Add this!

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
        option1Button = new Button((1920/2) - 250, 250, 500, 50, hoverSound, "Option 1", buttonTextFont);
        option2Button = new Button((1920/2) - 250, 320, 500, 50, hoverSound, "Option 2", buttonTextFont);
        option3Button = new Button((1920/2) - 250, 390, 500, 50, hoverSound, "Option 3", buttonTextFont);
        option4Button = new Button((1920/2) - 250, 460, 500, 50, hoverSound, "Option 4", buttonTextFont);

        tabMenuButton = new Button(0, 0, 150, 50, hoverSound, "Menu", buttonTextFont);
        tabSaveButton = new Button(150, 0, 150, 50, hoverSound, "Save", buttonTextFont);
        tabLoadButton = new Button(300, 0, 150, 50, hoverSound, "Load", buttonTextFont);
        tabExitButton = new Button(450, 0, 150, 50, hoverSound, "Exit", buttonTextFont);

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


        if (tabIsVisible) {
            tabMenuButton.draw(window.getMouseX(), window.getMouseY());
            tabSaveButton.draw(window.getMouseX(), window.getMouseY());
            tabLoadButton.draw(window.getMouseX(), window.getMouseY());
            tabExitButton.draw(window.getMouseX(), window.getMouseY());
        }

        if (option1Button.isVisible) {
            option1Button.draw(window.getMouseX(), window.getMouseY());
        }
        if (option2Button.isVisible) {
            option2Button.draw(window.getMouseX(), window.getMouseY());
        }
        if (option3Button.isVisible) {
            option3Button.draw(window.getMouseX(), window.getMouseY());
        }
        if (option4Button.isVisible) {
            option4Button.draw(window.getMouseX(), window.getMouseY());
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

        if (tabIsVisible) {
            if (tabMenuButton.checkClick(window.getMouseX(), window.getMouseY())) {
                clickSound.play();
                backgroundMusic.stop();
                State.current = State.menu;
                State.current.enter();
            }
            else if (tabSaveButton.checkClick(window.getMouseX(), window.getMouseY())) {
                clickSound.play();
                System.out.println("Save Clicked!");
            }
            else if (tabLoadButton.checkClick(window.getMouseX(), window.getMouseY())) {
                clickSound.play();
                System.out.println("Save Clicked!");
            }
            else if (tabExitButton.checkClick(window.getMouseX(), window.getMouseY())) {
                clickSound.play();
                System.out.println("Exit pressed! Closing the game...");
                GLFW.glfwSetWindowShouldClose(window.glfwWindow, true);   // This tells the while loop to stop, safely closing the game
            }
        }


        if (option1Button.checkClick(window.getMouseX(), window.getMouseY())) {

            System.out.println("Button Clicked! Hiding button now...");
            backgroundMusic.stop();
            clickSound.play();

            State.current = State.menu;

            State.current.enter();

            // TURN THE BUTTON OFF!
//            newGameButton.isVisible = false;    // This stops it from drawing AND stops the ghost clicks!
        }
        else if (option2Button.checkClick(window.getMouseX(), window.getMouseY())) {
            System.out.println("Button Clicked! Yay so simple to use hahaha...");
            clickSound.play();
            // State.current = State.game;
            // State.current.enter();
        }
        else if (option3Button.checkClick(window.getMouseX(), window.getMouseY())) {
            System.out.println("Button Clicked! Yay so simple to use hahaha...");
            clickSound.play();
        }
        else if (option4Button.checkClick(window.getMouseX(), window.getMouseY())) {
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
            if(tabIsVisible){
                tabIsVisible = false;
            }
            else{
                tabIsVisible = true;
            }
            System.out.println("Escape pressed! toggle: " + tabIsVisible);
            dialogue.setName("ESC");
            dialogue.setDialogueText("Escape pressed");
        }
        else if (key == GLFW.GLFW_KEY_A && action == GLFW.GLFW_PRESS) {
            System.out.println("The 'A' key was pressed!");
            dialogue.setName("A");
            dialogue.setDialogueText("A");
        }
    }
}