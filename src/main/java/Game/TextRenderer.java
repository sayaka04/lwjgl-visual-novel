package Game;

import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public class TextRenderer {
    private int textureId;
    private STBTTBakedChar.Buffer charData;
    private int fontHeight;
    private int textureSize = 512; // 512x512 is plenty of room for one font

    public TextRenderer(String ttfPath, int fontHeight) {
        this.fontHeight = fontHeight;
        this.charData = STBTTBakedChar.malloc(96); // We load 96 standard keyboard characters

        try {
            // 1. Read the raw TTF file into memory
            byte[] fileBytes = Files.readAllBytes(Paths.get(ttfPath));
            ByteBuffer ttfBuffer = MemoryUtil.memAlloc(fileBytes.length);
            ttfBuffer.put(fileBytes).flip();

            // 2. Create an empty image (bitmap) in memory
            ByteBuffer bitmap = MemoryUtil.memAlloc(textureSize * textureSize);

            // 3. Bake the letters from the TTF file into our image!
            STBTruetype.stbtt_BakeFontBitmap(ttfBuffer, fontHeight, bitmap, textureSize, textureSize, 32, charData);
            MemoryUtil.memFree(ttfBuffer); // Delete the raw file from memory

            // 4. Send our new alphabet image to the Graphics Card
            textureId = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, textureId);

            // Text fonts only use the Alpha (transparency) channel
            glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, textureSize, textureSize, 0, GL_ALPHA, GL_UNSIGNED_BYTE, bitmap);

            // Keep the text smooth
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

            MemoryUtil.memFree(bitmap); // Delete the temp image from RAM
        } catch (Exception e) {
            System.out.println("Failed to load font: " + ttfPath);
            e.printStackTrace();
        }
    }

    public void drawText(String text, float x, float y) {
        // Bind our alphabet texture
        glBindTexture(GL_TEXTURE_2D, textureId);

        glBegin(GL_QUADS);

        try (MemoryStack stack = stackPush()) {
            // STB draws from the bottom-left of the text, so we push the Y down so it draws like normal
            FloatBuffer xBuffer = stack.floats(x);
            FloatBuffer yBuffer = stack.floats(y + fontHeight);
            STBTTAlignedQuad quad = STBTTAlignedQuad.malloc(stack);

            // Loop through every letter in your word
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);

                // Skip letters we didn't load (like emojis)
                if (c < 32 || c >= 128) continue;

                // Do the math to find this specific letter on the invisible texture
                STBTruetype.stbtt_GetBakedQuad(charData, textureSize, textureSize, c - 32, xBuffer, yBuffer, quad, true);
                // Draw the letter!
                glTexCoord2f(quad.s0(), quad.t0()); glVertex2f(quad.x0(), quad.y0()); // Top-Left
                glTexCoord2f(quad.s1(), quad.t0()); glVertex2f(quad.x1(), quad.y0()); // Top-Right
                glTexCoord2f(quad.s1(), quad.t1()); glVertex2f(quad.x1(), quad.y1()); // Bottom-Right
                glTexCoord2f(quad.s0(), quad.t1()); glVertex2f(quad.x0(), quad.y1()); // Bottom-Left
            }
        }

        glEnd();
    }
}