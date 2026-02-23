package StateMachine;

import Components.*;
import Data.SaveData;
import Engine.EngineCore;
import Game.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.io.File; // Needed to check for images!

public class MenuLoadState extends State {

    private TextRenderer titleTextFont;
    private TextRenderer saveButtonTextFont;
    private TextRenderer tabReturnButtonTextFont;

    private Button tabReturnButton;

    // Renamed to strictly 1 through 6
    private Button saveStateButton1;
    private Button saveStateButton2;
    private Button saveStateButton3;
    private Button saveStateButton4;
    private Button saveStateButton5;
    private Button saveStateButton6;

    private BackgroundMusic backgroundMusic;
    private Sound hoverSound;
    private Sound clickSound;

    private Texture background;
    private Texture pictureFrame;

    // Array is now size 7! We will ignore index 0, so Slot 1 = slotImages[1]
    private Texture[] slotImages = new Texture[7];

    private Renderer renderer ;

    int x1 = (((1920/2)-(480/2)) / 2) - (480/2),
            x2 = (1920/2)-(480/2),
            x3 = ((1920/2)+((1920/2) / 2)) - (270/2);

    int y1 = ((1080/2) / 2) - (270/2) + (270/2),
            y2 = ((1080/2)+((1080/2) / 2)) - (270/2);

    @Override
    public void init(Window window) {
        // Init Font
        titleTextFont = new TextRenderer("assets/ui/font.ttf", 120);
        saveButtonTextFont = new TextRenderer("assets/ui/font.ttf", 70);
        tabReturnButtonTextFont = new TextRenderer("assets/ui/font.ttf", 48);

        // Init Sound...
        backgroundMusic = new BackgroundMusic("assets/ui/bgm.ogg");
        hoverSound = new Sound("assets/ui/hover.ogg");
        clickSound = new Sound("assets/ui/click.ogg");

        // Init Button
        float [] rgba = {0.0f, 0.0f, 0.25f, 0.0f};
        saveStateButton1 = new Button(x1, y1, 480, 270, hoverSound, "Load State 1", saveButtonTextFont, rgba);
        saveStateButton2 = new Button(x2, y1, 480, 270, hoverSound, "Load State 2", saveButtonTextFont, rgba);
        saveStateButton3 = new Button(x3, y1, 480, 270, hoverSound, "Load State 3", saveButtonTextFont, rgba);
        saveStateButton4 = new Button(x1, y2, 480, 270, hoverSound, "Load State 4", saveButtonTextFont, rgba);
        saveStateButton5 = new Button(x2, y2, 480, 270, hoverSound, "Load State 5", saveButtonTextFont, rgba);
        saveStateButton6 = new Button(x3, y2, 480, 270, hoverSound, "Load State 6", saveButtonTextFont, rgba);

        float [] rgba2 = {0.0f, 0.0f, 0.25f, 0.65f};
        tabReturnButton = new Button(0, 0, 150, 50, hoverSound, "Return", tabReturnButtonTextFont, rgba2);

        background = new Texture("assets/ui/bg_load.jpg");
        pictureFrame = new Texture("assets/ui/frame.png");

        renderer = new Renderer();
    }

    @Override
    public void enter(Window window) {
        System.out.println("Entering MenuLoadState - Loading slot images...");

        // Loop from 1 to 6 to check for images!
        for (int i = 1; i <= 6; i++) {
            File savedImage = new File("assets/save/save_slot_" + i + ".png");

            if (savedImage.exists()) {
                slotImages[i] = new Texture("assets/save/save_slot_" + i + ".png");
            } else {
                slotImages[i] = new Texture("assets/ui/default_img.png");
            }
        }
    }

