package com.vista.framework.listeners;

import com.vista.framework.driver.WebDriverFactory;
import com.vista.framework.utils.AllureReportHelper;
import com.vista.framework.utils.ScreenshotUtil;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.*;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Enhanced Test Listener for handling test execution events.
 * Captures screenshots on failure and logs test execution details to Allure.
 * Ensures Allure reports are populated correctly even on test failures.
 */
public class TestListener implements ITestListener {

    private static final Logger logger = LogManager.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("===============================================");
        logger.info("TEST STARTED: {}.{}",
                   result.getTestClass().getName(),
                   result.getMethod().getMethodName());
        logger.info("===============================================");

        // Attach test environment info to Allure
        try {
            AllureReportHelper.attachEnvironmentInfo("Test Class", result.getTestClass().getName());
            AllureReportHelper.attachEnvironmentInfo("Test Method", result.getMethod().getMethodName());
        } catch (Exception e) {
            logger.error("Failed to attach environment info", e);
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("✅ TEST PASSED: {}.{}",
                   result.getTestClass().getName(),
                   result.getMethod().getMethodName());
        logger.info("Execution Time: {} ms", result.getEndMillis() - result.getStartMillis());

        // Capture screenshot on success
        captureAndAttachScreenshot(result, "PASSED");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("❌ TEST FAILED: {}.{}",
                    result.getTestClass().getName(),
                    result.getMethod().getMethodName());
        logger.error("Failure Reason: {}", result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown");
        logger.error("Execution Time: {} ms", result.getEndMillis() - result.getStartMillis());

        // Capture screenshot on failure
        captureAndAttachScreenshot(result, "FAILED");

        // Attach error details to Allure (CRITICAL for non-blank reports)
        attachFailureDetails(result);

        // Attach page source on failure for debugging
        attachPageSource(result);

        // Update Allure test result with failure status
        updateAllureStatus(result, Status.FAILED);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("⏭️ TEST SKIPPED: {}.{}",
                   result.getTestClass().getName(),
                   result.getMethod().getMethodName());

        if (result.getThrowable() != null) {
            logger.warn("Skip reason: {}", result.getThrowable().getMessage());
            // Attach skip reason to Allure
            AllureReportHelper.attachText("Skip Reason", result.getThrowable().getMessage());
        }

        // Capture screenshot on skip (if driver is available)
        captureAndAttachScreenshot(result, "SKIPPED");

        // Update Allure test result with skipped status
        updateAllureStatus(result, Status.SKIPPED);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.warn("⚠️ TEST FAILED BUT WITHIN SUCCESS PERCENTAGE: {}.{}",
                   result.getTestClass().getName(),
                   result.getMethod().getMethodName());

        captureAndAttachScreenshot(result, "FLAKY");
        updateAllureStatus(result, Status.PASSED);
    }

    @Override
    public void onStart(ITestContext context) {
        logger.info("========================================================");
        logger.info("TEST EXECUTION STARTED");
        logger.info("Suite: {}", context.getSuite().getName());
        logger.info("Test: {}", context.getName());
        logger.info("Included Groups: {}", Arrays.toString(context.getIncludedGroups()));
        logger.info("Excluded Groups: {}", Arrays.toString(context.getExcludedGroups()));
        logger.info("========================================================");
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("========================================================");
        logger.info("TEST EXECUTION COMPLETED");
        logger.info("Suite: {}", context.getSuite().getName());
        logger.info("Test: {}", context.getName());
        logger.info("Passed Tests: {}", context.getPassedTests().size());
        logger.info("Failed Tests: {}", context.getFailedTests().size());
        logger.info("Skipped Tests: {}", context.getSkippedTests().size());
        logger.info("========================================================");

        // Clean up WebDriver
        WebDriverFactory.getInstance().quitDriver();
    }

    /**
     * Capture and attach screenshot to Allure report
     */
    private void captureAndAttachScreenshot(ITestResult result, String status) {
        try {
            WebDriver driver = WebDriverFactory.getInstance().getDriver();
            if (driver != null && WebDriverFactory.getInstance().isDriverInitialized()) {
                ScreenshotUtil screenshotUtil = new ScreenshotUtil(driver);

                String screenshotName = status + "_" + 
                                       result.getTestClass().getName() + "_" + 
                                       result.getMethod().getMethodName();

                String screenshotPath = screenshotUtil.captureScreenshot(screenshotName);

                if (screenshotPath != null && Files.exists(Paths.get(screenshotPath))) {
                    logger.info("Screenshot captured: {}", screenshotPath);

                    // Attach screenshot to Allure with proper error handling
                    byte[] screenshotBytes = Files.readAllBytes(Paths.get(screenshotPath));
                    Allure.addAttachment("Screenshot (" + status + ")", "image/png", 
                                       new ByteArrayInputStream(screenshotBytes), "png");
                    
                    logger.info("Screenshot attached to Allure: {}", status);
                } else {
                    logger.warn("Screenshot path is null or file doesn't exist");
                }
            } else {
                logger.debug("WebDriver not initialized, skipping screenshot");
            }
        } catch (Exception e) {
            logger.error("Failed to capture/attach screenshot", e);
        }
    }

    /**
     * Attach comprehensive failure details to Allure report
     */
    private void attachFailureDetails(ITestResult result) {
        try {
            Throwable throwable = result.getThrowable();
            
            // Attach error message
            if (throwable != null) {
                AllureReportHelper.attachError(throwable);
                
                // Attach exception class name
                AllureReportHelper.attachText("Exception Type", throwable.getClass().getName());
            } else {
                AllureReportHelper.attachText("Failure Reason", "Unknown (no exception)");
            }

            // Attach test metadata
            AllureReportHelper.attachText("Test Class", result.getTestClass().getName());
            AllureReportHelper.attachText("Test Method", result.getMethod().getMethodName());
            AllureReportHelper.attachText("Execution Time (ms)", 
                                         String.valueOf(result.getEndMillis() - result.getStartMillis()));

            // Attach TestNG result parameters
            if (result.getParameters() != null && result.getParameters().length > 0) {
                StringBuilder params = new StringBuilder();
                for (int i = 0; i < result.getParameters().length; i++) {
                    params.append("Param[").append(i).append("]: ")
                          .append(result.getParameters()[i]).append("\n");
                }
                AllureReportHelper.attachText("Test Parameters", params.toString());
            }

            logger.info("Failure details attached to Allure");
        } catch (Exception e) {
            logger.error("Failed to attach failure details", e);
        }
    }

    /**
     * Attach page source HTML for debugging failed tests
     */
    private void attachPageSource(ITestResult result) {
        try {
            WebDriver driver = WebDriverFactory.getInstance().getDriver();
            if (driver != null) {
                String pageSource = driver.getPageSource();
                if (pageSource != null && !pageSource.isEmpty()) {
                    AllureReportHelper.attachPageSource(pageSource);
                    logger.debug("Page source attached to Allure");
                }
            }
        } catch (Exception e) {
            logger.error("Failed to attach page source", e);
        }
    }

    /**
     * Update Allure test case status
     */
    private void updateAllureStatus(ITestResult result, Status status) {
        try {
            io.qameta.allure.Allure.getLifecycle().updateTestCase(testResult -> {
                testResult.setStatus(status);
                
                // Set status details with message
                if (result.getThrowable() != null) {
                    testResult.setStatusDetails(
                        new io.qameta.allure.model.StatusDetails()
                            .setMessage(result.getThrowable().getMessage())
                    );
                }
            });
        } catch (Exception e) {
            logger.error("Failed to update Allure status", e);
        }
    }
}
