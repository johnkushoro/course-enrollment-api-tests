package com.courseenrollment.pages.base;

import com.courseenrollment.config.ApiConstants;
import com.courseenrollment.pages.interfaces.IApiPage;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public abstract class BasePage implements IApiPage {
    
    protected final RequestSpecification baseRequestSpec;
    
    protected BasePage(RequestSpecification baseRequestSpec) {
        this.baseRequestSpec = baseRequestSpec;
    }
    
    @Override
    public boolean isAccessible() {
        try {
            Response response = given(baseRequestSpec)
                    .when()
                    .get(getEndpointPath());
            return response.getStatusCode() != ApiConstants.StatusCodes.NOT_FOUND;
        } catch (Exception e) {
            return false;
        }
    }
    
    protected RequestSpecification getAuthenticatedSpec(String token) {
        return given(baseRequestSpec)
                .header(ApiConstants.Headers.AUTHORIZATION, ApiConstants.Headers.BEARER + token);
    }
}