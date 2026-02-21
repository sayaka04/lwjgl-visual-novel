package Components;

import org.lwjgl.openal.AL10;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;

public class BackgroundMusic {

    private final int bufferId;
    private final int sourceId;
    private boolean isPlaying = false;

    /**
     * Loads an .ogg file into OpenAL and prepares it for looping playback.
     * @param filepath The path to your .ogg file (e.g., "src/main/resources/bgm.ogg")
     */
    public BackgroundMusic(String filepath) {
        // 1. Generate the OpenAL Buffer and Source
        this.bufferId = alGenBuffers();
        this.sourceId = alGenSources();

        // 2. Load the OGG file using LWJGL's MemoryStack (prevents Java memory leaks)
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer channelsBuffer = stack.mallocInt(1);
            IntBuffer sampleRateBuffer = stack.mallocInt(1);

            // Decode the audio file into raw PCM data
            ShortBuffer rawAudioBuffer = stb_vorbis_decode_filename(filepath, channelsBuffer, sampleRateBuffer);

            if (rawAudioBuffer == null) {
                throw new RuntimeException("Failed to load audio file! Check the path: " + filepath);
            }

            // Get channels and sample rate
            int channels = channelsBuffer.get(0);
            int sampleRate = sampleRateBuffer.get(0);

            // Determine if it's Mono or Stereo
            int format = -1;
            if (channels == 1) {
                format = AL_FORMAT_MONO16;
            } else if (channels == 2) {
                format = AL_FORMAT_STEREO16;
            }

            // 3. Send the audio data to the OpenAL Buffer
            alBufferData(bufferId, format, rawAudioBuffer, sampleRate);

            // 4. CRUCIAL FOR LEAKS: Free the raw audio data from RAM now that OpenAL has it
            MemoryUtil.memFree(rawAudioBuffer);
        }

        // 5. Attach the Buffer to the Source (Put the record on the player)
        alSourcei(sourceId, AL_BUFFER, bufferId);

        // Default BGM settings
        setLooping(true);
        setVolume(0.5f); // Start at 50% volume
    }

    public void play() {
        // Only tell it to play if it isn't already playing
        int state = alGetSourcei(sourceId, AL_SOURCE_STATE);
        if (state != AL_PLAYING) {
            alSourcePlay(sourceId);
            isPlaying = true;
        }
    }

    public void pause() {
        alSourcePause(sourceId);
        isPlaying = false;
    }

    public void stop() {
        alSourceStop(sourceId);
        isPlaying = false;
    }

    public void setLooping(boolean loop) {
        alSourcei(sourceId, AL_LOOPING, loop ? AL_TRUE : AL_FALSE);
    }

    /**
     * Sets the volume of the track.
     * @param volume 0.0f is silent, 1.0f is max volume.
     */
    public void setVolume(float volume) {
        alSourcef(sourceId, AL_GAIN, volume);
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * CRUCIAL: Call this when closing the game or permanently swapping tracks.
     * If you don't call this, the audio will stay trapped in your RAM forever.
     */
    public void cleanup() {
        stop();
        alDeleteSources(sourceId);
        alDeleteBuffers(bufferId);
    }
}