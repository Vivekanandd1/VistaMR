package com.vista.framework.utils;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.model.TestResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

/**
 * Enhanced Allure Report Helper for capturing detailed test information.
 * Ensures Allure reports are populated correctly even on test failures.
 */
public class AllureReportHelper {

    private static final Logger logger = LogManager.getLogger(AllureReportHelper.class);
    private static final AllureLifecycle lifecycle = Allure.getLifecycle();

    /**
     * Attach a screenshot file to Allure report
     */
    public static void attachScreenshot(String screenshotPath, String name) {
        try {
            if (screenshotPath == null || !Files.exists(Paths.get(screenshotPath))) {
                logger.warn("Screenshot file not found: {}", screenshotPath);
                return;
            }

            byte[] screenshotBytes = Files.readAllBytes(Paths.get(screenshotPath));
            lifecycle.addAttachment(name, "image/png", "png", new ByteArrayInputStream(screenshotBytes));
            logger.debug("Screenshot attached to Allure: {}", name);
        } catch (Exception e) {
            logger.error("Failed to attach screenshot to Allure", e);
        }
    }

    /**
     * Attach raw bytes to Allure report
     */
    public static void attachBytes(String name, String type, byte[] bytes) {
        try {
            lifecycle.addAttachment(name, type, null, new ByteArrayInputStream(bytes));
            logger.debug("Bytes attached to Allure: {}", name);
        } catch (Exception e) {
            logger.error("Failed to attach bytes to Allure", e);
        }
    }

    /**
     * Attach a text message to Allure report
     */
    public static void attachText(String name, String text) {
        try {
            lifecycle.addAttachment(name, "text/plain", "txt",
                    new ByteArrayInputStream(text.getBytes()));
            logger.debug("Text attached to Allure: {}", name);
        } catch (Exception e) {
            logger.error("Failed to attach text to Allure", e);
        }
    }

    /**
     * Attach an HTML snippet to Allure report
     */
    public static void attachHtml(String name, String html) {
        try {
            lifecycle.addAttachment(name, "text/html", "html",
                    new ByteArrayInputStream(html.getBytes()));
            logger.debug("HTML attached to Allure: {}", name);
        } catch (Exception e) {
            logger.error("Failed to attach HTML to Allure", e);
        }
    }

    /**
     * Attach error details and stack trace to Allure report
     */
    public static void attachError(Throwable throwable) {
        if (throwable == null) {
            return;
        }

        try {
            // Attach error message
            attachText("Error Message", throwable.getMessage());

            // Attach full stack trace
            StringBuilder stackTrace = new StringBuilder();
            stackTrace.append(throwable.toString()).append("\n\n");
            for (StackTraceElement element : throwable.getStackTrace()) {
                stackTrace.append("\tat ").append(element.toString()).append("\n");
            }

            // Include cause if present
            if (throwable.getCause() != null) {
                stackTrace.append("\nCaused by: ").append(throwable.getCause().toString()).append("\n");
                for (StackTraceElement element : throwable.getCause().getStackTrace()) {
                    stackTrace.append("\tat ").append(element.toString()).append("\n");
                }
            }

            attachText("Stack Trace", stackTrace.toString());
            logger.debug("Error details attached to Allure");
        } catch (Exception e) {
            logger.error("Failed to attach error details to Allure", e);
        }
    }

    /**
     * Start a custom step in Allure report
     */
    public static String startStep(String stepName) {
        String uuid = UUID.randomUUID().toString();
        lifecycle.startStep(uuid, new StepResult().setName(stepName).setStatus(Status.PASSED));
        return uuid;
    }

    /**
     * Stop a custom step with success status
     */
    public static void stopStep(String uuid) {
        lifecycle.stopStep(uuid);
    }

    /**
     * Stop a custom step with failure status
     */
    public static void stopStep(String uuid, Status status, String message) {
        lifecycle.updateStep(uuid, stepResult -> stepResult.setStatus(status).setStatusDetails(
                new io.qameta.allure.model.StatusDetails().setMessage(message)
        ));
        lifecycle.stopStep(uuid);
    }

    /**
     * Update current test result with additional information
     */
    public static void updateTestResult(String name, Status status, String message) {
        lifecycle.updateTestCase(testResult -> {
            testResult.setName(name);
            testResult.setStatus(status);
            if (message != null) {
                testResult.setStatusDetails(
                        new io.qameta.allure.model.StatusDetails()
                                .setMessage(message)
                                .setTrace(message)
                );
            }
        });
    }

    /**
     * Attach environment information (browser, OS, etc.)
     */
    public static void attachEnvironmentInfo(String envName, String envValue) {
        attachText("Environment: " + envName, envValue);
    }

    /**
     * Attach test metadata (URL, user, timestamp, etc.)
     */
    public static void attachTestMetadata(String key, String value) {
        attachText(key, value);
    }

    /**
     * Attach page source HTML for debugging failed tests
     */
    public static void attachPageSource(String pageSource) {
        if (pageSource != null && !pageSource.isEmpty()) {
            attachHtml("Page Source", pageSource);
        }
    }

    /**
     * Attach multiple screenshots from a list of file paths
     */
    public static void attachScreenshots(List<String> screenshotPaths, String baseName) {
        if (screenshotPaths == null || screenshotPaths.isEmpty()) {
            return;
        }

        for (int i = 0; i < screenshotPaths.size(); i++) {
            String path = screenshotPaths.get(i);
            String name = baseName + (screenshotPaths.size() > 1 ? " #" + (i + 1) : "");
            attachScreenshot(path, name);
        }
    }

    /**
     * Create a step that wraps an action with automatic pass/fail handling
     */
    public static void runStep(String stepName, Runnable action) {
        String stepUuid = startStep(stepName);
        try {
            action.run();
            stopStep(stepUuid);
        } catch (Exception e) {
            stopStep(stepUuid, Status.BROKEN, e.getMessage());
            throw e;
        }
    }

    /**
     * Create a step that wraps an action returning a value
     */
    public static <T> T runStep(String stepName, java.util.function.Supplier<T> action) {
        String stepUuid = startStep(stepName);
        try {
            T result = action.get();
            stopStep(stepUuid);
            return result;
        } catch (Exception e) {
            stopStep(stepUuid, Status.BROKEN, e.getMessage());
            throw e;
        }
    }

    /**
     * Log a message as an Allure attachment (useful for debugging)
     */
    public static void logMessage(String title, String message) {
        attachText(title, message);
    }
}
