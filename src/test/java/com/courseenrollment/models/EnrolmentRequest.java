package com.courseenrollment.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EnrolmentRequest {
    
    @JsonProperty("courseCode")
    private String courseCode;
    
    @JsonProperty("username")
    private String username;
    
    public EnrolmentRequest() {
    }
    
    public EnrolmentRequest(String courseCode, String username) {
        this.courseCode = courseCode;
        this.username = username;
    }
    
    public String getCourseCode() {
        return courseCode;
    }
    
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
}