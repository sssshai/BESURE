package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 备用控制器
 */
@Controller
public class DefaultController {
    @RequestMapping("{page}")
    public String main(@PathVariable String page) {
        return page;
    }
}
