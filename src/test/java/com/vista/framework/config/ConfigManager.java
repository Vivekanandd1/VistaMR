package com.vista.framework.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Centralized Configuration Manager with profile support.
 * Loads configuration from multiple sources with priority:
 * 1. System Properties (for CI/CD overrides)
 * 2. Environment Variables (for Docker/GitHub Actions)
 * 3. .env file (for local development)
 * 4. application.properties (for default values)
 */
public class ConfigManager {
    
    private static final Logger logger = LogManager.getLogger(ConfigManager.class);
    private static final ConfigManager instance = new ConfigManager();
    
    private final Map<String, String> envMap = new HashMap<>();
    private final Properties appProperties = new Properties();
    
    private ConfigManager() {
        loadDotEnv();
        loadApplicationProperties();
        logger.info("Configuration Manager initialized successfully");
    }
    
    public static ConfigManager getInstance() {
        return instance;
    }
    
    private void loadDotEnv() {
        try (BufferedReader br = new BufferedReader(new FileReader(".env"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.trim().startsWith("#")) {
                    continue;
                }
                if (line.contains("=")) {
                    String[] parts = line.split("=", 2);
                    envMap.put(parts[0].trim(), parts[1].trim());
                }
            }
            logger.info("Loaded .env file successfully");
        } catch (IOException e) {
            logger.warn(".env file not found - using environment variables only");
        }
    }
    
    private void loadApplicationProperties() {
        try (var input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                appProperties.load(input);
                logger.info("Loaded application.properties successfully");
            } else {
                logger.warn("application.properties not found in classpath");
            }
        } catch (IOException e) {
            logger.error("Error loading application.properties", e);
        }
    }
    
    /**
     * Get configuration value with priority: System Properties > Env Variables > .env > application.properties
     */
    public String get(String key) {
        // Priority 1: System Properties
        String value = System.getProperty(key);
        if (value != null && !value.isEmpty()) {
            logger.debug("Found '{}' in System Properties", key);
            return value;
        }
        
        // Priority 2: Environment Variables
        value = System.getenv(key);
        if (value != null && !value.isEmpty()) {
            logger.debug("Found '{}' in Environment Variables", key);
            return value;
        }
        
        // Priority 3: .env file
        value = envMap.get(key);
        if (value != null && !value.isEmpty()) {
            logger.debug("Found '{}' in .env file", key);
            return value;
        }
        
        // Priority 4: application.properties
        value = appProperties.getProperty(key);
        if (value != null && !value.isEmpty()) {
            logger.debug("Found '{}' in application.properties", key);
            return value;
        }
        
        logger.warn("Configuration key '{}' not found in any source", key);
        return null;
    }
    
    /**
     * Get configuration value with default fallback
     */
    public String get(String key, String defaultValue) {
        String value = get(key);
        return value != null ? value : defaultValue;
    }
    
    /**
     * Get boolean configuration value
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value.trim());
    }
    
    /**
     * Get integer configuration value
     */
    public int getInt(String key, int defaultValue) {
        String value = get(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer value for key '{}': {}", key, value);
            return defaultValue;
        }
    }
    
    /**
     * Get long configuration value
     */
    public long getLong(String key, long defaultValue) {
        String value = get(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            logger.warn("Invalid long value for key '{}': {}", key, value);
            return defaultValue;
        }
    }
    
    /**
     * Get the current environment profile
     */
    public String getProfile() {
        return get("PROFILE", "local");
    }
    
    /**
     * Check if running in CI/CD environment
     */
    public boolean isCI() {
        return getBoolean("CI", false) || getProfile().equalsIgnoreCase("ci");
    }
    
    /**
     * Check if running in headless mode
     */
    public boolean isHeadless() {
        return getBoolean("headless", true);
    }
}
