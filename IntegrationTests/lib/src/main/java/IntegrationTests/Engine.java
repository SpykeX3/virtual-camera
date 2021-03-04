package IntegrationTests;

import java.io.IOException;

public class Engine {
    private Process engine;

    public Engine(String location) {
        String cmd = location + Gradle.gradle() + " run";
        try {
            engine = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            throw new RuntimeException("Failed to start engine: " + cmd, e);
        }
    }

    public void stop() {
        if (engine != null) {
            engine.destroyForcibly();
            engine = null;
        }
    }
}
