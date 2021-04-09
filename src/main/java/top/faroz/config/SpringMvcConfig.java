package top.faroz.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.faroz.interceptor.LoginInterceptor;

import javax.annotation.Resource;

/**
 * @ClassName SpringMvcConfig
 * @Description TODO
 * @Author FARO_Z
 * @Date 2021/3/23 下午7:37
 * @Version 1.0
 **/
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    /**
     * 绑定自定义过滤器
     */
    // @Autowired
    // LogInterceptor logInterceptor;

    @Resource
    LoginInterceptor loginInterceptor;

    // @Resource
    // ActionInterceptor actionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // registry.addInterceptor(logInterceptor)
        //         .addPathPatterns("/**") //添加拦截路径
        //         .excludePathPatterns("/login"); //设置排除的拦截路径


        /**
         * 对部分访问进行拦截
         */
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**") //先对所有部分进行拦截
                .excludePathPatterns( //在排除不像拦截的部分
                        "/test/**",
                        "/redis/**",
                        "/user/login",
                        "/category/all",
                        "/ebook/list",
                        "/doc/all/**",
                        "/doc/vote/**",
                        "/doc/find-content/**",
                        "/ebook-snapshot/**"
                );

        // registry.addInterceptor(actionInterceptor)
        //         .addPathPatterns(
        //                 "/*/save",
        //                 "/*/delete/**",
        //                 "/*/reset-password");
    }
}
