package com.courseenrollment.tests;

import com.courseenrollment.base.BaseTest;
import com.courseenrollment.config.ApiConfig;
import com.courseenrollment.config.ApiConstants;
import com.courseenrollment.models.CourseRequest;
import com.courseenrollment.models.EnrolmentRequest;
import com.courseenrollment.pages.CoursePage;
import com.courseenrollment.clients.EnrolmentClient;
import com.courseenrollment.utils.TestDataGenerator;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Enrolment Negative Tests")
class EnrolmentNegativeTest extends BaseTest {

    private static final String TEST_COURSE_CODE = "JAVA101";
    private static final String INVALID_TOKEN = "invalid_token_123";

    private CoursePage coursePage;
    private EnrolmentClient enrolmentClient;
    private String instructorToken;
    private String studentToken;

    @BeforeEach
    void setUpTestData() {
        coursePage = pageFactory.getCoursePage();
        enrolmentClient = new EnrolmentClient(getBaseSpec());
        instructorToken = getInstructorToken();
        studentToken = getStudentToken();
    }

    @Test
    @DisplayName("Should reject enrolment without token")
    void testEnrolWithoutToken() {
        EnrolmentRequest request = new EnrolmentRequest(TEST_COURSE_CODE, ApiConfig.STUDENT_USERNAME);

        Response response = given(getBaseSpec())
                .body(request)
                .when()
                .post(ApiConstants.Endpoints.ENROLMENTS_ENROL);

        assertEquals(ApiConstants.StatusCodes.UNAUTHORIZED, response.getStatusCode(),
                "Enrolling without token should return 401");
    }

    @Test
    @DisplayName("Should return not found when enrolling into non-existent course")
    void testEnrolIntoNonExistentCourse() {
        EnrolmentRequest request = new EnrolmentRequest("COURSE_DOES_NOT_EXIST_999", ApiConfig.STUDENT_USERNAME);

        Response response = enrolmentClient.enrolInCourse(request, studentToken);

        assertEquals(ApiConstants.StatusCodes.NOT_FOUND, response.getStatusCode(),
                "Enrolling into non-existent course should return 404");
    }

    @Test
    @DisplayName("Should reject duplicate enrolment for same student and course")
    void testDuplicateEnrolment() {
        CourseRequest courseRequest = TestDataGenerator.createCourseRequest(
                "Duplicate Enrolment Test Course",
                ApiConfig.INSTRUCTOR_USERNAME,
                TestDataGenerator.generateCourseCode(),
                5
        );
        String courseId = null;
        EnrolmentRequest enrolmentRequest = null;

        try {
            Response createResponse = coursePage.createCourse(courseRequest, instructorToken);
            assertEquals(ApiConstants.StatusCodes.CREATED, createResponse.getStatusCode());
            courseId = coursePage.extractCourseId(createResponse);
            assertNotNull(courseId, "Course ID should not be null after creation");
            String courseCode = courseRequest.getCourseCode();
            enrolmentRequest = new EnrolmentRequest(courseCode, ApiConfig.STUDENT_USERNAME);

            Response firstResponse = enrolmentClient.enrolInCourse(enrolmentRequest, studentToken);
            assertEquals(ApiConstants.StatusCodes.CREATED, firstResponse.getStatusCode(),
                    "First enrolment should succeed");

            Response secondResponse = enrolmentClient.enrolInCourse(enrolmentRequest, studentToken);

            assertEquals(ApiConstants.StatusCodes.BAD_REQUEST, secondResponse.getStatusCode(),
                    "Duplicate enrolment should return 400");
        } finally {
            if (enrolmentRequest != null) {
                enrolmentClient.dropFromCourse(enrolmentRequest, studentToken);
            }
            if (courseId != null) {
                coursePage.deleteCourse(courseId, instructorToken);
            }
        }
    }

    @Test
    @DisplayName("Should return not found when dropping course not actively enrolled")
    void testDropCourseNotEnrolled() {
        EnrolmentRequest request = new EnrolmentRequest("COURSE_DOES_NOT_EXIST_999", ApiConfig.STUDENT_USERNAME);

        Response response = enrolmentClient.dropFromCourse(request, studentToken);

        assertEquals(ApiConstants.StatusCodes.NOT_FOUND, response.getStatusCode(),
                "Dropping non-existent or not actively enrolled course should return 404");
    }

    @Test
    @DisplayName("Should reject active enrolments lookup without token")
    void testActiveEnrolmentsWithoutToken() {
        EnrolmentRequest request = new EnrolmentRequest();
        request.setUsername(ApiConfig.STUDENT_USERNAME);

        Response response = given(getBaseSpec())
                .body(request)
                .when()
                .post(ApiConstants.Endpoints.ENROLMENTS_ACTIVE);

        assertEquals(ApiConstants.StatusCodes.UNAUTHORIZED, response.getStatusCode(),
                "Active enrolments without token should return 401");
    }

    @Test
    @DisplayName("Should reject enrolment history lookup without token")
    void testEnrolmentHistoryWithoutToken() {
        EnrolmentRequest request = new EnrolmentRequest();
        request.setUsername(ApiConfig.STUDENT_USERNAME);

        Response response = given(getBaseSpec())
                .body(request)
                .when()
                .post(ApiConstants.Endpoints.ENROLMENTS_HISTORY);

        assertEquals(ApiConstants.StatusCodes.UNAUTHORIZED, response.getStatusCode(),
                "Enrolment history without token should return 401");
    }

    @Test
    @DisplayName("Should reject enrolment with invalid course code")
    void testEnrolWithInvalidCourseCode() {
        EnrolmentRequest request = new EnrolmentRequest("", ApiConfig.STUDENT_USERNAME);

        Response response = enrolmentClient.enrolInCourse(request, studentToken);

        assertEquals(ApiConstants.StatusCodes.BAD_REQUEST, response.getStatusCode(),
                "Enrolling with empty course code should return 400");
    }

    @Test
    @DisplayName("Should reject enrolment with null username")
    void testEnrolWithNullUsername() {
        EnrolmentRequest request = new EnrolmentRequest(TEST_COURSE_CODE, null);

        Response response = enrolmentClient.enrolInCourse(request, studentToken);

        assertEquals(ApiConstants.StatusCodes.BAD_REQUEST, response.getStatusCode(),
                "Enrolling with null username should return 400");
    }

    @Test
    @DisplayName("Should reject drop with invalid token")
    void testDropWithInvalidToken() {
        EnrolmentRequest request = new EnrolmentRequest(TEST_COURSE_CODE, ApiConfig.STUDENT_USERNAME);
        String invalidToken = INVALID_TOKEN;

        Response response = enrolmentClient.dropFromCourse(request, invalidToken);

        int statusCode = response.getStatusCode();
        assertTrue(statusCode == ApiConstants.StatusCodes.UNAUTHORIZED || 
                  statusCode == ApiConstants.StatusCodes.FORBIDDEN,
                "Drop with invalid token should return 401 or 403, got: " + statusCode);
    }
}