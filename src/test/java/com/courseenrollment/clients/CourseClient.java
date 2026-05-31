package com.courseenrollment.clients;

import com.courseenrollment.config.ApiConstants;
import com.courseenrollment.models.CourseRequest;
import com.courseenrollment.models.CourseUpdateRequest;
import com.courseenrollment.utils.ResponseParser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class CourseClient {
    
    private final RequestSpecification baseRequestSpec;
    
    public CourseClient(RequestSpecification baseRequestSpec) {
        this.baseRequestSpec = baseRequestSpec;
    }
    
    public Response createCourse(CourseRequest courseRequest, String instructorToken) {
        return given(baseRequestSpec)
                .header(ApiConstants.Headers.AUTHORIZATION, ApiConstants.Headers.BEARER + instructorToken)
                .body(courseRequest)
                .when()
                .post(ApiConstants.Endpoints.COURSES);
    }
    
    public Response getAllCourses() {
        return given(baseRequestSpec)
                .when()
                .get(ApiConstants.Endpoints.COURSES_ALL);
    }
    
    public Response getCoursesByTitle(String title) {
        return given(baseRequestSpec)
                .pathParam(ApiConstants.PathParams.TITLE, title)
                .when()
                .get(ApiConstants.Endpoints.COURSES_BY_TITLE);
    }

    public Response getCoursesByInstructor(String instructor) {
        return given(baseRequestSpec)
                .pathParam(ApiConstants.PathParams.INSTRUCTOR, instructor)
                .when()
                .get(ApiConstants.Endpoints.COURSES_BY_INSTRUCTOR);
    }

    public Response getCourseAvailability(String courseCode) {
        return given(baseRequestSpec)
                .pathParam(ApiConstants.PathParams.COURSE_CODE, courseCode)
                .when()
                .get(ApiConstants.Endpoints.COURSES_AVAILABILITY);
    }
    
    public Response updateCourse(String courseId, CourseRequest courseRequest, String instructorToken) {
        return given(baseRequestSpec)
                .header(ApiConstants.Headers.AUTHORIZATION, ApiConstants.Headers.BEARER + instructorToken)
                .body(courseRequest)
                .pathParam(ApiConstants.PathParams.ID, courseId)
                .when()
                .put(ApiConstants.Endpoints.COURSES + ApiConstants.Endpoints.ID_PATH);
    }
    
    public Response updateCourse(String courseId, CourseUpdateRequest courseUpdateRequest, String instructorToken) {
        return given(baseRequestSpec)
                .header(ApiConstants.Headers.AUTHORIZATION, ApiConstants.Headers.BEARER + instructorToken)
                .body(courseUpdateRequest)
                .pathParam(ApiConstants.PathParams.ID, courseId)
                .when()
                .put(ApiConstants.Endpoints.COURSES + ApiConstants.Endpoints.ID_PATH);
    }
    
    public Response deleteCourse(String courseId, String instructorToken) {
        return given(baseRequestSpec)
                .header(ApiConstants.Headers.AUTHORIZATION, ApiConstants.Headers.BEARER + instructorToken)
                .pathParam(ApiConstants.PathParams.ID, courseId)
                .when()
                .delete(ApiConstants.Endpoints.COURSES + ApiConstants.Endpoints.ID_PATH);
    }
    
    public String extractCourseId(Response response) {
        return ResponseParser.extractCourseId(response);
    }
    
    public String extractCourseCode(Response response) {
        return ResponseParser.extractCourseCode(response);
    }
    
    public Integer extractAvailableSlots(Response response) {
        return ResponseParser.extractAvailableSlots(response);
    }
    
    public String findCourseIdBySearch(String title) {
        return ResponseParser.findCourseIdBySearch(this, title);
    }
}