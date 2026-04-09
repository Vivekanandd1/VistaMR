package com.vista.pages;

import com.vista.framework.utils.ElementUtils;
import com.vista.framework.wait.WaitStrategy;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * Login Page Object Model.
 * Handles all interactions with the login page.
 */
public class LoginPage {
    
    private static final Logger logger = LogManager.getLogger(LoginPage.class);
    
    private final WebDriver driver;
    private final ElementUtils elementUtils;
    private final WaitStrategy wait;
    
    // Locators
    private final By loginEmail = By.id("kreditz_email");
    private final By loginPassword = By.id("kreditz_current_password");
    private final By loginBtn = By.cssSelector("button[type='submit']");
    private final By langDropdownLocator = By.xpath("//div[contains(@class,'custom-dropdown-language')]");
    private final By nextBtn = By.id("next-btn");
    private final By submitBtn = By.id("button_id");
    private final By EnglishLang =  By.xpath("//a[contains(@onclick,'English')]");
    
    // Language options
    private final By[] languageOptions = {
        By.xpath("//a[contains(@onclick,'English')]"),
        By.xpath("//a[normalize-space()='Engelska']"),
        By.xpath("//a[normalize-space()='Englisch']"),
        By.xpath("//a[normalize-space()='Inglés']")
    };
    
    public LoginPage(WebDriver driver, WaitStrategy wait) {
        this.driver = driver;
        this.wait = wait;
        this.elementUtils = new ElementUtils(driver, wait);
    }
    
    /**
     * Enter email address
     */
    @Step("Enter email: {email}")
    public LoginPage enterEmail(String email) {
        logger.debug("Entering email: {}", email);
        elementUtils.sendKeys(loginEmail, email);
        return this;
    }
    
    /**
     * Enter password
     */
    @Step("Enter password")
    public LoginPage enterPassword(String password) {
        logger.debug("Entering password");
        elementUtils.sendKeys(loginPassword, password);
        return this;
    }
    
    /**
     * Click login button
     */
    @Step("Click login button")
    public void clickLogin() {
        logger.debug("Clicking login button");
        elementUtils.click(loginBtn);
    }
    
    /**
     * Complete login with language selection
     */
    @Step("Complete login with language selection")
    public void completeLogin() {
        logger.debug("Completing login with English language selection");
        
        // Click language dropdown
        elementUtils.click(langDropdownLocator);
        
        // Try to select English
        for (By langOption : languageOptions) {
            try {
            	wait.waitForElementVisibility(EnglishLang);
                if (elementUtils.exists(EnglishLang)) {
                	wait.waitForElementToBeClickable(EnglishLang).click();
                    logger.info(" English Language selected successfully");
                    return;
                }
            } catch (Exception e) {
                logger.debug("Language option not available: {}", langOption);
            }
        }
        
        logger.warn("No matching language option found");
    }
    
    /**
     * Perform complete login with credentials
     */
    @Step("Login with credentials")
    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLogin();
//        wait.waitForPageLoad();
        completeLogin();
        logger.info("Login completed successfully");
    }
    
    /**
     * Check if login page is displayed
     */
    public boolean isDisplayed() {
        return elementUtils.isDisplayed(loginEmail);
    }
}
