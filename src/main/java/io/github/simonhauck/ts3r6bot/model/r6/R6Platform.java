package io.github.simonhauck.ts3r6bot.model.r6;

public enum R6Platform {

    UPLAY("uplay"),
    PLAYSTATION("playstation"),
    XBOX("xbox");

    private String _name;

    /**
     * @param name of the platform in the properties
     */
    R6Platform(String name) {
        _name = name;
    }

    /**
     * get the {@link R6Platform} by the given name
     *
     * @param name can not be {@code null} or empty
     * @return the platform or null if not found
     */
    public static R6Platform getR6PlatformByName(String name) {
        assert name != null;
        for (R6Platform platform : R6Platform.values()) {
            if (name.equals(platform._name)) return platform;
        }

        return null;
    }

}
