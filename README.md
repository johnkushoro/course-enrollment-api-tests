# Course Enrollment API Test Suite

A **very simple, clean, and demo-friendly** automated test suite for the Course Enrollment Management System API, built with Java, Maven, JUnit 5, and REST Assured.

## Why This Framework is Special

- **Very Easy to Understand** - Clear naming, logical structure, minimal complexity  
- **Demo-Friendly** - Each operation is self-contained and easy to explain  
- **CRUD Compliant** - Perfect Create, Read, Update, Delete patterns  
- **No Hardcoding** - Everything configurable via environment variables  
- **Type Safe** - Strong typing with proper DTOs  
- **Production Ready** - Follows all best practices for API test automation  

## Quick Start (Very Simple!)

### 1. Clone & Setup
```bash
git clone <repository-url>
cd course-enrollment-api-tests
```

### 2. Configure Environment (No Hardcoded Values!)
Create a `.env` file:
```bash
# API Configuration
BASE_URL=https://courseenrollmentapimanagementsystem.onrender.com
CANDIDATE_ID=your_candidate_id

# Credentials (auto-generated from CANDIDATE_ID)
INSTRUCTOR_USERNAME=instructor_your_candidate_id_your_candidate_id
INSTRUCTOR_PASSWORD=secPass341
STUDENT_USERNAME=student_your_candidate_id_your_candidate_id
STUDENT_PASSWORD=secPass341

# Timeouts (configurable)
DEFAULT_TIMEOUT_MS=30000
```

### 3. Run Tests
```bash
mvn clean test
```

## Very Clean Project Structure

```
src/test/java/com/courseenrollment/
├── config/
│   ├── ApiConfig.java                    # Environment configuration
│   └── ApiConstants.java                 # Centralized API constants
├── models/                               # Simple data objects
│   ├── LoginRequest.java                 # Username + Password
│   ├── CourseRequest.java                # Complete course data
│   ├── CourseUpdateRequest.java          # Partial course updates
│   └── EnrolmentRequest.java             # CourseCode + Username
├── clients/                              # API operations (CRUD)
│   ├── AuthenticationClient.java         # Login operations
│   ├── CourseClient.java                 # Course CRUD operations
│   └── EnrolmentClient.java              # Enrollment operations
├── utils/                                # Helper utilities
│   ├── ResponseParser.java               # Simple response handling
│   ├── TestDataGenerator.java            # Generate test data
│   ├── WaitUtils.java                    # Wait strategies
│   ├── ExtentManager.java                # Test reporting
│   └── ExtentTestWatcher.java            # Test result tracking
├── base/
│   └── BaseTest.java                     # Foundation for all tests
└── tests/                                # All test classes
    ├── HealthCheckTest.java              # API health verification
    ├── AuthenticationTest.java           # Login functionality
    ├── CourseManagementTest.java         # Course CRUD operations
    ├── EndToEndJourneyTest.java          # Complete user workflows
    └── ...                               # Other test classes
```

## Demo-Friendly Examples

### 1. Authentication (Very Simple!)
```java
// Get tokens - very easy!
String instructorToken = getInstructorToken();
String studentToken = getStudentToken();

// Login manually
AuthenticationClient authClient = createAuthClient();
Response response = authClient.loginInstructor();
String token = authClient.extractToken(response);
```

### 2. Course Management (Perfect CRUD!)
```java
CourseClient courseClient = new CourseClient(getBaseSpec());

// CREATE
CourseRequest course = TestDataGenerator.createCourseRequest();
Response response = courseClient.createCourse(course, instructorToken);

// READ
Response allCourses = courseClient.getAllCourses();
Response coursesByTitle = courseClient.getCoursesByTitle("Java Programming");
Response availability = courseClient.getCourseAvailability("JAVA101");

// UPDATE
CourseUpdateRequest update = new CourseUpdateRequest("New Title", 30, 25, "2024-12-31");
courseClient.updateCourse(courseId, update, instructorToken);

// DELETE
courseClient.deleteCourse(courseId, instructorToken);
```

### 3. Enrollment Operations
```java
EnrolmentClient enrolmentClient = new EnrolmentClient(getBaseSpec());

// Enroll student
EnrolmentRequest enrollment = new EnrolmentRequest("JAVA101", "student_123");
enrolmentClient.enrolInCourse(enrollment, studentToken);

// Get enrollment history
enrolmentClient.getEnrolmentHistory("student_123", studentToken);

// Drop from course
enrolmentClient.dropFromCourse(enrollment, studentToken);
```

## Configuration (No Hardcoded Values!)

Configuration is centralized in two files:

