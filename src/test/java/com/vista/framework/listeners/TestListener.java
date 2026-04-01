package com.vista.framework.listeners;

import com.vista.framework.driver.WebDriverFactory;
import com.vista.framework.utils.ScreenshotUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.*;

import java.util.Arrays;

/**
 * Test Listener for handling test execution events.
 * Captures screenshots on failure and logs test execution details.
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
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("✅ TEST PASSED: {}.{}", 
                   result.getTestClass().getName(), 
                   result.getMethod().getMethodName());
        logger.info("Execution Time: {} ms", result.getEndMillis() - result.getStartMillis());
        
        // Capture screenshot on success (optional)
        captureScreenshot(result, "PASSED");
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("❌ TEST FAILED: {}.{}", 
                    result.getTestClass().getName(), 
                    result.getMethod().getMethodName());
        logger.error("Failure Reason: {}", result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown");
        logger.error("Execution Time: {} ms", result.getEndMillis() - result.getStartMillis());
        
        // Capture screenshot on failure
        captureScreenshot(result, "FAILED");
        
        // Log full stack trace
        if (result.getThrowable() != null) {
            logger.error("Stack trace:", result.getThrowable());
        }
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("⏭️ TEST SKIPPED: {}.{}", 
                   result.getTestClass().getName(), 
                   result.getMethod().getMethodName());
        
        if (result.getThrowable() != null) {
            logger.warn("Skip reason: {}", result.getThrowable().getMessage());
        }
    }
    
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.warn("⚠️ TEST FAILED BUT WITHIN SUCCESS PERCENTAGE: {}.{}", 
                   result.getTestClass().getName(), 
                   result.getMethod().getMethodName());
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
    
    private void captureScreenshot(ITestResult result, String status) {
        try {
            if (WebDriverFactory.getInstance().isDriverInitialized()) {
                ScreenshotUtil screenshotUtil = new ScreenshotUtil(
                    WebDriverFactory.getInstance().getDriver());
                
                String screenshotPath = screenshotUtil.captureScreenshot(
                    status + "_" + result.getTestClass().getName() + "_" + 
                    result.getMethod().getMethodName());
                
                if (screenshotPath != null) {
                    logger.info("Screenshot captured: {}", screenshotPath);
                    
                    // Attach screenshot to Allure report
                    attachScreenshotToAllure(screenshotPath);
                }
            } else {
                logger.debug("WebDriver not initialized, skipping screenshot");
            }
        } catch (Exception e) {
            logger.error("Failed to capture screenshot", e);
        }
    }
    
    private void attachScreenshotToAllure(String screenshotPath) {
        try {
            io.qameta.allure.Allure.addAttachment(
                "Screenshot",
                "image/png",
                java.nio.file.Files.newInputStream(java.nio.file.Paths.get(screenshotPath)),
                "png"
            );
        } catch (Exception e) {
            logger.error("Failed to attach screenshot to Allure report", e);
        }
    }
}
