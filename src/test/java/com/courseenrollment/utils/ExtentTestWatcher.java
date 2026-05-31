package com.courseenrollment.utils;

import com.aventstack.extentreports.Status;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

public class ExtentTestWatcher implements TestWatcher {

    @Override
    public void testSuccessful(ExtensionContext context) {
        if (ExtentManager.getTest() != null) {
            ExtentManager.getTest().log(Status.PASS, "Test passed successfully");
        }
        // Clean up ThreadLocal after test completion
        ExtentManager.removeTest();
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        if (ExtentManager.getTest() != null) {
            ExtentManager.getTest().log(Status.FAIL, "Test failed: " + cause.getMessage());
        }
        // Clean up ThreadLocal after test completion
        ExtentManager.removeTest();
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        if (ExtentManager.getTest() != null) {
            ExtentManager.getTest().log(Status.SKIP, "Test was aborted: " + cause.getMessage());
        }
        // Clean up ThreadLocal after test completion
        ExtentManager.removeTest();
    }

    @Override
    public void testDisabled(ExtensionContext context, java.util.Optional<String> reason) {
        if (ExtentManager.getTest() != null) {
            ExtentManager.getTest().log(Status.SKIP, "Test was disabled: " + reason.orElse("No reason provided"));
        }
        // Clean up ThreadLocal after test completion
        ExtentManager.removeTest();
    }
}