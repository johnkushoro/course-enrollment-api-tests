package com.courseenrollment.config;

public class ApiConfig {
    
    public static final String BASE_URL = getEnvOrDefault("BASE_URL", "https://courseenrollmentapimanagementsystem.onrender.com");
    
    public static final String APPLICATION_ID = getEnvOrDefault("APPLICATION_ID", "16978214");
    public static final String CANDIDATE_ID = getEnvOrDefault("CANDIDATE_ID", "16978214");
    
    public static final String INSTRUCTOR_USERNAME = getEnvOrDefault("INSTRUCTOR_USERNAME", generateInstructorUsername());
    public static final String INSTRUCTOR_PASSWORD = getEnvOrDefault("INSTRUCTOR_PASSWORD", "secPass341");
    public static final String STUDENT_USERNAME = getEnvOrDefault("STUDENT_USERNAME", generateStudentUsername());
    public static final String STUDENT_PASSWORD = getEnvOrDefault("STUDENT_PASSWORD", "secPass341");
    
    // Endpoint constants are now centralized in ApiConstants.Endpoints
    
    public static final int DEFAULT_TIMEOUT_MS = Integer.parseInt(getEnvOrDefault("DEFAULT_TIMEOUT_MS", "30000"));
    
    private static String getEnvOrDefault(String envVar, String defaultValue) {
        String value = System.getenv(envVar);
        return (value != null && !value.trim().isEmpty()) ? value : defaultValue;
    }
    
    private static String generateInstructorUsername() {
        return "instructor_" + CANDIDATE_ID + CANDIDATE_ID;
    }
    
    private static String generateStudentUsername() {
        return "student_" + CANDIDATE_ID + CANDIDATE_ID;
    }
    
    private ApiConfig() {
    }
}