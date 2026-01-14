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

	private By SearchBox = By.id("search_request");
	private By table = By.cssSelector(".table.k-sortable");
	private By Spinner = By.id("globalSpinner");
	private By CertificateResult = By.xpath("//tbody/tr/td[3]");
	private By NoRecordsMessage = By.cssSelector(".container.below_text_center");
	private By userEmail = By.id("organization_user_email");
	private By userPhone = By.id("organization_user_phone_number");
	private By submitBtn = By.className("submit_new_user");
	private By alertMessage = By.xpath("//div[@class='alert-message']");

	public CertificateLogs(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
	}

	public void CertificateSearch(String CertificateNumber) {
//		driver.findElement(SearchBox).sendKeys(CertificateNumber, Keys.ENTER);
//		wait.until(ExpectedConditions.invisibilityOfElementLocated(Spinner));
//		wait.until(ExpectedConditions.visibilityOfElementLocated(table));
//		if(driver.findElement(table).isDisplayed()) {
//		WebElement resultRow = wait.until(ExpectedConditions.visibilityOfElementLocated(CertificateResult));
//		String actualCertificate = resultRow.getText().trim();
//		Assert.assertEquals(actualCertificate, CertificateNumber, "Certificate number mismatch in search result");
//		}
//		else {
//			System.out.println(CertificateNumber +  " Not Found");
//		}

		driver.findElement(SearchBox).sendKeys(CertificateNumber, Keys.ENTER);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(Spinner));
		wait.until(ExpectedConditions.or(ExpectedConditions.visibilityOfElementLocated(table),
				ExpectedConditions.visibilityOfElementLocated(NoRecordsMessage)));
		if (driver.findElements(table).size() > 0) {

			WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(CertificateResult));

			Assert.assertEquals(result.getText().trim(), CertificateNumber, "Certificate number does not match");

		} else {
			Assert.fail("Certificate number " + CertificateNumber + " was not found");
		}

	}

}
