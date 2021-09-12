import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class TestBase64 {
    @Test
    public void testBase64Decoder() {
        String s = "6UK5MEId74jp8dXjCLkQHqXRZ1F/eK3eeCvHk7pHo+cA6UK5MEId74jp8dXjCLkQHqXRZ1F/eK3eeCvHk7pHow==";
        try {
            byte[] b = Base64.getDecoder().decode(s.getBytes("ISO8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}
