import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import testcases.AuthTest;
import testcases.FeedbackTest;
import testcases.OrderTest;
import testcases.ProductTest;
import testcases.UserUpdateTests;

@Suite
@SelectClasses({
        AuthTest.class,
        FeedbackTest.class,
        OrderTest.class,
        ProductTest.class,
        UserUpdateTests.class
})
public class AllTestSuite {
}