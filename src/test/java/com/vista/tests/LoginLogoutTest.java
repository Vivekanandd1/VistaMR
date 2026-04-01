package com.vista.tests;

import com.vista.framework.base.BaseUiTest;
import com.vista.framework.config.ConfigKeys;
import com.vista.framework.config.ConfigManager;
import com.vista.pages.*;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Login/Logout Test Cases.
 * Validates authentication functionality.
 */
public class LoginLogoutTest extends BaseUiTest {
    
    private static final Logger logger = LogManager.getLogger(LoginLogoutTest.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    @Description("This test case validates Login/Logout functionality")
    @Owner("Vivekanand Deshmukh")
    @Severity(SeverityLevel.CRITICAL)
    @Test(priority = 1)
    public void testLoginLogout() {
        logger.info("Starting Login/Logout test");
        
        // Get credentials from config
        String email = config.get(ConfigKeys.EMAIL);
        String password = config.get(ConfigKeys.PASSWORD);
        
        // Validate credentials are configured
        if (email == null || password == null) {
            logger.error("Credentials not configured in environment variables or .env file");
            throw new RuntimeException("Credentials not configured! Please set Email and Password in .env file or environment variables.");
        }
        
        // Initialize page objects
        LoginPage loginPage = new LoginPage(driver, wait);
        DashboardPage dashboardPage = new DashboardPage(driver, wait);
        
        // Navigate to application
        navigateToHome();
        
        // Perform login
        loginPage.login(email, password);
        
        // Verify dashboard is loaded
        Assert.assertTrue(dashboardPage.isDashboardLoaded(), "Dashboard did not load after login");
        
        // Verify page title
        wait.waitForTitleIs("Kreditz | Vista - Dashboard - All");
        String actualTitle = getPageTitle();
        Assert.assertEquals(actualTitle, "Kreditz | Vista - Dashboard - All", "Page title mismatch after login!");
        
        logger.info("Login verified successfully");
        
        // Perform logout
        dashboardPage.logout();
        
        // Verify redirect to login page
        wait.waitForTitleIs("Kreditz | Vista");
        
        logger.info("Login/Logout test completed successfully");
    }
}
