package Engine;

import Components.BackgroundMusic;
import Components.Sound;
import Components.Texture;
import Data.GameAssets;
import Data.StoryData;
import com.google.gson.Gson;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class EngineCore {

    public static GameAssets masterAssets;
    public static StoryData currentChapter;

    // --- RAM CACHES: This holds the actual loaded OpenGL/OpenAL objects! ---
    public static Map<String, Texture> textures = new HashMap<>();
    public static Map<String, BackgroundMusic> music = new HashMap<>();
    public static Map<String, Sound> sounds = new HashMap<>();

    public static void initEngine() {
        Gson gson = new Gson();
        try {
            System.out.println("Booting engine... Loading Asset Registry.");
            masterAssets = gson.fromJson(new FileReader("assets/assets.json"), GameAssets.class);

            // 1. Load Visuals into Textures
            if (masterAssets.visuals != null) {
                for (Map.Entry<String, String> entry : masterAssets.visuals.entrySet()) {
                    // Update this folder path to wherever you keep your images!
                    // Example: "C:\\Users\\Acer\\OneDrive\\Desktop\\" + entry.getValue()
                    String path = "assets/visuals/" + entry.getValue();
                    textures.put(entry.getKey(), new Texture(path));
                }
            }

            // 2. Load Audio into BackgroundMusic (BGM/Ambience) and Sound (SFX)
            if (masterAssets.audio != null) {
                for (Map.Entry<String, String> entry : masterAssets.audio.entrySet()) {
                    // Update this folder path to wherever you keep your audio!
                    String path = "assets/audio/" + entry.getValue();
                    music.put(entry.getKey(), new BackgroundMusic(path));
                    sounds.put(entry.getKey(), new Sound(path));
                }
            }

            System.out.println("All assets successfully loaded into RAM!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startNewGame() {
        Gson gson = new Gson();
        try {
            System.out.println("Starting Game... Loading Scene 1.");

            currentChapter = gson.fromJson(new FileReader("assets/scene1.json"), StoryData.class);
            currentChapter.setScriptDialogue("1");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadChapter(String filename) {
        Gson gson = new Gson();
        try {
            System.out.println("Loading new chapter: " + filename);

            // Reads the new JSON file from your assets folder!
            currentChapter = gson.fromJson(new FileReader("assets/" + filename), StoryData.class);

            // Automatically start at node "1" of the new chapter
            currentChapter.setScriptDialogue("1");

            System.out.println("Chapter loaded successfully!");

        } catch (Exception e) {
            System.err.println("CRITICAL ERROR: Could not find chapter file: " + filename);
            e.printStackTrace();
        }
    }
}