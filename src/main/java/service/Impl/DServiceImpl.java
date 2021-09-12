package service.Impl;

import contract.DeployUtil;
import it.unisa.dia.gas.jpbc.Element;
import mapper.ConsultMapper;
import mapper.ProvStoreMapper;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import pojo.VO.EHR;
import pojo.VO.Provenance;
import service.CSService;
import service.DService;
import service.HService;
import tetryon.*;
import util.ArraysUtil;
import util.BytesUtil;
import util.CryptoUtil;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static service.Impl.SysParamServiceImpl.*;

@Service
public class DServiceImpl implements DService {
    public static Element k_rou_y_rou_plus_1;
    public static byte[] pidD;
    public static Element sigma_PB_l;
    public static EHR ehr;
    public static byte[] perD;
    public static byte[] sigma_perD;
    public static Provenance PB_l;
    public static byte[] C_rou_y_rou_plus_1;
    private final Logger logger = Logger.getLogger(DServiceImpl.class);
    @Resource
    private CSService csServiceImpl;
    @Resource
    private HService hServiceImpl;
    @Resource
    private ProvStoreMapper provStoreMapper;
    @Resource
    private ConsultMapper consultMapper;
    private Element tk;


    @Override
    public void consult_D() {
//        Element b = pairing.getZr().newRandomElement().getImmutable();
//        k_rou_y_rou_plus_1 = aP.duplicate().mulZn(b.duplicate()).getImmutable();
        k_rou_y_rou_plus_1 = pairing.getZr().newRandomElement().getImmutable();
    }

    @Override
    public boolean authenticate(byte[] perD, byte[] sigma_perD) {
        return csServiceImpl.authenticate(perD, sigma_perD);
    }

    @Override
    public void createEHR(String idP, EHR ehr) {
        DServiceImpl.ehr = ehr;
        byte[] ehrBytes = BytesUtil.toByteArray(ehr);
        C_rou_y_rou_plus_1 = CryptoUtil.AESEncrypt(
                CryptoUtil.getHash("SHA-256", k_rou_y_rou_plus_1.duplicate()), ehrBytes);
        int currentStage = consultMapper.selMaxStage(idP) + 1;
        if (currentStage == 1) {
            if (!(consultMapper.insC_rou_y_rou(idP, currentStage, Base64.getEncoder().encodeToString(C_rou_y_rou_plus_1),
                    Base64.getEncoder().encodeToString(k_rou_y_rou_plus_1.toBytes())) > 0)) {
                logger.warn("update c_rou_y_rou failed!");
            }
        } else {
            byte[] ck_rou_y_rou = Base64.getDecoder().decode(
                    provStoreMapper.selCk_rou_y_rouByStage(idP, currentStage - 1));
            byte[] ck_rou_y_rou_plus_1 = CryptoUtil.AESEncrypt(
                    CryptoUtil.getHash("SHA-256", k_rou_y_rou_plus_1), ck_rou_y_rou);
            if (!(consultMapper.insC_rou_y_rou(idP, currentStage, Base64.getEncoder().encodeToString(C_rou_y_rou_plus_1),
                    Base64.getEncoder().encodeToString(ck_rou_y_rou_plus_1)) > 0)) {
                logger.warn("update c_rou_y_rou failed!");
            }
        }
    }

    @Override
    public void sendTKPIDDToDoctor(Element tk, byte[] pidD) {
        this.tk = tk;
        DServiceImpl.pidD = pidD;
    }

    @Override
    public void appoint_D(byte[] enc_perD_sigma_perD, int length, int length1) {
        byte[] perD_sigma_perD = CryptoUtil.AESDecrypt(
                CryptoUtil.getHash("SHA-256", tk.toBytes()), enc_perD_sigma_perD);

        byte[][] splitByte = ArraysUtil.splitByte(perD_sigma_perD, length, length1);
        perD = splitByte[0];
        sigma_perD = splitByte[1];

        //P(P, sigma_perD)=P(H(perD), pkP)
        Element left = SysParamServiceImpl.pairing.pairing(
                SysParamServiceImpl.pairing.getG1().newElementFromBytes(sigma_perD), SysParamServiceImpl.P);

        Element hash_perD = SysParamServiceImpl.pairing.getG1().newElementFromHash(perD, 0, perD.length);

        Element right = SysParamServiceImpl.pairing.pairing(hash_perD, SysParamServiceImpl.pkP);

        if (!left.isEqual(right)) {
            logger.warn("Doctor's verification failed!");
        }
    }

