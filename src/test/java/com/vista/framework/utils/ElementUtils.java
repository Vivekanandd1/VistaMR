package com.vista.framework.utils;

import com.vista.framework.wait.WaitStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for common WebElement interactions.
 * Provides safe and reusable element operations.
 */
public class ElementUtils {
    
    private static final Logger logger = LogManager.getLogger(ElementUtils.class);
    
    private final WebDriver driver;
    private final WaitStrategy waitStrategy;
    private final Actions actions;
    
    public ElementUtils(WebDriver driver, WaitStrategy waitStrategy) {
        this.driver = driver;
        this.waitStrategy = waitStrategy;
        this.actions = new Actions(driver);
    }
    
    /**
     * Safe click with wait and exception handling
     */
    public void click(By locator) {
        try {
            WebElement element = waitStrategy.waitForElementToBeClickable(locator);
            scrollToElement(element);
            element.click();
            logger.debug("Clicked on element: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to click element: {}", locator, e);
            throw new ElementInteractionException("Failed to click element: " + locator, e);
        }
    }
    
    /**
     * Safe click with JavaScript fallback
     */
    public void clickWithJSFallback(By locator) {
        try {
            click(locator);
        } catch (Exception e) {
            logger.warn("Standard click failed, using JavaScript fallback for: {}", locator);
            WebElement element = waitStrategy.waitForElementPresence(locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }
    
    /**
     * Send keys to element
     */
    public void sendKeys(By locator, String text) {
        try {
            WebElement element = waitStrategy.waitForElementToBeClickable(locator);
            element.clear();
            element.sendKeys(text);
            logger.debug("Sent keys to element: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to send keys to element: {}", locator, e);
            throw new ElementInteractionException("Failed to send keys to element: " + locator, e);
        }
    }

    /**
     * Send keys to element (without clearing, supports special keys)
     */
    public void sendKeys(By locator, CharSequence... keys) {
        try {
            WebElement element = waitStrategy.waitForElementToBeClickable(locator);
            element.sendKeys(keys);
            logger.debug("Sent keys to element: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to send keys to element: {}", locator, e);
            throw new ElementInteractionException("Failed to send keys to element: " + locator, e);
        }
    }
    
    /**
     * Append keys to element (without clearing)
     */
    public void appendKeys(By locator, String text) {
        try {
            WebElement element = waitStrategy.waitForElementPresence(locator);
            element.sendKeys(text);
            logger.debug("Appended keys to element: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to append keys to element: {}", locator, e);
            throw new ElementInteractionException("Failed to append keys to element: " + locator, e);
        }
    }
    
    /**
     * Get text from element
     */
    public String getText(By locator) {
        try {
            WebElement element = waitStrategy.waitForElementVisibility(locator);
            String text = element.getText();
            logger.debug("Got text from element {}: {}", locator, text);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get text from element: {}", locator, e);
            throw new ElementInteractionException("Failed to get text from element: " + locator, e);
        }
    }
    
    /**
     * Get attribute value from element
     */
    public String getAttribute(By locator, String attribute) {
        try {
            WebElement element = waitStrategy.waitForElementPresence(locator);
            String value = element.getAttribute(attribute);
            logger.debug("Got attribute '{}' from element {}: {}", attribute, locator, value);
            return value;
        } catch (Exception e) {
            logger.error("Failed to get attribute from element: {}", locator, e);
            throw new ElementInteractionException("Failed to get attribute from element: " + locator, e);
        }
    }
    
    /**
     * Check if element is displayed
     */
    public boolean isDisplayed(By locator) {
        try {
            WebElement element = waitStrategy.waitForElementVisibility(locator);
            boolean displayed = element.isDisplayed();
            logger.debug("Element {} is displayed: {}", locator, displayed);
            return displayed;
        } catch (Exception e) {
            logger.debug("Element {} is not displayed", locator);
            return false;
        }
    }
    
    /**
     * Check if element exists in DOM
     */
    public boolean exists(By locator) {
        try {
            driver.findElement(locator);
            logger.debug("Element {} exists", locator);
            return true;
        } catch (NoSuchElementException e) {
            logger.debug("Element {} does not exist", locator);
            return false;
        }
    }
    
    /**
     * Select from dropdown by value
     */
    public void selectByValue(By locator, String value) {
        try {
            WebElement element = waitStrategy.waitForElementToBeClickable(locator);
            Select select = new Select(element);
            select.selectByValue(value);
            logger.debug("Selected value '{}' from dropdown: {}", value, locator);
        } catch (Exception e) {
            logger.error("Failed to select value from dropdown: {}", locator, e);
            throw new ElementInteractionException("Failed to select value from dropdown: " + locator, e);
        }
    }
    
    /**
     * Select from dropdown by visible text
     */
    public void selectByVisibleText(By locator, String text) {
        try {
            WebElement element = waitStrategy.waitForElementToBeClickable(locator);
            Select select = new Select(element);
            select.selectByVisibleText(text);
            logger.debug("Selected text '{}' from dropdown: {}", text, locator);
        } catch (Exception e) {
            logger.error("Failed to select text from dropdown: {}", locator, e);
            throw new ElementInteractionException("Failed to select text from dropdown: " + locator, e);
        }
    }
    
    /**
     * Select from dropdown by visible text
     */

    
    /**
     * Select from dropdown by index
     */
    public void selectByIndex(By locator, int index) {
        try {
            WebElement element = waitStrategy.waitForElementToBeClickable(locator);
            Select select = new Select(element);
            select.selectByIndex(index);
            logger.debug("Selected index '{}' from dropdown: {}", index, locator);
        } catch (Exception e) {
            logger.error("Failed to select index from dropdown: {}", locator, e);
            throw new ElementInteractionException("Failed to select index from dropdown: " + locator, e);
        }
    }
    
    /**
     * Get selected value from dropdown
     */
    public String getSelectedValue(By locator) {
        try {
            WebElement element = waitStrategy.waitForElementPresence(locator);
            Select select = new Select(element);
            String value = select.getFirstSelectedOption().getAttribute("value");
            logger.debug("Selected value from dropdown {}: {}", locator, value);
            return value;
        } catch (Exception e) {
            logger.error("Failed to get selected value from dropdown: {}", locator, e);
            throw new ElementInteractionException("Failed to get selected value from dropdown: " + locator, e);
        }
    }
    
    /**
     * Hover over element
     */
    public void hoverOver(By locator) {
        try {
            WebElement element = waitStrategy.waitForElementVisibility(locator);
            actions.moveToElement(element).perform();
            logger.debug("Hovered over element: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to hover over element: {}", locator, e);
            throw new ElementInteractionException("Failed to hover over element: " + locator, e);
        }
    }
    
    /**
     * Double click on element
     */
    public void doubleClick(By locator) {
        try {
            WebElement element = waitStrategy.waitForElementToBeClickable(locator);
            actions.doubleClick(element).perform();
            logger.debug("Double clicked on element: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to double click element: {}", locator, e);
            throw new ElementInteractionException("Failed to double click element: " + locator, e);
        }
    }
    
    public void mouseClick(By locator) {
        try {
            WebElement element = waitStrategy.waitForElementToBeClickable(locator);
            actions.moveToElement(element).build().perform();
            logger.debug("Double clicked on element: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to double click element: {}", locator, e);
            throw new ElementInteractionException("Failed to double click element: " + locator, e);
        }
    }
    
    /**
     * Right click on element
     */
    public void rightClick(By locator) {
        try {
            WebElement element = waitStrategy.waitForElementToBeClickable(locator);
            actions.contextClick(element).perform();
            logger.debug("Right clicked on element: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to right click element: {}", locator, e);
            throw new ElementInteractionException("Failed to right click element: " + locator, e);
        }
    }
    
    /**
     * Scroll to element
     */
    public void scrollToElement(By locator) {
        try {
            WebElement element = waitStrategy.waitForElementPresence(locator);
            scrollToElement(element);
            logger.debug("Scrolled to element: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to scroll to element: {}", locator, e);
            throw new ElementInteractionException("Failed to scroll to element: " + locator, e);
        }
    }
    
    /**
     * Scroll to element (WebElement version)
     */
    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }
    
    /**
     * Scroll to bottom of page
     */
    public void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        logger.debug("Scrolled to bottom of page");
    }
    
