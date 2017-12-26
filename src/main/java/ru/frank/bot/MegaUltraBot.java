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
import ru.frank.service.fileService.FilePathUploader;

import java.io.IOException;

@Component
public class MegaUltraBot extends TelegramLongPollingBot{

    private final String BOT_USER_NAME = "MegaUltraBot";
    private final String TOKEN = "458631815:AAFChJilHO8JIkske5O0kXntuCGP68XTi3s";

    // TODO Move that path to configuration file, not hardcoded.
    //private String pathToExcelFile = "F:\\JavaProjects\\TelegramBot\\src\\main\\resources\\Test_Document.xlsx";

    @Autowired
    private FilePathUploader filePathUploader;

    @Autowired
    private ExcelReader excelReader;


    // Getting new message and answer it.
    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if(message != null && message.hasText()){
            String botAnswer = parseIncomingText(message.getText());
            sendMessage(message, botAnswer);
        }
    }

    // Parse incoming message and return answer String
    public String parseIncomingText(String textToParse){

        String textToParseWithoutOffice;
        String officeLetter = textToParse.substring(0, 1);
        // List of bot's commands
        if(textToParse.contains("/help")){
            return "Пришли номер розетки или номер порта и я отвечу тебе, где это и чье.";
            // Attention! Russian letters used in .contain() method ! //
        } else if(officeLetter.equalsIgnoreCase("А")){
            if(textToParse.contains("-")){
                textToParseWithoutOffice = textToParse.substring(1).replaceAll("-", ".").replaceAll(" ", "");
                try{
                    return excelReader.findLineInExcelFile(getPathToCrossJournal(officeLetter), textToParseWithoutOffice);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else if(officeLetter.equalsIgnoreCase("T")){
            if(textToParse.contains("-")){
                textToParseWithoutOffice = textToParse.substring(1).replaceAll("-", ".").replaceAll(" ", "");
                try{
                    return excelReader.findLineInExcelFile(getPathToCrossJournal(officeLetter), textToParseWithoutOffice);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return "Не понятная команда, для вызова меню команд, введите /help";
    }

    private String getPathToCrossJournal(String officeLetter){
        return filePathUploader.getCrossJournalPath(officeLetter);
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
