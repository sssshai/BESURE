package controller;

import mapper.ProvenanceMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class RetrieveController {
    @Resource
    private ProvenanceMapper provenanceMapper;

    @RequestMapping("retrieve")
    @ResponseBody
    public Object retrieve(@RequestParam("operate") String operate) {
        //省略service
        if ("block".equals(operate)) {//检索所有区块数据
            return provenanceMapper.selAllBlock();
        }
        return null;
    }
}
