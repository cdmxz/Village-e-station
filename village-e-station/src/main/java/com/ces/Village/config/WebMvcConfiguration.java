package com.ces.Village.config;

import com.ces.Village.common.JacksonObjectMapper;
import com.ces.Village.interceptor.JwtTokenUserInterceptor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

/**
 * 配置类，注册web层相关组件
 */
@Log4j2
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final JwtTokenUserInterceptor jwtTokenUserInterceptor;
    public WebMvcConfiguration(JwtTokenUserInterceptor jwtTokenUserInterceptor) {
        this.jwtTokenUserInterceptor = jwtTokenUserInterceptor;
    }

    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns("/**");    // 拦截所有请求，通过判断是否有 @LoginRequired 注解 决定是否需要登录
    }

    /**
     * 设置静态资源映射
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/front-text/**")
                .addResourceLocations("classpath:/front-text/");
        registry.addResourceHandler("classpath:mapper/*.xml")
                .addResourceLocations("classpath:mapper/*.xml");
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
    }

    /**
     * 扩展Spring MVC框架的消息转化器
     *
     * @param converters
     */
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器...");
        //创建一个消息转换器对象
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        //需要为消息转换器设置一个对象转换器，对象转换器可以将Java对象序列化为json数据
        converter.setObjectMapper(new JacksonObjectMapper());
        //将自己的消息转化器加入容器中
        converters.add(0, converter);
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")//配置允许跨域的路径
//                .allowedOrigins("*")//配置允许访问的跨域资源的请求域名
//                .allowedMethods("PUT,POST,GET,DELETE,OPTIONS")//配置允许访问该跨域资源服务器的请求方法，如：POST、GET、PUT、DELETE等
//                .allowedHeaders("*"); //配置允许请求header的访问，如 ：X-TOKEN
//    }
}