    /**
     * Scroll to top of page
     */
    public void scrollToTop() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0)");
        logger.debug("Scrolled to top of page");
    }
    
    /**
     * Get all options from dropdown
     */
    public List<String> getDropdownOptions(By locator) {
        try {
            WebElement element = waitStrategy.waitForElementPresence(locator);
            Select select = new Select(element);
            List<String> options = new ArrayList<>();
            for (WebElement option : select.getOptions()) {
                options.add(option.getText());
            }
            logger.debug("Got {} options from dropdown: {}", options.size(), locator);
            return options;
        } catch (Exception e) {
            logger.error("Failed to get dropdown options: {}", locator, e);
            throw new ElementInteractionException("Failed to get dropdown options: " + locator, e);
        }
    }
    
    /**
     * Wait for element to be enabled
     */
    public boolean isEnabled(By locator) {
        try {
            WebElement element = waitStrategy.waitForElementPresence(locator);
            return element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Wait for element to disapper
     */
    public boolean isDisappeared(By locator) {
        try {
            Boolean element = waitStrategy.waitForElementInvisibility(locator);
            return element;
        } catch (Exception e) {
            return false;
        }
    }
    
    
    /**
     * Custom exception for element interactions
     */
    public static class ElementInteractionException extends RuntimeException {
        public ElementInteractionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
