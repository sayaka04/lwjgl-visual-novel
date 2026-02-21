package Game;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.ALC10.alcCreateContext;
import static org.lwjgl.openal.ALC10.alcMakeContextCurrent;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

public class WindowHelper {

    protected int width, height;
    protected String title;
    protected long glfwWindow;

    protected double mouseX = 0;
    protected double mouseY = 0;

    public double getMouseX() {
        return mouseX;
    }
    public double getMouseY() {
        return mouseY;
    }

    protected void setupGLFW() {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
    }

    protected void setupWindow() {
        // Create the Window
        this.glfwWindow = GLFW.glfwCreateWindow(
                this.width, this.height, this.title, GLFW.glfwGetPrimaryMonitor(), MemoryUtil.NULL
        );

        if (this.glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window");
        }

        // Make the OpenGL context current
        GLFW.glfwMakeContextCurrent(this.glfwWindow);

        // Enable V-Sync
        GLFW.glfwSwapInterval(1);

        // Make the window visible
        GLFW.glfwShowWindow(this.glfwWindow);
    }


    protected void setupOpenGL() {
        // Turn on OpenGL Alpha Blending for transparent PNGs!
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glEnable(GL_TEXTURE_2D);	// Enable textures

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


    protected void setupOpenAL() {
        // --- OPENAL AUDIO SETUP ---
        // 1. Find the default audio device
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        long audioDevice = alcOpenDevice(defaultDeviceName);

        // 2. Create an audio context and make it active
        long audioContext = alcCreateContext(audioDevice, new int[]{0});
        alcMakeContextCurrent(audioContext);

        // 3. Tell LWJGL to make the OpenAL functions available
        ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
        AL.createCapabilities(alcCapabilities);
    }

}
