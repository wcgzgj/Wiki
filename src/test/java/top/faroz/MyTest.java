package top.faroz;

import org.junit.jupiter.api.Test;
import top.faroz.util.SnowFlake;

/**
 * @ClassName MyTest
 * @Description TODO
 * @Author FARO_Z
 * @Date 2021/3/27 下午5:11
 * @Version 1.0
 **/
public class MyTest {
    // @Autowired
    // private SnowFlake snowFlake;

    @Test
    void snowFlakeTest() {
        SnowFlake snowFlake = new SnowFlake();
        for (int i = 0; i < 10; i++) {
            System.out.println(snowFlake.nextId());
        }

    }
}
