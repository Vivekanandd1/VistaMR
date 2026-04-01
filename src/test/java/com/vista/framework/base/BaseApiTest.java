package com.vista.framework.base;

import com.vista.framework.api.ApiClient;
import com.vista.framework.config.ConfigKeys;
import com.vista.framework.config.ConfigManager;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import com.vista.framework.listeners.TestListener;
import com.vista.framework.listeners.AnnotationTransformer;
import io.qameta.allure.testng.AllureTestNg;

/**
 * Base Test class for API tests.
 * Provides API client initialization and common utilities.
 */
@Listeners({AllureTestNg.class, TestListener.class, AnnotationTransformer.class})
public abstract class BaseApiTest {
    
    protected static final Logger logger = LogManager.getLogger(BaseApiTest.class);
    protected static final ConfigManager config = ConfigManager.getInstance();
    
    protected ApiClient apiClient;
    protected String baseUrl;
    protected String authToken;
    
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        logger.info("Setting up API test environment");
        
        // Initialize API client
        apiClient = ApiClient.getInstance();
        
        // Get base URL
        baseUrl = config.get(ConfigKeys.API_BASE_URL, 
                config.get(ConfigKeys.BASE_URL, "https://vista.kreditz-dev.com"));
        
        apiClient.setBaseUrl(baseUrl);
        
        logger.info("API test environment setup completed. Base URL: {}", baseUrl);
    }
    
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        logger.info("Tearing down API test environment");
        
        // Clear any stored tokens
        authToken = null;
        apiClient.clearHeaders();
        
        logger.info("API test environment teardown completed");
    }
    
    /**
     * Set authentication token for API calls
     */
    protected void setAuthToken(String token) {
        this.authToken = token;
        apiClient.setAuthToken(token);
        logger.debug("Auth token set for API calls");
    }
    
    /**
     * Get OAuth2 token using client credentials
     */
    protected String getOAuth2Token(String clientId, String clientSecret) {
        String tokenEndpoint = "/kreditz/api/v3/authorizations/access_token";
        
        Response response = apiClient.post(tokenEndpoint, 
            "client_id=" + clientId + "&client_secret=" + clientSecret + "&grant_type=client_credentials");
        
        authToken = response.jsonPath().getString("data.access_token");
        logger.info("OAuth2 token obtained successfully");
        
        return authToken;
    }
    
    /**
     * Get API client instance
     */
    protected ApiClient getApiClient() {
        return apiClient;
    }
}
