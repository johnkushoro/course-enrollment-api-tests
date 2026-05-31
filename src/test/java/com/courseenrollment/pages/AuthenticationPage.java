package com.courseenrollment.pages;

import com.courseenrollment.config.ApiConfig;
import com.courseenrollment.config.ApiConstants;
import com.courseenrollment.models.LoginRequest;
import com.courseenrollment.pages.base.BasePage;
import com.courseenrollment.pages.interfaces.IAuthenticationOperations;
import com.courseenrollment.utils.ResponseParser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class AuthenticationPage extends BasePage implements IAuthenticationOperations {
    
    public AuthenticationPage(RequestSpecification baseRequestSpec) {
        super(baseRequestSpec);
    }
    
    @Override
    public String getEndpointPath() {
        return ApiConstants.Endpoints.LOGIN;
    }
    
    @Override
    public Response loginInstructor() {
        LoginRequest loginRequest = new LoginRequest(
            ApiConfig.INSTRUCTOR_USERNAME, 
            ApiConfig.INSTRUCTOR_PASSWORD
        );
        
        return given(baseRequestSpec)
                .body(loginRequest)
                .when()
                .post(ApiConstants.Endpoints.LOGIN);
    }
    
    @Override
    public Response loginStudent() {
        LoginRequest loginRequest = new LoginRequest(
            ApiConfig.STUDENT_USERNAME, 
            ApiConfig.STUDENT_PASSWORD
        );
        
        return given(baseRequestSpec)
                .body(loginRequest)
                .when()
                .post(ApiConstants.Endpoints.LOGIN);
    }
    
    @Override
    public String extractToken(Response response) {
        return ResponseParser.extractToken(response);
    }
    
    public Response loginWithInvalidCredentials(String username, String password) {
        LoginRequest loginRequest = new LoginRequest(username, password);
        
        return given(baseRequestSpec)
                .body(loginRequest)
                .when()
                .post(ApiConstants.Endpoints.LOGIN);
    }
}