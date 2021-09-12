import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class TestParams {
    public static Map<Element, Element> params() throws UnsupportedEncodingException {
        TypeACurveGenerator pg = new TypeACurveGenerator(512, 512);
        PairingParameters typeAParams = pg.generate();
        Pairing pairing = PairingFactory.getPairing(typeAParams);

        Element element = pairing.getG1().newRandomElement().getImmutable();
        byte[] bytes = element.toBytes();
        String s = new String(bytes, "ISO8859-1");
        Element element1 = pairing.getG1().newElementFromBytes(s.getBytes("ISO8859-1"));
        Map<Element, Element> map = new HashMap<>();
        map.put(element, element1);
        return map;
    }
}
