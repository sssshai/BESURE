package service.Impl;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;
import mapper.ParamsMapper;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import service.SysParamService;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

@Service
public class SysParamServiceImpl implements SysParamService {
    public static BigInteger p;//群G和Gt的素数阶
    public static Field G;//群G
    public static Field Gt;//群Gt
    public static Element P;//群G的生成元
    public static Element pkH;
    public static Element skH;
    public static Element pkP;
    public static Element skP;
    public static Element k;//私钥对
    public static Pairing pairing;
    public static String[] idKS = new String[]{"ks1", "ks2", "ks3", "ks4", "ks5"};
    //    public static String[] idKS;
    public static String idCS = "cs";
    public static String idH = "h";
    @Resource
    public ParamsMapper paramsMapper;

    /**
     * 如果数据库中有系统参数，取出后直接赋值给本类，否则才需要初始化参数，后写入数据库（同缓存）
     */
    @Override
    public void init() {
        //SSP - Super-singular Curve over GF(p) (512-bit modulus p, k=2)
//        TypeACurveGenerator pg = new TypeACurveGenerator(512, 512);
//        PairingParameters typeAParams = pg.generate();
//        Pairing pairing = PairingFactory.getPairing(typeAParams);
//        System.out.println(pairing.getG1().getOrder());
//        CurveElement e = (CurveElement) pairing.getG1().newRandomElement();
//        BigInteger x = e.getX().toBigInteger();
//        BigInteger y = e.getY().toBigInteger();
//        System.out.println(e.getField().getA());
//        System.out.println(e.getField().getB());
//        BigInteger p = pairing.getG1().getOrder();
//        System.out.println(x.pow(3).add(x).mod(p));
//        System.out.println(y.pow(2).mod(p));

        Logger logger = Logger.getLogger(SysParamServiceImpl.class);

        TypeACurveGenerator pg = new TypeACurveGenerator(512, 512);
        PairingParameters typeAParams = pg.generate();
        pairing = PairingFactory.getPairing(typeAParams);

        String strP = paramsMapper.selP();
        String strK = paramsMapper.selk();
        String strP_pub = paramsMapper.selP_pub();
        String strS = paramsMapper.sels();
        if (strP == null) {
            //重新创建并写入数据库
            logger.warn("系统初始化参数，重新创建并写入数据库");
            p = pairing.getG1().getOrder();
            G = pairing.getG1();
            Gt = pairing.getGT();
            P = pairing.getG1().newRandomElement().getImmutable();
            skH = pairing.getZr().newRandomElement().getImmutable();
            k = pairing.getZr().newElement().setFromHash(skH.toBytes(), 0, skH.toBytes().length);
            pkH = P.duplicate().mulZn(skH.duplicate()).getImmutable();
            //Element转String
            byte[] byteP = P.toBytes();
            byte[] byteP_pub = pkH.toBytes();
            byte[] bytes = skH.toBytes();
            byte[] bytesk = k.toBytes();

            try {
                paramsMapper.insParams(
                        new String(byteP, "ISO8859-1"),
                        new String(byteP_pub, "ISO8859-1"),
                        new String(bytes, "ISO8859-1"),
                        new String(bytesk, "ISO8859-1"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            //已经存在系统参数
            logger.warn("系统初始化参数，已经存在系统参数");
            //String转Element
            try {
                P = pairing.getG1().newElementFromBytes(strP.getBytes("ISO8859-1"));
                k = pairing.getZr().newElementFromBytes(strK.getBytes("ISO8859-1"));
                pkH = pairing.getG1().newElementFromBytes(strP_pub.getBytes("ISO8859-1"));
                skH = pairing.getZr().newElementFromBytes(strS.getBytes("ISO8859-1"));
                skP = pairing.getZr().newRandomElement().getImmutable();
                pkP = P.duplicate().mulZn(skP.duplicate().getImmutable());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        logger.info("AS的公钥为：" + pkH.toString().substring(0, 15));
    }
}
