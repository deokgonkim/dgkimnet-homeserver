package homeserver.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.BotSession;

@Service
public class TelegramBot extends TelegramLongPollingBot {
    
    private static final Logger LOG = LoggerFactory.getLogger(TelegramBot.class);
    
    private static final String BOT_TOKEN_FILE = ".bot_token";
    private static final String BOT_CHAT_ID_FILE = ".bot_chat_id";
    
    private String botToken = null;
    private long chatId = 0L;
    
    private BotSession botSession = null;
    
    static {
        // This should called first.
        ApiContextInitializer.init();
    }
    
    @PostConstruct
    public void initTelegramBot() {
        TelegramBotsApi api = new  TelegramBotsApi();
        
        try {
            botSession = api.registerBot(this);
        } catch (TelegramApiRequestException e) {
            LOG.warn(e.getMessage(), e);
        }
    }
    
    @PreDestroy
    public void endBotSession() {
        if (botSession != null) {
            botSession.stop();
        }
    }
    
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            long chatId = message.getChatId();
            if (chatId != this.chatId) {
                this.chatId = chatId;
                persistChatId(chatId);
            }
            this.sendMessage("Re: " + message.getText());
        } else {
            LOG.info("onUpdateReceived : update doesn't have message?");
        }
        LOG.info("Message received : {} - {}", update.getUpdateId(), update.getMessage());
        LOG.info("id {}", update.getMessage().getChatId());
    }
    
    @Override
    public String getBotUsername() {
        return "dgkimnetbot";
    }
    
    @Override
    public String getBotToken() {
        if (botToken == null) {
            botToken = getFileContent(System.getProperty("user.home")+ System.getProperty("file.separator") + BOT_TOKEN_FILE);
        }
        return botToken;
        //return System.getProperty("bot_token");
    }
    
    public void sendMessage(String message) {
        if (chatId == 0L) {
            readChatId();
        }
        if (chatId == 0L) {
            LOG.info("Telegram ChatId is not yet persisted");
            LOG.info("Please send some text to this bot, so this bot can catch chatId");
            LOG.info("Not sending - {}", message);
            return;
        }
        SendMessage sendMessage = new SendMessage()
                .setChatId(chatId)
                .setText(message);
        
        try {
            LOG.info("Sending message : {}", message);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOG.warn(e.getMessage(), e);
        }
    }
    
    public void readChatId() {
        String str = getFileContent(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + BOT_CHAT_ID_FILE);
        if (str != null && str.length() > 1) {
            chatId = Long.valueOf(str).longValue();
        }
    }
    
    public void persistChatId(long chatId) {
        String fileName = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + BOT_CHAT_ID_FILE;
        LOG.info("Storing chatId {} in {}", chatId, fileName);
        persistFileContent(fileName, String.valueOf(chatId));
    }
    
    private static String getFileContent(String fileName) {
        String str = null;
        File f = new File(fileName);
        try (FileInputStream fis = new FileInputStream(f);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {
            str = br.readLine();
        } catch (FileNotFoundException e) {
            LOG.warn(e.getMessage(), e);
        } catch (IOException e) {
            LOG.warn(e.getMessage(), e);
        }
        return str;
    }
    
    private static void persistFileContent(String fileName, String content) {
        File f = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(f);
             OutputStreamWriter osw = new OutputStreamWriter(fos);
             BufferedWriter bw = new BufferedWriter(osw)) {
            bw.write(content);
        } catch (FileNotFoundException e) {
            LOG.warn(e.getMessage(), e);
        } catch (IOException e) {
            LOG.warn(e.getMessage(), e);
        }
    }
}
