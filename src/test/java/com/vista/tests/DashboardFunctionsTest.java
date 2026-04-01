package com.vista.tests;

import com.vista.framework.base.BaseUiTest;
import com.vista.framework.config.ConfigKeys;
import com.vista.framework.config.ConfigManager;
import com.vista.pages.CertificateLogsPage;
import com.vista.pages.DashboardPage;
import com.vista.pages.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Dashboard Functions Test Cases.
 * Validates dashboard search and filter functionalities.
 */
public class DashboardFunctionsTest extends BaseUiTest {
    
    private static final Logger logger = LogManager.getLogger(DashboardFunctionsTest.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    private static final String CERTIFICATE_NUMBER = "51818";
    
    @Description("This test case validates Dashboard Search and Filter functionalities")
    @Owner("Vivekanand Deshmukh")
    @Severity(SeverityLevel.CRITICAL)
    @Test(priority = 1)
    public void testDashboardSearchAndFilters() throws InterruptedException {
        logger.info("Starting Dashboard Search and Filters test");
        
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
        CertificateLogsPage certificateLogsPage = new CertificateLogsPage(driver, wait);
        
        // Navigate to application and login
        navigateToHome();
        loginPage.login(email, password);
        
        // Verify dashboard is loaded
        Assert.assertTrue(dashboardPage.isDashboardLoaded(), "Dashboard did not load after login");
        
        // Test certificate search
        logger.info("Testing certificate search functionality");
        certificateLogsPage.searchCertificate(CERTIFICATE_NUMBER);
        logger.info("Certificate search completed successfully");
        
        // Test source filter
        logger.info("Testing source filter");
        certificateLogsPage.applySourceFilter();
        
        // Test request type filter
        logger.info("Testing request type filter");
        certificateLogsPage.applyRequestTypeFilter();
        
        // Test market filter
        logger.info("Testing market filter");
        certificateLogsPage.applyMarketFilter();
        
        logger.info("All filter tests completed successfully");
        
        // Perform logout
        dashboardPage.logout();
        
        logger.info("Dashboard Search and Filters test completed successfully");
    }
    
    @Description("This test case validates Certificate Search with custom number")
    @Owner("Vivekanand Deshmukh")
    @Severity(SeverityLevel.NORMAL)
    @Test(priority = 2)
    public void testCertificateSearchWithCustomNumber() throws InterruptedException {
        logger.info("Starting Certificate Search with custom number test");
        
        // Get credentials from config
        String email = config.get(ConfigKeys.EMAIL);
        String password = config.get(ConfigKeys.PASSWORD);
        
        // Validate credentials are configured
        if (email == null || password == null) {
            logger.error("Credentials not configured");
            throw new RuntimeException("Credentials not configured!");
        }
        
        // Generate random certificate number for testing
        String certNumber = "91818";
        
        // Initialize page objects
        LoginPage loginPage = new LoginPage(driver, wait);
        DashboardPage dashboardPage = new DashboardPage(driver, wait);
        CertificateLogsPage certificateLogsPage = new CertificateLogsPage(driver, wait);
        
        // Navigate to application and login
        navigateToHome();
        loginPage.login(email, password);
        
        // Verify dashboard is loaded
        Assert.assertTrue(dashboardPage.isDashboardLoaded(), "Dashboard did not load after login");
        
        // Test certificate search (may fail if certificate doesn't exist - expected behavior)
        logger.info("Testing certificate search with generated number: {}", certNumber);
        
        certificateLogsPage.searchwithInvalidCertificate(certNumber);
        
        // Perform logout
        dashboardPage.logout();
        
        logger.info("Certificate Search test completed");
    }
}
