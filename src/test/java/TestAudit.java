import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import service.AuditService;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class TestAudit {
    @Resource
    private AuditService auditServiceImpl;

    @Test
    public void testSig() {
        System.out.println(auditServiceImpl.checkSig("1"));
    }
}
