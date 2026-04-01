package com.vista.framework.driver;

import com.vista.framework.config.ConfigKeys;
import com.vista.framework.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Collections;

/**
 * WebDriver Factory with support for multiple browsers and remote execution.
 * Implements singleton pattern for driver lifecycle management.
 */
public class WebDriverFactory {
    
    private static final Logger logger = LogManager.getLogger(WebDriverFactory.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    private static ThreadLocal<WebDriver> driverInstance = new ThreadLocal<>();
    private static WebDriverFactory instance;
    
    private WebDriverFactory() {}
    
    public static WebDriverFactory getInstance() {
        if (instance == null) {
            instance = new WebDriverFactory();
        }
        return instance;
    }
    
    /**
     * Create and configure WebDriver based on configuration
     */
    public WebDriver createDriver() {
        String browser = config.get(ConfigKeys.BROWSER, "chrome").toLowerCase();
        boolean isRemote = config.getBoolean("REMOTE_EXECUTION", false);
        
        logger.info("Initializing WebDriver for browser: {} (Remote: {})", browser, isRemote);
        
        WebDriver driver;
        
        if (isRemote) {
            driver = createRemoteDriver(browser);
        } else {
            driver = createLocalDriver(browser);
        }
        
        configureDriver(driver);
        driverInstance.set(driver);
        
        logger.info("WebDriver initialized successfully");
        return driver;
    }
    
    private WebDriver createLocalDriver(String browser) {
        return switch (browser) {
            case "chrome" -> createChromeDriver();
            case "firefox" -> createFirefoxDriver();
            case "edge" -> createEdgeDriver();
            default -> {
                logger.warn("Unknown browser '{}', defaulting to Chrome", browser);
                yield createChromeDriver();
            }
        };
    }
    
    private WebDriver createRemoteDriver(String browser) {
        String remoteUrl = config.get(ConfigKeys.REMOTE_WEBDRIVER_URL, "http://localhost:4444/wd/hub");
        logger.info("Connecting to Remote WebDriver at: {}", remoteUrl);
        
        try {
            var capabilities = getBrowserOptions(browser);
            return new RemoteWebDriver(new URL(remoteUrl), capabilities);
        } catch (MalformedURLException e) {
            logger.error("Invalid remote WebDriver URL: {}", remoteUrl, e);
            throw new RuntimeException("Failed to create remote WebDriver", e);
        }
    }
    
    private WebDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions();

        // Chrome-specific configurations
        if (config.isHeadless()) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
        }

        options.addArguments("--incognito");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-extensions");
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);

        logger.info("Creating ChromeDriver with headless: {}", config.isHeadless());
        return new ChromeDriver(options);
    }

    private WebDriver createFirefoxDriver() {
        FirefoxOptions options = new FirefoxOptions();

        if (config.isHeadless()) {
            options.addArguments("-headless");
        }

        options.addArguments("-width=1920", "-height=1080");

        logger.info("Creating FirefoxDriver with headless: {}", config.isHeadless());
        return new FirefoxDriver(options);
    }

    private WebDriver createEdgeDriver() {
        EdgeOptions options = new EdgeOptions();

        if (config.isHeadless()) {
            options.addArguments("--headless");
        }

        options.addArguments("--window-size=1920,1080");
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        
        logger.info("Creating EdgeDriver with headless: {}", config.isHeadless());
        return new EdgeDriver(options);
    }

    private org.openqa.selenium.Capabilities getBrowserOptions(String browser) {
        return switch (browser) {
            case "chrome" -> {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--no-sandbox");
                yield options;
            }
            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("-headless");
                yield options;
            }
            default -> new ChromeOptions();
        };
    }
    
    private void configureDriver(WebDriver driver) {
        int implicitWait = config.getInt(ConfigKeys.IMPLICIT_WAIT, 10);
        int pageLoadTimeout = config.getInt(ConfigKeys.PAGE_LOAD_TIMEOUT, 60);
        
        driver.manage().timeouts().implicitlyWait(Duration.ofMinutes(implicitWait));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
        driver.manage().window().maximize();
        
        logger.debug("Driver configured with implicit wait: {}s, page load timeout: {}s", 
                     implicitWait, pageLoadTimeout);
    }
    
    /**
     * Get the current WebDriver instance
     */
    public WebDriver getDriver() {
        WebDriver driver = driverInstance.get();
        if (driver == null) {
            logger.error("WebDriver not initialized!");
            throw new IllegalStateException("WebDriver not initialized. Call createDriver() first.");
        }
        return driver;
    }
    
    /**
     * Check if WebDriver is initialized
     */
    public boolean isDriverInitialized() {
        return driverInstance.get() != null;
    }
    
    /**
     * Quit the current WebDriver and clean up
     */
    public void quitDriver() {
        WebDriver driver = driverInstance.get();
        if (driver != null) {
            try {
                logger.info("Quitting WebDriver");
                driver.quit();
            } catch (Exception e) {
                logger.error("Error while quitting WebDriver", e);
            } finally {
                driverInstance.remove();
            }
        }
    }
    
    /**
     * Close the current WebDriver without quitting
     */
    public void closeDriver() {
        WebDriver driver = driverInstance.get();
        if (driver != null) {
            try {
                logger.info("Closing WebDriver");
                driver.close();
            } catch (Exception e) {
                logger.error("Error while closing WebDriver", e);
            }
        }
    }
}
