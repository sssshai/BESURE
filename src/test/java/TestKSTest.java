import org.junit.Test;


public class TestKSTest {
    @Test
    public void testGenSpw() {
        TestKS.genSpw(TestP.blindPw("123"), 5, 3);
    }
}