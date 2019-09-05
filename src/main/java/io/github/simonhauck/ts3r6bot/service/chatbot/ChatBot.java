package io.github.simonhauck.ts3r6bot.service.chatbot;

import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import io.github.simonhauck.ts3r6bot.persistence.UserServicePersistence;
import io.github.simonhauck.ts3r6bot.ts3.TS3FilteredEventListener;
import io.github.simonhauck.ts3r6bot.ts3.TS3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
public class ChatBot extends TS3FilteredEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(ChatBot.class);

    private TS3Service _ts3Service;
    private int _botId;
    private Locale _locale;

    private UserServicePersistence _persistence;
    private Environment _env;

    private List<ITextCommand> _commands;

    //------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------

    @Autowired
    public ChatBot(TS3Service service, UserServicePersistence userServicePersistence, Environment environment) {
        _ts3Service = service;
        _persistence = userServicePersistence;
        _env  = environment;
        _botId = service.getTs3Api().whoAmI().getId();
        _locale = Locale.forLanguageTag(Objects.requireNonNull(_env.getProperty("noob.locale")));

        //Add commands here
        _commands = new ArrayList<>();
        _commands.add(new HelloResponse());
        _commands.add(new RegisterUser(_persistence));
        _commands.add(new NoobInfo(_persistence, _locale));
    }

    //------------------------------------------------------------------------------------------------------------------
    // TS3FilteredEventListener methods
    //------------------------------------------------------------------------------------------------------------------

    @Override
    public void onTextMessageReceived(TextMessageEvent textMessageEvent) {
        //Ignore messages from bot
        if (textMessageEvent.getInvokerId() == _botId) return;

        //Ignore messages that are not for the bot
        if (!textMessageEvent.getMessage().startsWith("!")) return;

        LOG.info("Text message received from " + textMessageEvent.getInvokerName() + " Message: " + textMessageEvent.getMessage());
        String receivedCommand = textMessageEvent.getMessage().split(" ")[0];
        for (ITextCommand command : _commands) {

            //Check if the correct command was found
            if (receivedCommand.equals(command.getStartingCommand())) {

                if (command.isCommandValid(textMessageEvent)) {
                    //Command valid, evaluate the command
                    LOG.debug("Text Command found, command is valid");
                    command.onMessageReceived(textMessageEvent, _ts3Service);
                    return;
                } else {
                    //Send help information
                    LOG.debug("Text Command found, but command is invalid!");
                    sendExplanation(textMessageEvent, command);
                    return;
                }

            }
        }

        LOG.info("No corresponding text command found.");
        sendAllCommands(textMessageEvent.getTargetMode());
    }

    //------------------------------------------------------------------------------------------------------------------
    // Public methods
    //------------------------------------------------------------------------------------------------------------------

    /**
     * called after initializing the service. Register the required callbacks
     */
    @PostConstruct
    public void postConstruct() {
        LOG.info("ChatBot is registered and subscribing to TS3 callbacks...");
        _ts3Service.getEventDispatcher().registerEventListener(this);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Private methods
    //------------------------------------------------------------------------------------------------------------------

    /**
     * send an explanation in the server channel
     *
     * @param event   the text message event. Currently unused
     * @param command the command to get the explanation. Can not be {@code null}
     */
    private void sendExplanation(TextMessageEvent event, ITextCommand command) {
        String message = "Command Invalid: " + command.getExplanation();

        _ts3Service.getTs3AsyncApi().sendServerMessage(message);
    }

    /**
     * send all available commands to the ts3 server
     *
     * @param targetMode not used
     */
    private void sendAllCommands(TextMessageTargetMode targetMode) {
        String response = "All commands: \n";
        for (ITextCommand command : _commands) {
            response += "-" + command.getExplanation() + "\n";
        }

        _ts3Service.getTs3AsyncApi().sendServerMessage(response);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Get and Set methods
    //------------------------------------------------------------------------------------------------------------------
}