### [`ApiConfig.java`](src/test/java/com/courseenrollment/config/ApiConfig.java) - Environment Configuration
```java
// Environment variables with sensible defaults
public static final String BASE_URL = getEnvOrDefault("BASE_URL", "https://...");
public static final String INSTRUCTOR_USERNAME = getEnvOrDefault("INSTRUCTOR_USERNAME", generateInstructorUsername());
public static final int DEFAULT_TIMEOUT_MS = Integer.parseInt(getEnvOrDefault("DEFAULT_TIMEOUT_MS", "30000"));
```

### [`ApiConstants.java`](src/test/java/com/courseenrollment/config/ApiConstants.java) - API Constants
```java
// Centralized endpoints, headers, status codes, and field names
public static final class Endpoints {
    public static final String INSTRUCTOR_LOGIN = "/instructor/login";
    public static final String COURSES = "/courses";
    public static final String ENROLMENTS_ENROL = "/enrolments/enrol";
    // ... all API endpoints
}

public static final class StatusCodes {
    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int UNAUTHORIZED = 401;
    // ... all status codes
}
```

### Environment Variables
| Variable | Description | Example |
|----------|-------------|---------|
| `BASE_URL` | API base URL | `https://api.example.com` |
| `CANDIDATE_ID` | Your candidate ID | `12345678` |
| `INSTRUCTOR_USERNAME` | Instructor username | `instructor_12345678_12345678` |
| `STUDENT_USERNAME` | Student username | `student_12345678_12345678` |
| `DEFAULT_TIMEOUT_MS` | Request timeout | `30000` |

## Test Categories (Very Easy to Understand!)

### 1. Health Check
```java
@Test
void testHealthCheckEndpoint() {
    Response response = given(getBaseSpec()).get(ApiConstants.Endpoints.STATUS);
    assertEquals(ApiConstants.StatusCodes.OK, response.getStatusCode());
}
```

### 2. Authentication Tests
- Instructor login success
- Student login success  
- Token generation and validation
- Invalid credentials handling

### 3. Course Management (Full CRUD)
- **Create** courses with valid data
- **Read** all courses, search by title/instructor
- **Update** course details
- **Delete** courses
- Error scenarios (invalid data, unauthorized access)

### 4. Enrollment Operations
- Student enrollment in courses
- View enrollment history
- Drop from courses
- Invalid enrollment scenarios

### 5. End-to-End Journeys
- Complete user workflows
- Multi-step business processes
- Data consistency validation

## Reporting (Auto-Generated!)

### HTML Reports
- **Location**: `test-reports/TestReport_YYYY-MM-DD_HH-MM-SS.html`
- **Auto-opens** in browser after test execution
- **Rich details**: Test results, logs, execution timeline

### Console Logging
```bash
INFO  - Health check test completed successfully
INFO  - Course created successfully: JAVA101
INFO  - Student enrolled in course: JAVA101
```

## Running Tests (Multiple Ways!)

```bash
# Run all tests
mvn clean test

# Run specific test class
mvn test -Dtest=AuthenticationTest
mvn test -Dtest=CourseManagementTest

# Run single test method
mvn test -Dtest=AuthenticationTest#testInstructorLogin

# Run with different environment
BASE_URL=https://staging-api.com mvn test
```

## Architecture (Clean & Simple!)

### Design Principles
- **Single Responsibility** - Each class has one clear purpose
- **No Hardcoding** - Everything configurable
- **Type Safety** - Strong typing with DTOs
- **Easy to Demo** - Clear method names and simple flows

### Key Components

#### 1. Configuration Layer
- [`ApiConfig.java`](src/test/java/com/courseenrollment/config/ApiConfig.java) - Environment configuration and credentials
- [`ApiConstants.java`](src/test/java/com/courseenrollment/config/ApiConstants.java) - Centralized API constants (endpoints, status codes, headers)

#### 2. Model Layer
Simple, clean DTOs:
- [`LoginRequest`](src/test/java/com/courseenrollment/models/LoginRequest.java) - Login credentials
- [`CourseRequest`](src/test/java/com/courseenrollment/models/CourseRequest.java) - Course data
- [`EnrolmentRequest`](src/test/java/com/courseenrollment/models/EnrolmentRequest.java) - Enrollment data

#### 3. Client Layer (Perfect CRUD)
- [`AuthenticationClient`](src/test/java/com/courseenrollment/clients/AuthenticationClient.java) - Login operations
- [`CourseClient`](src/test/java/com/courseenrollment/clients/CourseClient.java) - Course CRUD
- [`EnrolmentClient`](src/test/java/com/courseenrollment/clients/EnrolmentClient.java) - Enrollment operations

