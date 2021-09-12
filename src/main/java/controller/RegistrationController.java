package controller;

import it.unisa.dia.gas.jpbc.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import service.AuthenticationServerService;
import service.Impl.KSServiceImpl;
import service.Impl.SysParamServiceImpl;
import service.KSService;
import service.PService;
import service.SysParamService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class RegistrationController {
    @Resource
    private PService pServiceImpl;
    @Resource
    private KSService ksServiceImpl;
    @Resource
    private SysParamService sysParamServiceImpl;
    @Resource
    private AuthenticationServerService authenticationServerServiceImpl;

    /**
     * 病人注册
     */
    @RequestMapping("reg")
    public String registration(HttpServletRequest req) {
        String idP = req.getParameter("username");
        String pwP = req.getParameter("password");

        sysParamServiceImpl.init();

        Element pwP_star = pServiceImpl.blindPw(pwP).getImmutable();
        Element[] sigma_star = ksServiceImpl.genSpw(pwP_star);
        Element spwP = pServiceImpl.getSpw(sigma_star, pwP_star, KSServiceImpl.Qs, pwP);

        pServiceImpl.register_P(spwP, SysParamServiceImpl.idKS, SysParamServiceImpl.idCS,
                SysParamServiceImpl.idH, idP, pwP_star);

        //save user information to login
        authenticationServerServiceImpl.createUser(idP, pwP);

        return "forward:/code?code=1";
    }
}
