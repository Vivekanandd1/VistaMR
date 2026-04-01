package com.vista.pages;

import com.vista.framework.utils.ElementUtils;
import com.vista.framework.wait.WaitStrategy;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * User Management Page Object Model.
 * Handles all interactions with user management functionality.
 */
public class UserManagementPage {
    
    private static final Logger logger = LogManager.getLogger(UserManagementPage.class);
    
    private final WebDriver driver;
    private final ElementUtils elementUtils;
    private final WaitStrategy wait;
    
    // Locators
    private final By userManagementMenu = By.xpath("//a[normalize-space()='User Management']");
    private final By createUserBtn = By.xpath("//a[normalize-space()='Create User']");
    private final By userName = By.id("organization_user_name");
    private final By userEmail = By.id("organization_user_email");
    private final By userPhone = By.id("organization_user_phone_number");
    private final By submitBtn = By.className("submit_new_user");
    private final By alertMessage = By.xpath("//div[@class='alert-message']");
    private final By userRows = By.xpath("//tbody/tr");
    private final By userActionsDropdown = By.xpath(".//a[@class='dropdown-toggle']");
    private final By deactivateOption = By.xpath(".//a[@data-commit='Deactivate']");
    private final By confirmDeactivateBtn = By.xpath("//button[text()='Deactivate']");
    
    public UserManagementPage(WebDriver driver, WaitStrategy wait) {
        this.driver = driver;
        this.wait = wait;
        this.elementUtils = new ElementUtils(driver, wait);
    }
    
    /**
     * Click on User Management menu
     */
    @Step("Navigate to User Management")
    public UserManagementPage navigateToUserManagement() {
        logger.debug("Clicking User Management menu");
        elementUtils.click(userManagementMenu);
        return this;
    }
    
    /**
     * Click on Create User button
     */
    @Step("Navigate to Create User page")
    public UserManagementPage navigateToCreateUser() {
        logger.debug("Clicking Create User button");
        elementUtils.click(createUserBtn);
        return this;
    }
    
    /**
     * Enter user name
     */
    @Step("Enter user name: {name}")
    public UserManagementPage enterUserName(String name) {
        logger.debug("Entering user name: {}", name);
        elementUtils.sendKeys(userName, name);
        return this;
    }
    
    /**
     * Enter user email
     */
    @Step("Enter user email: {email}")
    public UserManagementPage enterUserEmail(String email) {
        logger.debug("Entering user email: {}", email);
        elementUtils.sendKeys(userEmail, email);
        return this;
    }
    
    /**
     * Enter user phone number
     */
    @Step("Enter user phone number: {phone}")
    public UserManagementPage enterUserPhone(String phone) {
        logger.debug("Entering user phone number: {}", phone);
        elementUtils.sendKeys(userPhone, phone);
        return this;
    }
    
    /**
     * Click submit button
     */
    @Step("Submit user creation form")
    public void submit() {
        logger.debug("Clicking submit button");
        elementUtils.click(submitBtn);
        wait.waitForElementInvisibility(alertMessage);
    }
    
    /**
     * Create a new user
     */
    @Step("Create user: {name}, {email}, {phone}")
    public void createUser(String name, String email, String phone) {
        logger.info("Creating new user: {}", name);
        navigateToUserManagement();
        navigateToCreateUser();
        enterUserName(name);
        enterUserEmail(email);
        enterUserPhone(phone);
        submit();
        logger.info("User created successfully: {}", name);
    }
    
    /**
     * Delete user by name
     */
    @Step("Delete user: {userName}")
    public void deleteUser(String userName) {
        logger.info("Deleting user: {}", userName);
        
        wait.waitForElementsPresence(userRows);
        List<WebElement> rows = driver.findElements(userRows);
        
        for (WebElement row : rows) {
            try {
                String name = row.findElement(By.xpath("./td[2]")).getText();
                
                if (name.equalsIgnoreCase(userName)) {
                    logger.debug("Found user to delete: {}", userName);
                    
                    row.findElement(userActionsDropdown).click();
                    row.findElement(deactivateOption).click();
                    elementUtils.click(confirmDeactivateBtn);
                    
                    wait.waitForElementInvisibility(alertMessage);
                    logger.info("User deleted successfully: {}", userName);
                    return;
                }
            } catch (Exception e) {
                logger.debug("Row does not match or user not found");
            }
        }
        
        logger.warn("User not found for deletion: {}", userName);
    }
    
    /**
     * Check if user exists in the list
     */
    @Step("Check if user exists: {userName}")
    public boolean isUserExists(String userName) {
        wait.waitForElementsPresence(userRows);
        List<WebElement> rows = driver.findElements(userRows);
        
        for (WebElement row : rows) {
            try {
                String name = row.findElement(By.xpath("./td[2]")).getText();
                if (name.equalsIgnoreCase(userName)) {
                    return true;
                }
            } catch (Exception e) {
                logger.debug("Error checking row");
            }
        }
        
        return false;
    }
}
