import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * TestDate Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>12ÔÂ 12, 2019</pre>
 */
public class TestDateTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: Date()
     */
    @Test
    public void testDate() throws Exception {
        assertThat(TestDate.Date(), is("2019-07-26 20:56:06"));
    }
}
