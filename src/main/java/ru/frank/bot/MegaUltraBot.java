package ru.frank.bot;

import org.apache.log4j.Logger;
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
import ru.frank.service.fileService.UserAccessChecker;

import java.io.IOException;

@Component
public class MegaUltraBot extends TelegramLongPollingBot{

    private final String BOT_USER_NAME = "MegaUltraBot";
    private final String TOKEN = "";

    final static Logger logger = Logger.getLogger(MegaUltraBot.class.getName());

    @Autowired
    private FilePathUploader filePathUploader;

    @Autowired
    private ExcelReader excelReader;

    @Autowired
    private UserAccessChecker userAccessChecker;


    /**
     * Getting new message and answer it.
     *
     * @param update - This object represents an incoming update.
     */
    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        System.out.println(message.getFrom().getId());
        if(message.hasText() & userAccessChecker.checkUserAccess(message.getFrom().getId().toString())){
            logger.debug("Incoming message: " + message.getFrom().getId()
                    + " " + message.getFrom().getUserName() + ": " + message.getText());
            String botAnswer = parseIncomingText(message.getText());
            botAnswer = checkBotAswerLength(botAnswer);
            logger.error("Bot answer: " + botAnswer);
            sendMessage(message, botAnswer);
        }
    }

    // Parse incoming message and return answer String
    // TODO Add validation by regular expressions

    /**
     * Base method for build bot answer.
     *
     * @param textToParse - text from user's incoming message
     * @return
     */
    public String parseIncomingText(String textToParse){

        if(textToParse.contains("/start")){
            return "Здравствуй, коллега. Это ОПИТИ Бот. Я могу найти для тебя номер розетки, порт на коммутаторе, " +
                    "ip адрес или фамилию сотрудника. Пришли данные в формате: " +
                    "A XX.XX , где А - первая буква названия офиса, XX.XX - номер розетки, порта или ip адрес. " +
                    "Например, на запрос 'А 10.247.1.121' Бот ответит тебе информация по данному ip адресу (ФИО сотрудника, имя ПК и прочее). " +
                    "Для вызова помощи пришли мне /help";
        }

        if(textToParse.contains("/help")){
            return "Доступные команды:" +
                    "А ХХ.ХХ , где А - первая буква офиса, ХХ.ХХ - номер розетки, порта на коммутаторе или ip адрес." +
                    " Например, команда Т 10.247.1.110 - пришлет информацию по ip адресу (ФИО пользователя, если есть) на площадке Технопарк.";
        }

        // Find network rosette
        if(textToParse.length() < 10 && textToParse.contains(".")){
            try {
                return getNetworkRosette(textToParse);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Find ip address and username
        if(textToParse.length() > 10){
            try {
                return getIpInformation(textToParse);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "Не понятная команда, для вызова меню команд, введите /help";
    }

    /**
     * Finds line with param in excel file.
     *
     * @param lineForFind - rosette number to find
     * @return String
     * @throws IOException
     */
    private String getNetworkRosette(String lineForFind) throws IOException{
        String rosetteToFind = lineForFind.substring(1).replaceAll(" ", "");
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

    /**
     * Finds line with param in excel file.
     *
     * @param lineForFind - rosette number to find
     * @return String
     * @throws IOException
     */
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
     * Get path to excel file named "CrossJournal" by OfficeLetter (WARNING! Russian Letters in method!)
     * A - Aurora
     * T - Technopark
     * K - Kronos
     *
     * @param officeLetter
     * @return String PathToFile;
     */
    private String getPathToCrossJournal(String officeLetter){
        return filePathUploader.getCrossJournalPath(officeLetter);
    }

    /**
     * Get path to excel file named "IpJournal" by OfficeLetter (WARNING! Russian Letters in method!)
     * A - Aurora
     * T - Technopark
     * K - Kronos
     *
     * @param officeLetter
     * @return String PathToFile;
     */
    private String getPathToIpJournal(String officeLetter){
        return filePathUploader.getIPJournalPath(officeLetter);
    }

    /**
     * Send text constructed by Bot to user who's asking.
     *
     * @param message
     * @param text
     */
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

    /**
     * Bot's answer must be less than 4096 UTF-8 chars
     *
     * At this version maximum is 1024 chars at Bot's answer.
     * @param botAnswer
     * @return String botAnswer with modify length (less that 1024 chars).
     */
    private String checkBotAswerLength(String botAnswer){
        if(botAnswer.length() >  1024){
            return botAnswer.substring(0, 1024);
        } else{
            return botAnswer;
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
