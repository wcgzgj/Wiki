package top.faroz.req;

/**
 * 将实体类，封装一层，专门作为请求
 */
public class UserQueryReq extends PageReq{

    private String loginName;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }


    @Override
    public String toString() {
        return "UserQueryReq{} " + super.toString();
    }
}