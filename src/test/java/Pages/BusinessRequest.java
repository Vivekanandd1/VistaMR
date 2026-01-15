package Pages;

import java.time.LocalDateTime;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import AbstractComponent.BaseLineTest;

public class BusinessRequest extends BaseLineTest {

	String URL = "https://vista.kreditz-dev.com/login";
	WebDriver driver;
	WebDriverWait wait;
	
	By NewRequest = By.cssSelector("span.menu-icon-new-request");
	By RecpName = By.id("recipient_name");
	By EmailBox = By.id("e-post");
    By CaseID = By.xpath("//input[@placeholder='Case id']");
    By RequestTypes = By.cssSelector("select[name='type']");
    By Markets = By.xpath("//select[@name='corporate_country_id']");
    By VrfTyp = By.id("account_verification");
    By Environment = By.id("env");
    By SubmitBtn = By.id("submit-check");
    By Nordea = By.xpath("//a[normalize-space()='Nordea']");
	By Handlesbanken = By.xpath("//a[normalize-space()='Handelsbanken']");
	By NextBtn = By.id("next-btn");
	By SSNField = By.xpath("(//input[@id='ssn'])[1]");
	By OrgNumber = By.xpath("(//input[@id='ssn'])[2]");
	By ConinueBtn = (By.id("submit"));
    
	public BusinessRequest(WebDriver driver, WebDriverWait wait) {

		this.driver = driver;
		this.wait = wait;

	}

	public void FormFillup(String Name, String Email) {
		driver.findElement(NewRequest).click();
		wait.until(ExpectedConditions.elementToBeClickable(RecpName)).sendKeys(Name);
		driver.findElement(EmailBox).sendKeys(Email);
		driver.findElement(CaseID).sendKeys(LocalDateTime.now().toString());
		select(driver.findElement(RequestTypes), "corporate");
		wait.until(ExpectedConditions.visibilityOfElementLocated(Markets));
		select(driver.findElement(Markets), "1");
		select(driver.findElement(VrfTyp), "false");
		select(driver.findElement(Environment), "sandbox");
		driver.findElement(SubmitBtn).click();
	}


	public void Consent() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,950)");
		driver.findElement(Handlesbanken).click();
		wait.until(ExpectedConditions.elementToBeClickable(NextBtn)).click();
		wait.until(ExpectedConditions.elementToBeClickable(SSNField)).sendKeys("201212121214");
		driver.findElement(OrgNumber).sendKeys("8899336624");
		driver.findElement(ConinueBtn).click();
	}

}
