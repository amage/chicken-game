package ru.highcode.chicken;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class GameData {
    private final Map<String, String> texts = new HashMap<>();

    private GameData() {
    }

    static GameData load() throws IOException {
        final GameData result = new GameData();
        Files.list(Paths.get("texts")).forEach(p -> {
            try {
                result.texts.put(p.getFileName().toString(), new String(Files.readAllBytes(p)));
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        return result;
    }

    public Map<String, String> getTexts() {
        return texts;
    }
}
