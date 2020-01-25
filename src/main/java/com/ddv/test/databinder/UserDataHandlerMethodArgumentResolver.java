package com.ddv.test.databinder;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class UserDataHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String USER_PERM_CTXT_ID_HEADER = "userPermCtxtId";
    
    private AuthenticationService authenticationService;
    
    UserDataHandlerMethodArgumentResolver(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return (parameter.getParameterAnnotation(UserData.class) != null);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String userPermCtxtId = webRequest.getHeader(USER_PERM_CTXT_ID_HEADER);
        if (userPermCtxtId != null) {
            return UserSessionFactory.materialize(userPermCtxtId, authenticationService);
        }
        throw new Exception("Missing user identifier");
    }

}
