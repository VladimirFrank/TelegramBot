package ru.frank;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.TelegramBotsApi;
import ru.frank.bot.MegaUltraBot;

/**
 * Created by sbt-filippov-vv on 21.12.2017.
 */
@Configuration
public class ContextConfiguration {

    @Bean
    public TelegramBotsApi telegramBotsApi(){
        return new TelegramBotsApi();
    }

    @Bean
    public MegaUltraBot megaUltraBot(){
        return new MegaUltraBot();
    }


}
