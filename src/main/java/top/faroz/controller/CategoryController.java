package top.faroz.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.faroz.req.CategoryQueryReq;
import top.faroz.req.CategorySaveReq;
import top.faroz.resp.CommonResp;
import top.faroz.resp.CategoryQueryResp;
import top.faroz.resp.PageResp;
import top.faroz.service.CategoryService;

import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName CategoryController
 * @Description TODO
 * @Author FARO_Z
 * @Date 2021/3/20 下午2:05
 * @Version 1.0
 **/
@RestController
@RequestMapping("/category")
public class CategoryController {
    private static final Logger LOG= LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;


    @RequestMapping("/all")
    public CommonResp all() {
        List<CategoryQueryResp> all = categoryService.all();
        CommonResp<List<CategoryQueryResp>> resp = new CommonResp<>();
        resp.setContent(all);
        return resp;
    }


    @RequestMapping("/list")
    public CommonResp list(@Valid CategoryQueryReq req) {
        PageResp<CategoryQueryResp> list = categoryService.list(req);

        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
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
    public CommonResp save(@RequestBody @Valid CategorySaveReq req) {


        CommonResp resp = new CommonResp<>();
        categoryService.save(req);
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
        categoryService.delete(id);
        return resp;
    }

}
