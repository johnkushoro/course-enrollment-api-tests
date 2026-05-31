package com.courseenrollment.tests;

import com.courseenrollment.base.BaseTest;
import com.courseenrollment.config.ApiConfig;
import com.courseenrollment.config.ApiConstants;
import com.courseenrollment.models.CourseRequest;
import com.courseenrollment.models.CourseUpdateRequest;
import com.courseenrollment.pages.CoursePage;
import com.courseenrollment.utils.TestDataGenerator;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Course Negative Tests")
class CourseNegativeTest extends BaseTest {

    private static final String SOME_COURSE_ID = "some-course-id";
    private static final String COURSE_ID_NOT_NULL_MESSAGE = "Course ID should not be null after creation";
    private static final String UPDATED_TITLE = "Updated Title";
    private static final String COURSE_CREATION_SUCCESS_MESSAGE = "Course creation should return 201 Created";
    
    private String getFutureEndDate() {
        return java.time.LocalDate.now().plusDays(30).toString();
    }

    private CoursePage coursePage;
    private String instructorToken;
    private String studentToken;

    @BeforeEach
    void setUpTestData() {
        coursePage = pageFactory.getCoursePage();
        instructorToken = getInstructorToken();
        studentToken = getStudentToken();
    }

    @Test
    @DisplayName("Should reject course creation without token")
    void testCreateCourseWithoutToken() {

        CourseRequest courseRequest = TestDataGenerator.createCourseRequest();

        Response response = coursePage.createCourseWithoutToken(courseRequest);

        assertEquals(ApiConstants.StatusCodes.UNAUTHORIZED, response.getStatusCode(),
                "Creating course without token should return 401");
    }

    @Test
    @DisplayName("Should reject course creation with student token")
    void testCreateCourseWithStudentToken() {

        CourseRequest courseRequest = TestDataGenerator.createCourseRequest();

        Response response = coursePage.createCourse(courseRequest, studentToken);

        assertEquals(ApiConstants.StatusCodes.FORBIDDEN, response.getStatusCode(),
                "Creating course with student token should return 403");
    }

    @Test
    @DisplayName("Should reject course creation with invalid token")
    void testCreateCourseWithInvalidToken() {

        CourseRequest courseRequest = TestDataGenerator.createCourseRequest();
        String invalidToken = "invalid_token_123";

        Response response = coursePage.createCourse(courseRequest, invalidToken);

        int statusCode = response.getStatusCode();
        assertTrue(statusCode == ApiConstants.StatusCodes.UNAUTHORIZED || statusCode == ApiConstants.StatusCodes.FORBIDDEN,
                "Creating course with invalid token should return 401 or 403, got: " + statusCode);
    }

    @Test
    @DisplayName("Should reject course creation with duplicate course code")
    void testCreateCourseWithDuplicateCode() {
        String duplicateCode = TestDataGenerator.generateCourseCode();
        CourseRequest firstCourse = TestDataGenerator.createCourseRequest(
                "First Course",
                ApiConfig.INSTRUCTOR_USERNAME,
                duplicateCode,
                10
        );
        CourseRequest secondCourse = TestDataGenerator.createCourseRequest(
                "Duplicate Course",
                ApiConfig.INSTRUCTOR_USERNAME,
                duplicateCode,
                15
        );
        String courseId = null;

        try {
            Response firstResponse = coursePage.createCourse(firstCourse, instructorToken);
            assertEquals(ApiConstants.StatusCodes.CREATED, firstResponse.getStatusCode(),
                    "First course creation should succeed");
            courseId = coursePage.extractCourseId(firstResponse);
            assertNotNull(courseId, COURSE_ID_NOT_NULL_MESSAGE);

            Response secondResponse = coursePage.createCourse(secondCourse, instructorToken);

            assertEquals(ApiConstants.StatusCodes.BAD_REQUEST, secondResponse.getStatusCode(),
                    "Creating course with duplicate code should return 400");
        } finally {
            if (courseId != null) {
                coursePage.deleteCourse(courseId, instructorToken);
            }
        }
    }

    @Test
    @DisplayName("Should reject course creation with invalid data")
    void testCreateCourseWithInvalidData() {
        CourseRequest invalidCourse = TestDataGenerator.createCourseRequest(
                "",
                ApiConfig.INSTRUCTOR_USERNAME,
                "",
                -1
        );

        Response response = coursePage.createCourse(invalidCourse, instructorToken);

        assertEquals(ApiConstants.StatusCodes.BAD_REQUEST, response.getStatusCode(),
                "Creating course with invalid data should return 400");
    }

    @Test
    @DisplayName("Should reject course creation with null title")
    void testCreateCourseWithNullTitle() {
        CourseRequest courseRequest = TestDataGenerator.createCourseRequest(
                null,
                ApiConfig.INSTRUCTOR_USERNAME,
                TestDataGenerator.generateCourseCode(),
                10
        );

        Response response = coursePage.createCourse(courseRequest, instructorToken);

        assertEquals(ApiConstants.StatusCodes.BAD_REQUEST, response.getStatusCode(),
                "Creating course with null title should return 400");
    }

    @Test
    @DisplayName("Should reject course creation with negative capacity")
    void testCreateCourseWithNegativeCapacity() {
        CourseRequest courseRequest = TestDataGenerator.createCourseRequest(
                "Valid Course Title",
                ApiConfig.INSTRUCTOR_USERNAME,
                TestDataGenerator.generateCourseCode(),
                -5
        );

        Response response = coursePage.createCourse(courseRequest, instructorToken);

        assertEquals(ApiConstants.StatusCodes.BAD_REQUEST, response.getStatusCode(),
                "Creating course with negative capacity should return 400");
    }

