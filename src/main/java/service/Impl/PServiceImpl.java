package service.Impl;

import it.unisa.dia.gas.jpbc.Element;
import mapper.ConsultMapper;
import mapper.ProvStoreMapper;
import mapper.RegistrationMapper;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import pojo.VO.EHR;
import service.DService;
import service.HService;
import service.PService;
import util.ArraysUtil;
import util.CryptoUtil;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Base64;

import static service.Impl.KSServiceImpl.*;
import static service.Impl.SysParamServiceImpl.*;

@Service
public class PServiceImpl implements PService {
    public static byte[] cskP;
    public static Element auCS;
    public static Element auH;
    public static Element a;
    public static Element[] au;
    public static Element spwP;
    public static byte[] pidD;
    public static String auxD;
    public static byte[] ck_rou_y_rou;
    //    public static Element k_rou_y_rou_plus_1;
    private static Element r;
    private static String tpD;
    private final Logger logger = Logger.getLogger(AuditServiceImpl.class);
    @Resource
    private RegistrationMapper registrationMapper;
    private Element tk;
    private String idP;
    private String idH;
    @Resource
    private HService hServiceImpl;
    @Resource
    private DService dServiceImpl;
    @Resource
    private ConsultMapper consultMapper;
    @Resource
    private ProvStoreMapper provStoreMapper;

    @Override
    public Element blindPw(String pwP) {
        r = pairing.getZr().newRandomElement().getImmutable();
        return pairing.getG1().newElement().setFromHash(pwP.getBytes(), 0, pwP.getBytes().length).
                mulZn(r.duplicate()).getImmutable();
    }

