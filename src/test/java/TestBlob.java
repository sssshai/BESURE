import mapper.ProvStoreMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import util.CryptoUtil;

import java.util.Base64;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class TestBlob {

    @Autowired
    private ProvStoreMapper storeMapper;

    @Test
    public void saveBlob() {
        byte[] key = "123".getBytes();
        byte[] m = "456".getBytes();
        System.out.println(new String(m));
        byte[] c = CryptoUtil.AESEncrypt(CryptoUtil.getHash("SHA-256", key), m);
        String base64 = Base64.getEncoder().encodeToString(c);
        System.out.println(base64);
        storeMapper.insert(base64);
    }


    @Test
    public void testBlob() {
        String base64 = storeMapper.select();
        System.out.println(base64);
        byte[] c = Base64.getDecoder().decode(base64);
        byte[] m = CryptoUtil.AESDecrypt(CryptoUtil.getHash("SHA-256", "123".getBytes()), c);
        System.out.println(new String(m));
    }
}
