package com.vista.tests;

import com.vista.framework.base.BaseUiTest;
import com.vista.framework.config.ConfigKeys;
import com.vista.framework.config.ConfigManager;
import com.vista.framework.data.TestDataFactory;
import com.vista.pages.DashboardPage;
import com.vista.pages.LoginPage;
import com.vista.pages.UserManagementPage;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * User Creation Test Cases.
 * Validates user management functionality including create and delete.
 */
public class CreateUserTest extends BaseUiTest {
    
    private static final Logger logger = LogManager.getLogger(CreateUserTest.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    // Test data
    private String generatedName;
    private String generatedEmail;
    private String generatedPhone;
    
    @Description("This test case validates User Creation and Deletion functionality")
    @Owner("Vivekanand Deshmukh")
    @Severity(SeverityLevel.NORMAL)
    @Test(priority = 1)
    public void testUserCreationAndDeletion() {
        logger.info("Starting User Creation/Deletion test");
        
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
        generatedPhone = TestDataFactory.generatePhoneNumber();
        
        logger.info("Generated test data - Name: {}, Email: {}, Phone: {}", 
                   generatedName, generatedEmail, generatedPhone);
        
        // Initialize page objects
        LoginPage loginPage = new LoginPage(driver, wait);
        UserManagementPage userManagementPage = new UserManagementPage(driver, wait);
        DashboardPage dashboardPage = new DashboardPage(driver, wait);
        
        // Navigate to application and login
        navigateToHome();
        loginPage.login(email, password);
        
        // Create user
        userManagementPage.createUser(generatedName, generatedEmail, generatedPhone);
        
        // Verify user was created
        Assert.assertTrue(userManagementPage.isUserExists(generatedName), 
                         "User was not created successfully");
        logger.info("User created successfully: {}", generatedName);
        
        // Delete user
        userManagementPage.deleteUser(generatedName);
        
        // Perform logout
        dashboardPage.logout();
        
        logger.info("User Creation/Deletion test completed successfully");
    }
}
