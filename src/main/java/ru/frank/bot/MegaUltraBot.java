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

        if(textToParse.contains("/help")){
            return "Пришли номер розетки или номер порта и я отвечу тебе, где это и чье.";
        }

        if(textToParse.contains("-")){
            try {
                return getNetworkRosette(textToParse);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(textToParse.contains(".")){
            try {
                return getIpInformation(textToParse);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return "Не понятная команда, для вызова меню команд, введите /help";
    }

    /**
     *
     * @param lineForFind - rosette number to find
     * @return String
     * @throws IOException
     */
    private String getNetworkRosette(String lineForFind) throws IOException{
        String rosetteToFind = lineForFind.substring(1).replaceAll(" ", "").replaceAll("-", ".");
        if(lineForFind.contains("а") || lineForFind.contains("А")){
            return excelReader.findLineInExcelFile(getPathToCrossJournal("а"), rosetteToFind);
        } else if(lineForFind.contains("т") || lineForFind.contains("Т")){
            return excelReader.findLineInExcelFile(getPathToCrossJournal("т"), rosetteToFind);
        } else if(lineForFind.contains("к") || lineForFind.contains("К")){
            return excelReader.findLineInExcelFile(getPathToCrossJournal("к"), rosetteToFind);
        } else{
            return "Не найден офис с таким ID.";
        }
    }

    private String getIpInformation(String lineForFind) throws IOException{
        String ipInfoToFind = lineForFind.substring(1).replaceAll(" ", "");
        if(lineForFind.contains("а") || lineForFind.contains("А")){
            return excelReader.findLineInExcelFile(getPathToIpJournal("а"), ipInfoToFind);
        } else if(lineForFind.contains("т") || lineForFind.contains("Т")){
            return excelReader.findLineInExcelFile(getPathToIpJournal("т"), ipInfoToFind);
        } else if(lineForFind.contains("к") || lineForFind.contains("К")){
            return excelReader.findLineInExcelFile(getPathToIpJournal("к"), ipInfoToFind);
        } else{
            return "Не найден офис с таким ID.";
        }
    }

    /**
     * Get path to excel file by OfficeLetter (WARNING! Russian Letters in method!)
     * A - Aurora
     * T - Technopark
     * K - Kronos
     * @param officeLetter
     * @return String PathToFile;
     */
    private String getPathToCrossJournal(String officeLetter){
        return filePathUploader.getCrossJournalPath(officeLetter);
    }

    private String getPathToIpJournal(String officeLetter){
        return filePathUploader.getIPJournalPath(officeLetter);
    }

    private void sendMessage(Message message, String text){
        // Temporary verification
        // TODO Replace verification to finding method
        if(text == null || text.equals("")){
            text = "Не найдена информация по вашему запросу";
        }
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
