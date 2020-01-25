package com.ddv.test.databinder;

public class UserSession {
    
    private User user;
    private Long userPermCtxtId;
    private boolean isPvqPassed;
    private Long verificationQuestionId;
    
    UserSession() {
        // default constructor;
    }
    
    public User getUser() {
        return user;
    }
    void setUser(User user) {
        this.user = user;
    }
    
    public Long getUserPermCtxtId() {
        return userPermCtxtId;
    }
    void setUserPermCtxtId(long userPermCtxtId) {
        this.userPermCtxtId = userPermCtxtId;
    }
    
    public boolean isPvqPassed() {
        return isPvqPassed;
    }
    void setPvqPassed(boolean isPvqPassed) {
        this.isPvqPassed = isPvqPassed;
    }
    
    public Long getVerificationQuestionId() {
        return verificationQuestionId;
    }
    void setVerificationQuestionId(Long verificationQuestionId) {
        this.verificationQuestionId = verificationQuestionId;
    }
    
    void copyTo(UserSession other) {
        other.user = user;
        other.userPermCtxtId = userPermCtxtId;
        other.isPvqPassed = isPvqPassed;
        other.verificationQuestionId = verificationQuestionId;
    }
    
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("User= ").append(user)
              .append("\nUserPermCtxtId= ").append(userPermCtxtId)
              .append("\nIs Pvq passed= ").append(isPvqPassed)
              .append("\nVerification question id= ").append(verificationQuestionId);
        return buffer.toString();
    }
}
