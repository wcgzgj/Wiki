package top.faroz.service;

import org.springframework.stereotype.Service;
import top.faroz.mapper.DemoMapper;
import top.faroz.pojo.Demo;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName DemoService
 * @Description TODO
 * @Author FARO_Z
 * @Date 2021/3/20 下午4:27
 * @Version 1.0
 **/
@Service
public class DemoService {

    @Resource
    private DemoMapper mapper;

    public List<Demo> getList() {
        return mapper.selectByExample(null);
    }


}
