package com.courseenrollment.pages.interfaces;

import io.restassured.response.Response;

public interface IAuthenticationOperations {
    Response loginInstructor();
    Response loginStudent();
    String extractToken(Response response);
}