    @Override
    public byte[] outsource(String idP) {
        //check if the first time come to this department
        //generate PB_l
        PB_l = new Provenance();
        byte[] ehrHash = CryptoUtil.getHash("SHA-256", ehr.content.getBytes());
        PB_l.setEhrHash(new String(ehrHash));
        PB_l.setPidD(new String(pidD));
        PB_l.setDepName(ehr.depName);
        PB_l.setDepID(ehr.depID);
        PB_l.setIdP(idP);
        PB_l.setHName(ehr.HName);
        PB_l.setHID(ehr.HID);
        PB_l.setStartCreateTime(System.currentTimeMillis());
        PB_l.setEndCreateTime(System.currentTimeMillis());

        //H generates signature sigma_PB_l on PB_l
        Element sigma_PB_l = hServiceImpl.genSig(BytesUtil.toByteArray(PB_l));
        DServiceImpl.sigma_PB_l = sigma_PB_l;

        if (consultMapper.selMaxStage(idP) == 1) {
            PB_l.setViewHash(new ArrayList<>());
//            PB_l.setBlock(null);
            PB_l.setStartViewTime(0L);
            PB_l.setEndViewTime(0L);
            // D ensure signature sigma_PB_l is valid
            if (!sendPBToH(sigma_PB_l, BytesUtil.toByteArray(PB_l))) {
                logger.warn("signature sigma_PB_l verification failed!");
            }
        } else {
            int lastStage = consultMapper.selMaxStage(idP);
            //TODO add view ehr
            PB_l.setViewHash(new ArrayList<>());
            PB_l.getViewHash().add(new String(CryptoUtil.getHash("SHA-256", ehr.content.getBytes())));
//            PB_l.setBlock(provStoreMapper.selBl_l(idP, lastStage));
            PB_l.setStartViewTime(System.currentTimeMillis());
            PB_l.setEndViewTime(System.currentTimeMillis());

            //H generates signature sigma_PB_l_minus_1 on PB_l_minus_1
            String PB_l_minus_1 = provStoreMapper.selPB_l(idP, lastStage - 1);
            Element sigma_PB_l_minus_1 = hServiceImpl.genSig(PB_l_minus_1.getBytes());

            // D ensure both signature sigma_PB_l_minus_1 and sigma_PB_l are valid
            if (!(sendPBToH(sigma_PB_l_minus_1, PB_l_minus_1.getBytes()))
                    && sendPBToH(sigma_PB_l, BytesUtil.toByteArray(PB_l))) {
                logger.warn("signature sigma_PB_l verification failed!");
            }
        }
        //send TX
        byte[] provBytes_hash = CryptoUtil.getHash("SHA-256", BytesUtil.toByteArray(PB_l));
        Element PB_l_hash = pairing.getZr().newElementFromHash(provBytes_hash, 0, provBytes_hash.length);
        byte[] PB_l_hash_sigma_PB_l_hash = CryptoUtil.getHash(
                "SHA-256", ArraysUtil.mergeByte(PB_l_hash.toBytes(), sigma_PB_l.toBytes()));
        return pairing.getZr().newElementFromHash(
                PB_l_hash_sigma_PB_l_hash, 0, PB_l_hash_sigma_PB_l_hash.length).toBytes();
    }

    @Override
    public boolean sendPBToH(Element sigma_PB_l, byte[] PB_l) {
        //verify
        long time1 = System.currentTimeMillis();
        Element left = pairing.pairing(sigma_PB_l, P).getImmutable();
        Element right = pairing.pairing(pairing.getG1().newElementFromHash(PB_l, 0, PB_l.length), pkH);
        long time2 = System.currentTimeMillis();
        long hVerify = time2 - time1;
        System.out.println("hVerify = " + hVerify);

        boolean result = left.isEqual(right);
        return result;
    }

