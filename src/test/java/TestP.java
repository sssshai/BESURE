import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;
import util.ArraysUtil;
import util.CryptoUtil;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

public class TestP {
    static TypeACurveGenerator pg = new TypeACurveGenerator(512, 512);
    static PairingParameters typeAParams = pg.generate();
    static Pairing pairing = PairingFactory.getPairing(typeAParams);
    private static final Element r = pairing.getZr().newRandomElement().getImmutable();
    private static Element skP = pairing.getZr().newRandomElement().getImmutable();
    private static Element pkP = TestKS.P.duplicate().mulZn(skP.duplicate().getImmutable());

    public static Element blindPw(String pwP) {
        return pairing.getG1().newElement().setFromHash(pwP.getBytes(), 0, pwP.getBytes().length).
                mulZn(r.duplicate()).getImmutable();
    }

    public static Element getSpw(Element[] sigma_star, Element pwP_star, Element[] Qs, String pwP, int gamma, Element Q, int n, Element P, Element s) {
        //check Eq.2
        for (int i = 1; i < n + 1; i++) {
            Element left = pairing.pairing(sigma_star[i], P).getImmutable();
            Element right = pairing.pairing(pwP_star, Qs[i]).getImmutable();
            if (!left.isEqual(right)) {
                System.out.println("Eq.2 does not hold");
            }
        }
        Element tmp = pairing.getG1().newElement().getImmutable();
        Element subtmp = pairing.getG1().newElement().getImmutable();
        for (int l = 1; l < gamma; l++) {
            for (int e = 1; e <= gamma; e++) {
                if (e == l) continue;
                if (e == 1) {
                    tmp = tmp.duplicate().pow(BigInteger.valueOf(1 / (e - l))).getImmutable();
                } else if (e - l == 1) {
                    tmp = tmp.duplicate().pow(BigInteger.valueOf(e)).getImmutable();
                } else {
                    tmp = tmp.duplicate().pow(BigInteger.valueOf(e / (e - l))).getImmutable();
                }
            }
            tmp = tmp.duplicate().mul(sigma_star[l].duplicate()).getImmutable();
            subtmp = subtmp.duplicate().add(tmp.duplicate()).getImmutable();
            tmp = pairing.getG1().newElement().getImmutable();
        }

        Element sigma_pw_temp = subtmp.duplicate().mulZn(r.negate().duplicate()).getImmutable();

        Element sigma_pw = pairing.getG1().newElement().setFromHash(pwP.getBytes(), 0, pwP.getBytes().length).getImmutable().mulZn(s.duplicate()).getImmutable();
        Element l_temp = pairing.pairing(sigma_pw_temp.duplicate(), P.duplicate()).getImmutable();
        Element r = pairing.pairing(pairing.getG1().newElement().setFromHash(pwP.getBytes(), 0, pwP.getBytes().length), Q.duplicate()).getImmutable();
        Element l = pairing.pairing(sigma_pw.duplicate(), P.duplicate()).getImmutable();
        if (!l.isEqual(r)) {
            System.out.println("Eq.3 does not hold");
        } else {
            try {
                byte[] b = ArraysUtil.mergeByte(sigma_pw.toBytes(), pwP.getBytes("ISO8859-1"));
                return pairing.getZr().newElementFromHash(b, 0, b.length).getImmutable();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void register_P(Element spwP, String[] idKS, String idCS, String idH, String idP, Element pwP_star, int n) {
        //compute au, send au to KS
        Element[] au = new Element[n + 1];
        for (int i = 1; i < n + 1; i++) {
            byte[] b1 = new byte[0];
            try {
                b1 = ArraysUtil.mergeByte(idKS[i - 1].getBytes("ISO8859-1"), spwP.toBytes());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            au[i] = pairing.getZr().newElementFromHash(b1, 0, b1.length);
        }
        //send cskP, idP, and auCS to CS
        byte[] cskP = CryptoUtil.AESEncrypt(CryptoUtil.getHash("SHA-256", spwP), skP.toBytes());
        try {
            byte[] b2 = ArraysUtil.mergeByte(idCS.getBytes("ISO8859-1"), spwP.toBytes());
            Element auCS = pairing.getZr().newElementFromHash(b2, 0, b2.length).getImmutable();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //send idP and auH to H
        try {
            byte[] b3 = ArraysUtil.mergeByte(idH.getBytes("ISO8859-1"), spwP.toBytes());
            Element auH = pairing.getZr().newElementFromHash(b3, 0, b3.length).getImmutable();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
