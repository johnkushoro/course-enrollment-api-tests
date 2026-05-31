package com.courseenrollment.pages;

import io.restassured.specification.RequestSpecification;

public class PageFactory {
    
    private final RequestSpecification baseRequestSpec;
    
    public PageFactory(RequestSpecification baseRequestSpec) {
        this.baseRequestSpec = baseRequestSpec;
    }
    
    public CoursePage getCoursePage() {
        return new CoursePage(baseRequestSpec);
    }
    
    public AuthenticationPage getAuthenticationPage() {
        return new AuthenticationPage(baseRequestSpec);
    }
}