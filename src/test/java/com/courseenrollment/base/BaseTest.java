package com.courseenrollment.base;

import com.courseenrollment.config.ApiConfig;
import com.courseenrollment.config.ApiConstants;
import com.courseenrollment.pages.PageFactory;
import com.courseenrollment.utils.ExtentManager;
import com.courseenrollment.utils.ExtentTestWatcher;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(ExtentTestWatcher.class)
public abstract class BaseTest {

    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    protected static RequestSpecification baseRequestSpec;
    protected PageFactory pageFactory;

    @BeforeAll
    static void globalSetup() {
        ExtentManager.initReports();
        
        RestAssured.baseURI = ApiConfig.BASE_URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        baseRequestSpec = new RequestSpecBuilder()
                .setContentType(ApiConstants.Headers.APPLICATION_JSON)
                .setAccept(ApiConstants.Headers.APPLICATION_JSON)
                .build();

        logger.info("Base test setup completed. Base URL: {}", ApiConfig.BASE_URL);
    }

    @BeforeEach
    void setUp(TestInfo testInfo) {
        String testName = testInfo.getDisplayName();
        String testClass = testInfo.getTestClass().map(Class::getSimpleName).orElse("Unknown");
        ExtentManager.createTest(testName, "Test in " + testClass);
        
        pageFactory = new PageFactory(baseRequestSpec);
        
        logger.debug("Test setup completed for: {}", testName);
    }
    
    @AfterAll
    static void globalTearDown() {
        logger.info("Generating test report...");
        ExtentManager.flushReports();
        logger.info("Test execution completed");
    }

    protected RequestSpecification getAuthenticatedSpec(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }

        return new RequestSpecBuilder()
                .addRequestSpecification(baseRequestSpec)
                .addHeader(ApiConstants.Headers.AUTHORIZATION, ApiConstants.Headers.BEARER + token)
                .build();
    }

    protected RequestSpecification getBaseSpec() {
        return baseRequestSpec;
    }

    protected String getInstructorToken() {
        Response response = pageFactory.getAuthenticationPage().loginInstructor();
        return pageFactory.getAuthenticationPage().extractToken(response);
    }

    protected String getStudentToken() {
        Response response = pageFactory.getAuthenticationPage().loginStudent();
        return pageFactory.getAuthenticationPage().extractToken(response);
    }
}