package com.ddv.test.databinder;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class FullyAuthUserDataHandlerMethodArgumentResolver extends UserDataHandlerMethodArgumentResolver {

    public FullyAuthUserDataHandlerMethodArgumentResolver(AuthenticationService authenticationService) {
        super(authenticationService);
    }
    
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return (parameter.getParameterAnnotation(FullyAuthUserData.class) != null);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        UserSession rslt = (UserSession)super.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        if (rslt.getUserPermCtxtId() == null) {
            throw new Exception("Not fully authenticated user");
        }
        return rslt;
    }

}
