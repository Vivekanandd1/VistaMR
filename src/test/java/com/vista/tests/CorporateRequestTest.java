package com.vista.tests;

import com.vista.framework.base.BaseUiTest;
import com.vista.framework.config.ConfigKeys;
import com.vista.framework.config.ConfigManager;
import com.vista.framework.data.TestDataFactory;
import com.vista.pages.*;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WindowType;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Corporate Request Test Cases.
 * Validates end-to-end corporate request flow.
 */
public class CorporateRequestTest extends BaseUiTest {
    
    private static final Logger logger = LogManager.getLogger(CorporateRequestTest.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    private String generatedName;
    private String generatedEmail;
    private String parentWindowHandle;
    
    @Description("This test case validates E2E Corporate Request flow")
    @Owner("Vivekanand Deshmukh")
    @Severity(SeverityLevel.CRITICAL)
    @Test(priority = 1)
    public void testCorporateRequestFlow() throws InterruptedException {
        logger.info("Starting Corporate Request E2E test");
        
        // Get credentials from config
        String email = config.get(ConfigKeys.EMAIL);
        String password = config.get(ConfigKeys.PASSWORD);
        
        // Validate credentials are configured
        if (email == null || password == null) {
            logger.error("Credentials not configured");
            throw new RuntimeException("Credentials not configured!");
        }
        
        // Generate test data
        generatedName = TestDataFactory.generateFullName();
        generatedEmail = TestDataFactory.generateEmail();
        
        logger.info("Generated test data - Name: {}, Email: {}", generatedName, generatedEmail);
        
        // Initialize page objects
        LoginPage loginPage = new LoginPage(driver, wait);
        DashboardPage dashboardPage = new DashboardPage(driver, wait);
        NewRequestPage newRequestPage = new NewRequestPage(driver, wait);
        BankConsentPage bankConsentPage = new BankConsentPage(driver, wait);
        
        // Navigate to application and login
        navigateToHome();
        loginPage.login(email, password);
        
        // Verify dashboard is loaded
        Assert.assertTrue(dashboardPage.isDashboardLoaded(), "Dashboard did not load after login");
        
        // Fill and submit corporate request form
        newRequestPage.fillCorporateRequestForm(generatedName, generatedEmail);
        
        // Get request URL and open in new tab
        String requestUrl = newRequestPage.getRequestUrl();
        logger.info("Request URL obtained: {}", requestUrl);
        
        // Store parent window handle
        parentWindowHandle = driver.getWindowHandle();
        
        // Open request URL in new tab
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get(requestUrl);
        wait.waitForPageLoad();
        
        // Switch to new tab
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(parentWindowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
        
        // Complete corporate bank consent flow
        bankConsentPage.completeCorporateConsent();
        
        // Wait for success message
        bankConsentPage.waitForSuccess();
        Assert.assertTrue(bankConsentPage.isSuccessDisplayed(), "Success message not displayed");
        
        logger.info("Corporate request completed successfully in bank portal");
        
        // Switch back to parent window
        driver.switchTo().window(parentWindowHandle);
        
        // Closing the modal
        newRequestPage.ModalClose();
        
        // Perform logout
        dashboardPage.logout();
        
        logger.info("Corporate Request E2E test completed successfully");
    }
}
