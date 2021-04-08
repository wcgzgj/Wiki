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


    //GET http://localhost:8880/user/list?name=Spring
    //上面的请求形式，Spring会自动映射，会将name映射到req中
    //需要使用 @Valid开启校验规则 ，因为XxxReq都是集成自PageReq，PageReq上加了注解校验
    @RequestMapping("/list")
    public CommonResp list(@Valid UserQueryReq req) {
        PageResp<UserQueryResp> list = userService.list(req);
        //这里用resp封装user的实体
        //在controller层，不要出现实体类domain!!
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
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        CommonResp resp = new CommonResp<>();
        userService.delete(id);
        return resp;
    }

}
