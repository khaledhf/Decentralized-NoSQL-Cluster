package org.project.configuration;

public final class DBConfig {
    public static final String BASE_DIRECTORY = "Database";
    public static final String FILE_EXTENSION = ".json";
    public static final int NODES_NUMBER = 4;

    private DBConfig() {
        throw new AssertionError("Cannot instantiate " + getClass().getName());
    }
}