    @Override
    public void sendBlockHash(String idP, String blockHash) {
        //send blockHash to CS
        csServiceImpl.receiveProv(idP, PB_l, blockHash, sigma_PB_l, C_rou_y_rou_plus_1, perD, sigma_perD);
    }

    @Override
    public EHR get_k_rou_y_rou(String idP, int stage, byte[] ck_rou_y_rou) {
        //解密已存的最新的密钥明文
        byte[] k_rou_y_rou = CryptoUtil.AESDecrypt(CryptoUtil.getHash(
                "SHA-256", k_rou_y_rou_plus_1), ck_rou_y_rou);
        if (csServiceImpl.authenticate(perD, sigma_perD)) {
            if (stage == 1) {
                return (EHR) BytesUtil.toObject(CryptoUtil.AESDecrypt(CryptoUtil.getHash("SHA-256", k_rou_y_rou),
                        Base64.getDecoder().decode(consultMapper.selC_rou_y_rou(idP, stage))));
            } else {
                return (EHR) BytesUtil.toObject(CryptoUtil.AESDecrypt(CryptoUtil.getHash("SHA-256", k_rou_y_rou),
                        Base64.getDecoder().decode(consultMapper.selC_rou_y_rou(idP, stage))));
            }
//            List<String> ck_rou_y_rouList = consultMapper.selCk_rou_y_rou(idP);
//            List<String> C_rou_y_rouList = consultMapper.selC_rou_y_rou(idP);
//            byte[] key = k_rou_y_rou;
//            for (int i = ck_rou_y_rouList.size() - 1; i > 1; i--) {
//                EHR ehr = (EHR) BytesUtil.toObject(
//                        CryptoUtil.AESDecrypt(key, Base64.getDecoder().decode(C_rou_y_rouList.get(i))));
//                key = CryptoUtil.AESDecrypt(CryptoUtil.getHash("SHA-256", key),
//                        Base64.getDecoder().decode(ck_rou_y_rouList.get(i)));
//                ehrList.add(ehr);
//            }
        } else {
            logger.warn("CS authentication failed!");
            return null;
        }
    }

    @Override
    public void callContract() {
        //P(P, sigma_perD)=P(H(perD), pkP)
        G1Point P = Pairing.P1();
        G2Point g2 = new G2Point(
                new Fp2(
                        new BigInteger("10857046999023057135944570762232829481370756359578518086990519993285655852781"),
                        new BigInteger("11559732032986387107991004021392285783925812861821192530917403151452391805634")
                ),
                new Fp2(
                        new BigInteger("8495653923123431417604973247489272438418190587263600148770280649306958101930"),
                        new BigInteger("4082367875863433681332203403145435568316851327593401208105741076214120093531")
                )
        );
        try {
            BigInteger perD_int = new BigInteger(1, CryptoUtil.getHash("SHA-256", perD));
            System.out.println("perd_int:"+perD_int);
            G1Point perD_hash = G1.mul(P, perD_int);
            G2Point sigma_perD = G2.ECTwistMul(g2, perD_int.multiply(new BigInteger(1, skP.toBytes())));
            G2Point pkP = G2.ECTwistMul(g2, new BigInteger(1, skP.toBytes()));

            List<BigInteger> params1 = new ArrayList<>();
            params1.add(P.x.c0);
            params1.add(P.y.c0);

            List<BigInteger> params2 = new ArrayList<>();
            params2.add(sigma_perD.x.b);
            params2.add(sigma_perD.x.a);
            params2.add(sigma_perD.y.b);
            params2.add(sigma_perD.y.a);

            List<BigInteger> params3 = new ArrayList<>();
            params3.add(perD_hash.x.c0);
            params3.add(perD_hash.y.c0);

            List<BigInteger> params4 = new ArrayList<>();
            params4.add(pkP.x.b);
            params4.add(pkP.x.a);
            params4.add(pkP.y.b);
            params4.add(pkP.y.a);

            DeployUtil.Verify(params1, params2, params3, params4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
