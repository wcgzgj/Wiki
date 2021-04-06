package top.faroz.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.faroz.req.DocQueryReq;
import top.faroz.req.DocSaveReq;
import top.faroz.resp.DocQueryResp;
import top.faroz.resp.CommonResp;
import top.faroz.resp.PageResp;
import top.faroz.service.DocService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName DocController
 * @Description TODO
 * @Author FARO_Z
 * @Date 2021/3/20 下午2:05
 * @Version 1.0
 **/
@RestController
@RequestMapping("/doc")
public class DocController {
    private static final Logger LOG= LoggerFactory.getLogger(DocController.class);

    @Autowired
    private DocService docService;


    @RequestMapping("/all")
    public CommonResp all() {
        List<DocQueryResp> all = docService.all();
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        resp.setContent(all);
        return resp;
    }


    @RequestMapping("/list")
    public CommonResp list(@Valid DocQueryReq req) {
        PageResp<DocQueryResp> list = docService.list(req);

        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
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
    public CommonResp save(@RequestBody @Valid DocSaveReq req) {


        CommonResp resp = new CommonResp<>();
        docService.save(req);
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
        docService.delete(list);
        return resp;
    }

}
