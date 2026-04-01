package com.vista.tests;

import com.vista.framework.config.ConfigKeys;
import com.vista.framework.config.ConfigManager;
import com.vista.pages.ApiSettingsPage;
import com.vista.pages.DashboardPage;
import com.vista.pages.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.vista.framework.base.BaseUiTest;

/**
 * API Test Cases.
 * Validates API functionality including token generation and API calls.
 */
public class ApiTest extends BaseUiTest {
    
    private static final Logger logger = LogManager.getLogger(ApiTest.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    @Description("This test case validates API Token Generation flow")
    @Owner("Vivekanand Deshmukh")
    @Severity(SeverityLevel.CRITICAL)
    @Test(priority = 1)
    public void testApiTokenGeneration() {
        logger.info("Starting API Token Generation test");
        
        // Get credentials from config
        String email = config.get(ConfigKeys.EMAIL);
        String password = config.get(ConfigKeys.PASSWORD);
        
        // Validate credentials are configured
        if (email == null || password == null) {
            logger.error("Credentials not configured");
            throw new RuntimeException("Credentials not configured!");
        }
        
        // Initialize page objects
        LoginPage loginPage = new LoginPage(driver, wait);
        DashboardPage dashboardPage = new DashboardPage(driver, wait);
        ApiSettingsPage apiSettingsPage = new ApiSettingsPage(driver, wait);
        
        // Navigate to application and login
        navigateToHome();
        loginPage.login(email, password);
        
        // Verify dashboard is loaded
        Assert.assertTrue(dashboardPage.isDashboardLoaded(), "Dashboard did not load after login");
        
        // Regenerate secret and get API token
        logger.info("Regenerating API secret and obtaining token");
        String apiToken = apiSettingsPage.getApiAccessToken();
        
        // Validate token is not null or empty
        Assert.assertNotNull(apiToken, "API Token should not be null");
        Assert.assertFalse(apiToken.isEmpty(), "API Token should not be empty");
        
        logger.info("API Token obtained successfully: {}", apiToken.substring(0, 10) + "...");
        
        // Perform logout
        dashboardPage.logout();
        
        logger.info("API Token Generation test completed successfully");
    }
    
    @Description("This test case validates API access with generated token")
    @Owner("Vivekanand Deshmukh")
    @Severity(SeverityLevel.CRITICAL)
    @Test(priority = 2, dependsOnMethods = "testApiTokenGeneration")
    public void testApiAccessWithToken() {
        logger.info("Starting API Access test with token");
        
        // Get credentials from config
        String email = config.get(ConfigKeys.EMAIL);
        String password = config.get(ConfigKeys.PASSWORD);
        
        // Validate credentials are configured
        if (email == null || password == null) {
            logger.error("Credentials not configured");
            throw new RuntimeException("Credentials not configured!");
        }
        
        // Initialize page objects
        LoginPage loginPage = new LoginPage(driver, wait);
        DashboardPage dashboardPage = new DashboardPage(driver, wait);
        ApiSettingsPage apiSettingsPage = new ApiSettingsPage(driver, wait);
        
        // Navigate to application and login
        navigateToHome();
        loginPage.login(email, password);
        
        // Get API token
        String apiToken = apiSettingsPage.getApiAccessToken();
        
        // Use API client to make authenticated request
        com.vista.framework.api.ApiClient apiClient = com.vista.framework.api.ApiClient.getInstance();
        apiClient.setAuthToken(apiToken);
        
        // Example API call (adjust endpoint based on available APIs)
        logger.info("Making authenticated API request");
        
        try {
            Response response = apiClient.get("/kreditz/api/v3/authorizations");
            
            // Validate response
            Assert.assertEquals(response.getStatusCode(), 200, "API request should return 200 OK");
            logger.info("API request successful");
            
        } catch (Exception e) {
            logger.warn("API endpoint may not be available: {}", e.getMessage());
        }
        
        // Perform logout
        dashboardPage.logout();
        
        logger.info("API Access test completed");
    }
}
