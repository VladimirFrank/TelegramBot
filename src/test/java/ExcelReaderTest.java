import org.junit.Assert;
import org.junit.Test;
import ru.frank.dataParser.ExcelReader;

import java.io.IOException;

public class ExcelReaderTest {

    ExcelReader excelReader = new ExcelReader();
    String pathToFile = "F:\\JavaProjects\\TelegramBot\\src\\main\\resources\\Test_Document.xlsx";

    @Test
    public void findRowByValueTest1(){
        try {
            String result = excelReader.findRowByValue(pathToFile,
                    "1/0/1");
            System.out.println(result);
            Assert.assertTrue(result.equals("1.01 | 1/0/1 | "));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void findRowByValueTest2(){
        try {
            String result = excelReader.findRowByValue(pathToFile,
                    "1.05");
            System.out.println(result);
            Assert.assertTrue(result.equals("1.05 | 1/0/5 | "));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
