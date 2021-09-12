package controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pojo.DO.User;
import service.AuthenticationServerService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class AuthenticationServerController {
    @Resource
    private AuthenticationServerService authenticationServerServiceImpl;

    /**
     * 登录验证
     */
    @RequestMapping("as")
    public String authenticationServer(HttpServletRequest req) {
        Logger logger = Logger.getLogger(AuthenticationServerController.class);
        //获取表单数据
        String uname = req.getParameter("username");
        String password = req.getParameter("password");
        int uid = authenticationServerServiceImpl.checkUser(uname, password);

        //根据不同用户角色返回不同对象
        User user = authenticationServerServiceImpl.saveUser(uid, uname, password);
        logger.warn("用户" + user.getUname() + "开始登录");
        //用户登录
        if (uid > 0) {
            logger.warn("AS验证用户ID：" + uid + "和pwd：" + password + "通过");
            logger.error("用户" + user.getUname() + "登录成功");
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            return "forward:/code?code=1";
        } else if (uid == -1) {
            logger.error("用户" + user.getUname() + "登录失败");
            return "redirect:/login";
        }
        return null;
    }

    /**
     * 状态码返回给前台：owner=1 auditor=2
     */
    @RequestMapping("code")
    @ResponseBody
    public int returnRuleCode(@RequestParam("code") int code) {
        return code;
    }
}
