package com.ddv.test.databinder;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class DataBinderWebConfiguration implements WebMvcConfigurer {

    @Autowired
    private AuthenticationService authenticationService;
    
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new UserDataHandlerMethodArgumentResolver(authenticationService));
        resolvers.add(new FullyAuthUserDataHandlerMethodArgumentResolver(authenticationService));
    }
}
