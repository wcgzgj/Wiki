package top.faroz.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.faroz.mapper.DocMapper;
import top.faroz.pojo.Doc;
import top.faroz.pojo.DocExample;
import top.faroz.req.DocQueryReq;
import top.faroz.req.DocSaveReq;
import top.faroz.resp.DocQueryResp;
import top.faroz.resp.PageResp;
import top.faroz.util.CopyUtil;
import top.faroz.util.SnowFlake;

import javax.annotation.Resource;
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
    private DocMapper mapper;

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
        /**
         * 自动生成的sql是这样的
         * <select id="selectByExample" parameterType="top.faroz.pojo.DocExample" resultMap="BaseResultMap">
         *     select
         *     <if test="distinct">
         *       distinct
         *     </if>
         *     <include refid="Base_Column_List" />
         *     from doc
         *     <if test="_parameter != null">
         *       <include refid="Example_Where_Clause" />
         *     </if>
         *     <if test="orderByClause != null">
         *       order by ${orderByClause}
         *     </if>
         *   </select>
         *
         *   最后的order by 会拼接上我们写的内容
         *   即会成为 order by sort acs，即通过sort的升序排列
         */
        docExample.setOrderByClause("sort asc");
        List<Doc> docs = mapper.selectByExample(docExample);

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
        // /**
        //  * 动态sql，如果req中，没有传入名字，那么，就不设置模糊查询
        //  */
        // if (!ObjectUtils.isEmpty(req.getName())) {
        //     //设置模糊查询条件
        //     criteria.andNameLike("%"+req.getName()+"%");
        // }

        PageHelper.startPage(req.getPage(),req.getSize());
        List<Doc> docs = mapper.selectByExample(docExample);

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
        if (ObjectUtils.isEmpty(req.getId())) {

            //新增
            doc.setId(snowFlake.nextId());
            mapper.insert(doc);
        } else {
            //更新
            mapper.updateByPrimaryKey(doc);
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
