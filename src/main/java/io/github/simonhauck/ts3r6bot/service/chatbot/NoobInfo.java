package io.github.simonhauck.ts3r6bot.service.chatbot;

import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import io.github.simonhauck.ts3r6bot.model.ts3.TS3NoobUser;
import io.github.simonhauck.ts3r6bot.persistence.UserServicePersistence;
import io.github.simonhauck.ts3r6bot.ts3.TS3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoobInfo implements ITextCommand {

    private static final Logger LOG = LoggerFactory.getLogger(NoobInfo.class);

    private Locale _locale;
    private UserServicePersistence _servicePersistence;

    //------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------

    /**
     * create a noob info text module
     *
     * @param servicePersistence can not be {@code null}
     */
    public NoobInfo(UserServicePersistence servicePersistence, Locale locale) {
        assert servicePersistence != null;
        assert locale != null;
        _servicePersistence = servicePersistence;
        _locale = locale;
    }

    //------------------------------------------------------------------------------------------------------------------
    // ITextCommand methods
    //------------------------------------------------------------------------------------------------------------------

    @Override
    public String getStartingCommand() {
        return "!noob";
    }

    @Override
    public String getExplanation() {
        return "The bot lists all noob users with the remaining time. Command: !noob";
    }

    @Override
    public boolean isCommandValid(TextMessageEvent event) {
        return event.getMessage().equals("!noob");
    }

    @Override
    public void onMessageReceived(TextMessageEvent event, TS3Service service) {
        //Get the noobs
        List<TS3NoobUser> noobUsers = _servicePersistence.getAllActiveTS3NoobUser();

        if (noobUsers == null) {
            LOG.error("The query to get all noob users returned null for the list");
            return;
        }

        if (noobUsers.size() == 0) {
            service.getTs3AsyncApi().sendServerMessage("Currently are 0 noobs registered!");
        } else {
            LOG.info("Noobs requested. Query found " + noobUsers.size() + " users that are noobs");
            String result = "Currently are " + noobUsers.size() + " noobs registered! \n";

            //Date format
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", _locale);

            //Add all users
            for (TS3NoobUser noob : noobUsers) {
                String userString ="User "+noob.getTs3Name()+" is noob until "+dateFormat.format(new Date(noob.getNoobUntilTimeStamp()))+"\n";
                result += userString;
            }

            result += "-------------------------------------------------------";

            service.getTs3AsyncApi().sendServerMessage(result);
        }

        LOG.debug("Finished noob request");

    }

    //------------------------------------------------------------------------------------------------------------------
    // Public methods
    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------------------------------
    // Private methods
    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------------------------------
    // Get and Set methods
    //------------------------------------------------------------------------------------------------------------------
}
