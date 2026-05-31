package com.courseenrollment.utils;

import com.courseenrollment.models.CourseRequest;
import com.courseenrollment.models.EnrolmentRequest;
import com.courseenrollment.models.LoginRequest;
import com.courseenrollment.config.ApiConfig;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class TestDataGenerator {
    
    private static final Random random = new Random();
    private static final String DEFAULT_CATEGORY = "Test Category";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    
    private TestDataGenerator() {
    }
    
    public static String generateUniqueId() {
        return LocalDateTime.now().format(formatter) + "_" + random.nextInt(1000);
    }
    
    public static String generateCourseCode() {
        return "TC" + generateUniqueId();
    }
    
    public static LoginRequest createInstructorLoginRequest() {
        return new LoginRequest(ApiConfig.INSTRUCTOR_USERNAME, ApiConfig.INSTRUCTOR_PASSWORD);
    }
    
    public static LoginRequest createStudentLoginRequest() {
        return new LoginRequest(ApiConfig.STUDENT_USERNAME, ApiConfig.STUDENT_PASSWORD);
    }
    
    public static CourseRequest createCourseRequest() {
        String uniqueId = generateUniqueId();
        return new CourseRequest(
                "Test Course " + uniqueId,
                ApiConfig.INSTRUCTOR_USERNAME,
                "TC" + uniqueId,
                DEFAULT_CATEGORY,
                20
        );
    }
    
    public static CourseRequest createCourseRequest(String title) {
        String uniqueId = generateUniqueId();
        return new CourseRequest(
                title,
                ApiConfig.INSTRUCTOR_USERNAME,
                "TC" + uniqueId,
                DEFAULT_CATEGORY,
                20
        );
    }
    
    public static CourseRequest createCourseRequest(String title, String instructor, String courseCode, Integer totalCapacity) {
        return new CourseRequest(
                title,
                instructor,
                courseCode,
                DEFAULT_CATEGORY,
                totalCapacity
        );
    }
    
    public static EnrolmentRequest createEnrolmentRequest(String courseCode) {
        return new EnrolmentRequest(courseCode, ApiConfig.STUDENT_USERNAME);
    }
    
    public static EnrolmentRequest createEnrolmentRequest(String courseCode, String username) {
        return new EnrolmentRequest(courseCode, username);
    }
}