package StateMachine;

import Components.*;
import Data.SaveData;
import Engine.EngineCore;
import Game.Window;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class GameState extends State {

    // --- RENDER LAYERS & MEMORY ---
    private final String[] renderLayers = {"Background", "Left", "Middle", "Right", "Foreground"};
    private Map<String, String> activeVisuals = new HashMap<>(); // Remembers what is currently on screen!

    // --- AUDIO MEMORY ---
    private BackgroundMusic currentBGM;      // Remembers playing BGM
    private BackgroundMusic currentAmbience; // Remembers playing Ambience

    // These two lines are for the save file!
    public String currentBgmId = "none";
    public String currentAmbienceId = "none";

    // --- UI COMPONENTS ---
    private TextRenderer buttonTextFont;
    private Button option1Button;
    private Button option2Button;
    private Button option3Button;
    private Button option4Button;

    private boolean tabIsVisible = false;
    private boolean hasChoices = true;

    private Button tabMenuButton;
    private Button tabSaveButton;
    private Button tabLoadButton;
    private Button tabExitButton;

    private Sound hoverSound;
    private Sound clickSound;

    private Dialogue dialogue;
    private Renderer renderer;

    @Override
    public void init(Window window) {

        // Init Font
        buttonTextFont = new TextRenderer("assets/ui/font.ttf", 48);

        // Init UI Sounds (These are safe to leave hardcoded since they never change!)
        hoverSound = new Sound("assets/ui/hover.ogg");
        clickSound = new Sound("assets/ui/click.ogg");

        float [] rgba = {0.0f, 0.0f, 0.25f, 0.65f};

        // Init Buttons
        option1Button = new Button((1920/2) - 250, 250, 500, 50, hoverSound, "Option 1", buttonTextFont, rgba);
        option2Button = new Button((1920/2) - 250, 320, 500, 50, hoverSound, "Option 2", buttonTextFont, rgba);
        option3Button = new Button((1920/2) - 250, 390, 500, 50, hoverSound, "Option 3", buttonTextFont, rgba);
        option4Button = new Button((1920/2) - 250, 460, 500, 50, hoverSound, "Option 4", buttonTextFont, rgba);

        tabMenuButton = new Button(0, 0, 150, 50, hoverSound, "Menu", buttonTextFont, rgba);
        tabSaveButton = new Button(150, 0, 150, 50, hoverSound, "Save", buttonTextFont, rgba);
        tabLoadButton = new Button(300, 0, 150, 50, hoverSound, "Load", buttonTextFont, rgba);
        tabExitButton = new Button(450, 0, 150, 50, hoverSound, "Exit", buttonTextFont, rgba);

        dialogue = new Dialogue(0, 780, 1920, 300, hoverSound, "assets/ui/font.ttf", 48);
        dialogue.isVisible = false;

        renderer = new Renderer();
    }


    public void scriptUpdate(Window window){

        // --- 1. UPDATE VISUAL MEMORY ---
        if (EngineCore.currentChapter.scriptDialogue.hasSetVisuals()) {
            for (int i = 0; i < renderLayers.length; i++) {
                String layerName = renderLayers[i];
                if (EngineCore.currentChapter.scriptDialogue.setVisuals.containsKey(layerName)) {
                    String textureId = EngineCore.currentChapter.scriptDialogue.setVisuals.get(layerName);

                    if (textureId.equalsIgnoreCase("none") || textureId.isEmpty()) {
                        activeVisuals.remove(layerName);
                    } else {
                        activeVisuals.put(layerName, textureId);
                    }
                }
            }
        }

        // --- 2. UPDATE AUDIO MEMORY ---
        if (EngineCore.currentChapter.scriptDialogue.hasSetAudio()) {
            Map<String, String> setAudio = EngineCore.currentChapter.scriptDialogue.setAudio;

            // Handle Background Music
            if (setAudio.containsKey("BackgroundMusic")) {
                String bgmId = setAudio.get("BackgroundMusic");
                this.currentBgmId = bgmId;
                if (bgmId.equalsIgnoreCase("none")) {
                    if (currentBGM != null) { currentBGM.stop(); currentBGM = null; }
                } else {
                    BackgroundMusic nextBGM = EngineCore.music.get(bgmId);
                    if (nextBGM != null && nextBGM != currentBGM) {
                        if (currentBGM != null) currentBGM.stop();
                        currentBGM = nextBGM;
                        currentBGM.play();
                    }
                }
            }

            // Handle Ambience
            if (setAudio.containsKey("Ambience")) {
                String ambId = setAudio.get("Ambience");
                this.currentAmbienceId = ambId;
                if (ambId.equalsIgnoreCase("none")) {
                    if (currentAmbience != null) { currentAmbience.stop(); currentAmbience = null; }
                } else {
                    BackgroundMusic nextAmb = EngineCore.music.get(ambId);
                    if (nextAmb != null && nextAmb != currentAmbience) {
                        if (currentAmbience != null) currentAmbience.stop();
                        currentAmbience = nextAmb;
                        currentAmbience.play();
                    }
                }
            }

            // Handle Sound Effects (Play once, no memory needed)
            if (setAudio.containsKey("SoundEffect")) {
                String sfxId = setAudio.get("SoundEffect");
                Sound sfx = EngineCore.sounds.get(sfxId);
                if (sfx != null) sfx.play();
            }
        }


        // --- 3. UPDATE CHOICES ---
        option1Button.isVisible = false;
        option2Button.isVisible = false;
        option3Button.isVisible = false;
        option4Button.isVisible = false;

        if(EngineCore.currentChapter.scriptDialogue.hasChoices()){
            hasChoices = true;
            int totalChoices = EngineCore.currentChapter.scriptDialogue.choices.size();
            for(int i = 0; i < totalChoices; i++){
                String choiceText = EngineCore.currentChapter.scriptDialogue.getChoiceText(i);

                switch (i){
                    case 0:
                        option1Button.isVisible = true;
                        option1Button.setText(choiceText);
                        break;
                    case 1:
                        option2Button.isVisible = true;
                        option2Button.setText(choiceText);
                        break;
                    case 2:
                        option3Button.isVisible = true;
                        option3Button.setText(choiceText);
                        break;
                    case 3:
                        option4Button.isVisible = true;
                        option4Button.setText(choiceText);
                        break;
                }
            }
        } else {
            hasChoices = false; // Important for Spacebar logic!
        }

        // --- 4. UPDATE DIALOGUE TEXT ---
        if(EngineCore.currentChapter.scriptDialogue.hasSpeaker() || EngineCore.currentChapter.scriptDialogue.hasText()){
            dialogue.isVisible = true;
            dialogue.setName(EngineCore.currentChapter.scriptDialogue.speaker != null ? EngineCore.currentChapter.scriptDialogue.speaker : "");
            dialogue.setDialogueText(EngineCore.currentChapter.scriptDialogue.text != null ? EngineCore.currentChapter.scriptDialogue.text : "");
        }
    }

    @Override
    public void enter(Window window) {
        scriptUpdate(window);
        System.out.println("Entering GameState");
    }

    @Override
    public void update(Window window) {

        // --- DRAW VISUALS FROM MEMORY ---
        for (int i = 0; i < renderLayers.length; i++) {
            String currentLayer = renderLayers[i];

            if (activeVisuals.containsKey(currentLayer)) {
                String textureKey = activeVisuals.get(currentLayer);
                Texture texture = EngineCore.textures.get(textureKey);

                if (texture != null) {
                    renderer.drawImage(texture, 0, 0, 1920, 1080);
                }
            }
        }

        // --- DRAW UI ---
        if (tabIsVisible) {
            tabMenuButton.draw(window.getMouseX(), window.getMouseY());
            tabSaveButton.draw(window.getMouseX(), window.getMouseY());
            tabLoadButton.draw(window.getMouseX(), window.getMouseY());
            tabExitButton.draw(window.getMouseX(), window.getMouseY());
        }

        if(hasChoices){
            if (option1Button.isVisible) option1Button.draw(window.getMouseX(), window.getMouseY());
            if (option2Button.isVisible) option2Button.draw(window.getMouseX(), window.getMouseY());
            if (option3Button.isVisible) option3Button.draw(window.getMouseX(), window.getMouseY());
            if (option4Button.isVisible) option4Button.draw(window.getMouseX(), window.getMouseY());
        }

        if (dialogue.isVisible) {
            dialogue.draw(window.getMouseX(), window.getMouseY());
        }

        GL11.glColor3f(1.0f, 1.0f, 1.0f);
    }

    @Override
    public void exit() {
        super.exit();
        // Stop audio if we leave the GameState entirely
        if (currentBGM != null) currentBGM.stop();
        if (currentAmbience != null) currentAmbience.stop();
    }

    @Override
    public void mouseClickHandler(Window window){

        if (tabIsVisible) {
            if (tabMenuButton.checkClick(window.getMouseX(), window.getMouseY())) {
                clickSound.play();

                // Stop dynamic audio when returning to menu
                if (currentBGM != null) {
                    currentBGM.stop();
                    currentBGM = null;
                }
                if (currentAmbience != null) {
                    currentAmbience.stop();
                    currentAmbience = null;
                }
                activeVisuals.clear();
                State.current.exit();
                State.current = State.menu;
                State.current.enter(window);
                return;
            }
            else if (tabSaveButton.checkClick(window.getMouseX(), window.getMouseY())) {
                clickSound.play();
                saveScreenshot("assets/save/temp_screenshot.png");
                State.current = State.save;
                State.current.enter(window);
                return;
            }
            else if (tabLoadButton.checkClick(window.getMouseX(), window.getMouseY())) {
                clickSound.play();
                State.current = State.ingame_load;
                State.current.enter(window);
                return;
            }
            else if (tabExitButton.checkClick(window.getMouseX(), window.getMouseY())) {
                clickSound.play();
                GLFW.glfwSetWindowShouldClose(window.glfwWindow, true);
                return;
            }
        }

        // Handle Choices Clicks
        if(hasChoices) {
            if (option1Button.checkClick(window.getMouseX(), window.getMouseY())) {
                clickSound.play();
                EngineCore.currentChapter.setScriptDialogue(EngineCore.currentChapter.scriptDialogue.choices.get(0).target);
                this.scriptUpdate(window);
                return;
            }
            else if (option2Button.checkClick(window.getMouseX(), window.getMouseY())) {
                clickSound.play();
                EngineCore.currentChapter.setScriptDialogue(EngineCore.currentChapter.scriptDialogue.choices.get(1).target);
                this.scriptUpdate(window);
                return;
            }
            else if (option3Button.checkClick(window.getMouseX(), window.getMouseY())) {
                clickSound.play();
                EngineCore.currentChapter.setScriptDialogue(EngineCore.currentChapter.scriptDialogue.choices.get(2).target);
                this.scriptUpdate(window);
                return;
            }
            else if (option4Button.checkClick(window.getMouseX(), window.getMouseY())) {
                clickSound.play();
                EngineCore.currentChapter.setScriptDialogue(EngineCore.currentChapter.scriptDialogue.choices.get(3).target);
                this.scriptUpdate(window);
                return;
            }
        }

        if(EngineCore.currentChapter.scriptDialogue.hasProceedTo()){
            clickSound.play();
            System.out.println("Proceed to found!");
            EngineCore.currentChapter.setScriptDialogue(EngineCore.currentChapter.scriptDialogue.proceedTo.toString());
            this.scriptUpdate(window);
        }
        else if (EngineCore.currentChapter.scriptDialogue.hasLoadNextScript()) {
            clickSound.play();
            System.out.println("Ready to load new chapter: " + EngineCore.currentChapter.scriptDialogue.loadNextScript);
            String nextFile = EngineCore.currentChapter.scriptDialogue.loadNextScript;
            EngineCore.loadChapter(nextFile); // Load the new JSON
            EngineCore.currentChapter.setScriptDialogue("1"); // Start at Node 1 of the new file
            this.scriptUpdate(window); // Update the screen!
        }
    }

    @Override
    public void keyPressedHandler(Window window, int key, int action) {

        // SPACEBAR LOGIC: Advance the story if there are no choices!
        if (key == GLFW.GLFW_KEY_SPACE && action == GLFW.GLFW_PRESS) {

            // Only advance with spacebar if there are no buttons to click
            if (!hasChoices && !tabIsVisible) {

                // Proceed to next dialogue line
                if (EngineCore.currentChapter.scriptDialogue.hasProceedTo()) {
                    EngineCore.currentChapter.setScriptDialogue(EngineCore.currentChapter.scriptDialogue.proceedTo);
                    this.scriptUpdate(window);
                }
                // Load the next chapter
                else if (EngineCore.currentChapter.scriptDialogue.hasLoadNextScript()) {
                    System.out.println("Ready to load new chapter: " + EngineCore.currentChapter.scriptDialogue.loadNextScript);
                    // Note: You will eventually tell EngineCore to load the new JSON here!
                }
            }
        }
        else if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
            tabIsVisible = !tabIsVisible;
            System.out.println("Escape pressed! toggle: " + tabIsVisible);
        }
    }


    // --- SAVE / LOAD HELPERS ---

    // Packages current memory into a SaveData object
    public SaveData createSaveData() {
        SaveData save = new SaveData();
        save.chapterFile = EngineCore.currentChapterFilename;
        save.currentNodeId = EngineCore.currentChapter.currentNodeId;
        save.activeVisuals = new HashMap<>(this.activeVisuals); // Copy it safely
        save.currentBgmId = this.currentBgmId;
        save.currentAmbienceId = this.currentAmbienceId;
        return save;
    }

    // Injects loaded data back into the game
    public void restoreSaveData(SaveData save) {
        this.activeVisuals = save.activeVisuals;
        this.currentBgmId = save.currentBgmId;
        this.currentAmbienceId = save.currentAmbienceId;

        // Stop current audio before overwriting
        if (currentBGM != null) currentBGM.stop();
        if (currentAmbience != null) currentAmbience.stop();

        // Restore BGM perfectly
        if (!currentBgmId.equals("none") && EngineCore.music.containsKey(currentBgmId)) {
            currentBGM = EngineCore.music.get(currentBgmId);
            currentBGM.play();
        } else {
            currentBGM = null;
        }

        // Restore Ambience perfectly
        if (!currentAmbienceId.equals("none") && EngineCore.music.containsKey(currentAmbienceId)) {
            currentAmbience = EngineCore.music.get(currentAmbienceId);
            currentAmbience.play();
        } else {
            currentAmbience = null;
        }
    }

    // Add this method to the bottom of GameState.java
    private void saveScreenshot(String filename) {
        int width = 1920; // Your window width
        int height = 1080; // Your window height

        // 1. Create a buffer to hold the screen data
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);

        // 2. Tell OpenGL to read the screen into our buffer!
        GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        // 3. Convert the OpenGL buffer into a Java Image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int i = (x + (width * y)) * 4;
                int r = buffer.get(i) & 0xFF;
                int g = buffer.get(i + 1) & 0xFF;
                int b = buffer.get(i + 2) & 0xFF;
                // OpenGL reads bottom-to-top, so we have to flip the Y coordinate!
                image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
            }
        }

        // 4. Save it as a PNG!
        try {
            ImageIO.write(image, "PNG", new File(filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}