package com.vista.pages;

import com.vista.framework.utils.ElementUtils;
import com.vista.framework.wait.WaitStrategy;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

/**
 * Certificate Logs Page Object Model. Handles all interactions with certificate
 * logs and search functionality.
 */
public class CertificateLogsPage {

	private static final Logger logger = LogManager.getLogger(CertificateLogsPage.class);

	private final WebDriver driver;
	private final ElementUtils elementUtils;
	private final WaitStrategy wait;

	// Locators
	private final By searchBox = By.xpath("(//input[@placeholder='Search'])[1]");
	private final By alert = By.xpath("//div[@class='alert-message']");
	private final By spinner = By.id("globalSpinner");
	private final By dwnldBTN = By.cssSelector(".download-csv-btn-container");
	private final By certificateResult = By.xpath("//tbody/tr/td[3]");
	private final By firstResultRow = By.xpath("//tbody/tr[1]/td[3]");
	private final By noRecordsMessage = By.xpath("//*[@id='rebranding-data-container']//strong");
	private final By filterBtn = By.className("filter-dropdown");
	private final By sourceFilter = By.xpath("(//div[@class='custom-select-trigger ellipsis'])[1]");
	private final By sourceOptions = By.xpath("//span[contains(@class,'custom-option')]");
	private final By applyBtn = By.xpath("//button[text()='Apply']");
	private final By clearFilter = By.className("clear-filter");
	private final By requestTypes = By.xpath("(//div[contains(@class,'custom-select-trigger')])[2]");
	private final By marketFilter = By.xpath("//div[contains(@class,'custom-select-market')]");

	// Filter values
	private String requestTypeFilter = "iframe";
	private String certTypeFilter = "consumer";
	private String marketFilterValue = "sweden";

	public CertificateLogsPage(WebDriver driver, WaitStrategy wait) {
		this.driver = driver;
		this.wait = wait;
		this.elementUtils = new ElementUtils(driver, wait);
	}

	/**
	 * Set request type filter value
	 */
	public CertificateLogsPage setRequestTypeFilter(String requestType) {
		this.requestTypeFilter = requestType;
		return this;
	}

	/**
	 * Set certificate type filter value
	 */
	public CertificateLogsPage setCertTypeFilter(String certType) {
		this.certTypeFilter = certType;
		return this;
	}

	/**
	 * Set market filter value
	 */
	public CertificateLogsPage setMarketFilter(String market) {
		this.marketFilterValue = market;
		return this;
	}
	

	@Step("Search for certificate: {certificateNumber}")
	public CertificateLogsPage searchCertificate(String certificateNumber) {
		logger.info("Searching for certificate: {}", certificateNumber);

		// Wait for any alert to disappear before searching
		try {
			wait.waitForElementInvisibility(alert);
		} catch (Exception e) {
			logger.debug("No alert message present");
		}

		// Enter certificate number and press ENTER
		WebElement searchInput = wait.waitForElementVisibility(searchBox);
		searchInput.clear();
		searchInput.sendKeys(certificateNumber, Keys.ENTER);

		// Wait for table to refresh (short hard wait for AJAX to complete)
		wait.hardWait(2000);

		// Wait for either result or no records message
		By foundElement = null;
		try {
			foundElement = wait.waitForEitherElement(dwnldBTN, noRecordsMessage);
		} catch (Exception e) {
			logger.error("Search result timeout - neither download button nor no-records message appeared");
		}

		if (foundElement != null && foundElement.equals(dwnldBTN)) {
			// Certificate found - get the first result row
			WebElement result = wait.waitForElementVisibility(firstResultRow);
			String actual = result.getText().trim();
			
			if (certificateNumber.equals(actual)) {
				logger.info("Certificate found in system: {} (matched)", actual);
			} else {
				logger.warn("Certificate mismatch - searched: {}, found: {}", certificateNumber, actual);
			}
		} else if (foundElement != null && foundElement.equals(noRecordsMessage)) {
			// No records found
			WebElement noRecordMsg = wait.waitForElementVisibility(noRecordsMessage);
			logger.warn("Certificate not found: {}", noRecordMsg.getText().trim());
		} else {
			// Timeout occurred - neither element found
			logger.error("Search result timeout - no response from system");
		}

		// Clear search box for next operation (re-find element to avoid stale reference)
		driver.findElement(searchBox).clear();
		logger.info("Certificate search completed successfully");

		return this;
	}
	
