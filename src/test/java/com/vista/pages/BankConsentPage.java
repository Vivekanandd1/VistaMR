package com.vista.pages;

import com.vista.framework.utils.ElementUtils;
import com.vista.framework.wait.WaitStrategy;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * Bank Consent Page Object Model.
 * Handles all interactions with the bank consent/redirect page.
 */
public class BankConsentPage {
    
    private static final Logger logger = LogManager.getLogger(BankConsentPage.class);
    
    private final WebDriver driver;
    private final ElementUtils elementUtils;
    private final WaitStrategy wait;
    
    // Locators
    private final By nordea = By.xpath("//a[normalize-space()='Nordea']");
    private final By handelsbanken = By.xpath("//a[normalize-space()='Handelsbanken']");
    private final By nextBtn = By.id("next-btn");
    private final By ssnField = By.id("ssn");
    private final By submitBtn = By.id("submit");
    private final By orgNumber = By.xpath("(//input[@id='ssn'])[2]");
    private final By continueBtn = By.id("submit");
    private final By successHeader = By.xpath("//div/h1[text()='Success!']");
    
    public BankConsentPage(WebDriver driver, WaitStrategy wait) {
        this.driver = driver;
        this.wait = wait;
        this.elementUtils = new ElementUtils(driver, wait);
    }
    
    /**
     * Scroll to bank selection
     */
    @Step("Scroll to bank selection")
    public BankConsentPage scrollToBankSelection() {
        logger.debug("Scrolling to bank selection");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,950)");
        wait.waitForPageLoad();
        return this;
    }
    
    /**
     * Select Nordea bank
     */
    @Step("Select Nordea bank")
    public BankConsentPage selectNordea() {
        logger.debug("Selecting Nordea bank");
        elementUtils.click(nordea);
        return this;
    }
    
    /**
     * Select Handelsbanken
     */
    @Step("Select Handelsbanken")
    public BankConsentPage selectHandelsbanken() {
        logger.debug("Selecting Handelsbanken");
        elementUtils.click(handelsbanken);
        return this;
    }
    
    /**
     * Click next button
     */
    @Step("Click next button")
    public BankConsentPage clickNext() {
        logger.debug("Clicking next button");
        elementUtils.click(nextBtn);
        return this;
    }
    
    /**
     * Enter SSN/Personnummer
     */
    @Step("Enter SSN: {ssn}")
    public BankConsentPage enterSsn(String ssn) {
        logger.debug("Entering SSN");
        elementUtils.sendKeys(ssnField, ssn);
        return this;
    }
    
    /**
     * Enter test SSN
     */
    @Step("Enter test SSN")
    public BankConsentPage enterTestSsn() {
        logger.debug("Entering test SSN");
        elementUtils.sendKeys(ssnField, "201212121214");
        return this;
    }
    
    /**
     * Enter organization number
     */
    @Step("Enter organization number: {orgNumber}")
    public BankConsentPage enterOrgNumber(String orgNumber) {
        logger.debug("Entering organization number: {}", orgNumber);
        elementUtils.sendKeys(this.orgNumber, orgNumber);
        return this;
    }
    
    /**
     * Click submit/continue button
     */
    @Step("Click submit button")
    public void clickSubmit() {
        logger.debug("Clicking submit button");
        elementUtils.click(submitBtn);
        wait.waitForPageLoad();
    }
    
    /**
     * Perform manual consent flow
     */
    @Step("Complete manual consent flow")
    public void completeManualConsent() {
        logger.info("Completing manual consent flow");
        scrollToBankSelection();
        selectHandelsbanken();
        clickNext();
        enterTestSsn();
        clickSubmit();
        logger.info("Manual consent flow completed");
    }
    
    /**
     * Perform corporate consent flow
     */
    @Step("Complete corporate consent flow")
    public void completeCorporateConsent() {
        logger.info("Completing corporate consent flow");
        scrollToBankSelection();
        selectHandelsbanken();
        clickNext();
        enterTestSsn();
        enterOrgNumber("8899336624");
        clickSubmit();
        logger.info("Corporate consent flow completed");
    }
    
    /**
     * Check if success message is displayed
     */
    @Step("Verify success message")
    public boolean isSuccessDisplayed() {
        return elementUtils.isDisplayed(successHeader);
    }
    
    /**
     * Wait for success message
     */
    @Step("Wait for success message")
    public void waitForSuccess() {
        logger.debug("Waiting for success message");
        wait.waitForElementVisibility(successHeader);
    }
}
