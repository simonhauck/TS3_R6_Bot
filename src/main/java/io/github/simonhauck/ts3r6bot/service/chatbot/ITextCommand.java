package io.github.simonhauck.ts3r6bot.service.chatbot;

import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import io.github.simonhauck.ts3r6bot.ts3.TS3Service;

public interface ITextCommand {

    /**
     * get the starting command
     *
     * @return can not be {@code null}
     */
    String getStartingCommand();

    /**
     * @return an explanation for the command with the parameters
     */
    String getExplanation();

    /**
     * check if the command is valid
     *
     * @param event the textmessage event. Can not be {@code null}
     * @return true if the command and the params are valid
     */
    boolean isCommandValid(TextMessageEvent event);

    /**
     * called when a message from a user is available
     *
     * @param event   can not be {@code null}
     * @param service can not be {@code null}
     */
    void onMessageReceived(TextMessageEvent event, TS3Service service);

}
