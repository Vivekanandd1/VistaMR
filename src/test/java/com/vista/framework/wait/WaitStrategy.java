package com.vista.framework.wait;

import com.vista.framework.config.ConfigKeys;
import com.vista.framework.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.function.Function;

/**
 * Centralized Wait Strategy with fluent waiting and retry mechanisms.
 * Replaces all Thread.sleep() calls with intelligent waits.
 */
public class WaitStrategy {
    
    private static final Logger logger = LogManager.getLogger(WaitStrategy.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    private final WebDriver driver;
    private final WebDriverWait explicitWait;
    private final FluentWait<WebDriver> fluentWait;
    
    public WaitStrategy(WebDriver driver) {
        this.driver = driver;
        int explicitWaitTimeout = config.getInt(ConfigKeys.EXPLICIT_WAIT, 5);
        int pollInterval = 2;
        
        this.explicitWait = new WebDriverWait(driver, Duration.ofMinutes(explicitWaitTimeout));
        
        this.fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(explicitWaitTimeout))
                .pollingEvery(Duration.ofSeconds(pollInterval))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
        
        logger.debug("WaitStrategy initialized with timeout: {}s", explicitWaitTimeout);
    }
    
    /**
     * Wait for element to be clickable
     */
    public WebElement waitForElementToBeClickable(By locator) {
        logger.debug("Waiting for element to be clickable: {}", locator);
        return explicitWait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    /**
     * Wait for element to be visible
     */
    public WebElement waitForElementVisibility(By locator) {
        logger.debug("Waiting for element visibility: {}", locator);
        return explicitWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    /**
     * Wait for element to be present in DOM
     */
    public WebElement waitForElementPresence(By locator) {
        logger.debug("Waiting for element presence: {}", locator);
        return explicitWait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    
    /**
     * Wait for element to be invisible
     */
    public boolean waitForElementInvisibility(By locator) {
        logger.debug("Waiting for element invisibility: {}", locator);
        return explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
    
    /**
     * Wait for title to contain specific text
     */
    public boolean waitForTitleContains(String title) {
        logger.debug("Waiting for title to contain: {}", title);
        return explicitWait.until(ExpectedConditions.titleContains(title));
    }
    
    /**
     * Wait for title to be exact match
     */
    public boolean waitForTitleIs(String title) {
        logger.debug("Waiting for title to be: {}", title);
        return explicitWait.until(ExpectedConditions.titleIs(title));
    }
    
    /**
     * Wait for URL to contain specific text
     */
    public boolean waitForUrlContains(String url) {
        logger.debug("Waiting for URL to contain: {}", url);
        return explicitWait.until(ExpectedConditions.urlContains(url));
    }
    
    /**
     * Wait for page to be fully loaded
     */
    public void waitForPageLoad() {
        logger.debug("Waiting for page load to complete");
        explicitWait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
        logger.debug("Page load completed");
    }
    
    /**
     * Wait for AJAX calls to complete
     */
    public void waitForAjaxToComplete() {
        logger.debug("Waiting for AJAX to complete");
        explicitWait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return jQuery.active == 0").equals(true));
        logger.debug("AJAX completed");
    }
    
    /**
     * Wait with fluent wait using custom condition
     */
    public <T> T waitForCondition(Function<WebDriver, T> condition) {
        logger.debug("Waiting for custom condition");
        return fluentWait.until(condition);
    }
    
    /**
     * Wait for multiple elements to be present
     */
    public java.util.List<WebElement> waitForElementsPresence(By locator) {
        logger.debug("Waiting for multiple elements presence: {}", locator);
        return explicitWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }
    
    /**
     * Wait for alert to be present
     */
    public Alert waitForAlert() {
        logger.debug("Waiting for alert");
        return explicitWait.until(ExpectedConditions.alertIsPresent());
    }
    
    /**
     * Wait for frame to be available
     */
    public void waitForFrame(By locator) {
        logger.debug("Waiting for frame: {}", locator);
        explicitWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
    }
    
    /**
     * Wait for frame to be available by index
     */
    public void waitForFrame(int index) {
        logger.debug("Waiting for frame at index: {}", index);
        explicitWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(index));
    }
    
    /**
     * Smart wait that handles stale element exceptions
     */
    public WebElement smartWait(By locator, int maxRetries) {
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                return waitForElementToBeClickable(locator);
            } catch (StaleElementReferenceException e) {
                retryCount++;
                logger.warn("Stale element detected, retry {}/{}", retryCount, maxRetries);
                if (retryCount >= maxRetries) {
                    throw e;
                }
            }
        }
        return null;
    }
    
    public void waitForSpinnerToDisappear(By spinnerLocator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMinutes(1));

        wait.until(driver -> {
            JavascriptExecutor js = (JavascriptExecutor) driver;

            List<WebElement> elements = driver.findElements(spinnerLocator);

            // If spinner not present → good
            if (elements.isEmpty()) {
                return true;
            }

            WebElement spinner = elements.get(0);

            // Use JS to check real visibility
            Boolean isHidden = (Boolean) js.executeScript(
                "var elem = arguments[0];" +
                "if (!elem) return true;" +
                "var style = window.getComputedStyle(elem);" +
                "return (style.display === 'none' || " +
                       "style.visibility === 'hidden' || " +
                       "style.opacity === '0');",
                spinner
            );

            return isHidden;
        });
    }
    /**
     * Hard wait - use only when absolutely necessary
     */
    public void hardWait(int seconds) {
        try {
            logger.trace("Hard wait for {}sc", seconds);
            Thread.sleep(seconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Hard wait interrupted", e);
        }
    }
    
    /**
     * Wait for new window/tab to open
     */
    public void waitForNewWindow(int expectedWindowCount) {
        logger.debug("Waiting for window count to be: {}", expectedWindowCount);
        explicitWait.until(ExpectedConditions.numberOfWindowsToBe(expectedWindowCount));
    }
    
    /**
     * Wait for element text to contain specific value
     */
    public boolean waitForElementTextContains(By locator, String text) {
        logger.debug("Waiting for element text to contain: {}", text);
        return explicitWait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }
    
    /**
     * Wait for element attribute to have specific value
     */
    public boolean waitForElementAttribute(By locator, String attribute, String value) {
        logger.debug("Waiting for element attribute {} to be: {}", attribute, value);
        return explicitWait.until(ExpectedConditions.attributeToBe(locator, attribute, value));
    }
    
    /**
     * Wait for element to be selected
     */
    public boolean waitForElementSelected(By locator) {
        logger.debug("Waiting for element to be selected: {}", locator);
        return explicitWait.until(ExpectedConditions.elementToBeSelected(locator));
    }
    
    /**
     * Wait for element to be deselected
     */
    public boolean waitForElementNotSelected(By locator) {
        logger.debug("Waiting for element to be deselected: {}", locator);
        return explicitWait.until(ExpectedConditions.not(ExpectedConditions.elementToBeSelected(locator)));
    }

	/**
	 * Wait for either of two elements to be visible.
	 *
	 * @param locator1 first element locator
	 * @param locator2 second element locator
	 * @return true if either element is visible, false if timeout occurs
	 */
	public Boolean waitForElementVisibility(By locator1, By locator2) {
		logger.debug("Waiting for either element to be visible: {} or {}", locator1, locator2);
		try {
			return explicitWait.until(ExpectedConditions.or(
				ExpectedConditions.visibilityOfElementLocated(locator1),
				ExpectedConditions.visibilityOfElementLocated(locator2)
			));
		} catch (Exception e) {
			logger.debug("Neither element became visible within timeout");
			return false;
		}
	}

	/**
	 * Wait for either of two elements to be present and displayed.
	 * Returns which locator was found, or null if neither appeared.
	 *
	 * @param locator1 first element locator
	 * @param locator2 second element locator
	 * @return the locator that was found, or null if timeout occurs
	 */
	public By waitForEitherElement(By locator1, By locator2) {
		logger.debug("Waiting for either element: {} or {}", locator1, locator2);

		int timeoutSeconds = config.getInt("SEARCH_RESULT_TIMEOUT", 10);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));

		return wait.until(driver -> {
			try {
				if (!driver.findElements(locator1).isEmpty()) {
					WebElement el1 = driver.findElement(locator1);
					if (el1.isDisplayed()) {
						logger.debug("Element found: {}", locator1);
						return locator1;
					}
				}

				if (!driver.findElements(locator2).isEmpty()) {
					WebElement el2 = driver.findElement(locator2);
					if (el2.isDisplayed()) {
						logger.debug("Element found: {}", locator2);
						return locator2;
					}
				}

			} catch (StaleElementReferenceException e) {
				logger.trace("Stale element detected, retrying...");
			} catch (Exception e) {
				logger.trace("Waiting for element: {}", e.getMessage());
			}

			return null;
		});
	}

	public void waitEitherElement(By Locator1, By Locator2) {
		WebDriverWait wait= new WebDriverWait(driver, Duration.ofMinutes(2));
		wait.until(driver ->		
        driver.findElements(Locator1).size() > 0 ||
        driver.findElements(Locator2).size() > 0
        );
		
	}

}
