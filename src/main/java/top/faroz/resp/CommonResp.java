package top.faroz.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName CommonResp
 * @Description 统一返回值
 *              通过这种方法，可以更方便前端处理后端传来的数据
 * @Author FARO_Z
 * @Date 2021/3/22 上午8:41
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResp<T> {
    /**
     * 返回是否成功
     */
    private boolean isSuccess=true;

    /**
     * 返回失败时，返回的错误信息
     */
    private String message;

    /**
     * 返回成功的时候，返回的内容
     */
    private T content;
}
