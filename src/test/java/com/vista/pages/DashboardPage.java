package com.vista.pages;

import com.vista.framework.utils.ElementUtils;
import com.vista.framework.wait.WaitStrategy;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Dashboard Page Object Model.
 * Handles all interactions with the dashboard page.
 */
public class DashboardPage {
    
    private static final Logger logger = LogManager.getLogger(DashboardPage.class);
    
    private final WebDriver driver;
    private final ElementUtils elementUtils;
    private final WaitStrategy wait;
    
    // Locators
    private final By requestLogHeader = By.xpath("//div/h3[text()='Request Log']");
    private final By spinner = By.id("globalSpinner");
    private final By profile = By.xpath("//div[contains(@class,'dropdown-username-profile')]");
    private final By logoutBtn = By.xpath("//a[normalize-space()='Sign out']");
    private final By pageTitle = By.xpath("//title[text()='Kreditz | Vista']");
    private final By searchBox = By.id("search_request");
    private final By filterBtn = By.className("filter-dropdown");
    
    public DashboardPage(WebDriver driver, WaitStrategy wait) {
        this.driver = driver;
        this.wait = wait;
        this.elementUtils = new ElementUtils(driver, wait);
    }
    
    /**
     * Check if dashboard is loaded
     */
    @Step("Verify spinner is gone")
    public boolean isSpinnerDisappeared() {
        return elementUtils.isDisappeared(spinner);
    }
    
    /**
     * Click on profile dropdown
     */
    @Step("Click profile dropdown")
    public void clickProfile() {
        logger.debug("Clicking profile dropdown");
        elementUtils.click(profile);
    }
    
    /**
     * Click logout button
     */
    @Step("Click logout button")
    public void clickLogout() {
        logger.debug("Clicking logout button");
        elementUtils.click(logoutBtn);
    }
    
    /**
     * Perform logout
     */
    @Step("Logout from application")
    public void logout() {
        logger.info("Performing logout");
        isSpinnerDisappeared();
        clickProfile();
        clickLogout();
        wait.waitForPageLoad();
        logger.info("Logout completed successfully");
    }
    
    /**
     * Search for certificate
     */
    @Step("Search for certificate: {certificateNumber}")
    public DashboardPage searchCertificate(String certificateNumber) {
        logger.debug("Searching for certificate: {}", certificateNumber);
        elementUtils.sendKeys(searchBox, certificateNumber);
        wait.waitForPageLoad();
        return this;
    }
    
    /**
     * Click filter button
     */
    @Step("Click filter button")
    public void clickFilter() {
        logger.debug("Clicking filter button");
        elementUtils.click(filterBtn);
    }
    
    /**
     * Get page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }
    
    /**
     * Get current URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

	public boolean isDashboardLoaded() {
		return elementUtils.isDisplayed(requestLogHeader);
	}
}
