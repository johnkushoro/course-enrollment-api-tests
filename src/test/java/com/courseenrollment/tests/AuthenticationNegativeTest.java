package com.courseenrollment.tests;

import com.courseenrollment.base.BaseTest;
import com.courseenrollment.config.ApiConfig;
import com.courseenrollment.config.ApiConstants;
import com.courseenrollment.models.CourseRequest;
import com.courseenrollment.pages.AuthenticationPage;
import com.courseenrollment.pages.CoursePage;
import com.courseenrollment.utils.TestDataGenerator;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Authentication Negative Tests")
class AuthenticationNegativeTest extends BaseTest {

    private AuthenticationPage authenticationPage;
    private CoursePage coursePage;

    @BeforeEach
    void setUpTestData() {
        authenticationPage = pageFactory.getAuthenticationPage();
        coursePage = pageFactory.getCoursePage();
    }

    @Test
    @DisplayName("Should reject login with invalid credentials")
    void testInvalidCredentials() {
        String invalidUsername = "invalid_user";
        String invalidPassword = "wrong_password";

        Response response = authenticationPage.loginWithInvalidCredentials(invalidUsername, invalidPassword);

        assertEquals(ApiConstants.StatusCodes.UNAUTHORIZED, response.getStatusCode(),
                "Login with invalid credentials should return 401");
    }

    @Test
    @DisplayName("Should reject login with empty username")
    void testEmptyUsername() {
        String emptyUsername = "";
        String validPassword = "password";

        Response response = authenticationPage.loginWithInvalidCredentials(emptyUsername, validPassword);

        int statusCode = response.getStatusCode();
        assertTrue(statusCode == ApiConstants.StatusCodes.BAD_REQUEST || statusCode == ApiConstants.StatusCodes.UNAUTHORIZED,
                "Login with empty username should return 400 or 401, got: " + statusCode);
    }

    @Test
    @DisplayName("Should reject login with empty password")
    void testEmptyPassword() {
        String validUsername = ApiConfig.INSTRUCTOR_USERNAME;
        String emptyPassword = "";

        Response response = authenticationPage.loginWithInvalidCredentials(validUsername, emptyPassword);

        int statusCode = response.getStatusCode();
        assertTrue(statusCode == ApiConstants.StatusCodes.BAD_REQUEST || statusCode == ApiConstants.StatusCodes.UNAUTHORIZED,
                "Login with empty password should return 400 or 401, got: " + statusCode);
    }

    @Test
    @DisplayName("Should reject login with empty request body")
    void testNullRequestBody() {
        String emptyBody = "";

        Response response = given(getBaseSpec())
                .body(emptyBody)
                .when()
                .post(ApiConstants.Endpoints.LOGIN);

        int statusCode = response.getStatusCode();
        assertTrue(statusCode == ApiConstants.StatusCodes.BAD_REQUEST || 
                  statusCode == ApiConstants.StatusCodes.UNAUTHORIZED || 
                  statusCode == ApiConstants.StatusCodes.UNSUPPORTED_MEDIA_TYPE,
                "Login with empty body should return 400, 401, or 415, got: " + statusCode);
    }

    @Test
    @DisplayName("Should reject login with malformed JSON")
    void testMalformedJson() {
        String malformedJson = "{invalid json}";

        Response response = given(getBaseSpec())
                .body(malformedJson)
                .when()
                .post(ApiConstants.Endpoints.LOGIN);

        int statusCode = response.getStatusCode();
        assertTrue(statusCode == ApiConstants.StatusCodes.BAD_REQUEST || 
                  statusCode == ApiConstants.StatusCodes.UNSUPPORTED_MEDIA_TYPE,
                "Login with malformed JSON should return 400 or 415, got: " + statusCode);
    }

    @Test
    @DisplayName("Should reject login with non-existent user")
    void testNonExistentUser() {
        String nonExistentUsername = "user_does_not_exist_999";
        String anyPassword = "password";

        Response response = authenticationPage.loginWithInvalidCredentials(nonExistentUsername, anyPassword);

        assertEquals(ApiConstants.StatusCodes.UNAUTHORIZED, response.getStatusCode(),
                "Login with non-existent user should return 401");
    }

    @Test
    @DisplayName("Should reject requests to protected endpoints without token")
    void testProtectedEndpointWithoutToken() {
        CourseRequest courseRequest = TestDataGenerator.createCourseRequest();
        Response response = coursePage.createCourseWithoutToken(courseRequest);

        assertEquals(ApiConstants.StatusCodes.UNAUTHORIZED, response.getStatusCode(),
                "Protected endpoint without token should return 401");
    }

    @Test
    @DisplayName("Should reject requests with invalid token format")
    void testInvalidTokenFormat() {
        String invalidToken = "invalid_token_format";

        Response response = given(getBaseSpec())
                .header(ApiConstants.Headers.AUTHORIZATION, ApiConstants.Headers.BEARER + invalidToken)
                .when()
                .get(ApiConstants.Endpoints.COURSES);

        assertEquals(ApiConstants.StatusCodes.UNAUTHORIZED, response.getStatusCode(),
                "Request with invalid token should return 401 Unauthorized");
    }

    @Test
    @DisplayName("Should reject login with null credentials")
    void testNullCredentials() {
        String nullUsername = null;
        String nullPassword = null;

        Response response = authenticationPage.loginWithInvalidCredentials(nullUsername, nullPassword);

        assertEquals(ApiConstants.StatusCodes.BAD_REQUEST, response.getStatusCode(),
                "Login with null credentials should return 400");
    }
}