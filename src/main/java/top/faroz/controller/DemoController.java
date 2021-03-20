package top.faroz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.faroz.pojo.Demo;
import top.faroz.service.DemoService;

import java.util.List;

/**
 * @ClassName DemoController
 * @Description TODO
 * @Author FARO_Z
 * @Date 2021/3/20 下午2:05
 * @Version 1.0
 **/
@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private DemoService demoService;


    @RequestMapping("/list")
    public List<Demo> list() {
        return demoService.getList();
    }

}
