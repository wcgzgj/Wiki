package top.faroz.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.faroz.mapper.UserMapper;
import top.faroz.pojo.User;
import top.faroz.pojo.UserExample;
import top.faroz.req.UserQueryReq;
import top.faroz.req.UserSaveReq;
import top.faroz.resp.UserQueryResp;
import top.faroz.resp.PageResp;
import top.faroz.util.CopyUtil;
import top.faroz.util.SnowFlake;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName UserService
 * @Description TODO
 * @Author FARO_Z
 * @Date 2021/3/20 下午4:27
 * @Version 1.0
 **/
@Service
public class UserService {
    private static final Logger LOG= LoggerFactory.getLogger(UserService.class);

    /**
     * Resource功能和@AutoWired差不多，只不过，Resource是JDK自带的
     */
    @Resource
    private UserMapper mapper;

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
    public PageResp<UserQueryResp> list(UserQueryReq req) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getLoginName())) {
            criteria.andLoginNameEqualTo(req.getLoginName());
        }

        PageHelper.startPage(req.getPage(),req.getSize());
        List<User> users = mapper.selectByExample(userExample);

        List<UserQueryResp> userResps = CopyUtil.copyList(users, UserQueryResp.class);

        PageInfo<User> info = new PageInfo<>(users);

        PageResp<UserQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(info.getTotal());
        pageResp.setList(userResps);

        return pageResp;
    }

    /**
     * 保存
     * 这里的保存，既要支持新增，也要支持更新
     * 是保存，还是新增，判断的依据是  req的id是否为空
     */
    public void save(UserSaveReq req) {
        User user=CopyUtil.copy(req,User.class);
        if (ObjectUtils.isEmpty(req.getId())) {

            //新增
            user.setId(snowFlake.nextId());
            mapper.insert(user);
        } else {
            //更新
            mapper.updateByPrimaryKey(user);
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
