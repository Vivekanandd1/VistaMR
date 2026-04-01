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
import io.qameta.allure.testng.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WindowType;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Manual Request Test Cases.
 * Validates end-to-end manual request flow.
 */
public class ManualRequestTest extends BaseUiTest {
    
    private static final Logger logger = LogManager.getLogger(ManualRequestTest.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    private String generatedName;
    private String generatedEmail;
    private String parentWindowHandle;
    
    @Description("This test case validates E2E Manual Request flow")
    @Owner("Vivekanand Deshmukh")
    @Severity(SeverityLevel.CRITICAL)
    @Tag("Manual Request")
    @Test(priority = 1)
    public void testManualRequestFlow() throws InterruptedException {
        logger.info("Starting Manual Request E2E test");
        
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
        
        // Fill and submit manual request form
        newRequestPage.fillManualRequestForm(generatedName, generatedEmail);
        
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
        
        // Complete bank consent flow
        bankConsentPage.completeManualConsent();
        
        // Wait for success message
        bankConsentPage.waitForSuccess();
        Assert.assertTrue(bankConsentPage.isSuccessDisplayed(), "Success message not displayed");
        
        logger.info("Request completed successfully in bank portal");
        
        // Switch back to parent window
        driver.switchTo().window(parentWindowHandle);
        
        // Closing the modal
        newRequestPage.ModalClose();
        
        // Perform logout
        dashboardPage.logout();
        
        logger.info("Manual Request E2E test completed successfully");
    }
}
