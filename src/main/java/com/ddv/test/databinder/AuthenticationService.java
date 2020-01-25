package com.ddv.test.databinder;

import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    
    public User findUserById(Long userId) {
        return new User();
    }
    
    public User findUserByUserPermCtxtId(Long userPermCtxtId) {
        return new User();
    }
}
