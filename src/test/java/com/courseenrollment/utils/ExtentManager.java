package com.courseenrollment.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Desktop;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExtentManager {
    
    private static final Logger logger = LoggerFactory.getLogger(ExtentManager.class);
    
    // Private constructor to hide implicit public constructor
    private ExtentManager() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    private static ExtentReports extent;
    private static String reportPath;
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    
    private static final String REPORTS_DIR = System.getProperty("reports.dir", "test-reports");
    private static final String ENVIRONMENT = System.getProperty("test.environment", "Test");
    
    public static void initReports() {
        if (extent == null) {
            File reportsDir = new File(REPORTS_DIR);
            if (!reportsDir.exists()) {
                boolean created = reportsDir.mkdirs();
                if (!created) {
                    logger.warn("Could not create {} directory", REPORTS_DIR);
                }
            }
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            reportPath = REPORTS_DIR + "/TestReport_" + timestamp + ".html";
            
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setDocumentTitle("Course Enrollment API Test Report");
            sparkReporter.config().setReportName("API Test Results");
            
            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Environment", ENVIRONMENT);
            extent.setSystemInfo("User", System.getProperty("user.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("OS", System.getProperty("os.name"));
        }
    }
    
    public static void createTest(String testName, String description) {
        ExtentTest extentTest = extent.createTest(testName, description);
        test.set(extentTest);
    }
    
    public static ExtentTest getTest() {
        return test.get();
    }
    
    public static void removeTest() {
        test.remove();
    }
    
    public static void flushReports() {
        if (extent != null) {
            extent.flush();
            
            // Check if running in CI environment - avoid opening browser automatically
            boolean isCI = System.getenv("CI") != null ||
                          System.getenv("JENKINS_URL") != null ||
                          System.getenv("GITHUB_ACTIONS") != null ||
                          System.getenv("GITLAB_CI") != null ||
                          System.getenv("TEAMCITY_VERSION") != null ||
                          System.getProperty("maven.surefire.debug") != null;
            
            try {
                File reportFile = new File(reportPath);
                if (reportFile.exists() && Desktop.isDesktopSupported() && !isCI) {
                    Desktop.getDesktop().browse(reportFile.toURI());
                    logger.info("Test report opened in browser: {}", reportPath);
                    return;
                }
            } catch (Exception e) {
                // Fall through to generate message
            }
            
            // Single point for report generation message
            logger.info("Test report generated: {}", reportPath);
            if (isCI) {
                logger.info("Running in CI environment - report not opened automatically");
            } else {
                logger.info("Please open the file manually in your browser");
            }
        }
    }
}