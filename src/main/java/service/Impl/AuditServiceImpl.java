package service.Impl;

import it.unisa.dia.gas.jpbc.Element;
import mapper.ConsultMapper;
import mapper.ProvStoreMapper;
import org.springframework.stereotype.Service;
import service.AuditService;
import util.ArraysUtil;
import util.CryptoUtil;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

import static service.Impl.SysParamServiceImpl.*;

@Service
public class AuditServiceImpl implements AuditService {
    private static byte[] sigma_PB_lBytes;
    private static byte[] PB_l;

    @Resource
    private ProvStoreMapper provStoreMapper;
    @Resource
    private ConsultMapper consultMapper;

    @Override
    public boolean checkSig(String idP) {
        int lastStage = consultMapper.selMaxStage(idP);
        try {
            sigma_PB_lBytes = provStoreMapper.selSigma_PB_l(idP, lastStage).getBytes("ISO8859-1");
            Element sigma_PB_l = SysParamServiceImpl.pairing.getG1().newElementFromBytes(sigma_PB_lBytes);
            PB_l = provStoreMapper.selPB_l(idP, lastStage).getBytes("ISO8859-1");
            Element left = pairing.pairing(sigma_PB_l, P).getImmutable();
            Element right = pairing.pairing(pairing.getG1().newElementFromHash(PB_l, 0, PB_l.length), pkH).getImmutable();

            return left.isEqual(right);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean checkHash(String idP, byte[] txContent) {
        byte[] provBytes_hash = CryptoUtil.getHash("SHA-256", PB_l);
        Element PB_l_hash = pairing.getZr().newElementFromHash(provBytes_hash, 0, provBytes_hash.length);
        byte[] PB_l_hash_sigma_PB_l_hash = CryptoUtil.getHash(
                "SHA-256", ArraysUtil.mergeByte(PB_l_hash.toBytes(), sigma_PB_lBytes));
        Element _txContent = pairing.getZr().newElementFromHash(
                PB_l_hash_sigma_PB_l_hash, 0, PB_l_hash_sigma_PB_l_hash.length).getImmutable();
        return _txContent.isEqual(pairing.getZr().newElementFromHash(txContent, 0, txContent.length).getImmutable());
    }

}
