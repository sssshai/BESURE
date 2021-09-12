import org.junit.Test;
import tetryon.Fp;
import tetryon.G1;
import tetryon.G1Point;
import tetryon.Pairing;
import util.CryptoUtil;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestMul {
    @Test
    public void mul() throws Exception {
        Fp px = new Fp(new BigInteger("1e462d01d1861f7ee499bf70ab12ade335d98586b52db847ee2ec1e790170e04", 16));
        Fp py = new Fp(new BigInteger("14bd807f4e64904b29e874fd824ff16e465b5798b19aafe0cae60a2dbcf91333", 16));
        G1Point p = new G1Point(px, py);
        byte[] b = new byte[32];
        new Random().nextBytes(b);
        BigInteger perD_int = new BigInteger(1, CryptoUtil.getHash("SHA-256", b));
        BigInteger bb = BigInteger.valueOf(123l);
        System.out.println("perd_int:"+perD_int);
        G1Point perD_hash = G1.mul(p, bb);
    }

    @Test
    public void mulTest1() {
        Fp px = new Fp(new BigInteger("1e462d01d1861f7ee499bf70ab12ade335d98586b52db847ee2ec1e790170e04", 16));
        Fp py = new Fp(new BigInteger("14bd807f4e64904b29e874fd824ff16e465b5798b19aafe0cae60a2dbcf91333", 16));
        Fp qx = new Fp(new BigInteger("15ea829def65cb28c5435094e1b8d06cb021a8671319cdad074ee89ce7c2c0bf", 16));
        Fp qy = new Fp(new BigInteger("0b68b46b86de49221fe4dbdce9b88518812c9d48fb502ada0a2ad9fc28312c89", 16));
        G1Point p = new G1Point(px, py);
        BigInteger s = new BigInteger("30586f85e8fcea91c0db1ed30aacf7350e72efd4cf756b3ce309f2159e275ff9", 16);

        G1Point q = G1Point.INF;

        try {
            long start = System.nanoTime();
            q = G1.mul(p, s);
            long ms = TimeUnit.MILLISECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS);
            System.out.println("g1EcMul test 1 took " + ms + " ms");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertEquals(q.x, qx);
        assertEquals(q.y, qy);
    }

}
