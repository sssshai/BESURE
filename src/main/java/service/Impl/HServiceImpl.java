package service.Impl;

import it.unisa.dia.gas.jpbc.Element;
import mapper.AppointMapper;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import pojo.DO.Doctor;
import service.DService;
import service.HService;
import service.PService;
import util.ArraysUtil;
import util.CryptoUtil;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

import static service.Impl.SysParamServiceImpl.pairing;

@Service
public class HServiceImpl implements HService {
    private final Logger logger = Logger.getLogger(HServiceImpl.class);
    @Resource
    private PService pServiceImpl;
    @Resource
    private DService dServiceImpl;
    @Resource
    private AppointMapper appointMapper;
    private Element auH;

    @Override
    public int authenticate(String idP, Element auH) {
        try {
            if (appointMapper.selIdP(idP, new String(auH.toBytes(), "ISO8859-1")) > 0) {
                //choose a treatment key
                Element tk = pairing.getG1().newRandomElement().getImmutable();
                this.auH = auH;
//                Element[] encTK = CryptoUtil.ElGamalEncrypt(P, pkH, tk.duplicate());
                //send encTK to H
                return appoint_H(tk, idP);
            } else {
                logger.warn("failed to authenticated with H!");
                return -1;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int appoint_H(Element tk, String ID_P) {
//        Element tk = CryptoUtil.ElGamalDecrypt(SysParamServiceImpl.skH, encTK).getImmutable();//解密获取tk
        long time1 = System.currentTimeMillis();
        Doctor doctor = new Doctor("doctor");//委派医生
        long tpD = System.currentTimeMillis();//当前时间
        byte[] idD_tpD = ArraysUtil.mergeByte(doctor.getID().getBytes(), Long.toString(tpD).getBytes());
        byte[] idD_tpD_tk = ArraysUtil.mergeByte(idD_tpD, tk.duplicate().toBytes());
        byte[] pidD = CryptoUtil.AESEncrypt(CryptoUtil.getHash("SHA-256", tk.duplicate()), idD_tpD_tk);
        byte[] pidDToP = CryptoUtil.AESEncrypt(CryptoUtil.getHash("SHA-256", auH.duplicate()), idD_tpD_tk);//医生的伪身份

        //send tk pidD to D
        dServiceImpl.sendTKPIDDToDoctor(tk.duplicate(), pidD);

        //send appointment information to P
        PServiceImpl.pidD = pidDToP;
        PServiceImpl.auxD = "dep";
        long time2 = System.currentTimeMillis();
        long hAppoint = time2 - time1;
        System.out.println(time1);
        System.out.println(time2);
        System.out.println("hAppoint = " + hAppoint);
        return pServiceImpl.sendAppointInfoToPatient(doctor.getID().getBytes().length,
                Long.toString(tpD).getBytes().length, tk.duplicate().toBytes().length);
    }

    @Override
    public Element genSig(byte[] PB_l) {
        long time1 = System.currentTimeMillis();
        Element sig = SysParamServiceImpl.pairing.getG1().newElementFromHash(PB_l, 0, PB_l.length).
                mulZn(SysParamServiceImpl.skH);
        long time2 = System.currentTimeMillis();
        long hGenSig = time2 - time1;
        System.out.println("hGenSig = " + hGenSig);
        return sig;
    }
}
