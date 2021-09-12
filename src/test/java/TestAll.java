import contract.DeployUtil;
import it.unisa.dia.gas.jpbc.Element;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pojo.DO.User;
import pojo.VO.AuditResult;
import pojo.VO.EHR;
import service.*;
import service.Impl.KSServiceImpl;
import service.Impl.SysParamServiceImpl;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Base64;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class TestAll {
    public static long PTime = 0;
    public static long DTime = 0;
    public static long HTime = 0;
    public static long KSTime = 0;
    public static long CSTime = 0;
    public static long ATime = 0;
    @Resource
    private SysParamService sysParamServiceImpl;
    @Resource
    private PService pServiceImpl;
    @Resource
    private AuditService auditServiceImpl;
    @Resource
    private AuthenticationServerService authenticationServerServiceImpl;
    @Resource
    private DService dServiceImpl;
    @Resource
    private KSService ksServiceImpl;

    @Test
    public void testAll() {
        System.out.println(DeployUtil.deploy());

//        long time1 = System.currentTimeMillis();
//        while (DeployUtil.getConfirmedNumber(DeployUtil.contract.getTransactionReceipt().get()) <= 12) {
//
//        }
//        long time2 = System.currentTimeMillis();
//
//        System.out.println(time2 - time1);

        DeployUtil.Store(1, "1111111111111");
        DeployUtil.Audit(1);

        String test = "1";
        SysParamServiceImpl.idKS = new String[100];
        for (int i = 0; i < SysParamServiceImpl.idKS.length; i++) {
            SysParamServiceImpl.idKS[i] = "ks" + i;
        }

        EHR ehr = new EHR();
        ehr.setIdP(test);
        ehr.setIdD(test);
        ehr.setHName(test);
        ehr.setHID(test);
        ehr.setDepID(test);
        ehr.setDepName(test);
        ehr.setContent(test);

        testRegistration(test, test);
        testLogin(test, test);
        testAppoint(test);
        testConsult(test, test, ehr);
        String result = testProvStore(test);
        testAudit(test, result);

        SqlSession session = null;
        try (InputStream is = Resources.getResourceAsStream("mybatis-config.xml")) {
            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(is);
            session = factory.openSession();
            session.delete("clean.deleteCS");
            session.delete("clean.deleteH");
            session.delete("clean.deleteKS");
            session.delete("clean.deleteUser");
            session.delete("clean.deleteEhr");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.commit();
                session.close();
                System.out.println("数据库清理成功");
            } else {
                System.out.println("数据库清理失败");
            }
        }
        System.out.println("PTime :" + PTime + "ms");
        System.out.println("DTime :" + DTime + "ms");
        System.out.println("HTime :" + HTime + "ms");
        System.out.println("KSTime:" + KSTime + "ms");
        System.out.println("CSTime:" + CSTime + "ms");
        System.out.println("ATime :" + ATime + "ms");
        DeployUtil.cleanUpResourcesFile();
    }

    public void testRegistration(String idP, String pwP) {
        sysParamServiceImpl.init();

        long time1 = System.currentTimeMillis();
        Element pwP_star = pServiceImpl.blindPw(pwP).getImmutable();
        long time2 = System.currentTimeMillis();
        PTime += time2 - time1;

        Element[] sigma_star = ksServiceImpl.genSpw(pwP_star);
        long time3 = System.currentTimeMillis();
        KSTime += time3 - time2;

        Element spwP = pServiceImpl.getSpw(sigma_star, pwP_star, KSServiceImpl.Qs, pwP);
        pServiceImpl.register_P(spwP, SysParamServiceImpl.idKS, SysParamServiceImpl.idCS, SysParamServiceImpl.idH, idP, pwP_star);
        //save user information so as to login
        if (authenticationServerServiceImpl.createUser(idP, pwP) > 0) {
            System.out.println("register succeeded");
        } else {
            System.out.println("register failed");
        }
        long time4 = System.currentTimeMillis();
        PTime += time4 - time3;
    }

    public void testLogin(String uname, String password) {
        int uid = authenticationServerServiceImpl.checkUser(uname, password);

        //根据不同用户角色返回不同对象
        User user = authenticationServerServiceImpl.saveUser(uid, uname, password);
        //用户登录
        if (uid > 0) {
            System.out.println("login succeeded");
        } else if (uid == -1) {
            System.out.println("login failed");
        }
    }

    public void testAppoint(String idP) {
        long time5 = System.currentTimeMillis();
        int ret = pServiceImpl.authenticate(idP);
        if (ret == 1) {
            System.out.println("appoint succeeded");
        } else {
            System.out.println("appoint failed");
        }
        long time6 = System.currentTimeMillis();
        HTime += time6 - time5;
        PTime += time6 - time5;
    }

    public void testConsult(String uname, String password, EHR _ehr) {
        //return ehrList for doctor's view
        long time7 = System.currentTimeMillis();
        EHR ehr = pServiceImpl.consult_P(uname, password);
        System.out.println(ehr);
        System.out.println("consult succeeded");
        long time8 = System.currentTimeMillis();
        PTime += time8 - time7;

        dServiceImpl.createEHR(uname, _ehr);
        System.out.println(ehr);
        System.out.println("create ehr succeeded");
        long time9 = System.currentTimeMillis();
        DTime += time9 - time8;
    }

    public String testProvStore(String idP) {
        long time10 = System.currentTimeMillis();
        byte[] txContent = dServiceImpl.outsource(idP);
        String txContentStr = "";
        try {
            txContentStr = new String(Base64.getEncoder().encode(txContent), "ISO8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // add verification
        dServiceImpl.callContract();
        TransactionReceipt createTx = DeployUtil.Store(Integer.parseInt(idP), txContentStr);

        System.out.println("createTx has received " + DeployUtil.getConfirmedNumber(createTx));

        long time11 = System.currentTimeMillis();
        dServiceImpl.sendBlockHash(idP, txContentStr);
        System.out.println("prov store succeeded");
        long time12 = System.currentTimeMillis();

        DTime += time12 - time10;
        CSTime += time12 - time11;
        return txContentStr;
    }

    public void testAudit(String idP, String result) {
        //http将Base64编码中的+转为空格
        long time13 = System.currentTimeMillis();
        result = result.replace(" ", "+");

        byte[] txContent = null;
        try {
            txContent = Base64.getDecoder().decode(result.getBytes("ISO8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //存放审计结果
        AuditResult auditResult = new AuditResult();
        auditResult.setCheckPn(true);
        auditResult.setCheckSig(auditServiceImpl.checkSig(idP));
        auditResult.setCheckHash(auditServiceImpl.checkHash(idP, txContent));

        System.out.println("audit succeeded");
        long time14 = System.currentTimeMillis();
        ATime = time14 - time13;
    }
}
