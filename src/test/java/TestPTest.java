import it.unisa.dia.gas.jpbc.Element;
import org.junit.Test;
import util.ArraysUtil;

public class TestPTest {
    @Test
    public void testBlindPw() {
        Element pwP_star = TestP.blindPw("123").getImmutable();
        Element[] sigma_star = TestKS.genSpw(pwP_star, 5, 3);
        Element spwP = TestP.getSpw(sigma_star, pwP_star, TestKS.Qs, "123", 3, TestKS.Q, 5, TestKS.P, TestKS.s);
        TestP.register_P(spwP, new String[]{"ks1", "ks2", "ks3", "ks4", "ks5"}, "cs", "h", "123", pwP_star, 5);
    }

    @Test
    public void testElementToString() {
        Element[] elements = new Element[6];
        for (int i = 1; i < 6; i++) {
            elements[i] = TestP.pairing.getG1().newRandomElement().getImmutable();
        }
        ArraysUtil.ElementString es = ArraysUtil.elementToString(elements);
        System.out.println(es);

        Element[] elements1 = ArraysUtil.stringToElement(TestP.pairing, es);
        for (int i = 1; i < 6; i++) {
            System.out.println(elements[i].isEqual(elements1[i]));
        }
    }
}
