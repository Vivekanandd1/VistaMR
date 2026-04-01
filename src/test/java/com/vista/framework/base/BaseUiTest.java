package com.vista.framework.base;

import com.vista.framework.config.ConfigKeys;
import com.vista.framework.config.ConfigManager;
import com.vista.framework.driver.WebDriverFactory;
import com.vista.framework.utils.ElementUtils;
import com.vista.framework.wait.WaitStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import com.vista.framework.listeners.TestListener;
import com.vista.framework.listeners.AnnotationTransformer;
import io.qameta.allure.testng.AllureTestNg;

/**
 * Base Test class for UI tests.
 * Provides WebDriver lifecycle management and common utilities.
 */
@Listeners({AllureTestNg.class, TestListener.class, AnnotationTransformer.class})
public abstract class BaseUiTest {
    
    protected static final Logger logger = LogManager.getLogger(BaseUiTest.class);
    protected static final ConfigManager config = ConfigManager.getInstance();
    
    protected WebDriver driver;
    protected WaitStrategy wait;
    protected ElementUtils elementUtils;
    
    protected String baseUrl;
    
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        logger.info("Setting up test environment");
        
        // Initialize WebDriver
        WebDriverFactory factory = WebDriverFactory.getInstance();
        driver = factory.createDriver();
        
        // Initialize utilities
        wait = new WaitStrategy(driver);
        elementUtils = new ElementUtils(driver, wait);
        
        // Get base URL
        baseUrl = config.get(ConfigKeys.BASE_URL, 
                config.get(ConfigKeys.APP_URL, "https://vista.kreditz-dev.com"));
        
        logger.info("Test environment setup completed. Base URL: {}", baseUrl);
    }
    
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        logger.info("Tearing down test environment");
        
        if (driver != null) {
            try {
                driver.quit();
                logger.info("WebDriver quit successfully");
            } catch (Exception e) {
                logger.error("Error while quitting WebDriver", e);
            }
        }
        
        WebDriverFactory.getInstance().quitDriver();
        logger.info("Test environment teardown completed");
    }
    
    /**
     * Navigate to base URL
     */
    protected void navigateToHome() {
        logger.info("Navigating to home page: {}", baseUrl);
        driver.get(baseUrl);
        wait.waitForPageLoad();
    }
    
    /**
     * Navigate to specific URL
     */
    protected void navigateTo(String url) {
        logger.info("Navigating to: {}", url);
        driver.get(url);
        wait.waitForPageLoad();
    }
    
    /**
     * Navigate to specific path
     */
    protected void navigateToPath(String path) {
        String url = baseUrl + path;
        logger.info("Navigating to path: {}", path);
        driver.get(url);
        wait.waitForPageLoad();
    }
    
    /**
     * Get current URL
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    /**
     * Get page title
     */
    protected String getPageTitle() {
        return driver.getTitle();
    }
    
    /**
     * Refresh page
     */
    protected void refreshPage() {
        logger.info("Refreshing page");
        driver.navigate().refresh();
        wait.waitForPageLoad();
    }
    
    /**
     * Go back in browser history
     */
    protected void goBack() {
        logger.info("Going back in browser history");
        driver.navigate().back();
        wait.waitForPageLoad();
    }
    
    /**
     * Go forward in browser history
     */
    protected void goForward() {
        logger.info("Going forward in browser history");
        driver.navigate().forward();
        wait.waitForPageLoad();
    }
}
