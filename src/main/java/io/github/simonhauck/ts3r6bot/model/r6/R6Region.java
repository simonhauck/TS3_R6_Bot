package io.github.simonhauck.ts3r6bot.model.r6;

public enum R6Region {

    EUROPE("EU"),
    ASIA("ASIA"),
    AMERICA("NA");

    private String _name;

    R6Region(String name) {
        _name = name;
    }

    /**
     * @param name
     * @return
     */
    public static R6Region getR6RegionByName(String name) {
        assert name != null;

        for (R6Region region : R6Region.values()) {
            if (region._name.equals(name)) return region;
        }

        return null;
    }
}