#### 4. Utility Layer
- [`ResponseParser`](src/test/java/com/courseenrollment/utils/ResponseParser.java) - Simple response handling (no defensive coding!)
- [`TestDataGenerator`](src/test/java/com/courseenrollment/utils/TestDataGenerator.java) - Generate unique test data
- [`WaitUtils`](src/test/java/com/courseenrollment/utils/WaitUtils.java) - Configurable wait strategies

#### 5. Base Layer
[`BaseTest`](src/test/java/com/courseenrollment/base/BaseTest.java) - Foundation providing:
```java
// Authentication helpers
getInstructorToken()  // Get valid instructor token
getStudentToken()     // Get valid student token

// Request specifications  
getBaseSpec()                    // Basic request spec
getAuthenticatedSpec(token)      // Authenticated request spec

// Utility helpers
createAuthClient()               // Create authentication client
waitForServiceReadiness()        // Wait for API to be ready
```

## What Makes This Framework Special

### 1. No Hardcoded Values
- **Before**: Hardcoded URLs, endpoints, status codes, credentials scattered everywhere
- **After**: Everything centralized in [`ApiConfig`](src/test/java/com/courseenrollment/config/ApiConfig.java) and [`ApiConstants`](src/test/java/com/courseenrollment/config/ApiConstants.java)

### 2. Simple Response Parsing
- **Before**: Defensive multiple path guessing `["token", "accessToken", "data.token"]`
- **After**: Clear single contracts `"token"` field only

### 3. Perfect CRUD Operations
- **Create**: `courseClient.createCourse()`
- **Read**: `courseClient.getAllCourses()`, `courseClient.getCoursesByTitle()`
- **Update**: `courseClient.updateCourse()`
- **Delete**: `courseClient.deleteCourse()`

### 4. Type-Safe Operations
- **Before**: Generic `Object` parameters
- **After**: Strongly typed `CourseRequest`, `CourseUpdateRequest`, `EnrolmentRequest`

### 5. Demo-Friendly
Every operation is self-contained and easy to explain:
```java
// Very clear what this does!
String token = getInstructorToken();
CourseRequest course = TestDataGenerator.createCourseRequest();
Response response = courseClient.createCourse(course, token);
String courseId = courseClient.extractCourseId(response);
```

## Troubleshooting

### Common Issues
1. **Tests failing with timeout** - Increase `DEFAULT_TIMEOUT_MS` in `.env`
2. **Authentication failures** - Verify `CANDIDATE_ID` and credentials in `.env`
3. **API not responding** - Check `BASE_URL` and network connectivity

### Debug Mode
Set log level to DEBUG in [`logback-test.xml`](src/test/resources/logback-test.xml) for detailed logging.

## Performance & Quality

- **Parallel Execution**: Thread-safe design for faster test runs
- **Configurable Timeouts**: No hardcoded wait times
- **Comprehensive Cleanup**: Try/finally blocks ensure no test data pollution
- **Resource Efficient**: Optimized for CI/CD environments
- **Zero Code Duplication**: Reusable methods and centralized constants
- **Robust Error Handling**: Null-safe operations and diagnostic logging
- **CI/CD Ready**: Environment-aware reporting (no browser opening in CI)

## Quality Assurance Features

### Enterprise-Grade Improvements
- **Null-Safe ExtentTestWatcher**: Prevents cascading failures during test setup issues
- **Automatic ThreadLocal Cleanup**: `ExtentManager.removeTest()` after each test
- **Try/Finally Resource Cleanup**: All course creation tests have guaranteed cleanup
- **Strong Assertions**: `assertFalse(response.asString().trim().isEmpty())` instead of weak `assertNotNull(response.getBody())`
- **Response Content Validation**: Validates API returns expected resource data (title, courseCode, instructor)
- **Enrollment Verification**: Validates enrollment success before subsequent actions
- **Centralized Test Constants**: No hardcoded values like "JAVA101" or "invalid_token_123"
- **Cleanup Diagnostics**: Failed cleanup operations are logged for troubleshooting

### Reliability Features
- **Comprehensive Try/Finally Blocks**: All tests that create courses have proper cleanup
- **Reusable Cleanup Methods**: `cleanupCourse(String courseId)` eliminates code duplication
- **CI Environment Detection**: Automatic detection of CI environments to prevent browser opening
- **Authentication Test Reliability**: Uses valid request bodies for proper auth testing

---

**Framework Version**: 3.0.0 (Enterprise-Grade Quality - Zero Technical Debt!)
**Last Updated**: May 2026
**Java Version**: 17+
**Maven Version**: 3.9+

**Production-ready with enterprise-grade quality, comprehensive error handling, and zero code duplication!**