    @Test
    @DisplayName("Should reject course creation with zero capacity")
    void testCreateCourseWithZeroCapacity() {
        CourseRequest courseRequest = TestDataGenerator.createCourseRequest(
                "Valid Course Title",
                ApiConfig.INSTRUCTOR_USERNAME,
                TestDataGenerator.generateCourseCode(),
                0
        );

        Response response = coursePage.createCourse(courseRequest, instructorToken);

        assertEquals(ApiConstants.StatusCodes.BAD_REQUEST, response.getStatusCode(),
                "Creating course with zero capacity should return 400");
    }

    @Test
    @DisplayName("Should reject course update without token")
    void testUpdateCourseWithoutToken() {
        CourseRequest courseRequest = TestDataGenerator.createCourseRequest();
        String courseId = null;

        try {
            Response createResponse = coursePage.createCourse(courseRequest, instructorToken);
            assertEquals(ApiConstants.StatusCodes.CREATED, createResponse.getStatusCode(),
                    COURSE_CREATION_SUCCESS_MESSAGE);
            
            courseId = coursePage.extractCourseId(createResponse);
            assertNotNull(courseId, COURSE_ID_NOT_NULL_MESSAGE);
            CourseUpdateRequest updateRequest = new CourseUpdateRequest(UPDATED_TITLE, null, null, getFutureEndDate());

            Response updateResponse = coursePage.updateCourse(courseId, updateRequest, null);

            assertEquals(ApiConstants.StatusCodes.UNAUTHORIZED, updateResponse.getStatusCode(),
                    "Updating course without token should return 401");
        } finally {
            if (courseId != null) {
                coursePage.deleteCourse(courseId, instructorToken);
            }
        }
    }

    @Test
    @DisplayName("Should reject course update with student token")
    void testUpdateCourseWithStudentToken() {
        CourseRequest courseRequest = TestDataGenerator.createCourseRequest();
        String courseId = null;

        try {
            Response createResponse = coursePage.createCourse(courseRequest, instructorToken);
            assertEquals(ApiConstants.StatusCodes.CREATED, createResponse.getStatusCode(),
                    COURSE_CREATION_SUCCESS_MESSAGE);
            
            courseId = coursePage.extractCourseId(createResponse);
            assertNotNull(courseId, COURSE_ID_NOT_NULL_MESSAGE);
            CourseUpdateRequest updateRequest = new CourseUpdateRequest(UPDATED_TITLE, null, null, getFutureEndDate());

            Response updateResponse = coursePage.updateCourse(courseId, updateRequest, studentToken);

            assertEquals(ApiConstants.StatusCodes.FORBIDDEN, updateResponse.getStatusCode(),
                    "Updating course with student token should return 403");
        } finally {
            if (courseId != null) {
                coursePage.deleteCourse(courseId, instructorToken);
            }
        }
    }

    @Test
    @DisplayName("Should reject course update for non-existent course")
    void testUpdateNonExistentCourse() {

        CourseUpdateRequest updateRequest = new CourseUpdateRequest(UPDATED_TITLE, null, null, getFutureEndDate());

        Response response = coursePage.updateCourse(SOME_COURSE_ID, updateRequest, instructorToken);

        assertEquals(ApiConstants.StatusCodes.NOT_FOUND, response.getStatusCode(),
                "Updating non-existent course should return 404");
    }

    @Test
    @DisplayName("Should reject course deletion without token")
    void testDeleteCourseWithoutToken() {
        CourseRequest courseRequest = TestDataGenerator.createCourseRequest();
        String courseId = null;

        try {
            Response createResponse = coursePage.createCourse(courseRequest, instructorToken);
            assertEquals(ApiConstants.StatusCodes.CREATED, createResponse.getStatusCode());
            courseId = coursePage.extractCourseId(createResponse);
            assertNotNull(courseId, COURSE_ID_NOT_NULL_MESSAGE);

            Response deleteResponse = coursePage.deleteCourse(courseId, null);

            assertEquals(ApiConstants.StatusCodes.UNAUTHORIZED, deleteResponse.getStatusCode(),
                    "Deleting course without token should return 401");
        } finally {
            if (courseId != null) {
                coursePage.deleteCourse(courseId, instructorToken);
            }
        }
    }

    @Test
    @DisplayName("Should reject course deletion with student token")
    void testDeleteCourseWithStudentToken() {
        CourseRequest courseRequest = TestDataGenerator.createCourseRequest();
        String courseId = null;

        try {
            Response createResponse = coursePage.createCourse(courseRequest, instructorToken);
            assertEquals(ApiConstants.StatusCodes.CREATED, createResponse.getStatusCode());
            courseId = coursePage.extractCourseId(createResponse);
            assertNotNull(courseId, COURSE_ID_NOT_NULL_MESSAGE);

            Response deleteResponse = coursePage.deleteCourse(courseId, studentToken);

            assertEquals(ApiConstants.StatusCodes.FORBIDDEN, deleteResponse.getStatusCode(),
                    "Deleting course with student token should return 403");
        } finally {
            if (courseId != null) {
                coursePage.deleteCourse(courseId, instructorToken);
            }
        }
    }

    @Test
    @DisplayName("Should reject deletion of non-existent course")
    void testDeleteNonExistentCourse() {

        String nonExistentCourseId = SOME_COURSE_ID;

        Response response = coursePage.deleteCourse(nonExistentCourseId, instructorToken);

        assertEquals(ApiConstants.StatusCodes.NOT_FOUND, response.getStatusCode(),
                "Deleting non-existent course should return 404");
    }
}