	@Step("Search for certificate: {certificateNumber}")
	public CertificateLogsPage searchwithInvalidCertificate(String certificateNumber) {
		logger.info("Searching for certificate: {}", certificateNumber);

		// Wait for any alert to disappear before searching
		try {
			wait.waitForElementInvisibility(alert);
		} catch (Exception e) {
			logger.debug("No alert message present");
		}

		// Enter certificate number and press ENTER
		WebElement searchInput = wait.waitForElementVisibility(searchBox);
		searchInput.clear();
		searchInput.sendKeys(certificateNumber, Keys.ENTER);

		// Wait for table to refresh (short hard wait for AJAX to complete)
		wait.hardWait(3000);

		String text = driver.findElement(noRecordsMessage).getText();
		
		logger.info("Certificate is not present: " +certificateNumber +" " + text);
		

		// Clear search box for next operation (re-find element to avoid stale reference)
		driver.findElement(searchBox).clear();
		logger.info("Certificate search completed successfully");

		return this;
	}

	/**
	 * Validates the certificate number is not null or empty.
	 *
	 * @param certificateNumber the certificate number to validate
	 * @throws IllegalArgumentException if null or empty
	 */
	private void validateCertificateNumber(String certificateNumber) {
		if (certificateNumber == null || certificateNumber.trim().isEmpty()) {
			throw new IllegalArgumentException("Certificate number cannot be null or empty");
		}
	}

	/**
	 * Safely extracts the certificate number from the result row.
	 *
	 * @return the certificate number text, trimmed
	 * @throws AssertionError if the result element is not accessible
	 */
	private String getCertificateNumberFromResult() {
		try {
			WebElement resultElement = wait.waitForElementVisibility(certificateResult);
			String text = resultElement.getText();
			return (text != null) ? text.trim() : "";
		} catch (Exception e) {
			logger.error("Failed to extract certificate number from result", e);
			throw new AssertionError("Unable to read certificate number from search results", e);
		}
	}

	/**
	 * Clears the search box input field.
	 */
	private void clearSearchBox() {
		try {
			WebElement searchInput = wait.waitForElementVisibility(searchBox);
			searchInput.clear();
		} catch (Exception e) {
			logger.warn("Failed to clear search box: {}", e.getMessage());
		}
	}

	/**
	 * Click filter button
	 */
	@Step("Click filter button")
	private void clickFilter() {
		logger.debug("Clicking filter button");
		elementUtils.click(filterBtn);
	}

	/**
	 * Select filter option by text
	 */
	private void selectFilterOption(By filterLocator, String optionText) {
		logger.debug("Selecting filter option: {}", optionText);
		elementUtils.click(filterLocator);

		List<WebElement> options = driver.findElements(sourceOptions);
		for (WebElement option : options) {
			if (option.getText().equalsIgnoreCase(optionText)) {
				option.click();
				logger.debug("Selected option: {}", optionText);
				break;
			}
		}

		elementUtils.click(applyBtn);
		wait.waitForElementInvisibility(spinner);
	}

	/**
	 * Apply source filter
	 */
	@Step("Apply source filter: {requestType}")
	public CertificateLogsPage applySourceFilter() {
		logger.info("Applying source filter: {}", requestTypeFilter);
		clickFilter();
		selectFilterOption(sourceFilter, requestTypeFilter);
		return this;
	}

	/**
	 * Apply request type filter
	 */
	@Step("Apply request type filter: {certType}")
	public CertificateLogsPage applyRequestTypeFilter() {
		logger.info("Applying request type filter: {}", certTypeFilter);

		// Clear existing filters
		clearFilters();

		clickFilter();
		selectFilterOption(requestTypes, certTypeFilter);
		return this;
	}

	/**
	 * Apply market filter
	 */
	@Step("Apply market filter: {market}")
	public CertificateLogsPage applyMarketFilter() {
		logger.info("Applying market filter: {}", marketFilterValue);

		// Clear existing filters
		clearFilters();

		clickFilter();
		selectFilterOption(marketFilter, marketFilterValue);
		return this;
	}

	/**
	 * Clear all filters
	 */
	@Step("Clear all filters")
	public CertificateLogsPage clearFilters() {
		logger.debug("Clearing all filters");
		wait.waitForElementVisibility(clearFilter);
		elementUtils.click(clearFilter);
		wait.waitForElementInvisibility(spinner);
		return this;
	}

	/**
	 * Perform all filter operations (for dashboard test)
	 */
	@Step("Apply all filters")
	public void applyAllFilters() {
		logger.info("Applying all filters");
		applySourceFilter();
		applyRequestTypeFilter();
		applyMarketFilter();
		logger.info("All filters applied successfully");
	}
}