    @Override
    public void update(Window window) {

        renderer.drawImage(background, 0, 0, 1920, 1080);
        tabReturnButton.draw(window.getMouseX(), window.getMouseY());

        // Draw slots 1 through 6
        // slot 1
        renderer.drawImage(slotImages[1], x1, y1, 480, 270);
        saveStateButton1.draw(window.getMouseX(), window.getMouseY());
        renderer.drawImage(pictureFrame, x1-25, y1-25, 524, 320);

        // slot 2
        renderer.drawImage(slotImages[2], x2, y1, 480, 270);
        saveStateButton2.draw(window.getMouseX(), window.getMouseY());
        renderer.drawImage(pictureFrame, x2-25, y1-25, 524, 320);

        // slot 3
        renderer.drawImage(slotImages[3], x3, y1, 480, 270);
        saveStateButton3.draw(window.getMouseX(), window.getMouseY());
        renderer.drawImage(pictureFrame, x3-25, y1-25, 524, 320);

        // slot 4
        renderer.drawImage(slotImages[4], x1, y2, 480, 270);
        saveStateButton4.draw(window.getMouseX(), window.getMouseY());
        renderer.drawImage(pictureFrame, x1-25, y2-25, 524, 320);

        // slot 5
        renderer.drawImage(slotImages[5], x2, y2, 480, 270);
        saveStateButton5.draw(window.getMouseX(), window.getMouseY());
        renderer.drawImage(pictureFrame, x2-25, y2-25, 524, 320);

        // slot 6
        renderer.drawImage(slotImages[6], x3, y2, 480, 270);
        saveStateButton6.draw(window.getMouseX(), window.getMouseY());
        renderer.drawImage(pictureFrame, x3-25, y2-25, 524, 320);

        GL11.glColor3f(0.0f, 0.0f, 0.25f);
        titleTextFont.drawText("Load Game State", (1920/2)-330, 20);
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
    }

    // --- 3. THE UNIVERSAL LOAD HELPER FOR THE MENU ---
    private void loadGameFromSlot(int slotIndex, Window window) {
        String slotName = "save_slot_" + slotIndex;
        System.out.println("Loading game from " + slotName + "...");

        try {
            // 1. Read the JSON file from the assets/save/ folder
            java.io.FileReader reader = new java.io.FileReader("assets/save/" + slotName + ".json");
            SaveData save = new com.google.gson.Gson().fromJson(reader, SaveData.class);
            reader.close();

            // 2. Load Chapter & Node into the Engine
            EngineCore.loadChapter(save.chapterFile);
            EngineCore.currentChapter.setScriptDialogue(save.currentNodeId);

            // 3. Inject memory back into GameState
            GameState game = (GameState) State.game;
            game.restoreSaveData(save);

            // 4. VERY IMPORTANT: Stop the main menu music!
            State.menu.exit();

            // 5. Start the Game!
            State.current = game;
            State.current.enter(window);

        } catch (Exception e) {
            System.out.println("No save file found in slot " + slotIndex + "! It's empty.");
        }
    }

    @Override
    public void mouseClickHandler(Window window){
        if (saveStateButton1.checkClick(window.getMouseX(), window.getMouseY())) {
            clickSound.play();
            loadGameFromSlot(1, window);
        }
        else if (saveStateButton2.checkClick(window.getMouseX(), window.getMouseY())) {
            clickSound.play();
            loadGameFromSlot(2, window);
        }
        else if (saveStateButton3.checkClick(window.getMouseX(), window.getMouseY())) {
            clickSound.play();
            loadGameFromSlot(3, window);
        }
        else if (saveStateButton4.checkClick(window.getMouseX(), window.getMouseY())) {
            clickSound.play();
            loadGameFromSlot(4, window);
        }
        else if (saveStateButton5.checkClick(window.getMouseX(), window.getMouseY())) {
            clickSound.play();
            loadGameFromSlot(5, window);
        }
        else if (saveStateButton6.checkClick(window.getMouseX(), window.getMouseY())) {
            clickSound.play();
            loadGameFromSlot(6, window);
        }
        else if (tabReturnButton.checkClick(window.getMouseX(), window.getMouseY())) {
            System.out.println("Returning to Main Menu...");
            clickSound.play();
            State.current = State.menu; // Return goes to Menu here!
        }
    }

    @Override
    public void exit() {
        super.exit();
    }

    @Override
    public void keyPressedHandler(Window window, int key, int action) {
        if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
            System.out.println("Escape pressed! Does nothing!");
        }
    }
}