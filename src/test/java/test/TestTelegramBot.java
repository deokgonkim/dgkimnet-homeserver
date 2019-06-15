package test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import homeserver.service.TelegramBot;

public class TestTelegramBot {
    
    private static final Logger LOG = LoggerFactory.getLogger(TestTelegramBot.class);

    @Test
    public void test() {
        
        try {
            TelegramBot bot = new TelegramBot();
            bot.initTelegramBot();
            
            bot.sendMessage("Hi this is test");
            Thread.sleep(10000);
            bot.sendMessage("Hi this is test2");
            Thread.sleep(10000);
            bot.sendMessage("Hi this is test3");
        } catch (InterruptedException e) {
            LOG.debug(e.getMessage(), e);
        }
    }

}
