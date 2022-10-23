package com.workonenight.winteambe.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConf implements WebMvcConfigurer {

    private CorsConfiguration buildConfig() {

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        //  Cross domain configuration error , take .allowedOrigins Replace with .allowedOriginPatterns that will do .
        //  Set the domain name that allows cross domain requests
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedHeader("*");
        //  Set allowed methods
        corsConfiguration.addAllowedMethod("*");
        //  Whether to allow certificates
        corsConfiguration.setAllowCredentials(true);
        //  Cross domain allow time
        corsConfiguration.setMaxAge(3600L);
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }

    /** *  Turn on cross domain  */
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        //  Set routes that allow cross domain routing
        registry.addMapping("/**")
                //  Set the domain name that allows cross domain requests
                //.allowedOrigins("*")
                // Cross domain configuration error , take .allowedOrigins Replace with .allowedOriginPatterns that will do .
                .allowedOriginPatterns("*")
                //  Whether to allow certificates （cookies）
                .allowCredentials(true)
                //  Set allowed methods
                .allowedMethods("*")
                //  Cross domain allow time
                .maxAge(3600);
    }

}

