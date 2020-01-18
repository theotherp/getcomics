package org.nzbhydra.getcomics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebConfiguration extends WebMvcConfigurationSupport {


    private static final Logger logger = LoggerFactory.getLogger(WebConfiguration.class);

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.noCache())
                .resourceChain(false);
        registry.setOrder(0);

        registry.addResourceHandler("/ui/**")
                .addResourceLocations("classpath:/ui/")
                .setCacheControl(CacheControl.noCache())
                .resourceChain(false);


        registry.setOrder(0);
    }


    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }


    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        //registry.addMapping("/**").allowedOrigins("http://127.0.0.1:5076", "https://127.0.0.1:9091");
        //Later: Check when actually calling from other host, seems to work on server
    }


    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setForceEncoding(true);
        characterEncodingFilter.setEncoding("UTF-8");
        registrationBean.setFilter(characterEncodingFilter);
        return registrationBean;
    }

}
