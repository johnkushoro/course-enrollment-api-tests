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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Course Update And Instructor Search Tests")
class CourseUpdateAndInstructorSearchTest extends BaseTest {

    private CoursePage coursePage;
    private String instructorToken;

    @BeforeEach
    void setUpTestData() {
        coursePage = pageFactory.getCoursePage();
        instructorToken = getInstructorToken();
    }

    @Test
    @DisplayName("Should update course successfully")
    void testUpdateCourse() {
        CourseRequest originalCourse = TestDataGenerator.createCourseRequest(
                "Original Update Test Course",
                ApiConfig.INSTRUCTOR_USERNAME,
                TestDataGenerator.generateCourseCode(),
                12
        );
        Response createResponse = coursePage.createCourse(originalCourse, instructorToken);
        String courseId = coursePage.extractCourseId(createResponse);
        
        String updatedTitle = "Updated Course Title";
        int updatedTotalCapacity = 20;
        int updatedAvailableSlots = 20;
        String updatedEndDate = LocalDate.now().plusDays(120).toString();
        CourseUpdateRequest updateRequest = new CourseUpdateRequest(
                updatedTitle,
                updatedTotalCapacity,
                updatedAvailableSlots,
                updatedEndDate
        );

        Response updateResponse = coursePage.updateCourse(courseId, updateRequest, instructorToken);

        assertEquals(ApiConstants.StatusCodes.OK, updateResponse.getStatusCode(),
                "Course update should return 200");
        String responseBody = updateResponse.getBody().asString();
        assertNotNull(responseBody, "Update response should not be null");
        assertFalse(responseBody.trim().isEmpty(), "Update response should not be empty");

        coursePage.deleteCourse(courseId, instructorToken);
    }

    @Test
    @DisplayName("Should search courses by instructor successfully")
    void testSearchCoursesByInstructor() {
        CourseRequest courseRequest = TestDataGenerator.createCourseRequest(
                "Instructor Search Test Course",
                ApiConfig.INSTRUCTOR_USERNAME,
                TestDataGenerator.generateCourseCode(),
                15
        );
        Response createResponse = coursePage.createCourse(courseRequest, instructorToken);
        String courseId = coursePage.extractCourseId(createResponse);

        Response searchResponse = coursePage.getCoursesByInstructor(ApiConfig.INSTRUCTOR_USERNAME);

        assertEquals(ApiConstants.StatusCodes.OK, searchResponse.getStatusCode(),
                "Search by instructor should return 200");
        String body = searchResponse.getBody().asString();
        assertNotNull(body, "Search response should not be null");
        assertFalse(body.trim().isEmpty(), "Search response should not be empty");

        coursePage.deleteCourse(courseId, instructorToken);
    }

    @Test
    @DisplayName("Should return 404 for non-existent instructor")
    void testSearchNonExistentInstructor() {
        String nonExistentInstructor = "nonexistent_instructor_" + System.currentTimeMillis();

        Response response = coursePage.getCoursesByInstructor(nonExistentInstructor);

        assertEquals(ApiConstants.StatusCodes.NOT_FOUND, response.getStatusCode(),
                "Search for non-existent instructor should return 404");
    }

    @Test
    @DisplayName("Should update course with partial data")
    void testPartialCourseUpdate() {
        CourseRequest originalCourse = TestDataGenerator.createCourseRequest();
        Response createResponse = coursePage.createCourse(originalCourse, instructorToken);
        String courseId = coursePage.extractCourseId(createResponse);
        
        CourseUpdateRequest partialUpdate = new CourseUpdateRequest(
                "Partially Updated Title",
                null,
                null,
                null
        );

        Response updateResponse = coursePage.updateCourse(courseId, partialUpdate, instructorToken);

        assertEquals(ApiConstants.StatusCodes.OK, updateResponse.getStatusCode(),
                "Partial course update should return 200");

        coursePage.deleteCourse(courseId, instructorToken);
    }

    @Test
    @DisplayName("Should reject update with invalid course ID")
    void testUpdateWithInvalidCourseId() {
        String invalidCourseId = "invalid_course_id_123";
        CourseUpdateRequest updateRequest = new CourseUpdateRequest(
                "Updated Title",
                20,
                20,
                LocalDate.now().plusDays(30).toString()
        );

        Response response = coursePage.updateCourse(invalidCourseId, updateRequest, instructorToken);

        assertEquals(ApiConstants.StatusCodes.NOT_FOUND, response.getStatusCode(),
                "Update with invalid course ID should return 404");
    }

    @Test
    @DisplayName("Should reject update without authentication")
    void testUpdateWithoutAuth() {
        CourseRequest originalCourse = TestDataGenerator.createCourseRequest();
        Response createResponse = coursePage.createCourse(originalCourse, instructorToken);
        String courseId = coursePage.extractCourseId(createResponse);
        
        CourseUpdateRequest updateRequest = new CourseUpdateRequest(
                "Unauthorized Update",
                null,
                null,
                null
        );

        Response updateResponse = coursePage.updateCourse(courseId, updateRequest, null);

        assertEquals(ApiConstants.StatusCodes.UNAUTHORIZED, updateResponse.getStatusCode(),
                "Update without authentication should return 401");

        coursePage.deleteCourse(courseId, instructorToken);
    }

    @Test
    @DisplayName("Should handle empty instructor search gracefully")
    void testEmptyInstructorSearch() {
        String emptyInstructor = "";

        Response response = coursePage.getCoursesByInstructor(emptyInstructor);

        int statusCode = response.getStatusCode();
        assertTrue(statusCode == ApiConstants.StatusCodes.BAD_REQUEST || 
                  statusCode == ApiConstants.StatusCodes.NOT_FOUND,
                "Empty instructor search should return 400 or 404, got: " + statusCode);
    }
}