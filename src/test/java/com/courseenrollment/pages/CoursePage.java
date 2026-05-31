package com.courseenrollment.pages;

import com.courseenrollment.config.ApiConstants;
import com.courseenrollment.models.CourseRequest;
import com.courseenrollment.models.CourseUpdateRequest;
import com.courseenrollment.pages.base.BasePage;
import com.courseenrollment.pages.interfaces.ICourseOperations;
import com.courseenrollment.pages.interfaces.ICourseQueries;
import com.courseenrollment.utils.ResponseParser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class CoursePage extends BasePage implements ICourseOperations, ICourseQueries {
    
    public CoursePage(RequestSpecification baseRequestSpec) {
        super(baseRequestSpec);
    }
    
    @Override
    public String getEndpointPath() {
        return ApiConstants.Endpoints.COURSES;
    }
    
    @Override
    public Response createCourse(CourseRequest courseRequest, String token) {
        if (token == null) {
            return given(baseRequestSpec)
                    .body(courseRequest)
                    .when()
                    .post(ApiConstants.Endpoints.COURSES);
        }
        return getAuthenticatedSpec(token)
                .body(courseRequest)
                .when()
                .post(ApiConstants.Endpoints.COURSES);
    }
    
    @Override
    public Response updateCourse(String courseId, CourseRequest courseRequest, String token) {
        if (token == null) {
            return given(baseRequestSpec)
                    .body(courseRequest)
                    .pathParam(ApiConstants.PathParams.ID, courseId)
                    .when()
                    .put(ApiConstants.Endpoints.COURSES + ApiConstants.Endpoints.ID_PATH);
        }
        return getAuthenticatedSpec(token)
                .body(courseRequest)
                .pathParam(ApiConstants.PathParams.ID, courseId)
                .when()
                .put(ApiConstants.Endpoints.COURSES + ApiConstants.Endpoints.ID_PATH);
    }
    
    @Override
    public Response updateCourse(String courseId, CourseUpdateRequest courseUpdateRequest, String token) {
        if (token == null) {
            return given(baseRequestSpec)
                    .body(courseUpdateRequest)
                    .pathParam(ApiConstants.PathParams.ID, courseId)
                    .when()
                    .put(ApiConstants.Endpoints.COURSES + ApiConstants.Endpoints.ID_PATH);
        }
        return getAuthenticatedSpec(token)
                .body(courseUpdateRequest)
                .pathParam(ApiConstants.PathParams.ID, courseId)
                .when()
                .put(ApiConstants.Endpoints.COURSES + ApiConstants.Endpoints.ID_PATH);
    }
    
    @Override
    public Response deleteCourse(String courseId, String token) {
        if (token == null) {
            return given(baseRequestSpec)
                    .pathParam(ApiConstants.PathParams.ID, courseId)
                    .when()
                    .delete(ApiConstants.Endpoints.COURSES + ApiConstants.Endpoints.ID_PATH);
        }
        return getAuthenticatedSpec(token)
                .pathParam(ApiConstants.PathParams.ID, courseId)
                .when()
                .delete(ApiConstants.Endpoints.COURSES + ApiConstants.Endpoints.ID_PATH);
    }
    
    @Override
    public Response getAllCourses() {
        return given(baseRequestSpec)
                .when()
                .get(ApiConstants.Endpoints.COURSES_ALL);
    }
    
    @Override
    public Response getCoursesByTitle(String title) {
        return given(baseRequestSpec)
                .pathParam(ApiConstants.PathParams.TITLE, title)
                .when()
                .get(ApiConstants.Endpoints.COURSES_BY_TITLE);
    }
    
    @Override
    public Response getCoursesByInstructor(String instructor) {
        return given(baseRequestSpec)
                .pathParam(ApiConstants.PathParams.INSTRUCTOR, instructor)
                .when()
                .get(ApiConstants.Endpoints.COURSES_BY_INSTRUCTOR);
    }
    
    @Override
    public Response getCourseAvailability(String courseCode) {
        return given(baseRequestSpec)
                .pathParam(ApiConstants.PathParams.COURSE_CODE, courseCode)
                .when()
                .get(ApiConstants.Endpoints.COURSES_AVAILABILITY);
    }
    
    public String extractCourseId(Response response) {
        return ResponseParser.extractCourseId(response);
    }
    
    public Response createCourseWithoutToken(CourseRequest courseRequest) {
        return given(baseRequestSpec)
                .body(courseRequest)
                .when()
                .post(ApiConstants.Endpoints.COURSES);
    }

}