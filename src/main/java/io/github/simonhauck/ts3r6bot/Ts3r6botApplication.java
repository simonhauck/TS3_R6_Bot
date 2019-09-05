package io.github.simonhauck.ts3r6bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Ts3r6botApplication {

    private static final Logger LOG = LoggerFactory.getLogger(Ts3r6botApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(Ts3r6botApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void applicationReady() {
        LOG.info("Application ready...");
    }
}
