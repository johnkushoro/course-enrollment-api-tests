package com.courseenrollment.tests;

import com.courseenrollment.base.BaseTest;
import com.courseenrollment.config.ApiConstants;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Health Check Tests")
class HealthCheckTest extends BaseTest {
    
    @Test
    @DisplayName("Should return success status for health check")
    void testHealthCheckEndpoint() {
        Response response = given(getBaseSpec()).when().get(ApiConstants.Endpoints.STATUS);
        
        assertEquals(ApiConstants.StatusCodes.OK, response.getStatusCode(), "Health check should return 200 OK");
        String responseBody = response.getBody().asString();
        assertNotNull(responseBody, "Response body should not be null");
        assertFalse(responseBody.trim().isEmpty(), "Response body should not be empty");
    }
}