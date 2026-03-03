<br>
<br>
<p align="center">
<a href="https://github.com/sayaka04/LWJGL_Visual_Novel"><img src="https://img.shields.io/badge/LWJGL_Visual_Novel-sayaka04-0055ff?style=flat-pill" alt="LWJGL Visual Novel" style="height:70px"></a>
</p>

<h3 align="center">A custom visual novel engine built with Java and LWJGL.</h3>

<p align="center">
<a href="https://www.java.com/"><img src="https://img.shields.io/badge/Java-19-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java"></a>
<a href="https://www.lwjgl.org/"><img src="https://img.shields.io/badge/LWJGL-3.4.1-black?style=for-the-badge" alt="LWJGL"></a>
<a href="https://gradle.org/"><img src="https://img.shields.io/badge/Gradle-Kotlin_DSL-02303A?style=for-the-badge&logo=gradle&logoColor=white" alt="Gradle"></a>
<a href="https://www.opengl.org/"><img src="https://img.shields.io/badge/OpenGL-Legacy-5586A4?style=for-the-badge&logo=opengl&logoColor=white" alt="OpenGL"></a>
</p>

LWJGL_Visual_Novel is a 2D game engine built to explore graphics rendering, state management, and data serialization in Java. Hardware-accelerated rendering and audio are handled through OpenGL and OpenAL via the Lightweight Java Game Library (LWJGL).

---

## 🏗 System Architecture

The application is structured to separate the core engine logic from the game's story data.

* **Data-Driven Pipeline:** Scenes, dialogue, and character sprites are loaded dynamically using external JSON files parsed via Google Gson.
* **State Machine Pattern:** The main game loop delegates rendering and update processes to distinct states (`MenuState`, `GameState`, `LoadState`). This manages transitions between menus and active gameplay.

## 🛠 Technology Stack

| Component | Technology | Description |
| --- | --- | --- |
| **Language** | Java 19 | Primary programming language |
| **Graphics** | OpenGL (LWJGL) | Hardware-accelerated 2D rendering |
| **Audio** | OpenAL (LWJGL) | Positional and spatial audio management |
| **Data** | Google Gson | JSON serialization for saves and scripts |
| **Build Tool** | Gradle (Kotlin DSL) | Dependency and project lifecycle management |

---

## ✨ Current Features

* **OpenGL Rendering:** Processes textures, text rendering, and UI elements.
* **Save/Load System:** Generates memory snapshots and captures in-game screenshots, saving them to local files across 6 dynamic slots.
* **OpenAL Audio Engine:** Manages looping background music (BGM) and triggered UI sound effects (SFX).
* **Interactive UI:** Custom button components with active hover states and click detection.

*Note: The core game loop is functional. The "Settings" menu is currently a visual placeholder.*

## 📖 Background

In the summer of 2024, I developed a proof-of-concept visual novel engine using Java's built-in 2D graphics. While functional, the inclusion of custom parallax scrolling and proprietary text parsing made it demanding on system resources. The project was eventually set aside to rethink the approach.

For version 1.0 of this new engine, the focus was shifted to core architecture and performance:
1. An explicit graphics pipeline using LWJGL and OpenGL.
2. Hardware-accelerated audio using OpenAL.
3. Standardized data formats using JSON and Gson.
4. Automated build management using Gradle.
5. A State Machine for modular game logic.

## 🚀 Getting Started

This project uses Gradle for dependency management.

### Option 1: Quick Run (Standalone)

1. Go to the [Releases](https://github.com/sayaka04/lwjgl-visual-novel/releases) page and download the latest `.jar` file (e.g., `lwjgl-visual-novel-x.x.x.jar`).
2. Download the `assets` folder from this repository.
3. Place the `assets` folder and the `.jar` file in the **same directory**.
4. Open your terminal in that directory and run:
```bash
java -jar lwjgl-visual-novel-x.x.x.jar

```

### Option 2: Development Setup

1. Clone the repository:
```bash
git clone https://github.com/sayaka04/lwjgl-visual-novel.git
cd lwjgl-visual-novel

```


2. Build the project:
   Use the included Gradle wrapper to download dependencies and compile the code. This ensures the correct Gradle version (8.7) is used:
* **Windows:** `.\gradlew build`
* **Linux/Mac:** `./gradlew build`


3. Open and Run:
* **IDE:** Open the folder in **IntelliJ IDEA** (recommended). It will detect the Gradle files automatically. Locate `Main` and run it.
* **CLI:** After building, you can also run the project directly using:
```bash
.\gradlew run

```

