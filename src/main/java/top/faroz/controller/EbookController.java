package top.faroz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.faroz.req.EbookReq;
import top.faroz.resp.CommonResp;
import top.faroz.resp.EbookResp;
import top.faroz.service.EbookService;

import java.util.List;

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
    @RequestMapping("/list")
    public CommonResp list(EbookReq req) {
        List<EbookResp> list = ebookService.list(req);
        //这里用resp封装ebook的实体
        //在controller层，不要出现实体类domain!!
        CommonResp<List<EbookResp>> resp = new CommonResp<>();
        resp.setContent(list);
        return resp;
    }

}
