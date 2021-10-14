package com.agatah.dogtalk.exception;

public class BehavioristPrivilegesInSchoolNotFoundException extends RuntimeException{

    public BehavioristPrivilegesInSchoolNotFoundException(Long behavioristId, Long schoolId){
        super("Entity of type: BehavioristPrivilegesInSchool, behaviorist id: " + behavioristId + ", school id: "
                + schoolId + " not found");
    }
}
