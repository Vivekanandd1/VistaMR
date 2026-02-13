package Pages;

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

	private By SearchBox = By.id("search_request");
	private By table = By.cssSelector(".table.k-sortable");
	private By Spinner = By.id("globalSpinner");
	private By CertificateResult = By.xpath("//tbody/tr/td[3]");
	private By NoRecordsMessage = By.cssSelector(".container.below_text_center");

	public CertificateLogs(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
	}

	public void CertificateSearch(String certificateNumber) {

	    wait.until(ExpectedConditions.visibilityOfElementLocated(SearchBox)) .sendKeys(certificateNumber, Keys.ENTER);
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
	}

}
