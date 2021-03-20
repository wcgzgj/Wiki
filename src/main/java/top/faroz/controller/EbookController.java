package top.faroz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.faroz.pojo.Ebook;
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


    @RequestMapping("/list")
    public List<Ebook> list() {
        return ebookService.getList();
    }

}
