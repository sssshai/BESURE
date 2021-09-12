import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import util.DeployUtil;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * DeployUtil Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>12月 12, 2019</pre>
 */
public class DeployUtilTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: deploy()
     */
    @Test
    public void testDeploy() throws Exception {
    }

    /**
     * Method: getDeployAddress()
     */
    @Ignore
    @Test
    public void testGetDeployAddress() throws Exception {
        DeployUtil.writeResourceFile();
    }

    /**
     * Method: clear()
     */
    @Test
    public void testClear() throws Exception {
    }

    /**
     * Method: getPath()
     */
    @Test
    public void testGetPath() throws Exception {
        System.out.println(new DeployUtil().getPath(new StringBuilder("/D:/java/IdeaProjects/BESURE/web/WEB-INF/")));
        assertThat(new DeployUtil().getPath(
                        new StringBuilder("/D:/java/IdeaProjects/BESURE/web/WEB-INF/classes/")),
                is("D:/java/IdeaProjects/BESURE/web/")
        );
    }
}
