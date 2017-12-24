package ru.frank.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.TelegramBotsApi;
import ru.frank.bot.MegaUltraBot;
import ru.frank.dataParser.ExcelReader;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public TelegramBotsApi telegramBotsApi(){
        return new TelegramBotsApi();
    }

    @Bean
    public ExcelReader excelReader(){
        return new ExcelReader();
    }

}
