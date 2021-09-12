package controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pojo.DO.User;
import pojo.VO.EHR;
import service.DService;
import service.PService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ConsultController {
    private final Logger logger = Logger.getLogger(ConsultController.class);
    @Resource
    private PService pServiceImpl;
    @Resource
    private DService dServiceImpl;

    @RequestMapping("consult")
    @ResponseBody
    public EHR consult(HttpServletRequest req) {
        User user = ((User) req.getSession().getAttribute("user"));

        //return ehrList for doctor's view
        EHR ehr = pServiceImpl.consult_P(user.getUname(), user.getPassword());
        logger.warn(ehr);
        return ehr;
    }

    @RequestMapping("ehr")
    public String getEHR(@RequestBody EHR ehr, HttpServletRequest req) {
        User user = ((User) req.getSession().getAttribute("user"));

        dServiceImpl.createEHR(user.getUname(), ehr);

        logger.warn(ehr);
        return "forward:code?code=1";
    }
}
