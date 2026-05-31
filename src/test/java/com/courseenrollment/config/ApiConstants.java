
package com.courseenrollment.config;

public class ApiConstants {
    
    public static final class Endpoints {
        public static final String LOGIN = "/login";
        public static final String COURSES = "/courses";
        public static final String STATUS = "/status";
        
        // Course endpoints
        public static final String COURSES_ALL = "/courses/all";
        public static final String COURSE_BY_ID = "/courses/{id}";
        public static final String COURSES_BY_TITLE = "/courses/title/{title}";
        public static final String COURSES_BY_INSTRUCTOR = "/courses/instructor/{instructor}";
        public static final String COURSES_AVAILABILITY = "/courses/availability/{courseCode}";
        
        // Enrolment endpoints
        public static final String ENROLMENTS_ENROL = "/enrolments/enrol";
        public static final String ENROLMENTS_DROP = "/enrolments/drop";
        public static final String ENROLMENTS_HISTORY = "/enrolments/history";
        public static final String ENROLMENTS_ACTIVE = "/enrolments/active";
        
        // Path parameters
        public static final String ID_PATH = "/{id}";
        
        private Endpoints() {
            // Hide implicit public constructor
        }
    }
    
    public static final class Headers {
        public static final String AUTHORIZATION = "Authorization";
        public static final String BEARER = "Bearer ";
        public static final String CONTENT_TYPE = "Content-Type";
        public static final String APPLICATION_JSON = "application/json";
        
        private Headers() {
            // Hide implicit public constructor
        }
    }
    
    public static final class JsonFields {
        public static final String TOKEN = "token";
        public static final String ID = "id";
        public static final String MESSAGE = "message";
        public static final String ERROR = "error";
        
        private JsonFields() {
            // Hide implicit public constructor
        }
    }
    
    public static final class StatusCodes {
        public static final int OK = 200;
        public static final int CREATED = 201;
        public static final int NO_CONTENT = 204;
        public static final int BAD_REQUEST = 400;
        public static final int UNAUTHORIZED = 401;
        public static final int FORBIDDEN = 403;
        public static final int NOT_FOUND = 404;
        public static final int UNPROCESSABLE_ENTITY = 422;
        public static final int UNSUPPORTED_MEDIA_TYPE = 415;
        public static final int INTERNAL_SERVER_ERROR = 500;
        
        private StatusCodes() {
            // Hide implicit public constructor
        }
    }
    
    public static final class PathParams {
        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String INSTRUCTOR = "instructor";
        public static final String COURSE_CODE = "courseCode";
        
        private PathParams() {
            // Hide implicit public constructor
        }
    }
    
    private ApiConstants() {
        // Hide implicit public constructor
    }
}