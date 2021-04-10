package top.faroz.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 自定义 Mapper，作用是增加阅读数
 * 如果使用生成好的 mapper，那我们要先将文档信息查出来,+1,再更新
 * 这样，就需要执行两次 sql，比较消耗资源
 * 我们的自定义 mapper，就是根据 id,将文档的阅读数+1
 */
@Repository
@Mapper
public interface DocMapperCust {
    void viewCountIncrease(@Param("id") Long id);

    void voteCountIncrease(@Param("id") Long id);

    void updateEbookInfo();

}
