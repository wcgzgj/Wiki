package top.faroz;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.faroz.util.SnowFlake;

import javax.annotation.Resource;

@SpringBootTest
class WikiApplicationTests {

    @Resource
    private SnowFlake snowFlake;

    @Test
    void snowFlakeTest() {
        for (int i = 0; i < 10; i++) {
            System.out.println(snowFlake.nextId());
        }

    }


}
