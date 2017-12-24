package ru.frank.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.frank.dataParser.ExcelReader;

import java.io.IOException;

@Component
public class MegaUltraBot extends TelegramLongPollingBot{

    private final String BOT_USER_NAME = "MegaUltraBot";
    private final String TOKEN = "458631815:AAFChJilHO8JIkske5O0kXntuCGP68XTi3s";

    // TODO Move that path to configuration file, not hardcoded.
    private String pathToExcelFile = "F:\\JavaProjects\\TelegramBot\\src\\main\\resources\\Test_Document.xlsx";

    @Autowired
    private ExcelReader excelReader;


    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        String textMessage;
        if(message != null && message.hasText()){
            String botAnswer = parseIncomingText(message.getText());
            sendMessage(message, botAnswer);
        }
    }

    private String parseIncomingText(String textToParse){
        // List of bot's commands
        if(textToParse.contains("/help")){
            return "Пришли номер розетки или номер порта и я отвечу тебе, где это и чье.";
        } else if(textToParse.contains("/") || textToParse.contains(".")){
            try {
                return excelReader.findRowByValue(pathToExcelFile, textToParse);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "Не понятная команда, для вызова меню команд, введите /help";
    }

    private void sendMessage(Message message, String text){
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try{
            sendMessage(sendMessage);
        } catch (TelegramApiException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return this.BOT_USER_NAME;
    }

    @Override
    public String getBotToken() {
        return this.TOKEN;
    }
}
