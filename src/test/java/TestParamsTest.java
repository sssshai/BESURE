import it.unisa.dia.gas.jpbc.Element;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * TestParams Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>12ÔÂ 12, 2019</pre>
 */
public class TestParamsTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: params()
     */
    @Test(timeout = 4000)
    public void testParams() throws Exception {
        Map<Element, Element> map = TestParams.params();
        for (Map.Entry<Element, Element> entry : map.entrySet()) {
            assertThat(entry.getKey(), is(entry.getValue()));
        }
    }
}
