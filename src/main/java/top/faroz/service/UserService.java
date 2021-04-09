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
import top.faroz.req.UserLoginReq;
import top.faroz.req.UserQueryReq;
import top.faroz.req.UserResetPasswordReq;
import top.faroz.req.UserSaveReq;
import top.faroz.resp.PageResp;
import top.faroz.resp.UserLoginResp;
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
                /**
                 * 因为 BusinessException 继承了 RuntimeException，
                 * 又因为 RuntimeException 不需要 catch
                 * 所以方法后面可以不用加 throw
                 *
                 * 但是这种方法不适合统一管理
                 * 所以我们需要统一异常处理
                 * (自己写了一个统一异常处理类 ControllerExceptionHandler)
                 */
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }
        /**
         * req 有 id，更新
         */
        } else {
            /**
             * 我们将更新时的用户名设置为 null
             * 是为了让下面的 selective 忽略用户名的修改
             * 从而在后端实现防止用户修改用户名
             */
            user.setLoginName(null);
            /**
             * 不修改密码
             */
            user.setPassword(null);
            /**
             * Selective 表示，当内容不为空的时候，才会去选择修改
             */
            userMapper.updateByPrimaryKeySelective(user);
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

    /**
     * 修改密码
     * @param req
     */
    public void resetPassword(UserResetPasswordReq req) {
        User user=CopyUtil.copy(req,User.class);
        /**
         * 因为我们的 UserResetPasswordReq只有 id 和 password
         * 所以我们在更新的时候，要使用 selective，这样才不会让其他值变成 null
         */
        userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 登录
     *
     * 当用户名不存在或者密码错误的时候，我们不能单独输出
     * 而是应该输出 用户名不存在或密码错误  这样如果有黑客试图攻击的话，
     * 就无法判断是用户名错误，还是密码错误了
     * @param req
     */
    public UserLoginResp login(UserLoginReq req) {
        User userDB = selectByLoginName(req.getLoginName());
        if (ObjectUtils.isEmpty(userDB)) {
            LOG.info("用户名不存在, {}", req.getLoginName());
            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
        } else {
            if (userDB.getPassword().equals(req.getPassword())) {
                //登陆成功
                UserLoginResp userLoginResp = CopyUtil.copy(userDB, UserLoginResp.class);
                return userLoginResp;
            } else {
                LOG.info("密码不对, 输入密码：{}, 数据库密码：{}", req.getPassword(), userDB.getPassword());
                throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
            }
        }
    }



}
