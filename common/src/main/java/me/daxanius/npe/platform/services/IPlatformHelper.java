package me.daxanius.npe.platform.services;

import java.nio.file.Path;

public interface IPlatformHelper {
    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    Path getConfigDir();
}
