package top.faroz.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 因为项目启动的时候，会提示DataSource的问题，从而导致项目无法启动，这里只能暂时加上
 */
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@ComponentScan("top.faroz")
public class WikiApplication {

    private static final Logger LOG= LoggerFactory.getLogger(WikiApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(WikiApplication.class);
        ConfigurableEnvironment environment = app.run(args).getEnvironment();
        LOG.info("启动成功");
        LOG.info("地址:\thttp://127.0.0.1:{}",environment.getProperty("server.port"));
    }

}
