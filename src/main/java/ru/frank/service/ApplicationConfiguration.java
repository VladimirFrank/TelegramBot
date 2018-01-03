package ru.frank.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.TelegramBotsApi;
import ru.frank.bot.MegaUltraBot;
import ru.frank.dataParser.ExcelReader;
import ru.frank.service.fileService.FilePathUploader;
import ru.frank.service.fileService.UserAccessChecker;

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

    @Bean
    public FilePathUploader filePathUploader(){
        return new FilePathUploader();
    }

    @Bean
    public UserAccessChecker userAccessChecker(){
        return new UserAccessChecker();
    }

}
