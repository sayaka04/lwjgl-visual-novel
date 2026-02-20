package Game;

import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.libc.LibCStdlib.free;

public class Sound {
    private int bufferId;
    private int sourceId;

    public Sound(String filepath) {
        // 1. Generate an OpenAL Buffer (This holds the raw audio data in memory)
        this.bufferId = alGenBuffers();

        // 2. Load the .ogg file using STB (just like STBImage!)
        try (MemoryStack stack = stackPush()) {
            IntBuffer channels = stack.mallocInt(1);
            IntBuffer sampleRate = stack.mallocInt(1);

            // Decode the OGG file into raw audio samples
            ShortBuffer rawAudioBuffer = STBVorbis.stb_vorbis_decode_filename(filepath, channels, sampleRate);
            if (rawAudioBuffer == null) {
                throw new RuntimeException("Failed to load OGG audio file: " + filepath);
            }

            // Figure out if it's Mono or Stereo
            int format = -1;
            if (channels.get(0) == 1) {
                format = AL_FORMAT_MONO16;
            } else if (channels.get(0) == 2) {
                format = AL_FORMAT_STEREO16;
            }

            // 3. Send the audio data to the Sound Card!
            alBufferData(bufferId, format, rawAudioBuffer, sampleRate.get(0));

            // Free the C-memory now that the Sound Card has it
            free(rawAudioBuffer);
        }

        // 4. Generate an OpenAL Source (This is an invisible "speaker" in your game)
        this.sourceId = alGenSources();

        // 5. Tell the speaker to play our loaded buffer
        alSourcei(sourceId, AL_BUFFER, bufferId);
    }

    public void play() {
        // Tell OpenAL to play the speaker!
        alSourcePlay(sourceId);
    }
}