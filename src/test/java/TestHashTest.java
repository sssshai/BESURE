import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * test.TestHash Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>12ÔÂ 11, 2019</pre>
 */
public class TestHashTest {
    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: hashKeyForDisk(String key)
     */
    @Test
    public void testHashKeyForDisk() throws Exception {
        assertThat(TestHash.hashKeyForDisk("test"), is("098f6bcd4621d373cade4e832627b4f6"));
    }

    /**
     * Method: fileHashValue(String filePath)
     */
    @Test
    public void testFileHashValue() throws Exception {
        assertEquals("88a162ddcca3654ffc4d7700fd02b574", TestHash.bytesToHexString(Objects.requireNonNull(
                TestHash.fileHashValue("D:\\java\\IdeaProjects\\BESURE\\BESURE.iml"))));
    }


    /**
     * Method: bytesToHexString(byte[] bytes)
     */
    @Test
    public void testBytesToHexString() throws Exception {
/*
try { 
   Method method = test.TestHash.getClass().getMethod("bytesToHexString", byte[].class);
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }
}
