package Game;

import Components.Button;
import Components.Sound;
import Components.Renderer;
import Components.Texture;
import Components.TextRenderer;
import StateMachine.GameState;
import StateMachine.MainMenuState;
import StateMachine.State;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window extends WindowHelper{


    private static Window window = null;

    private Window(){
        this.width = 1920;
        this.height = 1080;
        this.title = "OpenGL Game Test";
    }

    public static Window get(){
        if (window == null){
            Window.window = new Window();
        }
        return Window.window;
    }

    public void init(){

        this.setupGLFW();       // Setup error callback and initialize GLFW
        this.setupWindow();     // Create window, make context current, enable V-Sync, show window

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // --- OpenGL Settings ---
        setupOpenGL();

        // --- OpenAL Settings ---
        setupOpenAL();


        // +------------------------+
        // |    MAIN CONTENTS HERE  |
        // +------------------------+

        // Initialize StateMachine and States
        State.menu = new MainMenuState();
        State.menu.init(this);
        State.game = new GameState();
        State.game.init(this);

        State.current = State.menu;
        State.menu.enter();

        // Initialize Inputs
        // 1. Listen for Mouse Movement
        GLFW.glfwSetCursorPosCallback(this.glfwWindow, (windowHandle, xpos, ypos) -> {
            this.mouseX = xpos;
            this.mouseY = ypos;
        });

        // 2. Listen for Mouse Clicks!
        GLFW.glfwSetMouseButtonCallback(this.glfwWindow, (windowHandle, mouseButton, action, mods) -> {

            if (mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS) {
                State.current.mouseClickHandler(this);
            }

        });

        // 3. Listen for Keyboard Presses!
        GLFW.glfwSetKeyCallback(this.glfwWindow, (windowHandle, key, scancode, action, mods) -> {

            if (key == GLFW.GLFW_KEY_DELETE && action == GLFW.GLFW_PRESS) {
                System.out.println("Escape pressed! Closing the game...");
                GLFW.glfwSetWindowShouldClose(this.glfwWindow, true);   // This tells the while loop to stop, safely closing the game
            }

            State.current.keyPressedHandler(this, key, action);

        });

        // +----------------------------+
        // |    END OF MAIN CONTENTS    |
        // +----------------------------+

    }

    public void loop() {

        double timePerFrame = 1.0 / 60.0;           // Target frame time for 60 FPS
        double lastTime = GLFW.glfwGetTime();       // Time of the last frame

        while (!GLFW.glfwWindowShouldClose(this.glfwWindow)) {
            double currentTime = GLFW.glfwGetTime(); // Current time at this iteration

            // POLL INPUT EVENTS
            // glfwPollEvents() checks for any user input events (keyboard, mouse, window resize, etc.)
            // These events are placed into GLFW's event queue.
            // It must be called every loop iteration, even if a new frame is not being rendered yet.
            // This ensures the application responds immediately to input (e.g., mouse clicks, key presses, window close).
            // DO NOT put this inside the FPS-limiting if-block, otherwise input may feel laggy.
            GLFW.glfwPollEvents();

            // UPDATE + DRAW ONLY AT 60 FPS
            if (currentTime - lastTime >= timePerFrame) {
                lastTime = currentTime; // Reset the timer

                // CLEAR THE FRAME
                // 1. glClearColor(r, g, b, a) sets the color that glClear will use.
                //    Here we use bright red: R=1, G=0, B=0, Alpha=1
                // 2. glClear(GL_COLOR_BUFFER_BIT) actually clears the color buffer (the screen) with the color set above.
                // NOTE: Order is important! glClearColor must come BEFORE glClear, otherwise the wrong background color is used.
                glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
                glClear(GL_COLOR_BUFFER_BIT);

                // +------------------------+
                // |    MAIN CONTENTS HERE  |
                // +------------------------+
                // This is where you update your game state, draw everything, and handle frame-based logic

                State.current.update(this);  // Run the current state logic

                // +----------------------------+
                // |    END OF MAIN CONTENTS    |
                // +----------------------------+

                // SWAP BUFFERS
                // Swap the front and back buffers so the rendered frame becomes visible
                // Must be done inside the FPS-limiting block to sync with your frame rate
                GLFW.glfwSwapBuffers(this.glfwWindow);
            }
        }
    }

    public void run(){
        System.out.println("Hello LWJGL: " + Version.getVersion() + "!");
        init();
        loop();

        //Free the memory
        glfwFreeCallbacks(this.glfwWindow);
        GLFW.glfwDestroyWindow(this.glfwWindow);

        //Terminate GLFW and the free error callback
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }
}
