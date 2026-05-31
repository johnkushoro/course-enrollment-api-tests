package com.courseenrollment.tests;

import com.courseenrollment.base.BaseTest;
import com.courseenrollment.config.ApiConstants;
import com.courseenrollment.pages.AuthenticationPage;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Authentication Tests")
class AuthenticationTest extends BaseTest {

    private AuthenticationPage authenticationPage;

    @BeforeEach
    void setUpTestData() {
        authenticationPage = pageFactory.getAuthenticationPage();
    }

    @Test
    @DisplayName("Should successfully login instructor and return valid token")
    void testInstructorLogin() {

        Response response = authenticationPage.loginInstructor();

        assertEquals(ApiConstants.StatusCodes.OK, response.getStatusCode(), "Instructor login should return 200");
        String token = authenticationPage.extractToken(response);
        assertNotNull(token, "Token should not be null");
        assertFalse(token.trim().isEmpty(), "Token should not be empty");
    }

    @Test
    @DisplayName("Should successfully login student and return valid token")
    void testStudentLogin() {

        Response response = authenticationPage.loginStudent();

        assertEquals(ApiConstants.StatusCodes.OK, response.getStatusCode(), "Student login should return 200");
        String token = authenticationPage.extractToken(response);
        assertNotNull(token, "Token should not be null");
        assertFalse(token.trim().isEmpty(), "Token should not be empty");
    }

    @Test
    @DisplayName("Should generate valid tokens for multiple login attempts")
    void testTokenGeneration() {

        String token1 = getStudentToken();
        String token2 = getStudentToken();

        assertNotNull(token1, "First token should not be null");
        assertFalse(token1.trim().isEmpty(), "First token should not be empty");
        assertNotNull(token2, "Second token should not be null");
        assertFalse(token2.trim().isEmpty(), "Second token should not be empty");
    }

    @Test
    @DisplayName("Should reject login with invalid credentials")
    void testInvalidCredentials() {
        String invalidUsername = "invalid_user";
        String invalidPassword = "invalid_password";

        Response response = authenticationPage.loginWithInvalidCredentials(invalidUsername, invalidPassword);

        assertEquals(ApiConstants.StatusCodes.UNAUTHORIZED, response.getStatusCode(),
                "Login with invalid credentials should return 401");
    }

    @Test
    @DisplayName("Should reject login with empty credentials")
    void testEmptyCredentials() {

        String emptyUsername = "";
        String emptyPassword = "";

        Response response = authenticationPage.loginWithInvalidCredentials(emptyUsername, emptyPassword);

        assertEquals(ApiConstants.StatusCodes.UNAUTHORIZED, response.getStatusCode(),
                "Login with empty credentials should return 401");
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