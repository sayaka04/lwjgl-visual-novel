package StateMachine;

import Components.*;
import Engine.EngineCore;
import Game.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class MainMenuState extends State {

    private TextRenderer titleTextFont; // Add this!
    private TextRenderer buttonTextFont; // Add this!

    private Button newGameButton; // Add this!
    private Button loadGameButton; // Add this!
    private Button settingButton; // Add this!
    private Button exitGameButton; // Add this!

    private BackgroundMusic backgroundMusic;
    private Sound hoverSound;
    private Sound clickSound;

    private Texture background;

    private Renderer renderer ;

    @Override
    public void init(Window window) {
        // Init Font
        titleTextFont = new TextRenderer("assets/ui/font.ttf", 120);
        buttonTextFont = new TextRenderer("assets/ui/font.ttf", 48);

        // Init Sound...
        backgroundMusic = new BackgroundMusic("assets/ui/bgm.ogg");
        hoverSound = new Sound("assets/ui/hover.ogg");
        clickSound = new Sound("assets/ui/click.ogg");

        float [] rgba = {0.0f, 0.0f, 0.25f, 0.65f};

        // Init Button: create a button at X: 500, Y: 300. Width: 200, Height: 100.
        newGameButton = new Button((1920/2) - 150, 320, 300, 50, hoverSound, "New Game", buttonTextFont, rgba);
        loadGameButton = new Button((1920/2) - 150, 390, 300, 50, hoverSound, "Load", buttonTextFont, rgba);
        settingButton = new Button((1920/2) - 150, 460, 300, 50, hoverSound, "Settings (Unused)", buttonTextFont, rgba);
        exitGameButton = new Button((1920/2) - 150, 530, 300, 50, hoverSound, "Exit", buttonTextFont, rgba);

        background = new Texture("assets/ui/bg_main.jpg");

        renderer = new Renderer();

    }

    @Override
    public void enter(Window window) {
        System.out.println("Entering MainMenuState");
        backgroundMusic.play();

    }

    @Override
    public void update(Window window) {

        renderer.drawImage(background, 0, 0, 1920, 1080);

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

        GL11.glColor3f(0.0f, 0.0f, 0.25f);                   // 1. Change color to BLACK
        titleTextFont.drawText("A Certain Visual Novel", (1920/2)-400, 20);   // 2. Draw your text
        GL11.glColor3f(1.0f, 1.0f, 1.0f);                   // 3. THE FIX: Reset the color back to WHITE immediately!
    }

    @Override
    public void exit() {
        backgroundMusic.stop();
    }

    @Override
    public void mouseClickHandler(Window window){
        if (newGameButton.checkClick(window.getMouseX(), window.getMouseY())) {
            System.out.println("New Game Clicked! Starting new game...");
            backgroundMusic.stop();
            clickSound.play();
            // 1. Read the JSON file and convert it directly into our Java Object
            EngineCore.startNewGame();
            State.current = State.game;
            State.current.enter(window);
        }
        else if (loadGameButton.checkClick(window.getMouseX(), window.getMouseY())) {
            System.out.println("Load Clicked!");
            State.current = State.menu_load;
            State.menu_load.enter(window);
            clickSound.play();
        }
        else if (settingButton.checkClick(window.getMouseX(), window.getMouseY())) {
            System.out.println("Button Clicked! Yay so simple to use hahaha...");
            clickSound.play();
        }
        else if (exitGameButton.checkClick(window.getMouseX(), window.getMouseY())) {
            clickSound.play();
            System.out.println("Exit clicked! Closing the game...");
            GLFW.glfwSetWindowShouldClose(window.glfwWindow, true);   // This tells the while loop to stop, safely closing the game
        }
    }

    @Override
    public void keyPressedHandler(Window window, int key, int action) {
        if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
            System.out.println("Escape pressed! Closing the game...");
            GLFW.glfwSetWindowShouldClose(window.glfwWindow, true);   // This tells the while loop to stop, safely closing the game
        }
    }
}