package top.faroz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.faroz.req.EbookQueryReq;
import top.faroz.req.EbookSaveReq;
import top.faroz.resp.CommonResp;
import top.faroz.resp.EbookQueryResp;
import top.faroz.resp.PageResp;
import top.faroz.service.EbookService;

import javax.validation.Valid;

/**
 * @ClassName EbookController
 * @Description TODO
 * @Author FARO_Z
 * @Date 2021/3/20 下午2:05
 * @Version 1.0
 **/
@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Autowired
    private EbookService ebookService;


    //GET http://localhost:8880/ebook/list?name=Spring
    //上面的请求形式，Spring会自动映射，会将name映射到req中
    //需要使用 @Valid开启校验规则 ，因为XxxReq都是集成自PageReq，PageReq上加了注解校验
    @RequestMapping("/list")
    public CommonResp list(@Valid EbookQueryReq req) {
        PageResp<EbookQueryResp> list = ebookService.list(req);
        //这里用resp封装ebook的实体
        //在controller层，不要出现实体类domain!!
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        resp.setContent(list);
        return resp;
    }

    /**
     * 保存电子书
     * 要加上 @RequestBody 因为前段传来的是json格式，要将其转换成对象
     * @param req
     * @return
     */
    @PostMapping("/save")
    public CommonResp save(@RequestBody EbookSaveReq req) {
        CommonResp resp = new CommonResp<>();
        ebookService.save(req);
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
        ebookService.delete(id);
        return resp;
    }

}
