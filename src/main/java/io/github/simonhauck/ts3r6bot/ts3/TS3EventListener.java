package io.github.simonhauck.ts3r6bot.ts3;

import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public interface TS3EventListener {

    /**
     * called when a client joins the server
     *
     * @param clientJoinEvent can not be {@code null}
     */
    void onClientJoin(ClientJoinEvent clientJoinEvent);

    /**
     * called when a text message is received
     *
     * @param textMessageEvent can not be {@code null}
     */
    void onTextMessageReceived(TextMessageEvent textMessageEvent);
}
