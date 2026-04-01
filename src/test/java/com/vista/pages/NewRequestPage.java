package com.vista.pages;

import com.vista.framework.utils.ElementUtils;
import com.vista.framework.wait.WaitStrategy;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.LocalDateTime;

/**
 * New Request Page Object Model.
 * Handles all interactions with the new request form.
 */
public class NewRequestPage {
    
    private static final Logger logger = LogManager.getLogger(NewRequestPage.class);
    
    private final WebDriver driver;
    private final ElementUtils elementUtils;
    private final WaitStrategy wait;
    
    // Locators for Manual Request
    private final By newReq = By.cssSelector("span.menu-icon-new-request");
    private final By recipientName = By.id("recipient_name");
    private final By emailBox = By.id("e-post");
    private final By caseIdBox = By.xpath("//input[@placeholder='Case id']");
    private final By certType = By.cssSelector("select[name='type']");
    private final By country = By.id("request-country-select");
    private final By mode = By.id("account_verification");
    private final By environment = By.id("env");
    private final By formSubmitBtn = By.id("submit-check");
    private final By requestUrlCopy = By.id("request_url_copy");
    private final By OkButton = By.xpath("//button[text()='OK']");
    
    // Locators for Corporate Request
    private final By corporateCountry = By.xpath("//select[@name='corporate_country_id']");
    
    public NewRequestPage(WebDriver driver, WaitStrategy wait) {
        this.driver = driver;
        this.wait = wait;
        this.elementUtils = new ElementUtils(driver, wait);
    }
    
    /**
     * Click on New Request menu
     */
    @Step("Navigate to New Request")
    public NewRequestPage navigateToNewRequest() {
        logger.debug("Clicking New Request menu");
        elementUtils.click(newReq);
        return this;
    }
    
    /**
     * Enter recipient name
     */
    @Step("Enter recipient name: {name}")
    public NewRequestPage enterRecipientName(String name) {
        logger.debug("Entering recipient name: {}", name);
        elementUtils.sendKeys(recipientName, name);
        return this;
    }
    
    /**
     * Enter email
     */
    @Step("Enter email: {email}")
    public NewRequestPage enterEmail(String email) {
        logger.debug("Entering email: {}", email);
        elementUtils.sendKeys(emailBox, email);
        return this;
    }
    
    /**
     * Enter case ID
     */
    @Step("Enter case ID: {caseId}")
    public NewRequestPage enterCaseId(String caseId) {
        logger.debug("Entering case ID: {}", caseId);
        elementUtils.sendKeys(caseIdBox, caseId);
        return this;
    }
    
    /**
     * Enter case ID with timestamp
     */
    @Step("Enter case ID with timestamp")
    public NewRequestPage enterCaseIdWithTimestamp() {
        String caseId = LocalDateTime.now().toString();
        logger.debug("Entering case ID with timestamp: {}", caseId);
        elementUtils.sendKeys(caseIdBox, caseId);
        return this;
    }
    
    /**
     * Select certificate type
     */
    @Step("Select certificate type: {type}")
    public NewRequestPage selectCertType(String type) {
        logger.debug("Selecting certificate type: {}", type);
        elementUtils.selectByValue(certType, type);
        return this;
    }
    
    /**
     * Select country
     */
    @Step("Select country: {country}")
    public NewRequestPage selectCountry(String countryValue) {
        logger.debug("Selecting country: {}", countryValue);
        elementUtils.selectByValue(this.country, countryValue);
        return this;
    }
    
    /**
     * Select verification type
     */
    @Step("Select verification type: {type}")
    public NewRequestPage selectVerificationType(String type) {
        logger.debug("Selecting verification type: {}", type);
        elementUtils.selectByValue(mode, type);
        return this;
    }
    
    /**
     * Select environment
     */
    @Step("Select environment: {env}")
    public NewRequestPage selectEnvironment(String env) {
        logger.debug("Selecting environment: {}", env);
        elementUtils.selectByValue(environment, env);
        return this;
    }
    
    /**
     * Click submit button
     */
    @Step("Submit request form")
    public void submit() {
        logger.debug("Clicking submit button");
        elementUtils.click(formSubmitBtn);
        wait.waitForPageLoad();
    }
    
    /**
     * Get request URL
     */
    @Step("Get request URL")
    public String getRequestUrl() {
        logger.debug("Getting request URL");
        wait.waitForElementVisibility(requestUrlCopy);
        String url = elementUtils.getAttribute(requestUrlCopy, "value");
        
        if (url == null || url.isEmpty()) {
            throw new RuntimeException("Request URL is null or empty");
        }
        
        logger.info("Request URL obtained: {}", url);
        return url;
    }
    
    /**
     * Click ok button on Form
     */
    @Step("Submit request form")
    public void ModalClose() {
        logger.debug("Clicking ok button on Form");
        elementUtils.click(OkButton);
        wait.waitForPageLoad();
    }
    
    /**
     * Fill manual request form
     */
    @Step("Fill manual request form for: {name}, {email}")
    public void fillManualRequestForm(String name, String email) {
        logger.info("Filling manual request form");
        navigateToNewRequest();
        enterRecipientName(name);
        enterEmail(email);
        enterCaseIdWithTimestamp();
        selectCertType("customer");
        selectCountry("1");
        selectVerificationType("false");
        selectEnvironment("sandbox");
        submit();
        logger.info("Manual request form submitted successfully");
    }
    
    /**
     * Fill corporate request form
     */
    @Step("Fill corporate request form for: {name}, {email}")
    public void fillCorporateRequestForm(String name, String email) {
        logger.info("Filling corporate request form");
        navigateToNewRequest();
        enterRecipientName(name);
        enterEmail(email);
        enterCaseIdWithTimestamp();
        selectCertType("corporate");
        elementUtils.selectByValue(corporateCountry, "1");
        selectVerificationType("false");
        selectEnvironment("sandbox");
        submit();
        logger.info("Corporate request form submitted successfully");
    }
}
