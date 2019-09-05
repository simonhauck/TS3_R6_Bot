package io.github.simonhauck.ts3r6bot.ts3;

public interface ITS3EventDispatcher {

    /**
     * register a new {@link TS3EventListener}
     *
     * @param listener can not be {@code null}
     */
    void registerEventListener(TS3EventListener listener);

    /**
     * unregister a {@link TS3EventListener}
     *
     * @param listener can not be {@code null}
     */
    void unregisterEventListener(TS3EventListener listener);
}
