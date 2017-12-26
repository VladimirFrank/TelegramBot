import org.junit.Assert;
import org.junit.Test;
import ru.frank.Application;
import ru.frank.bot.MegaUltraBot;

/**
 * Created by sbt-filippov-vv on 26.12.2017.
 */
public class MegaUltraBotTest {



    @Test
    public void sendMessageTest1(){
        MegaUltraBot megaUltraBot = new MegaUltraBot();
        String botAnswer = megaUltraBot.parseIncomingText("А 2-1-1");
        System.out.println(botAnswer);
    }

}
