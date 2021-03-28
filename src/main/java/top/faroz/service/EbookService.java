package top.faroz.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.faroz.mapper.EbookMapper;
import top.faroz.pojo.Ebook;
import top.faroz.pojo.EbookExample;
import top.faroz.req.EbookQueryReq;
import top.faroz.req.EbookSaveReq;
import top.faroz.resp.EbookQueryResp;
import top.faroz.resp.PageResp;
import top.faroz.util.CopyUtil;
import top.faroz.util.SnowFlake;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName EbookService
 * @Description TODO
 * @Author FARO_Z
 * @Date 2021/3/20 下午4:27
 * @Version 1.0
 **/
@Service
public class EbookService {
    private static final Logger LOG= LoggerFactory.getLogger(EbookService.class);

    /**
     * Resource功能和@AutoWired差不多，只不过，Resource是JDK自带的
     */
    @Resource
    private EbookMapper mapper;

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
    public PageResp<EbookQueryResp> list(EbookQueryReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();


        /**
         * 动态sql，如果req中，没有传入名字，那么，就不设置模糊查询
         */
        if (!ObjectUtils.isEmpty(req.getName())) {
            //设置模糊查询条件
            criteria.andNameLike("%"+req.getName()+"%");
        }
        if (!ObjectUtils.isEmpty(req.getCategoryId2())) {
            criteria.andCategory2IdEqualTo(req.getCategoryId2());
        }

        PageHelper.startPage(req.getPage(),req.getSize());
        List<Ebook> ebooks = mapper.selectByExample(ebookExample);

        //列表复制，将原类型，更改为 resp类型
        List<EbookQueryResp> ebookResps = CopyUtil.copyList(ebooks, EbookQueryResp.class);

        //       泛型类型是元素类型             要传入 查询到的数据列表
        PageInfo<Ebook> info = new PageInfo<>(ebooks);

        PageResp<EbookQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(info.getTotal());
        pageResp.setList(ebookResps);

        return pageResp;
    }

    /**
     * 保存
     * 这里的保存，既要支持新增，也要支持更新
     * 是保存，还是新增，判断的依据是  req的id是否为空
     */
    public void save(EbookSaveReq req) {
        Ebook ebook=CopyUtil.copy(req,Ebook.class);
        if (ObjectUtils.isEmpty(req.getId())) {

            //新增
            ebook.setId(snowFlake.nextId());
            mapper.insert(ebook);
        } else {
            //更新
            mapper.updateByPrimaryKey(ebook);
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
