package top.faroz.controller;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import top.faroz.req.UserLoginReq;
import top.faroz.req.UserQueryReq;
import top.faroz.req.UserResetPasswordReq;
import top.faroz.req.UserSaveReq;
import top.faroz.resp.CommonResp;
import top.faroz.resp.PageResp;
import top.faroz.resp.UserLoginResp;
import top.faroz.resp.UserQueryResp;
import top.faroz.service.UserService;
import top.faroz.util.SnowFlake;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

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

    /**
     * 雪花算法，生成id
     */
    @Resource
    private SnowFlake snowFlake;

    /**
     * redis 工具
     */
    @Resource
    private RedisTemplate redisTemplate;


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
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        userService.save(req);

        CommonResp resp = new CommonResp<>();
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

    /**
     * 重置密码
     * @param req
     * @return
     */
    @PostMapping("/reset-password")
    public CommonResp resetPassword(@RequestBody @Valid UserResetPasswordReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        userService.resetPassword(req);

        CommonResp resp = new CommonResp<>();
        return resp;
    }

    /**
     * 用户登录
     * @param req
     * @return
     */
    @PostMapping("/login")
    public CommonResp login(@RequestBody @Valid UserLoginReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp<UserLoginResp> resp = new CommonResp<>();
        UserLoginResp userLoginResp = userService.login(req);

        /**
         * redis需要我们手动生成一个不会重复的字符串，作为 token
         * 用 UUID，雪花算法，都可以
         */
        Long token = snowFlake.nextId();
        // {} 是 LOG 中的占位符
        LOG.info("生成单点登录 token:{}",token);
        userLoginResp.setToken(token.toString());

        /**
         * token: 手动生成的 token 作为 redis 的 key
         *
         * JSONObject.toJSON(userLoginResp): 作为 redis 的 value,
         * 这个 value 需要序列化，可以让 userLoginResp 实现Serializable接口，
         * 也可以像下面一样，将其转成 JSON 字符串
         *
         * 3600*24: 设置超时时间
         */
        redisTemplate.opsForValue().set(token.toString(), JSONObject.toJSON(userLoginResp),3600*24, TimeUnit.SECONDS);

        resp.setContent(userLoginResp);
        return resp;
    }

    /**
     * 登出
     * @param token
     * @return
     */
    @GetMapping("/logout/{token}")
    public CommonResp logout(@PathVariable String token) {
        CommonResp resp = new CommonResp<>();
        redisTemplate.delete(token);
        LOG.info("从 redis 中删除 token:{}",token);
        return resp;
    }

}
