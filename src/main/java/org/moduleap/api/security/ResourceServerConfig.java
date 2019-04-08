package org.moduleap.api.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    private static final String RESOURCE_ID = "pyl-api";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources){
        resources.resourceId(RESOURCE_ID).stateless(false);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception{
        //here will get the restrictions
        http.anonymous().disable().authorizeRequests().antMatchers("/test/**").access("hasAnyRole('ROLE_USER', 'ROLE_SUPPORTER','ROLE_ADMIN', 'ROLE_OWNER')").and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());

    }
}
