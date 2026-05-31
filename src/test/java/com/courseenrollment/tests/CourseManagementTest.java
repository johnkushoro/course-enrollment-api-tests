package com.courseenrollment.tests;

import com.courseenrollment.base.BaseTest;
import com.courseenrollment.config.ApiConfig;
import com.courseenrollment.config.ApiConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.courseenrollment.models.CourseRequest;
import com.courseenrollment.pages.CoursePage;
import com.courseenrollment.utils.TestDataGenerator;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Course Management Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CourseManagementTest extends BaseTest {
    
    private static final Logger logger = LoggerFactory.getLogger(CourseManagementTest.class);
    private static final String COURSE_CREATION_SUCCESS_MESSAGE = "Course creation should return 201 Created";
    private static final String COURSE_ID_NOT_NULL_MESSAGE = "Course ID should not be null after creation";

    private CoursePage coursePage;
    private String instructorToken;

    @BeforeEach
    void setUpTestData() {
        coursePage = pageFactory.getCoursePage();
        instructorToken = getInstructorToken();
    }
    
    private void cleanupCourse(String courseId) {
        if (courseId != null) {
            Response deleteResponse = coursePage.deleteCourse(courseId, instructorToken);
            
            if (deleteResponse.getStatusCode() != ApiConstants.StatusCodes.OK &&
                deleteResponse.getStatusCode() != ApiConstants.StatusCodes.NO_CONTENT) {
                
                logger.warn(
                    "Cleanup failed for course {}. Status: {}",
                    courseId,
                    deleteResponse.getStatusCode()
                );
            }
        }
    }

    @Test
    @Order(1)
    @DisplayName("Should successfully create a new course")
    void testCreateCourse() {
        CourseRequest courseRequest = TestDataGenerator.createCourseRequest();
        String courseId = null;

        try {
            Response response = coursePage.createCourse(courseRequest, instructorToken);

            assertEquals(ApiConstants.StatusCodes.CREATED, response.getStatusCode(),
                    COURSE_CREATION_SUCCESS_MESSAGE);
            
            courseId = coursePage.extractCourseId(response);
            assertNotNull(courseId, COURSE_ID_NOT_NULL_MESSAGE);
            
            // Validate response content
            assertEquals(courseRequest.getTitle(), response.jsonPath().getString("title"),
                    "Response should contain the correct course title");
            assertEquals(courseRequest.getCourseCode(), response.jsonPath().getString("courseCode"),
                    "Response should contain the correct course code");
            assertEquals(courseRequest.getInstructor(), response.jsonPath().getString("instructor"),
                    "Response should contain the correct instructor");
        } finally {
            cleanupCourse(courseId);
        }
    }

    @Test
    @Order(2)
    @DisplayName("Should successfully retrieve all courses")
    void testGetAllCourses() {
        Response response = coursePage.getAllCourses();

        assertEquals(ApiConstants.StatusCodes.OK, response.getStatusCode(),
                "Get all courses should return 200 OK");
        assertFalse(response.asString().trim().isEmpty(), "Response body should not be empty");
    }

    @Test
    @Order(3)
    @DisplayName("Should successfully retrieve courses by title")
    void testGetCoursesByTitle() {
        CourseRequest courseRequest = TestDataGenerator.createCourseRequest();
        String courseId = null;

        try {
            Response createResponse = coursePage.createCourse(courseRequest, instructorToken);
            assertEquals(ApiConstants.StatusCodes.CREATED, createResponse.getStatusCode(),
                    COURSE_CREATION_SUCCESS_MESSAGE);
            
            courseId = coursePage.extractCourseId(createResponse);
            assertNotNull(courseId, COURSE_ID_NOT_NULL_MESSAGE);
            String courseTitle = courseRequest.getTitle();

            Response response = coursePage.getCoursesByTitle(courseTitle);

            assertEquals(ApiConstants.StatusCodes.OK, response.getStatusCode(),
                    "Get courses by title should return 200 OK");
        } finally {
            cleanupCourse(courseId);
        }
    }

    @Test
    @Order(4)
    @DisplayName("Should successfully retrieve courses by instructor")
    void testGetCoursesByInstructor() {
        String instructorName = ApiConfig.INSTRUCTOR_USERNAME;

        Response response = coursePage.getCoursesByInstructor(instructorName);

        assertEquals(ApiConstants.StatusCodes.OK, response.getStatusCode(),
                "Get courses by instructor should return 200 OK");
    }

    @Test
    @Order(5)
    @DisplayName("Should successfully check course availability")
    void testGetCourseAvailability() {
        CourseRequest courseRequest = TestDataGenerator.createCourseRequest();
        String courseId = null;

        try {
            Response createResponse = coursePage.createCourse(courseRequest, instructorToken);
            assertEquals(ApiConstants.StatusCodes.CREATED, createResponse.getStatusCode(),
                    COURSE_CREATION_SUCCESS_MESSAGE);
            
            courseId = coursePage.extractCourseId(createResponse);
            assertNotNull(courseId, COURSE_ID_NOT_NULL_MESSAGE);
            String courseCode = courseRequest.getCourseCode();

            Response response = coursePage.getCourseAvailability(courseCode);

            assertEquals(ApiConstants.StatusCodes.OK, response.getStatusCode(),
                    "Get course availability should return 200 OK");
        } finally {
            cleanupCourse(courseId);
        }
    }

    @Test
    @Order(6)
    @DisplayName("Should successfully update a course")
    void testUpdateCourse() {
        CourseRequest originalCourse = TestDataGenerator.createCourseRequest();
        String courseId = null;
        
        try {
            Response createResponse = coursePage.createCourse(originalCourse, instructorToken);
            assertEquals(ApiConstants.StatusCodes.CREATED, createResponse.getStatusCode(),
                    COURSE_CREATION_SUCCESS_MESSAGE);
            
            courseId = coursePage.extractCourseId(createResponse);
            assertNotNull(courseId, COURSE_ID_NOT_NULL_MESSAGE);
            
            CourseRequest updatedCourse = TestDataGenerator.createCourseRequest(
                    "Updated Course Title",
                    ApiConfig.INSTRUCTOR_USERNAME,
                    originalCourse.getCourseCode(),
                    20
            );

            Response updateResponse = coursePage.updateCourse(courseId, updatedCourse, instructorToken);

            assertEquals(ApiConstants.StatusCodes.OK, updateResponse.getStatusCode(),
                    "Course update should return 200 OK");
        } finally {
            cleanupCourse(courseId);
        }
    }

    @Test
    @Order(7)
    @DisplayName("Should successfully delete a course")
    void testDeleteCourse() {
        CourseRequest courseRequest = TestDataGenerator.createCourseRequest();
        Response createResponse = coursePage.createCourse(courseRequest, instructorToken);
        String courseId = coursePage.extractCourseId(createResponse);

        Response deleteResponse = coursePage.deleteCourse(courseId, instructorToken);

        assertTrue(deleteResponse.getStatusCode() == ApiConstants.StatusCodes.OK || 
                  deleteResponse.getStatusCode() == ApiConstants.StatusCodes.NO_CONTENT,
                "Course deletion should return 200 OK or 204 No Content");
    }

    @Test
    @Order(8)
    @DisplayName("Should return 404 for non-existent course by title")
    void testGetNonExistentCourseByTitle() {
        String nonExistentTitle = "NonExistentCourse_" + System.currentTimeMillis();

        Response response = coursePage.getCoursesByTitle(nonExistentTitle);

        assertEquals(ApiConstants.StatusCodes.NOT_FOUND, response.getStatusCode(),
                "Get non-existent course by title should return 404");
    }

    @Test
    @Order(9)
    @DisplayName("Should return 404 for non-existent course availability")
    void testGetNonExistentCourseAvailability() {
        String nonExistentCourseCode = "NONEXIST" + System.currentTimeMillis();

        Response response = coursePage.getCourseAvailability(nonExistentCourseCode);

        assertEquals(ApiConstants.StatusCodes.NOT_FOUND, response.getStatusCode(),
                "Get non-existent course availability should return 404");
    }
}