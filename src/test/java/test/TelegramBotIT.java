package test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import homeserver.service.TelegramBot;

public class TelegramBotIT {
    
    private static final Logger LOG = LoggerFactory.getLogger(TelegramBotIT.class);

    @Test
    public void test() {
        
        try {
            TelegramBot bot = new TelegramBot();
            bot.initTelegramBot();
            
            bot.sendMessage("Hi this is test");
            Thread.sleep(5000);
            bot.sendMessage("Hi this is test2");
            Thread.sleep(5000);
            bot.sendMessage("Hi this is test3");
        } catch (InterruptedException e) {
            LOG.debug(e.getMessage(), e);
        }
    }

}
