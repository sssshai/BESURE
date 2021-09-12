import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * TestPath Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>12ÔÂ 12, 2019</pre>
 */
public class TestPathTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: realName(String filePath)
     */
    @Test
    public void testRealName() throws Exception {
        assertThat(TestPath.realName("D:\\java\\IdeaProjects\\ESP\\out\\artifacts\\ESP_war_exploded\\WEB-INF\\upload\\7_1_api-ms-win-core-console-l1-1-0.dll"),
                is("api-ms-win-core-console-l1-1-0.dll")
        );
    }
}
