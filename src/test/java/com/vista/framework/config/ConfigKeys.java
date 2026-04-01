package com.vista.framework.config;

/**
 * Configuration keys constants to prevent typos and provide central management.
 */
public final class ConfigKeys {
    
    // Browser Configuration
    public static final String BROWSER = "BROWSER";
    public static final String BROWSER_VERSION = "BROWSER_VERSION";
    public static final String HEADLESS = "headless";
    public static final String REMOTE_WEBDRIVER_URL = "REMOTE_WEBDRIVER_URL";
    
    // Application Configuration
    public static final String BASE_URL = "BASE_URL";
    public static final String APP_URL = "APP_URL";
    public static final String PROFILE = "PROFILE";
    
    // Timeout Configuration
    public static final String IMPLICIT_WAIT = "IMPLICIT_WAIT";
    public static final String EXPLICIT_WAIT = "EXPLICIT_WAIT";
    public static final String PAGE_LOAD_TIMEOUT = "PAGE_LOAD_TIMEOUT";
    public static final String SCRIPT_TIMEOUT = "SCRIPT_TIMEOUT";
    
    // Test Credentials
    public static final String EMAIL = "Email";
    public static final String PASSWORD = "Password";
    
    // Database Configuration
    public static final String DB_HOST = "DB_HOST";
    public static final String DB_PORT = "DB_PORT";
    public static final String DB_NAME = "DB_NAME";
    public static final String DB_USER = "DB_USER";
    public static final String DB_PASSWORD = "DB_PASSWORD";
    
    // API Configuration
    public static final String API_BASE_URL = "API_BASE_URL";
    public static final String API_CLIENT_ID = "API_CLIENT_ID";
    
    // Screenshot Configuration
    public static final String SCREENSHOT_ON_FAILURE = "SCREENSHOT_ON_FAILURE";
    public static final String SCREENSHOT_DIR = "SCREENSHOT_DIR";
    
    // Logging Configuration
    public static final String LOG_LEVEL = "LOG_LEVEL";
    
    // Retry Configuration
    public static final String MAX_RETRY_COUNT = "MAX_RETRY_COUNT";
    
    // CI/CD
    public static final String CI = "CI";
    
    private ConfigKeys() {
        // Prevent instantiation
    }
}
