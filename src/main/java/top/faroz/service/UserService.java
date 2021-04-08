package top.faroz.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import top.faroz.exception.BusinessException;
import top.faroz.exception.BusinessExceptionCode;
import top.faroz.mapper.UserMapper;
import top.faroz.pojo.User;
import top.faroz.pojo.UserExample;
import top.faroz.req.UserQueryReq;
import top.faroz.req.UserSaveReq;
import top.faroz.resp.PageResp;
import top.faroz.resp.UserQueryResp;
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
    private UserMapper userMapper;

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
        List<User> users = userMapper.selectByExample(userExample);

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
        /**
         * req无 id，新增
         */
        if (ObjectUtils.isEmpty(req.getId())) {
            if (ObjectUtils.isEmpty(selectByLoginName(req.getLoginName()))) {
                user.setId(snowFlake.nextId());
                userMapper.insert(user);
            } else {
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }
        /**
         * req 有 id，更新
         */
        } else {
            userMapper.updateByPrimaryKey(user);
        }
    }

    /**
     * 根据主键删除元素
     * @param id
     */
    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }

    public User selectByLoginName(String loginName) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andLoginNameEqualTo(loginName);

        /**
         * 虽然查出来的最多只可能是一条
         * 但是，我们还是要用 list 去接收
         */
        List<User> userList = userMapper.selectByExample(userExample);

        /**
         * 这里不用 size()==0去判断，是因为担心 userList 还可能是 null
         */
        if (CollectionUtils.isEmpty(userList)) {
            return null;
        } else {
            return userList.get(0);
        }
    }


}
