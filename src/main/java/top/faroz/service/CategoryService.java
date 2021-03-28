package top.faroz.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.faroz.mapper.CategoryMapper;
import top.faroz.pojo.Category;
import top.faroz.pojo.CategoryExample;
import top.faroz.req.CategoryQueryReq;
import top.faroz.req.CategorySaveReq;
import top.faroz.resp.CategoryQueryResp;
import top.faroz.resp.PageResp;
import top.faroz.util.CopyUtil;
import top.faroz.util.SnowFlake;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName CategoryService
 * @Description TODO
 * @Author FARO_Z
 * @Date 2021/3/20 下午4:27
 * @Version 1.0
 **/
@Service
public class CategoryService {
    private static final Logger LOG= LoggerFactory.getLogger(CategoryService.class);

    /**
     * Resource功能和@AutoWired差不多，只不过，Resource是JDK自带的
     */
    @Resource
    private CategoryMapper mapper;

    /**
     * 雪花算法，生成id
     */
    @Resource
    private SnowFlake snowFlake;


    /**
     * 模糊查询，查出有req中名字部分的书籍
     * @param req
     * @return
     */
    public PageResp<CategoryQueryResp> list(CategoryQueryReq req) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();


        /**
         * 这里就不设计动态查询了
         * 因为分类每次都是要全部列出来的
         */
        // /**
        //  * 动态sql，如果req中，没有传入名字，那么，就不设置模糊查询
        //  */
        // if (!ObjectUtils.isEmpty(req.getName())) {
        //     //设置模糊查询条件
        //     criteria.andNameLike("%"+req.getName()+"%");
        // }

        PageHelper.startPage(req.getPage(),req.getSize());
        List<Category> categorys = mapper.selectByExample(categoryExample);

        //列表复制，将原类型，更改为 resp类型
        List<CategoryQueryResp> categoryResps = CopyUtil.copyList(categorys, CategoryQueryResp.class);

        //       泛型类型是元素类型             要传入 查询到的数据列表
        PageInfo<Category> info = new PageInfo<>(categorys);

        PageResp<CategoryQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(info.getTotal());
        pageResp.setList(categoryResps);

        return pageResp;
    }

    /**
     * 保存
     * 这里的保存，既要支持新增，也要支持更新
     * 是保存，还是新增，判断的依据是  req的id是否为空
     */
    public void save(CategorySaveReq req) {
        Category category=CopyUtil.copy(req,Category.class);
        if (ObjectUtils.isEmpty(req.getId())) {

            //新增
            category.setId(snowFlake.nextId());
            mapper.insert(category);
        } else {
            //更新
            mapper.updateByPrimaryKey(category);
        }
    }

    /**
     * 根据主键删除元素
     * @param id
     */
    public void delete(Long id) {
        mapper.deleteByPrimaryKey(id);
    }


}
