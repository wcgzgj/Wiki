package top.faroz.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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

    // @Resource
    // LoginInterceptor loginInterceptor;

    // @Resource
    // ActionInterceptor actionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // registry.addInterceptor(logInterceptor)
        //         .addPathPatterns("/**") //添加拦截路径
        //         .excludePathPatterns("/login"); //设置排除的拦截路径


        // registry.addInterceptor(loginInterceptor)
        //         .addPathPatterns("/**")
        //         .excludePathPatterns(
        //                 "/test/**",
        //                 "/redis/**",
        //                 "/user/login",
        //                 "/category/all",
        //                 "/ebook/list",
        //                 "/doc/all/**",
        //                 "/doc/vote/**",
        //                 "/doc/find-content/**",
        //                 "/ebook-snapshot/**"
        //         );

        // registry.addInterceptor(actionInterceptor)
        //         .addPathPatterns(
        //                 "/*/save",
        //                 "/*/delete/**",
        //                 "/*/reset-password");
    }
}
