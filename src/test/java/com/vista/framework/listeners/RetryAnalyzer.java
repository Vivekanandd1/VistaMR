package com.vista.framework.listeners;

import com.vista.framework.config.ConfigKeys;
import com.vista.framework.config.ConfigManager;
import com.vista.framework.driver.WebDriverFactory;
import com.vista.framework.utils.ScreenshotUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retry Analyzer for failed tests.
 * Automatically retries failed tests based on configuration.
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    
    private static final Logger logger = LogManager.getLogger(RetryAnalyzer.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    private int retryCount = 0;
    private final int maxRetryCount;
    
    public RetryAnalyzer() {
        this.maxRetryCount = config.getInt(ConfigKeys.MAX_RETRY_COUNT, 2);
        logger.info("RetryAnalyzer initialized with max retry count: {}", maxRetryCount);
    }
    
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            logger.warn("Test '{}' failed. Retrying attempt {}/{}", 
                       result.getMethod().getMethodName(), retryCount, maxRetryCount);
            return true;
        }
        
        logger.error("Test '{}' failed after {} retry attempts", 
                    result.getMethod().getMethodName(), maxRetryCount);
        return false;
    }
    
    public int getRetryCount() {
        return retryCount;
    }
    
    public void reset() {
        retryCount = 0;
    }
}
