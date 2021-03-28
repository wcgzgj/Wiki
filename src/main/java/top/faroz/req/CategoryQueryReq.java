package top.faroz.req;

/**
 * 将实体类，封装一层，专门作为请求
 */
public class CategoryQueryReq extends PageReq{
    @Override
    public String toString() {
        return "CategoryQueryReq{} " + super.toString();
    }
}