package Game;

import Components.Button;
import Components.Sound;
import Components.Renderer;
import Components.Texture;
import Components.TextRenderer;
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




public class Window {

    private int width, height;
    private final String title;
    private long glfwWindow;

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
        //Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!GLFW.glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }

State.current.enter();

        // Configure GLFW
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);

        //Create the Window
        this.glfwWindow = GLFW.glfwCreateWindow(this.width, this.height, this.title, GLFW.glfwGetPrimaryMonitor(), MemoryUtil.NULL);

        if(this.glfwWindow == NULL){
            throw new IllegalStateException("Failed to create the GLFW window");
        }

        //Make the OpenGL Context Current
        GLFW.glfwMakeContextCurrent(this.glfwWindow);

        //Enable V-Sync
        GLFW.glfwSwapInterval(1);

        //Make the window Visible
        GLFW.glfwShowWindow(this.glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        myFont = new TextRenderer("C:\\Users\\Acer\\OneDrive\\Desktop\\Jersey10-Regular.ttf", 48);

// Turn on OpenGL Alpha Blending for transparent PNGs!
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);



        // --- OPENAL AUDIO SETUP ---
        // 1. Find the default audio device (your speakers/headphones)
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        long audioDevice = alcOpenDevice(defaultDeviceName);

        // 2. Create an audio context and make it active
        long audioContext = alcCreateContext(audioDevice, new int[]{0});
        alcMakeContextCurrent(audioContext);

        // 3. Tell LWJGL to make the OpenAL functions available to us
        ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
        AL.createCapabilities(alcCapabilities);





        // Init sound...
        clickSound = new Sound("C:\\Users\\Acer\\OneDrive\\Desktop\\edited683098__florianreichelt__click.ogg");

// Create a button at X: 500, Y: 300. Width: 200, Height: 100.
        myStartButton = new Button(500, 300, 200, 100, clickSound);
        anotherButton = new Button(0, 600, 200, 100, clickSound);

        // 1. Listen for Mouse Movement
        GLFW.glfwSetCursorPosCallback(this.glfwWindow, (windowHandle, xpos, ypos) -> {
            this.mouseX = xpos;
            this.mouseY = ypos;
        });




        GLFW.glfwSetMouseButtonCallback(this.glfwWindow, (windowHandle, mouseButton, action, mods) -> {

            if (mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS) {

                // Ask our Button object if it got clicked
                if (myStartButton.checkClick(mouseX, mouseY)) {

                    System.out.println("Button Clicked! Hiding button now...");
                    clickSound.play();

                    State.current = State.menu;
                    State.current.enter();


                    // TURN THE BUTTON OFF!
                    // This stops it from drawing AND stops the ghost clicks!
                    myStartButton.isVisible = false;
                }

                if (anotherButton.checkClick(mouseX, mouseY)) {

                    System.out.println("Button Clicked! Yay so simple to use hahaha...");
                    clickSound.play();

                    State.current = State.game;
                    State.current.enter();
                }
            }
        });



        // 3. Listen for Keyboard Presses!
        GLFW.glfwSetKeyCallback(this.glfwWindow, (windowHandle, key, scancode, action, mods) -> {

            // If the user presses the SPACEBAR...
            if (key == GLFW.GLFW_KEY_SPACE && action == GLFW.GLFW_PRESS) {
                System.out.println("Spacebar pressed! Ready to advance text.");
                // You can put logic here, like playing a sound or changing the text!
            }

            // If the user presses the ESCAPE key...
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
                System.out.println("Escape pressed! Closing the game...");
                // This tells the while loop to stop, safely closing the game
                GLFW.glfwSetWindowShouldClose(this.glfwWindow, true);
            }

            // Example: Pressing 'A'
            if (key == GLFW.GLFW_KEY_A && action == GLFW.GLFW_PRESS) {
                System.out.println("The 'A' key was pressed!");
            }
        });




        glEnable(GL_TEXTURE_2D);

        myBackground = new Texture("C:\\Users\\Acer\\OneDrive\\Desktop\\miki.png");
        renderer = new Renderer();
//        myTexture = loadTexture("C:\\Users\\Acer\\OneDrive\\Desktop\\watertile.png");

        // Get actual framebuffer size (important when maximized)
        int[] w = new int[1];
        int[] h = new int[1];
        GLFW.glfwGetFramebufferSize(this.glfwWindow, w, h);
        this.width = w[0];
        this.height = h[0];

        // Set viewport
        glViewport(0, 0, width, height);

        // Switch to projection matrix
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        // 2D orthographic projection (pixel coordinates)
        glOrtho(0, width, height, 0, -1, 1);

        // Switch back to modelview
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }
    Texture myBackground ;
    Renderer renderer ;

    private TextRenderer myFont; // Add this!

    int i = 0;

    private double mouseX = 0;
    private double mouseY = 0;
    private Button myStartButton; // Add this!
    private Button anotherButton; // Add this!
    Sound clickSound;

    public void loop() {



        double timePerFrame = 1.0 / 60.0;
        double lastTime = GLFW.glfwGetTime();

        while (!GLFW.glfwWindowShouldClose(this.glfwWindow)) {
            double currentTime = GLFW.glfwGetTime();


//            CurrentState.getInstance().update();

            // ONLY run this code if 1/60th of a second has passed
            if (currentTime - lastTime >= timePerFrame) {
                lastTime = currentTime; // Reset the timer

                glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
                glClear(GL_COLOR_BUFFER_BIT);

                i += 1; // Now this happens EXACTLY 60 times a second

                State.current.update();

                // --- DRAW EVERYTHING ---
                renderer.drawImage(myBackground, 0, 0, 1920, 1080);
                renderer.drawImage(myBackground, 500+i, 500+i, 320, 320);
                renderer.drawImage(myBackground, 300, 300+i, 320, 320);
                renderer.drawImage(myBackground, 500+i, i, 320, 320);
                renderer.drawImage(myBackground, i, 500+i, 320, 320);

                if (myStartButton.isVisible) {
                    myStartButton.draw(mouseX, mouseY);
                }
// Inside your 60 FPS loop, where you draw everything:
                anotherButton.draw(mouseX, mouseY);


                GL11.glColor3f(1.0f, 1.0f, 1.0f);
                myFont.drawText("Hello Visual Novel!", 100, 100);

                // Swap buffers must be INSIDE the if statement
                // so we only show new frames at 60fps
                GLFW.glfwSwapBuffers(this.glfwWindow);
            }

            // Poll Events should be OUTSIDE the if statement
            // so the window stays responsive even between frames
            GLFW.glfwPollEvents();
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
