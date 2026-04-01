package com.vista.pages;

import com.vista.framework.utils.ElementUtils;
import com.vista.framework.wait.WaitStrategy;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

/**
 * API Settings Page Object Model.
 * Handles all interactions with API settings and secret management.
 */
public class ApiSettingsPage {
    
    private static final Logger logger = LogManager.getLogger(ApiSettingsPage.class);
    
    private final WebDriver driver;
    private final ElementUtils elementUtils;
    private final WaitStrategy wait;
    
    // Locators
    private final By settingsMenu = By.xpath("//a[normalize-space()='Settings']");
    private final By apiSettingsMenu = By.xpath("//a[normalize-space()='Api settings']");
    private final By regenerateSecretBtn = By.xpath("//a[normalize-space()='Regenerate Client Secret']");
    private final By confirmBtn = By.xpath("//button[normalize-space()='Confirm']");
    private final By clientSecret = By.xpath("(//div[@class='client-id-serect']//li/label)[4]");
    
    // API Configuration
    private final String apiBaseUrl = "https://vista.kreditz-dev.com";
    private final String tokenEndpoint = "/kreditz/api/v3/authorizations/access_token";
    private final String clientId = "c71f1c07cb4b46f074c2592d1c7761";
    
    public ApiSettingsPage(WebDriver driver, WaitStrategy wait) {
        this.driver = driver;
        this.wait = wait;
        this.elementUtils = new ElementUtils(driver, wait);
    }
    
    /**
     * Click Settings menu
     */
    @Step("Navigate to Settings")
    public ApiSettingsPage navigateToSettings() {
        logger.debug("Clicking Settings menu");
        elementUtils.click(settingsMenu);
        return this;
    }
    
    /**
     * Click API Settings menu
     */
    @Step("Navigate to API Settings")
    public ApiSettingsPage navigateToApiSettings() {
        logger.debug("Clicking API Settings menu");
        elementUtils.click(apiSettingsMenu);
        return this;
    }
    
    /**
     * Hover over regenerate secret button
     */
    @Step("Hover over regenerate secret button")
    public ApiSettingsPage hoverOverRegenerateSecret() {
        logger.debug("Hovering over regenerate secret button");
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(regenerateSecretBtn)).perform();
        return this;
    }
    
    /**
     * Click regenerate secret button
     */
    @Step("Click regenerate secret button")
    public ApiSettingsPage clickRegenerateSecret() {
        logger.debug("Clicking regenerate secret button");
        elementUtils.click(regenerateSecretBtn);
        return this;
    }
    
    /**
     * Click confirm button
     */
    @Step("Click confirm button")
    public void clickConfirm() {
        logger.debug("Clicking confirm button");
        elementUtils.click(confirmBtn);
        wait.waitForElementVisibility(confirmBtn);
    }
    
    /**
     * Get client secret value
     */
    @Step("Get client secret")
    public String getClientSecret() {
        logger.debug("Getting client secret");
        return elementUtils.getText(clientSecret);
    }
    
    /**
     * Regenerate and get new client secret
     */
    @Step("Regenerate and get client secret")
    public String regenerateAndGetSecret() {
        logger.info("Regenerating client secret");
        navigateToSettings();
        navigateToApiSettings();
        hoverOverRegenerateSecret();
        clickRegenerateSecret();
        clickConfirm();
        String secret = getClientSecret();
        logger.info("Client secret regenerated successfully");
        return secret;
    }
    
    /**
     * Get OAuth2 access token using client secret
     */
    @Step("Get OAuth2 access token")
    public String getOAuth2Token(String clientSecret) {
        logger.info("Requesting OAuth2 access token");
        
        Response response = RestAssured.given()
                .contentType(io.restassured.http.ContentType.URLENC)
                .formParam("client_id", clientId)
                .formParam("client_secret", clientSecret)
                .when()
                .post(apiBaseUrl + tokenEndpoint)
                .then()
                .log().ifValidationFails()
                .extract()
                .response();
        
        JsonPath jsonPath = response.jsonPath();
        String accessToken = jsonPath.getString("data.access_token");
        
        logger.info("OAuth2 access token obtained successfully");
        return accessToken;
    }
    
    /**
     * Get full API access flow (regenerate secret and get token)
     */
    @Step("Get API access token (full flow)")
    public String getApiAccessToken() {
        logger.info("Getting API access token (full flow)");
        String secret = regenerateAndGetSecret();
        return getOAuth2Token(secret);
    }
}
