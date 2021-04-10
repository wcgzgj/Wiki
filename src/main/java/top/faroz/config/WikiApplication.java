package top.faroz.config;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 因为项目启动的时候，会提示DataSource的问题，从而导致项目无法启动，这里只能暂时加上
 */
@SpringBootApplication
@ComponentScan("top.faroz") //因为启动类移动了位置，所以需要为其加上注解，使其可以去扫描其他组件
@MapperScan("top.faroz.mapper") //mybatis mapper接口扫描
@EnableScheduling
public class WikiApplication {

    //配置了live template 模板后 可以使用  logf  快速生成
    private static final Logger LOG= LoggerFactory.getLogger(WikiApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(WikiApplication.class);
        ConfigurableEnvironment environment = app.run(args).getEnvironment();
        LOG.info("启动成功");
        LOG.info("地址:\thttp://127.0.0.1:{}",environment.getProperty("server.port"));
    }

}
