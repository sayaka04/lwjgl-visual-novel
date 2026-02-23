package Data;

import java.util.List;
import java.util.Map;

// The Root Object that matches a Scene JSON file
public class StoryData {

    public Map<String, DialogueNode> script;

    // --- Nested Classes ---

    public static class Choice {
        public String text;
        public String target;
    }

    public static class DialogueNode {
        // Changed to String so it can read "bg_lounge" instead of numbers!
        public Map<String, String> setAudio;
        public Map<String, String> setVisuals;

        public String speaker;
        public String text;

        // Routing variables
        public List<Choice> choices;
        public String proceedTo;
        public String loadNextScript;

        public boolean hasSetAudio() { return setAudio != null && !this.setAudio.isEmpty(); }
        public boolean hasSetVisuals() { return setVisuals != null  && !this.setVisuals.isEmpty(); }
        public boolean hasChoices() { return this.choices != null && !this.choices.isEmpty(); }
        public boolean hasProceedTo() { return this.proceedTo != null && !this.proceedTo.isEmpty(); }
        public boolean hasLoadNextScript() { return this.loadNextScript != null && !this.loadNextScript.isEmpty(); }
        public boolean hasSpeaker() { return this.speaker != null && !this.speaker.isEmpty(); }
        public boolean hasText() { return this.text != null && !this.text.isEmpty(); }

        public String getChoiceText(int index) {
            return choices.get(index).text;
        }
    }

    public DialogueNode scriptDialogue;
    public String currentScene;
    public String currentDialogue;
    public String currentNodeId = "1";

    public void setScriptDialogue(String index){
        this.currentNodeId = index;
        this.scriptDialogue = this.script.get(index);
    }
}