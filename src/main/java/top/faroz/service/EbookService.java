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
import top.faroz.req.EbookReq;
import top.faroz.resp.EbookResp;
import top.faroz.resp.PageResp;
import top.faroz.util.CopyUtil;

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
     * 模糊查询，查出有req中名字部分的书籍
     * @param req
     * @return
     */
    public PageResp<EbookResp> list(EbookReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        //下面这个算动态sql，如果req中，没有传入名字，那么，就不设置模糊查询
        if (!ObjectUtils.isEmpty(req.getName())) {
            //设置模糊查询条件
            criteria.andNameLike("%"+req.getName()+"%");
        }

        PageHelper.startPage(req.getPage(),req.getSize());
        List<Ebook> ebooks = mapper.selectByExample(ebookExample);

        //列表复制，将原类型，更改为 resp类型
        List<EbookResp> ebookResps = CopyUtil.copyList(ebooks, EbookResp.class);

        //       泛型类型是元素类型             要传入查询到的数据列表
        PageInfo<Ebook> info = new PageInfo<>(ebooks);

        PageResp<EbookResp> pageResp = new PageResp<>();
        pageResp.setTotal(info.getTotal());
        pageResp.setList(ebookResps);

        return pageResp;
    }


}
