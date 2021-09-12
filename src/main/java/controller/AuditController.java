package controller;

import contract.DeployUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pojo.DO.User;
import pojo.VO.AuditResult;
import service.AuditService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

@Controller
public class AuditController {
    private final Logger logger = Logger.getLogger(AuditController.class);

    @Resource
    private AuditService auditServiceImpl;

    @RequestMapping("audit")
    @ResponseBody
    AuditResult audit(HttpServletRequest req) {
        //清除session
        User user = (User) req.getSession().getAttribute("user");
        logger.warn("审计者" + user.getUname() + "开始审计文件");
        String idP = user.getUname();
        //http将Base64编码中的+转为空格

        TransactionReceipt provTx = DeployUtil.Audit(1);
        System.out.println("provTx has received " + DeployUtil.getConfirmedNumber(provTx));

        String content = DeployUtil.contract.getContentEvents(provTx).get(0)._content;

        byte[] txContent = null;
        try {
            txContent = Base64.getDecoder().decode(content.getBytes("ISO8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //存放审计结果
        AuditResult auditResult = new AuditResult();
        auditResult.setCheckPn(true);
        auditResult.setCheckSig(auditServiceImpl.checkSig(idP));
        auditResult.setCheckHash(auditServiceImpl.checkHash(idP, txContent));

        logger.warn(auditResult);
        return auditResult;
    }
}
