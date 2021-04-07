package top.faroz.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.faroz.mapper.ContentMapper;
import top.faroz.mapper.DocMapper;
import top.faroz.pojo.Content;
import top.faroz.pojo.Doc;
import top.faroz.pojo.DocExample;
import top.faroz.req.DocQueryReq;
import top.faroz.req.DocSaveReq;
import top.faroz.resp.DocQueryResp;
import top.faroz.resp.PageResp;
import top.faroz.util.CopyUtil;
import top.faroz.util.SnowFlake;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DocService
 * @Description TODO
 * @Author FARO_Z
 * @Date 2021/3/20 下午4:27
 * @Version 1.0
 **/
@Service
public class DocService {
    private static final Logger LOG= LoggerFactory.getLogger(DocService.class);

    /**
     * Resource功能和@AutoWired差不多，只不过，Resource是JDK自带的
     */
    @Resource
    private DocMapper docMapper;

    /**
     * 文档内容（富文本）接口
     */
    @Resource
    private ContentMapper contentMapper;

    /**
     * 雪花算法，生成id
     */
    @Resource
    private SnowFlake snowFlake;


    /**
     * 返回所有分类数据
     * 因为前端不需要分页，所以返回值只要是list就行
     * @return
     */
    public List<DocQueryResp> all() {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        List<Doc> docs = docMapper.selectByExample(docExample);

        //列表复制，将原类型，更改为 resp类型
        List<DocQueryResp> docResps = CopyUtil.copyList(docs, DocQueryResp.class);
        return docResps;
    }


    /**
     * 模糊查询，查出有req中名字部分的书籍
     * @param req
     * @return
     */
    public PageResp<DocQueryResp> list(DocQueryReq req) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();


        /**
         * 这里就不设计动态查询了
         * 因为分类每次都是要全部列出来的
         */
        PageHelper.startPage(req.getPage(),req.getSize());
        List<Doc> docs = docMapper.selectByExample(docExample);

        //列表复制，将原类型，更改为 resp类型
        List<DocQueryResp> docResps = CopyUtil.copyList(docs, DocQueryResp.class);

        //       泛型类型是元素类型             要传入 查询到的数据列表
        PageInfo<Doc> info = new PageInfo<>(docs);

        PageResp<DocQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(info.getTotal());
        pageResp.setList(docResps);

        return pageResp;
    }

    /**
     * 保存
     * 这里的保存，既要支持新增，也要支持更新
     * 是保存，还是新增，判断的依据是  req的id是否为空
     */
    public void save(DocSaveReq req) {
        Doc doc=CopyUtil.copy(req,Doc.class);
        Content content = CopyUtil.copy(req, Content.class);

        if (ObjectUtils.isEmpty(req.getId())) {

            //新增
            doc.setId(snowFlake.nextId());
            docMapper.insert(doc);

            content.setId(doc.getId());
            contentMapper.insert(content);
        } else {
            //更新
            docMapper.updateByPrimaryKey(doc);

            /**
             * updateByPrimaryKeyWithBLOBs表示更新带大字段的内容
             * 因为我们的 content 包含文本这一大字段内容
             * `content` mediumtext not null comment '内容',
             * 处于效率考虑，Mybatis 自动生成的代码，特地添加了一个大字段更新方法
             */
            int affects = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if (affects==0) {
                content.setId(doc.getId());
                contentMapper.insert(content);
            }
        }
    }

    /**
     * 根据一组主键，删除元素
     * @param ids
     */
    public void delete(List<String> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        ArrayList<Long> list = new ArrayList<>();
        for (String id : ids) {
            list.add(Long.parseLong(id));
        }
        criteria.andIdIn(list);
        docMapper.deleteByExample(docExample);
    }

    public String findContent(Long id) {
        Content content = contentMapper.selectByPrimaryKey(id);
        if (content==null) {
            return "";
        }
        return content.getContent();
    }


}
