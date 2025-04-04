import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import testcases.*;

@Suite
@SelectClasses({
        AuthTest.class,
        ProductTest.class,
        OrderTest.class,
        FeedbackTest.class,
        UserUpdateTests.class
})
public class AllTestSuite {

}