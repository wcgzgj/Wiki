package top.faroz.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import top.faroz.mapper.EbookMapper;
import top.faroz.pojo.Ebook;
import top.faroz.pojo.EbookExample;
import top.faroz.req.EbookReq;
import top.faroz.resp.EbookResp;
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
    public List<EbookResp> list(EbookReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        //模糊查询
        criteria.andNameLike("%"+req.getName()+"%");
        List<Ebook> ebooks = mapper.selectByExample(ebookExample);
        return CopyUtil.copyList(ebooks,EbookResp.class);
    }


}
