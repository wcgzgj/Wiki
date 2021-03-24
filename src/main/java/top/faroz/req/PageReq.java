package top.faroz.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private int page;
    private int size;
}
