package com.courseenrollment.pages.interfaces;

import io.restassured.response.Response;

public interface ICourseQueries {
    Response getAllCourses();
    Response getCoursesByTitle(String title);
    Response getCoursesByInstructor(String instructor);
    Response getCourseAvailability(String courseCode);
}