    @Override
    public Element getSpw(Element[] sigma_star, Element pwP_star, Element[] Qs, String pwP) {
        //check Eq.2
        for (int i = 1; i < n + 1; i++) {
            Element left = pairing.pairing(sigma_star[i], P).getImmutable();
            Element right = pairing.pairing(pwP_star, Qs[i]).getImmutable();
            if (!left.isEqual(right)) {
                logger.warn("Eq.2 does not hold");
            }
        }

        //generate sigma_pw
        Element tmp = pairing.getG1().newElement().getImmutable();
        Element subtmp = pairing.getG1().newElement().getImmutable();
        for (int l = 1; l < t; l++) {
            for (int e = 1; e <= t; e++) {
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

        Element sigma_pw = pairing.getG1().newElement().setFromHash(pwP.getBytes(), 0, pwP.getBytes().length)
                .getImmutable().mulZn(s.duplicate()).getImmutable();
        Element l_temp = pairing.pairing(sigma_pw_temp.duplicate(), P.duplicate()).getImmutable();

        //generate spwP
        Element r = pairing.pairing(pairing.getG1().newElement().setFromHash(pwP.getBytes(), 0, pwP.getBytes().length)
                , Q.duplicate()).getImmutable();
        Element l = pairing.pairing(sigma_pw.duplicate(), P.duplicate()).getImmutable();
        if (!l.isEqual(r)) {
            logger.warn("Eq.3 does not hold");
        } else {
            try {
                byte[] b = ArraysUtil.mergeByte(sigma_pw.toBytes(), pwP.getBytes("ISO8859-1"));
                spwP = pairing.getZr().newElementFromHash(b, 0, b.length).getImmutable();
                return spwP;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Override
    public void register_P(Element spwP, String[] idKS, String idCS, String idH, String idP, Element pwP_star) {
        //compute au, send au to KS
        au = new Element[n + 1];
        for (int i = 1; i < n + 1; i++) {
            byte[] b1 = new byte[0];
            try {
                b1 = ArraysUtil.mergeByte(idKS[i - 1].getBytes("ISO8859-1"), spwP.toBytes());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            au[i] = pairing.getZr().newElementFromHash(b1, 0, b1.length);
        }
        ArraysUtil.ElementString auES = ArraysUtil.elementToString(au);
        try {
            registrationMapper.toKS(idP, new String(pwP_star.toBytes(), "ISO8859-1"), auES.elements, 0);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //send cskP, idP, and auCS to CS
        cskP = CryptoUtil.AESEncrypt(CryptoUtil.getHash("SHA-256", spwP), skP.toBytes());
        try {
            byte[] idCS_spwP = ArraysUtil.mergeByte(idCS.getBytes("ISO8859-1"), spwP.toBytes());
            auCS = pairing.getZr().newElementFromHash(idCS_spwP, 0, idCS_spwP.length).getImmutable();
            registrationMapper.toCS(idP, new String(auCS.toBytes(), "ISO8859-1"), new String(cskP));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //send idP and auH to H
        try {
            byte[] b3 = ArraysUtil.mergeByte(idH.getBytes("ISO8859-1"), spwP.toBytes());
            auH = pairing.getZr().newElementFromHash(b3, 0, b3.length).getImmutable();
            registrationMapper.toH(idP, new String(auH.toBytes(), "ISO8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public EHR consult_P(String idP, String pwP) {
        try {
            byte[] idCS_spwP = ArraysUtil.mergeByte(idCS.getBytes("ISO8859-1"), spwP.toBytes());
            Element auCS_prime = pairing.getZr().newElementFromHash(idCS_spwP, 0, idCS_spwP.length).getImmutable();
            String auCS = consultMapper.selAuCS(idP);
            if (!auCS_prime.isEqual(
                    pairing.getZr().newElementFromBytes(auCS.getBytes("ISO8859-1")))) {
                logger.warn("failed to authenticate with CS!");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //generate a permit perD to authenticate D
        byte[] perD = ArraysUtil.mergeByte(idP.getBytes(), pidD, tpD.getBytes(), auxD.getBytes());
        Element perDHash = pairing.getG1().newElementFromHash(perD, 0, perD.length);
        Element sigma_perD = perDHash.mulZn(skP);

        byte[] perD_perDSig = ArraysUtil.mergeByte(perD, sigma_perD.toBytes());
        byte[] enc_perD_sigma_perD = CryptoUtil.AESEncrypt(CryptoUtil.getHash("SHA-256", tk.duplicate()), perD_perDSig);

        //D verify
        dServiceImpl.appoint_D(enc_perD_sigma_perD, perD.length, sigma_perD.toBytes().length);

        //share DH-key
        dServiceImpl.consult_D();
//        a = pairing.getZr().newRandomElement().getImmutable();
//        Element aP = P.duplicate().mulZn(a.duplicate()).getImmutable();
//        k_rou_y_rou_plus_1 = bP.duplicate().mulZn(a.duplicate()).getImmutable();

        //download enc_k_rou_y_rou and obtain k_rou_y_rou
        int maxStage = consultMapper.selMaxStage(idP);
        logger.warn("isTheFirstTime:" + (maxStage == 0));
        if (!(maxStage == 0)) {
            byte[] enc_k_rou_y_rou_plus_1 = Base64.getDecoder().decode(provStoreMapper.selCk_rou_y_rouByStage(idP, maxStage));
            byte[] k_rou_y_rou_plus_1 = CryptoUtil.AESDecrypt(CryptoUtil.getHash(
                    "SHA-256", spwP), enc_k_rou_y_rou_plus_1);
            return sendCk_rou_y_rouToD(idP, maxStage, CryptoUtil.AESEncrypt(CryptoUtil.getHash(
                    "SHA-256", DServiceImpl.k_rou_y_rou_plus_1), k_rou_y_rou_plus_1));
        }
        return null;
    }

    @Override
    public EHR sendCk_rou_y_rouToD(String idP, int stage, byte[] ck_rou_y_rou) {
        return dServiceImpl.get_k_rou_y_rou(idP, stage, ck_rou_y_rou);
    }

    @Override
    public int authenticate(String idP) {
        long hTime1 = System.currentTimeMillis();
        this.idP = idP;
        byte[] idH_spwP = ArraysUtil.mergeByte(SysParamServiceImpl.idH.getBytes(), spwP.toBytes());
        Element auH = pairing.getZr().newElement().setFromHash(idH_spwP, 0, idH_spwP.length).getImmutable();
        long hTime2 = System.currentTimeMillis();
        long hAuthenticateTime = hTime2 - hTime1;
        System.out.println("hAuthenticateTime = " + hAuthenticateTime);
        return hServiceImpl.authenticate(idP, auH);
    }

    public int sendAppointInfoToPatient(int idDLength, int tpDLength, int tkLength) {
        //decrypt and parse the appointment information
        byte[] idD_tpD_tk = CryptoUtil.AESDecrypt(CryptoUtil.getHash("SHA-256", auH.duplicate()), pidD);
        byte[][] splitByte = ArraysUtil.splitByte(idD_tpD_tk, idDLength, tpDLength, tkLength);
        byte[] idD = splitByte[0];
        tpD = new String(splitByte[1]);
        this.tk = pairing.getG1().newElementFromBytes(splitByte[2]);
        logger.warn("医生身份为" + new String(idD));
        logger.warn("有效期为" + Long.valueOf(tpD));
        logger.warn("辅助信息为" + auxD);
        return 1;
    }
}
