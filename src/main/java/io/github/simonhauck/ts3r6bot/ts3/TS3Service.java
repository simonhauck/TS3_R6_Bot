package io.github.simonhauck.ts3r6bot.ts3;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.reconnect.ConnectionHandler;
import com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.Objects;

@Service()
public class TS3Service {

    private static final Logger LOG = LoggerFactory.getLogger(TS3Service.class);

    private Environment _environment;

    //TS3 Fields
    private final TS3Config _ts3Config;
    private TS3Query _ts3Query;
    private TS3Api _ts3Api;
    private TS3ApiAsync _ts3AsyncApi;

    private ConnectionHandler _connectionHandler;
    private TS3EventDispatcher _ts3EventDispatcher;

    //------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------

    @Autowired
    public TS3Service(Environment environment) {
        _environment = environment;
        _ts3EventDispatcher = new TS3EventDispatcher();
        _ts3Config = new TS3Config();
        intTs3Config(_ts3Config);

        _connectionHandler = new ConnectionHandler() {
            @Override
            public void onConnect(TS3Query ts3Query) {
                LOG.info("TS3Query connected...");
                _ts3Api = createApi(ts3Query);
                _ts3AsyncApi = createAsyncApi(ts3Query);
            }

            @Override
            public void onDisconnect(TS3Query ts3Query) {
                LOG.info("TS3Query disconnected...");
            }
        };

        _ts3Config.setConnectionHandler(_connectionHandler);

        LOG.info("Connecting query to TS3Server...");
        _ts3Query = new TS3Query(_ts3Config);
        _ts3Query.connect();
    }

    //------------------------------------------------------------------------------------------------------------------
    // Public methods
    //------------------------------------------------------------------------------------------------------------------

    @PreDestroy
    public void onExit() {
        LOG.info("OnExit: Closing teamspeak connection...");
        _ts3Query.exit();
    }

    /**
     * @return an instance of the event dispatcher
     */
    public ITS3EventDispatcher getEventDispatcher() {
        return _ts3EventDispatcher;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Private methods
    //------------------------------------------------------------------------------------------------------------------

    /**
     * set the values for the config
     *
     * @param ts3Config where the values should be set. Can not be {@code null}
     */
    private void intTs3Config(TS3Config ts3Config) {
        assert ts3Config != null;

        ts3Config.setHost(_environment.getProperty("ts3.hostname"));
        ts3Config.setQueryPort(Integer.valueOf(Objects.requireNonNull(_environment.getProperty("ts3.queryPort"))));
        ts3Config.setReconnectStrategy(ReconnectStrategy.constantBackoff());
    }

    /**
     * initialize the api from the query.
     * The username, password and the server will be selected
     *
     * @param ts3Query can not be {@code null}
     * @return the initialized api
     */
    private TS3Api createApi(TS3Query ts3Query) {
        assert ts3Query != null;
        TS3Api api = ts3Query.getApi();

        String username = _environment.getProperty("ts3.adminUserName");
        String password = _environment.getProperty("ts3.adminPassword");
        api.login(username, password);
        int virtualServerID = Integer.valueOf(Objects.requireNonNull(_environment.getProperty("ts3.virtualServerID")));
        api.selectVirtualServerById(virtualServerID);
        api.setNickname("Almighty Ts3Bot");
        api.registerAllEvents();
        api.addTS3Listeners(_ts3EventDispatcher);

        return api;
    }

    /**
     * create an instance of the {@link TS3ApiAsync} api
     *
     * @param ts3Query connected query. Can not be {@code null}
     * @return the initialized api
     */
    private TS3ApiAsync createAsyncApi(TS3Query ts3Query) {
        return ts3Query.getAsyncApi();
    }

    //------------------------------------------------------------------------------------------------------------------
    // Get and Set methods
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @return the ts3 api
     */
    public TS3Api getTs3Api() {
        return _ts3Api;
    }

    /**
     * @return the ts3 async api
     */
    public TS3ApiAsync getTs3AsyncApi() {
        return _ts3AsyncApi;
    }
}
