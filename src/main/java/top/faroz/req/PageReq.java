package top.faroz.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * @ClassName PageReq
 * @Description 这个作为一个父类
 *                以后如果有哪个请求，需要用到分页，就应该继承PageReq
 * @Author FARO_Z
 * @Date 2021/3/24 下午3:17
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor

public class PageReq {
    @NotNull(message = "【页码】不能为空")
    private int page;

    /**
     * 之所以要对每页条数做限制
     * 是防止其他人直接使用脚本查询后端数据
     * 比如说后端有100w条数据
     * 如果不做校验，让别人用脚本直接已下载查出来
     * 那服务器就挂掉了
     */
    @NotNull(message = "【每页条数】不能为空")
    @Max(value = 1000,message = "【每页条数】不能超过1000")
    private int size;
}
