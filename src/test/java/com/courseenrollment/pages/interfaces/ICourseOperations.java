package com.courseenrollment.pages.interfaces;

import com.courseenrollment.models.CourseRequest;
import com.courseenrollment.models.CourseUpdateRequest;
import io.restassured.response.Response;

public interface ICourseOperations {
    Response createCourse(CourseRequest courseRequest, String token);
    Response updateCourse(String courseId, CourseRequest courseRequest, String token);
    Response updateCourse(String courseId, CourseUpdateRequest courseUpdateRequest, String token);
    Response deleteCourse(String courseId, String token);
}