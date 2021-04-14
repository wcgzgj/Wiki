package top.faroz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.faroz.pojo.Test;
import top.faroz.service.TestService;

import java.util.List;

/**
 * @ClassName TestController
 * @Description TODO
 * @Author FARO_Z
 * @Date 2021/3/20 下午2:05
 * @Version 1.0
 **/
@RestController
public class TestController {

    @Autowired
    private TestService testService;


    //从配置文件中，读取信息  :后面是默认值，如果配置文件中没有，会去使用
    @Value("${test.hello:TEST}")
    private String data;

    @RequestMapping("/hello")
    public String hello() {
        return data;
    }

    @RequestMapping("/test/list")
    public List<Test> list() {
        return testService.getList();
    }

}
