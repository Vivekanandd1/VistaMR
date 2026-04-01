package com.vista.framework.utils;

import com.vista.framework.config.ConfigKeys;
import com.vista.framework.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Screenshot utility for capturing test execution screenshots.
 * Supports automatic screenshots on test failure.
 */
public class ScreenshotUtil {
    
    private static final Logger logger = LogManager.getLogger(ScreenshotUtil.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    private final WebDriver driver;
    private final String screenshotDir;
    
    public ScreenshotUtil(WebDriver driver) {
        this.driver = driver;
        this.screenshotDir = config.get(ConfigKeys.SCREENSHOT_DIR, "screenshots");
        ensureScreenshotDirectoryExists();
    }
    
    /**
     * Capture screenshot with custom name
     */
    public String captureScreenshot(String screenshotName) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
            String fileName = screenshotName + "_" + timestamp + ".png";
            Path filePath = Paths.get(screenshotDir, fileName);
            
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(srcFile.toPath(), filePath);
            
            logger.info("Screenshot captured: {}", filePath.toAbsolutePath());
            return filePath.toAbsolutePath().toString();
        } catch (IOException e) {
            logger.error("Failed to capture screenshot: {}", screenshotName, e);
            return null;
        }
    }
    
    /**
     * Capture screenshot for failed test
     */
    public String captureFailedTestScreenshot(String testName, String className) {
        String screenshotName = "FAILED_" + className + "_" + testName;
        return captureScreenshot(screenshotName);
    }
    
    /**
     * Capture screenshot for passed test
     */
    public String capturePassedTestScreenshot(String testName, String className) {
        String screenshotName = "PASSED_" + className + "_" + testName;
        return captureScreenshot(screenshotName);
    }
    
    /**
     * Capture full page screenshot (using JavaScript)
     */
    public String captureFullPageScreenshot(String screenshotName) {
        try {
            // Scroll to bottom to ensure all content is loaded
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            js.executeScript("window.scrollTo(0, 0)");
            
            return captureScreenshot(screenshotName + "_fullpage");
        } catch (Exception e) {
            logger.error("Failed to capture full page screenshot", e);
            return captureScreenshot(screenshotName);
        }
    }
    
    /**
     * Capture screenshot of specific element
     */
    public String captureElementScreenshot(org.openqa.selenium.By locator, String screenshotName) {
        try {
            org.openqa.selenium.WebElement element = driver.findElement(locator);
            File srcFile = element.getScreenshotAs(OutputType.FILE);
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
            String fileName = screenshotName + "_" + timestamp + ".png";
            Path filePath = Paths.get(screenshotDir, "elements", fileName);
            
            Files.createDirectories(filePath.getParent());
            Files.copy(srcFile.toPath(), filePath);
            
            logger.info("Element screenshot captured: {}", filePath.toAbsolutePath());
            return filePath.toAbsolutePath().toString();
        } catch (IOException e) {
            logger.error("Failed to capture element screenshot: {}", screenshotName, e);
            return null;
        }
    }
    
    private void ensureScreenshotDirectoryExists() {
        try {
            Path dirPath = Paths.get(screenshotDir);
            Path elementsDirPath = Paths.get(screenshotDir, "elements");
            
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
                logger.debug("Created screenshot directory: {}", dirPath.toAbsolutePath());
            }
            
            if (!Files.exists(elementsDirPath)) {
                Files.createDirectories(elementsDirPath);
                logger.debug("Created elements screenshot directory: {}", elementsDirPath.toAbsolutePath());
            }
        } catch (IOException e) {
            logger.error("Failed to create screenshot directories", e);
        }
    }
    
    /**
     * Get the screenshot directory path
     */
    public String getScreenshotDir() {
        return screenshotDir;
    }
    
    /**
     * Check if screenshot on failure is enabled
     */
    public boolean isScreenshotOnFailureEnabled() {
        return config.getBoolean(ConfigKeys.SCREENSHOT_ON_FAILURE, true);
    }
}
