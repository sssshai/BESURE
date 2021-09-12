package controller;

import contract.DeployUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pojo.DO.User;
import service.DService;
import service.Impl.SysParamServiceImpl;
import service.SysParamService;
import tetryon.*;
import util.CryptoUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Base64;

import static service.Impl.DServiceImpl.perD;

@Controller
public class ProvStoreController {
    @Resource
    private DService dServiceImpl;

    @RequestMapping("provStore")
    public String provStore(HttpServletRequest req) {
        String idP = ((User) req.getSession().getAttribute("user")).getUname();
        byte[] txContent = dServiceImpl.outsource(idP);
        String txContentStr = "";
        try {
            //需要编码，否则前端报错
            txContentStr = new String(Base64.getEncoder().encode(txContent), "ISO8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // add verification
//        dServiceImpl.callContract();
        TransactionReceipt createTx = DeployUtil.Store(Integer.parseInt(idP), txContentStr);

        System.out.println("createTx has received " + DeployUtil.getConfirmedNumber(createTx));

        dServiceImpl.sendBlockHash(idP, createTx.getBlockHash());
        return "forward:code?code=1";
    }
}
