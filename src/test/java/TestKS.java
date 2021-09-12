import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;

import java.math.BigInteger;
import java.util.Arrays;

public class TestKS {
    static TypeACurveGenerator pg = new TypeACurveGenerator(512, 512);
    static PairingParameters typeAParams = pg.generate();
    static Pairing pairing = PairingFactory.getPairing(typeAParams);
    static Element P = pairing.getG1().newRandomElement();
    static Element[] Qs;
    static Element Q;
    static Element s;

    public static Element[] genSpw(Element pwP_star, int n, int t) {
        Element[][] a = new Element[n + 1][t];
        Element[][] aP = new Element[n + 1][t];
        Element[][] f = new Element[n + 1][n + 1];
        for (int i = 1; i < n + 1; i++) {
            Arrays.fill(f[i], pairing.getZr().newElement().getImmutable());
        }
        for (int i = 1; i < n + 1; i++) {
            for (int j = 0; j < t; j++) {
                a[i][j] = pairing.getZr().newRandomElement().getImmutable();
                aP[i][j] = P.duplicate().mulZn(a[i][j].duplicate());
            }
        }

        for (int i = 1; i < n + 1; i++) {
            for (int j = 1; j < n + 1; j++) {
                if (i == j) continue;
                for (int gamma = 0; gamma < t; gamma++) {
                    //fi(j) = ai,0 + ai,1 * j + ai,2 * j^2 + ... + ai,t-1 * j^t-1
                    f[i][j] = f[i][j].duplicate().add(
                            a[i][gamma].duplicate().pow(BigInteger.valueOf((long) Math.pow(j, gamma))));
                }
            }
        }

        //check Eq.1
        boolean checkResult = true;
        for (int i = 1; i < n + 1; i++) {
            for (int j = 1; j < n + 1; j++) {
                if (i == j) continue;
                //left = fj(i) * P
                Element left = P.duplicate().mulZn(f[j][i].duplicate());
                //right = aj,0 * P + i * aj,1 * P + i^2 * aj,2 * P + ... + i^t-1 * aj,t-1 * P
                Element right = pairing.getG1().newElement().getImmutable();
                Element temp = pairing.getZr().newElement().getImmutable();
                for (int gamma = 0; gamma < t; gamma++) {
                    temp = temp.duplicate().add(
                            a[j][gamma].duplicate().pow(BigInteger.valueOf((long) Math.pow(i, gamma)))).getImmutable();
                    right = right.duplicate().mul(
                            aP[j][gamma].duplicate().pow(BigInteger.valueOf((long) Math.pow(i, gamma)))).getImmutable();
                }
                Element tempp = P.duplicate().mulZn(temp).getImmutable();
                if (right != left) {
                    checkResult = false;
                    System.out.println("Eq.1 does not hold");
                    break;
                }
            }
        }

        Element[] ss = new Element[n + 1];
        Qs = new Element[n + 1];
        Arrays.fill(ss, pairing.getZr().newElement().getImmutable());
        Arrays.fill(Qs, pairing.getG1().newElement().getImmutable());
        for (int i = 1; i < n + 1; i++) {
            for (int j = 1; j < n + 1; j++) {
                ss[i] = ss[i].duplicate().add(f[j][i]).getImmutable();
            }
            Qs[i] = P.duplicate().mulZn(ss[i].duplicate());
        }

        s = pairing.getZr().newElement().getImmutable();
        Q = pairing.getG1().newElement().getImmutable();

        for (int i = 1; i < n + 1; i++) {
            s = s.duplicate().add(a[i][0]).getImmutable();
        }
        Q = P.duplicate().mulZn(s.duplicate()).getImmutable();

        Element[] sigma_star = new Element[n + 1];

        for (int i = 1; i < n + 1; i++) {
            sigma_star[i] = pwP_star.duplicate().mulZn(ss[i].duplicate()).getImmutable();
        }

        return sigma_star;
    }
}
