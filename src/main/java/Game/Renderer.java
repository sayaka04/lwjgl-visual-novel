package Game;

import org.lwjgl.opengl.GL11;

public class Renderer {

    public Renderer() {
        // 1. Turn on 2D Textures so we can draw images
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        // 2. Turn on Transparency (Blending)
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    /**
     * This is the function you will call to draw an image to the screen.
     * * @param texture The image you loaded in Step 1
     * @param x       The X position on the screen (0 is the left edge)
     * @param y       The Y position on the screen (0 is the top edge)
     * @param width   How wide to draw the image (in pixels)
     * @param height  How tall to draw the image (in pixels)
     */
    public void drawImage(Texture texture, float x, float y, float width, float height) {

        // 1. Tell the Graphics Card which image we want to use right now
        texture.bind();

        // 2. Tell the Graphics Card we are about to draw a shape with 4 corners (a Quad)
        GL11.glBegin(GL11.GL_QUADS);

        // -- TOP LEFT CORNER --
        GL11.glTexCoord2f(0, 0); // Pin the top-left of the IMAGE...
        GL11.glVertex2f(x, y);   // ...to the top-left of the RECTANGLE.

        // -- TOP RIGHT CORNER --
        GL11.glTexCoord2f(1, 0); // Pin the top-right of the IMAGE...
        GL11.glVertex2f(x + width, y); // ...to the top-right of the RECTANGLE.

        // -- BOTTOM RIGHT CORNER --
        GL11.glTexCoord2f(1, 1); // Pin the bottom-right of the IMAGE...
        GL11.glVertex2f(x + width, y + height); // ...to the bottom-right of the RECTANGLE.

        // -- BOTTOM LEFT CORNER --
        GL11.glTexCoord2f(0, 1); // Pin the bottom-left of the IMAGE...
        GL11.glVertex2f(x, y + height); // ...to the bottom-left of the RECTANGLE.

        // 3. Tell the Graphics Card we are done drawing this shape
        GL11.glEnd();
    }
}