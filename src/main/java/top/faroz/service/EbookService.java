package top.faroz.service;

import org.springframework.stereotype.Service;
import top.faroz.mapper.EbookMapper;
import top.faroz.pojo.Ebook;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName EbookService
 * @Description TODO
 * @Author FARO_Z
 * @Date 2021/3/20 下午4:27
 * @Version 1.0
 **/
@Service
public class EbookService {

    /**
     * Resource功能和@AutoWired差不多，只不过，Resource是JDK自带的
     */
    @Resource
    private EbookMapper mapper;

    public List<Ebook> getList() {
        return mapper.selectByExample(null);
    }


}
