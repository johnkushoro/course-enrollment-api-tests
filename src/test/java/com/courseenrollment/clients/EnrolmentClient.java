package com.courseenrollment.clients;

import com.courseenrollment.config.ApiConstants;
import com.courseenrollment.models.EnrolmentRequest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class EnrolmentClient {
    
    private final RequestSpecification baseRequestSpec;
    
    public EnrolmentClient(RequestSpecification baseRequestSpec) {
        this.baseRequestSpec = baseRequestSpec;
    }
    
    public Response enrolInCourse(EnrolmentRequest enrolmentRequest, String studentToken) {
        return given(baseRequestSpec)
                .header(ApiConstants.Headers.AUTHORIZATION, ApiConstants.Headers.BEARER + studentToken)
                .body(enrolmentRequest)
                .when()
                .post(ApiConstants.Endpoints.ENROLMENTS_ENROL);
    }
    
    public Response dropFromCourse(EnrolmentRequest enrolmentRequest, String studentToken) {
        return given(baseRequestSpec)
                .header(ApiConstants.Headers.AUTHORIZATION, ApiConstants.Headers.BEARER + studentToken)
                .body(enrolmentRequest)
                .when()
                .post(ApiConstants.Endpoints.ENROLMENTS_DROP);
    }
    
    public Response getEnrolmentHistory(String username, String studentToken) {
        EnrolmentRequest historyRequest = new EnrolmentRequest();
        historyRequest.setUsername(username);
        
        return given(baseRequestSpec)
                .header(ApiConstants.Headers.AUTHORIZATION, ApiConstants.Headers.BEARER + studentToken)
                .body(historyRequest)
                .when()
                .post(ApiConstants.Endpoints.ENROLMENTS_HISTORY);
    }
    
    public Response getActiveEnrolments(String username, String studentToken) {
        EnrolmentRequest activeRequest = new EnrolmentRequest();
        activeRequest.setUsername(username);
        
        return given(baseRequestSpec)
                .header(ApiConstants.Headers.AUTHORIZATION, ApiConstants.Headers.BEARER + studentToken)
                .body(activeRequest)
                .when()
                .post(ApiConstants.Endpoints.ENROLMENTS_ACTIVE);
    }
    
    public boolean isCourseInEnrolmentList(Response enrolmentResponse, String courseCode) {
        if (enrolmentResponse.getStatusCode() != ApiConstants.StatusCodes.OK) {
            return false;
        }
        
        try {
            return enrolmentResponse.jsonPath().getList("courseCode").contains(courseCode);
        } catch (Exception e) {
            return false;
        }
    }
    
    public int getEnrolmentCount(Response enrolmentResponse) {
        if (enrolmentResponse.getStatusCode() != ApiConstants.StatusCodes.OK) {
            return 0;
        }
        
        try {
            return enrolmentResponse.jsonPath().getList("$").size();
        } catch (Exception e) {
            return 0;
        }
    }
}