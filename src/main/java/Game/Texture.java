package Game;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Texture {

    // The ID the graphics card gives us to remember this image
    private int textureId;

    public Texture(String filepath) {
        // 1. Prepare buffers to hold the image width, height, and color channels
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        // Tell the image loader to flip the image vertically.
        // (OpenGL expects the bottom-left corner to be coordinate 0,0, but standard images use top-left)
        STBImage.stbi_set_flip_vertically_on_load(false);

        // 2. Load the image file from your hard drive into computer RAM
        // The '4' at the end forces the image to have Red, Green, Blue, and Alpha (transparency)
        ByteBuffer imagePixels = STBImage.stbi_load(filepath, width, height, channels, 4);

        if (imagePixels == null) {
            throw new RuntimeException("Failed to load image: " + filepath + " - " + STBImage.stbi_failure_reason());
        }

        // 3. Ask the Graphics Card for a new, empty texture ID
        textureId = GL11.glGenTextures();

        // Tell the Graphics Card: "I am about to work with this specific texture ID"
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

        // Tell the Graphics Card how to stretch the image if it needs to resize it
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        // 4. SEND THE PIXELS TO THE GRAPHICS CARD
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width.get(0), height.get(0), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imagePixels);

        // 5. Delete the raw pixels from our computer RAM, because the Graphics Card has its own copy now!
        STBImage.stbi_image_free(imagePixels);
    }

    // A method so we can easily tell OpenGL to use this texture later
    public void bind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
    }
}