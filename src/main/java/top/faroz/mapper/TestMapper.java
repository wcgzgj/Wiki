package top.faroz.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.faroz.pojo.Test;

import java.util.List;

@Repository
@Mapper
public interface TestMapper {
    List<Test> getList();
}
