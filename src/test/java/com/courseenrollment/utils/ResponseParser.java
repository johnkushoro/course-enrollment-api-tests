package com.courseenrollment.utils;

import com.courseenrollment.config.ApiConstants;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseParser {
    
    private static final Logger logger = LoggerFactory.getLogger(ResponseParser.class);
    
    private ResponseParser() {
    }
    
    public static String extractToken(Response response) {
        if (response.getStatusCode() != ApiConstants.StatusCodes.OK) {
            return null;
        }
        
        try {
            String token = response.jsonPath().getString("token");
            if (token != null && !token.trim().isEmpty()) {
                return token.trim();
            }
        } catch (Exception e) {
            logger.error("Error extracting token: {}", e.getMessage());
        }
        
        return null;
    }
    
    public static String extractCourseId(Response response) {
        if (response.getStatusCode() != ApiConstants.StatusCodes.CREATED) {
            return null;
        }
        
        try {
            String courseId = response.jsonPath().getString("id");
            if (courseId != null && !courseId.trim().isEmpty()) {
                return courseId.trim();
            }
        } catch (Exception e) {
            logger.error("Error extracting course ID: {}", e.getMessage());
        }
        
        return null;
    }
    
    public static String extractCourseCode(Response response) {
        try {
            String courseCode = response.jsonPath().getString("courseCode");
            if (courseCode != null && !courseCode.trim().isEmpty()) {
                return courseCode.trim();
            }
        } catch (Exception e) {
            logger.error("Error extracting course code: {}", e.getMessage());
        }
        
        return null;
    }
    
    public static Integer extractAvailableSlots(Response response) {
        try {
            return response.jsonPath().getInt("availableSlots");
        } catch (Exception e) {
            logger.error("Error extracting available slots: {}", e.getMessage());
        }
        
        return null;
    }
    
//    public static String findCourseIdBySearch(CourseClient courseClient, String title) {
//        try {
//            Response searchResponse = courseClient.getCoursesByTitle(title);
//            if (searchResponse.getStatusCode() == ApiConstants.StatusCodes.OK) {
//                return searchResponse.jsonPath().getString("courses[0].id");
//            }
//        } catch (Exception e) {
//            logger.error("Error finding course ID by search: {}", e.getMessage());
//        }
//
//        return null;
//    }
    
    public static boolean isSuccessfulResponse(Response response, int... expectedStatusCodes) {
        int actualStatus = response.getStatusCode();
        
        for (int expectedStatus : expectedStatusCodes) {
            if (actualStatus == expectedStatus) {
                return true;
            }
        }
        
        return false;
    }
    
    public static String extractErrorMessage(Response response) {
        try {
            String message = response.jsonPath().getString("message");
            if (message != null && !message.trim().isEmpty()) {
                return message.trim();
            }
        } catch (Exception e) {
            logger.error("Error extracting error message: {}", e.getMessage());
        }
        
        return "Unknown error occurred";
    }
}