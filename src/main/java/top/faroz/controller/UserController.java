package top.faroz.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.faroz.req.UserQueryReq;
import top.faroz.req.UserSaveReq;
import top.faroz.resp.CommonResp;
import top.faroz.resp.UserQueryResp;
import top.faroz.resp.PageResp;
import top.faroz.service.UserService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author FARO_Z
 * @Date 2021/3/20 下午2:05
 * @Version 1.0
 **/
@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger LOG= LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;


    @RequestMapping("/all/{ebookId}")
    public CommonResp all(@PathVariable Long ebookId) {
        List<UserQueryResp> all = userService.all(ebookId);
        CommonResp<List<UserQueryResp>> resp = new CommonResp<>();
        resp.setContent(all);
        return resp;
    }


    @RequestMapping("/list")
    public CommonResp list(@Valid UserQueryReq req) {
        PageResp<UserQueryResp> list = userService.list(req);

        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        resp.setContent(list);
        return resp;
    }

    /**
     * 保存电子书
     * 要加上 @RequestBody 因为前段传来的是json格式，要将其转换成对象
     * 加上 @Valid 注解，是为了开启校验
     * @param req
     * @return
     */
    @PostMapping("/save")
    public CommonResp save(@RequestBody @Valid UserSaveReq req) {


        CommonResp resp = new CommonResp<>();
        userService.save(req);
        return resp;
    }

    /**
     * 按照id，删除对应的元素
     * @param idsStr
     * @return
     */
    @DeleteMapping("/delete/{idsStr}")
    public CommonResp delete(@PathVariable String idsStr) {
        CommonResp resp = new CommonResp<>();
        List<String> list = Arrays.asList(idsStr.split(","));
        userService.delete(list);
        return resp;
    }

    @RequestMapping("/find-content/{id}")
    public CommonResp findContent(@PathVariable Long id) {
        CommonResp<String> resp = new CommonResp<>();
        String content = userService.findContent(id);
        resp.setContent(content);
        return resp;
    }

}
