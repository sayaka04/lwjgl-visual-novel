# Assets & Content Directory

This directory contains the game's data, media, and script files. The engine is designed to be data-driven, meaning the game's flow and appearance are controlled by the files in this folder rather than the Java source code.

## 📁 Directory Structure

| File/Folder        | Purpose |
|--------------------| --- |
| **`assets.json`**  | The central registry where all audio and visual files are declared and mapped to keys. |
| **`scripts.json`** | Contains the JSON files that define the game's scenes and dialogue. |
| **`audio`**        | Contains background music (BGM) and sound effects (SFX) in `.ogg` format. |
| **`visuals`**      | Stores background images (`.jpg`) and character sprites (`.png`). |
| **`ui`**           | Contains UI-specific textures, frames, fonts (`.ttf`), and menu sounds. |
| **`saves`**        | Local directory where game states and snapshots are persisted. |

## 🛠 Modifying the Game Flow

To change the story or create a new visual novel, you can modify the JSON files inside the `scripts` folder.

* **Dialogue:** Edit the text strings to change what characters say.
* **Scene Logic:** Change the `next_scene` or `background` references to link different visuals and dialogue blocks together.
* **Assets:** You can replace images or audio files; ensure you maintain the file names referenced in your JSON scripts, or update the scripts to match your new filenames.
* **Asset Registry:** When adding new files to the `audio` or `visuals` folders, you must register them in **`assets.json`** by mapping a unique key to the filename so the engine can recognize them.

## ⚠️ Important Note
When running the game via the JAR file, the engine expects the `assets` folder to be in the same root directory as the `.jar` file.