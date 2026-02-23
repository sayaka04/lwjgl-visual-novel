package Components;
import org.lwjgl.opengl.GL11;

public class Button {
    public float x, y, width, height;
    public String text;
    public boolean isVisible = true;
    public TextRenderer textRenderer;
    // Memory for the "One-Shot" hover trigger
    private boolean wasHoveredLastFrame = false;

    // Let the button own its sound!
    private Sound hoverSound;
    float [] rgba = new float[4];

    // Constructor
    public Button(float x, float y, float width, float height, Sound hoverSound, String text, TextRenderer textRenderer, float [] rgba) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hoverSound = hoverSound;
        this.text = text;
        this.textRenderer = textRenderer;
        this.rgba = rgba;
    }

    public void setText(String newText) {
        this.text = newText;
    }

    // The Button now draws ITSELF and checks its own hover state!
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
            GL11.glColor4f(rgba[0], rgba[1], rgba[2], rgba[3]+0.15f); // Yellow (Hover)
        } else {
            GL11.glColor4f(rgba[0], rgba[1], rgba[2], rgba[3]);
        }

        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x, y + height);

        GL11.glEnd();
        GL11.glColor3f(1.0f, 1.0f, 1.0f); // Reset color to white
        textRenderer.drawText(text, x+15, y-15);
    }

    // Checking clicks remains simple
    public boolean checkClick(double mouseX, double mouseY) {
        if (!isVisible) return false;
        return (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height);
    }
}