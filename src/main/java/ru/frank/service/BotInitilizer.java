package ru.frank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.frank.bot.MegaUltraBot;

/**
 * Created by sbt-filippov-vv on 21.12.2017.
 */

@Controller
public class BotInitilizer {

    private MegaUltraBot megaUltraBot;
    private TelegramBotsApi telegramBotsApi;


    @Autowired
    public BotInitilizer(MegaUltraBot megaUltraBot, TelegramBotsApi telegramBotsApi){
        ApiContextInitializer.init();
        try{
            telegramBotsApi.registerBot(megaUltraBot);
        } catch (TelegramApiException ex){
            ex.printStackTrace();
        }

    }

}
