package top.faroz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.faroz.mapper.TestMapper;
import top.faroz.pojo.Test;

import java.util.List;

/**
 * @ClassName TestService
 * @Description TODO
 * @Author FARO_Z
 * @Date 2021/3/20 下午4:27
 * @Version 1.0
 **/
@Service
public class TestService {

    @Autowired
    private TestMapper mapper;

    public List<Test> getList() {
        return mapper.getList();
    }


}
