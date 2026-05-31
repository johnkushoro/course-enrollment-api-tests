package com.courseenrollment.tests;

import com.courseenrollment.base.BaseTest;
import com.courseenrollment.clients.EnrolmentClient;
import com.courseenrollment.config.ApiConfig;
import com.courseenrollment.config.ApiConstants;
import com.courseenrollment.models.CourseRequest;
import com.courseenrollment.models.EnrolmentRequest;
import com.courseenrollment.pages.CoursePage;
import com.courseenrollment.utils.TestDataGenerator;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("End-to-End User Journey Test")
class EndToEndJourneyTest extends BaseTest {

    private static final String COURSE_ID_NOT_NULL_MESSAGE = "Course ID should not be null after creation";

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
    @DisplayName("Should complete instructor course creation workflow")
    void testInstructorCreatesCourse() {
        CourseRequest courseRequest = TestDataGenerator.createCourseRequest(
                "End-to-End Test Course",
                ApiConfig.INSTRUCTOR_USERNAME,
                TestDataGenerator.generateCourseCode(),
                10
        );
        String courseId = null;

        try {
            Response response = coursePage.createCourse(courseRequest, instructorToken);

            assertEquals(ApiConstants.StatusCodes.CREATED, response.getStatusCode(),
                    "Course creation should return 201");
            courseId = coursePage.extractCourseId(response);
            assertNotNull(courseId, COURSE_ID_NOT_NULL_MESSAGE);
        } finally {
            if (courseId != null) {
                coursePage.deleteCourse(courseId, instructorToken);
            }
        }
    }

    @Test
    @DisplayName("Should complete student enrollment workflow")
    void testStudentEnrollsInCourse() {
        CourseRequest courseRequest = TestDataGenerator.createCourseRequest();
        String courseId = null;
        
        try {
            Response createResponse = coursePage.createCourse(courseRequest, instructorToken);
            assertEquals(ApiConstants.StatusCodes.CREATED, createResponse.getStatusCode());
            courseId = coursePage.extractCourseId(createResponse);
            assertNotNull(courseId, COURSE_ID_NOT_NULL_MESSAGE);
            String courseCode = courseRequest.getCourseCode();
            
            EnrolmentRequest enrolmentRequest = TestDataGenerator.createEnrolmentRequest(courseCode);

            Response enrolResponse = enrolmentClient.enrolInCourse(enrolmentRequest, studentToken);

            assertEquals(ApiConstants.StatusCodes.CREATED, enrolResponse.getStatusCode(),
                    "Enrolment should succeed before continuing");
        } finally {
            if (courseId != null) {
                coursePage.deleteCourse(courseId, instructorToken);
            }
        }
    }

    @Test
    @DisplayName("Should check course availability")
    void testCheckCourseAvailability() {
        CourseRequest courseRequest = TestDataGenerator.createCourseRequest();
        String courseId = null;

        try {
            Response createResponse = coursePage.createCourse(courseRequest, instructorToken);
            assertEquals(ApiConstants.StatusCodes.CREATED, createResponse.getStatusCode());
            courseId = coursePage.extractCourseId(createResponse);
            assertNotNull(courseId, COURSE_ID_NOT_NULL_MESSAGE);
            String courseCode = courseRequest.getCourseCode();

            Response availabilityResponse = coursePage.getCourseAvailability(courseCode);

            assertEquals(ApiConstants.StatusCodes.OK, availabilityResponse.getStatusCode(),
                    "Course availability check should return 200");
        } finally {
            if (courseId != null) {
                coursePage.deleteCourse(courseId, instructorToken);
            }
        }
    }

    @Test
    @DisplayName("Should get active enrollments")
    void testGetActiveEnrolments() {
        Response response = enrolmentClient.getActiveEnrolments(ApiConfig.STUDENT_USERNAME, studentToken);

        assertEquals(ApiConstants.StatusCodes.OK, response.getStatusCode(),
                "Getting active enrollments should return 200");
        assertFalse(response.asString().trim().isEmpty(), "Response body should not be empty");
    }

    @Test
    @DisplayName("Should get enrollment history")
    void testGetEnrolmentHistory() {
        Response response = enrolmentClient.getEnrolmentHistory(ApiConfig.STUDENT_USERNAME, studentToken);

        assertEquals(ApiConstants.StatusCodes.OK, response.getStatusCode(),
                "Getting enrollment history should return 200");
        assertFalse(response.asString().trim().isEmpty(), "Response body should not be empty");
    }

    @Test
    @DisplayName("Should drop from course")
    void testDropFromCourse() {
        CourseRequest courseRequest = TestDataGenerator.createCourseRequest();
        String courseId = null;
        
        try {
            Response createResponse = coursePage.createCourse(courseRequest, instructorToken);
            assertEquals(ApiConstants.StatusCodes.CREATED, createResponse.getStatusCode());
            courseId = coursePage.extractCourseId(createResponse);
            assertNotNull(courseId, COURSE_ID_NOT_NULL_MESSAGE);
            String courseCode = courseRequest.getCourseCode();
            
            EnrolmentRequest enrolmentRequest = TestDataGenerator.createEnrolmentRequest(courseCode);
            Response enrolResponse = enrolmentClient.enrolInCourse(enrolmentRequest, studentToken);
            
            assertEquals(ApiConstants.StatusCodes.CREATED, enrolResponse.getStatusCode(),
                    "Enrolment should succeed before continuing");

            Response dropResponse = enrolmentClient.dropFromCourse(enrolmentRequest, studentToken);

            assertTrue(dropResponse.getStatusCode() == ApiConstants.StatusCodes.OK ||
                      dropResponse.getStatusCode() == ApiConstants.StatusCodes.NO_CONTENT,
                    "Course drop should return 200 or 204");
        } finally {
            if (courseId != null) {
                coursePage.deleteCourse(courseId, instructorToken);
            }
        }
    }

    @Test
    @DisplayName("Should delete course successfully")
    void testDeleteCourse() {
        CourseRequest courseRequest = TestDataGenerator.createCourseRequest();
        Response createResponse = coursePage.createCourse(courseRequest, instructorToken);
        String courseId = coursePage.extractCourseId(createResponse);

        Response deleteResponse = coursePage.deleteCourse(courseId, instructorToken);

        assertTrue(deleteResponse.getStatusCode() == ApiConstants.StatusCodes.OK || 
                  deleteResponse.getStatusCode() == ApiConstants.StatusCodes.NO_CONTENT,
                "Course deletion should return 200 or 204");
    }
}