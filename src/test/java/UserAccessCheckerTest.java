import org.junit.Assert;
import org.junit.Test;
import ru.frank.service.fileService.UserAccessChecker;

/**
 * Created by sbt-filippov-vv on 28.12.2017.
 */
public class UserAccessCheckerTest {

    UserAccessChecker userAccessChecker = new UserAccessChecker();

    @Test
    public void checkUserAccessTest1(){
        Assert.assertTrue(userAccessChecker.checkUserAccess("244100354"));
    }

    @Test
    public void checkUserAccessTest2(){
        Assert.assertFalse(userAccessChecker.checkUserAccess("12345123"));
    }
}
