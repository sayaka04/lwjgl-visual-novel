package Components;

import org.lwjgl.opengl.GL11;

public class Dialogue {


    public float x, y, width, height;
    public boolean isVisible = true;

    // Memory for the "One-Shot" hover trigger
    private boolean wasHoveredLastFrame = false;

    // Let the button own its sound!
    private final Sound hoverSound;

    private final TextRenderer nameTextRenderer;
    private final TextRenderer dialogueTextRenderer;

    private String name = "";
    private String dialogueText = "";

    public void setName(String name) {this.name = name;}
    public void setDialogueText(String dialogueText) {this.dialogueText = dialogueText;}



    public Dialogue(float x, float y, float width, float height, Sound hoverSound, String ttfPath, int fontHeight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hoverSound = hoverSound;
        this.nameTextRenderer = new TextRenderer(ttfPath, 60);
        this.dialogueTextRenderer = new TextRenderer(ttfPath, fontHeight);
    }

    public void draw(double mouseX, double mouseY) {
        if (!isVisible) return; // Don't do anything if the button is hidden

        // 1. Calculate if the mouse is currently over this button
        boolean currentlyHovered = (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height);

        // 2. The "One-Shot" Trigger! (If hovered now, but wasn't last frame)
        if (currentlyHovered && !wasHoveredLastFrame) {
            System.out.println("Button Hovered!");
            if (hoverSound != null) {
                hoverSound.play(); // Play the sound once!
            }
        }

        // Save the state for the next frame's check
        wasHoveredLastFrame = currentlyHovered;

        // 3. Draw the actual box
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL11.glBegin(GL11.GL_QUADS);

        // Color it based on the hover state we just calculated
        if (currentlyHovered) {
            GL11.glColor4f(0.02f, 0.02f, 0.02f, 0.65f); // Yellow (Hover)
        } else {
            GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.65f); // Blue (Normal)
        }

        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x, y + height);

        GL11.glEnd();

        GL11.glColor3f(1.0f, 1.0f, 1.0f); // set color to white
        nameTextRenderer.drawText(name, x+25,y);
        GL11.glColor3f(0.80f, 0.80f, 0.80f); // set color to slightly darker
        dialogueTextRenderer.drawText(dialogueText, x+35,y+60);
        GL11.glColor3f(1.0f, 1.0f, 1.0f); // Reset color to white

    }


}
