package top.faroz.util;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName CopyUtil
 * @Description TODO
 * @Author FARO_Z
 * @Date 2021/3/22 上午10:24
 * @Version 1.0
 **/
public class CopyUtil {
    /**
     * 单体复制
     */
    public static<T> T copy(Object source, Class<T> clazz) {
        if (source == null) {
            return null;
        }
        if (!clazz.isInstance(source)) {
            return null;
        }
        T obj = null;
        try {
            obj = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BeanUtils.copyProperties(source, obj);
        return obj;
    }

    /**
     * 列表复制
     */
    public static <T> List<T> copyList(List source, Class<T> clazz) {
        if (source.size()==0) {
            return null;
        }
        if (!clazz.isInstance(source.get(0))) {
            return null;
        }
        List<T> target = new ArrayList<>();
        if (!CollectionUtils.isEmpty(source)){
            for (Object c: source) {
                T obj = copy(c, clazz);
                target.add(obj);
            }
        }
        return target;
    }
}
