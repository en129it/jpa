package com.ddv.test.databinder;

public abstract class UserSessionFactory {
    private static final String USER_ID = "U";
    private static final String PVQ_PASSED = "PVQOK";
    private static final String PVQ_QUESTION_PREFIX = "PVQ";
    private static final String PVQ_QUESTION_POSTFIX = "Q";
    
    private static final int PVQ_QUESTION_PREFIX_LENGTH = PVQ_QUESTION_PREFIX.length();
    private static final int PVQ_QUESTION_POSTFIX_LENGTH = PVQ_QUESTION_POSTFIX.length();

    public static UserSession materialize(String id, AuthenticationService authenticationService) {
        UserSession rslt = new UserSession();
        
        String tid = id;
        if (tid.contains(PVQ_PASSED)) {
            tid = tid.replace(PVQ_PASSED, "");
            rslt.setPvqPassed(true);
        }
        int pos = tid.indexOf(PVQ_QUESTION_PREFIX);
        if (pos > -1) {
            int posEnd = tid.indexOf(PVQ_QUESTION_POSTFIX, pos + PVQ_QUESTION_PREFIX_LENGTH);
            if (posEnd > -1) {
                rslt.setVerificationQuestionId(Long.parseLong(tid.substring(pos + PVQ_QUESTION_PREFIX_LENGTH, posEnd)));
                tid = tid.replace(tid.subSequence(pos, posEnd + PVQ_QUESTION_POSTFIX_LENGTH), "");
            } else {
                tid = tid.replace(PVQ_QUESTION_PREFIX, "");
            }
        }
        if (tid.startsWith(USER_ID)) {
            rslt.setUser(authenticationService.findUserById(Long.parseLong(tid.substring(1))));
        } else {
            rslt.setUserPermCtxtId(Long.parseLong(tid));
            rslt.setUser(authenticationService.findUserByUserPermCtxtId(rslt.getUserPermCtxtId()));
        }
        return rslt;
    }
    
    public static String serialize(UserSession userSession) {
        String rslt;
        if (userSession.getUserPermCtxtId() != null) {
            rslt = userSession.getUserPermCtxtId().toString();
        } else {
            rslt = USER_ID + userSession.getUser().getUserId();
        }
        
        if (userSession.isPvqPassed()) {
            rslt = PVQ_PASSED + rslt;
        }
        
        if (userSession.getVerificationQuestionId() != null) {
            rslt = PVQ_QUESTION_PREFIX + userSession.getVerificationQuestionId() + PVQ_QUESTION_POSTFIX + rslt; 
        }
        return rslt;
    }
    
    public static UserSession makeFullyAuthenticated(UserSession source, Long userPermCtxtId) {
        UserSession rslt = new UserSession();
        rslt.setUserPermCtxtId(userPermCtxtId);
        return rslt;
    }
    
    public static UserSession makePvqPassed(UserSession source) {
        UserSession rslt = new UserSession();
        rslt.setPvqPassed(true);
        return rslt;
    }
    
    public static UserSession registerVerificationQuestionId(UserSession source, Long verificationQuestionId) {
        UserSession rslt = new UserSession();
        rslt.setVerificationQuestionId(verificationQuestionId);
        return rslt;
    }
    
    public static UserSession undecoratePvq(UserSession source) {
        UserSession rslt = new UserSession();
        rslt.setPvqPassed(false);
        rslt.setVerificationQuestionId(null);
        return rslt;
    }
}
