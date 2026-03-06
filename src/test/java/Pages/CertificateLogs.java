package Pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import AbstractComponent.BaseLineTest;

public class CertificateLogs extends BaseLineTest {

	WebDriver driver;
	WebDriverWait wait;
	String RequestType = "iframe";
	String CertType = "consumer";
	String MarketName = "sweden";

	private By SearchBox = By.id("search_request");
	private By table = By.cssSelector(".table.k-sortable");
	private By Spinner = By.id("globalSpinner");
	private By CertificateResult = By.xpath("//tbody/tr/td[3]");
	private By NoRecordsMessage = By.cssSelector(".container.below_text_center");
	private By FilterBtn = By.className("filter-dropdown");
	private By SourceFilter = By.xpath("(//div[@class='custom-select-trigger ellipsis'])[1]");
	private By SourceOptions = By.xpath("//span[contains(@class,'custom-option')]");
	private By ApplyBtn = By.xpath("//button[text()='Apply']");
	private By ClearFilter = By.className("clear-filter");
	private By RequestTypes = By.xpath("(//div[contains(@class,'custom-select-trigger')])[2]");
	private By MarketFilter = By.xpath("//div[contains(@class,'custom-select-market')]");

	public CertificateLogs(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
	}

	public void CertificateSearch(String certificateNumber) {

	    wait.until(ExpectedConditions.visibilityOfElementLocated(SearchBox)).sendKeys(certificateNumber, Keys.ENTER);
	    wait.until(ExpectedConditions.invisibilityOfElementLocated(Spinner));
	    wait.until(ExpectedConditions.or(ExpectedConditions.visibilityOfElementLocated(table), ExpectedConditions.visibilityOfElementLocated(NoRecordsMessage)));
	    if (driver.findElements(NoRecordsMessage).size() > 0 &&
	            driver.findElement(NoRecordsMessage).isDisplayed()) {
	        Assert.fail("Certificate number " + certificateNumber + " was not found");

	    } else {
	        WebElement result = wait.until(
	                ExpectedConditions.visibilityOfElementLocated(CertificateResult));
	        Assert.assertEquals(result.getText().trim(), certificateNumber, "Certificate number does not match");
	    }
	    
	    wait.until(ExpectedConditions.visibilityOfElementLocated(SearchBox)).clear();
	}
	
	public void SourceFilters() throws InterruptedException {
		wait.until(ExpectedConditions.visibilityOfElementLocated(FilterBtn)).click();
		wait.until(ExpectedConditions.elementToBeClickable(SourceFilter)).click();
		List<WebElement> SourceList = driver.findElements(SourceOptions);
		for(WebElement e: SourceList) {
			if(e.getText().equalsIgnoreCase(RequestType)) {
				e.click();
			}
		}
		wait.until(ExpectedConditions.elementToBeClickable(ApplyBtn)).click();
	}
	
	public void RequestFilters() throws InterruptedException {
		wait.until(ExpectedConditions.invisibilityOfElementLocated(Spinner));
		wait.until(ExpectedConditions.visibilityOfElementLocated(ClearFilter));
		wait.until(ExpectedConditions.elementToBeClickable(ClearFilter)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(Spinner));
		wait.until(ExpectedConditions.visibilityOfElementLocated(FilterBtn)).click();
		wait.until(ExpectedConditions.elementToBeClickable(RequestTypes)).click();
		List<WebElement> SourceList = driver.findElements(SourceOptions);
		for(WebElement e: SourceList) {
			if(e.getText().equalsIgnoreCase(CertType)) {
				e.click();
			}
		}
		wait.until(ExpectedConditions.elementToBeClickable(ApplyBtn)).click();
	}
	
	public void MarketFilters() throws InterruptedException {
		wait.until(ExpectedConditions.invisibilityOfElementLocated(Spinner));
		wait.until(ExpectedConditions.visibilityOfElementLocated(ClearFilter));
		wait.until(ExpectedConditions.elementToBeClickable(ClearFilter)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(Spinner));
		wait.until(ExpectedConditions.visibilityOfElementLocated(FilterBtn)).click();
		wait.until(ExpectedConditions.elementToBeClickable(MarketFilter)).click();
		List<WebElement> SourceList = driver.findElements(SourceOptions);
		for(WebElement e: SourceList) {
			if(e.getText().equalsIgnoreCase(MarketName)) {
				e.click();
			}
		}
		wait.until(ExpectedConditions.elementToBeClickable(ApplyBtn)).click();
	}

}
