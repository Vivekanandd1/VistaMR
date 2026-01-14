package Pages;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import AbstractComponent.BaseLineTest;

public class BusinessRequest extends BaseLineTest {

	String URL = "https://vista.kreditz-dev.com/login";
	WebDriver driver;
	WebDriverWait wait;
	WebElement Nordea;
	WebElement Handlesbanken;

	public BusinessRequest(WebDriver driver, WebDriverWait wait) {

		this.driver = driver;
		this.wait = wait;

	}

	public void FormFillup(String Name, String Email) {
		driver.findElement(By.cssSelector("span.menu-icon-new-request")).click();
		WebElement RecName = driver.findElement(By.id("recipient_name"));
		wait.until(ExpectedConditions.elementToBeClickable(RecName));
		RecName.sendKeys(Name);
		driver.findElement(By.id("e-post")).sendKeys(Email);
		WebElement EngCaseID = driver.findElement(By.xpath("//input[@placeholder='Case id']"));
		EngCaseID.sendKeys(LocalDateTime.now().toString());
		WebElement types = driver.findElement(By.cssSelector("select[name='type']"));
		select(types, "corporate");
		WebElement Markets = driver.findElement(By.xpath("//select[@name='corporate_country_id']"));
		wait.until(ExpectedConditions.visibilityOf(Markets));
		select(Markets, "1");
		WebElement VrfTyp = driver.findElement(By.id("account_verification"));
		select(VrfTyp, "false");
		WebElement env = driver.findElement(By.id("env"));
		select(env, "sandbox");
		driver.findElement(By.id("submit-check")).click();
	}

	public Select select(WebElement E, String S) {
		Select slct = new Select(E);
		slct.selectByValue(S);
		return slct;
	}

	public void Consent() {
		Nordea = driver.findElement(By.xpath("//a[normalize-space()='Nordea']"));
		Handlesbanken = driver.findElement(By.xpath("//a[normalize-space()='Handelsbanken']"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,950)");
		Handlesbanken.click();
		WebElement NextBtn = driver.findElement(By.id("next-btn"));
		wait.until(ExpectedConditions.elementToBeClickable(NextBtn));
		NextBtn.click();
		WebElement SSNField = driver.findElement(By.xpath("(//input[@id='ssn'])[1]"));
		WebElement OrgNumber = driver.findElement(By.xpath("(//input[@id='ssn'])[2]"));
		WebElement SubmitBtn = driver.findElement(By.id("submit"));
		wait.until(ExpectedConditions.elementToBeClickable(SSNField));
		if (SSNField.isDisplayed()) {
			SSNField.sendKeys("201212121214");
			OrgNumber.sendKeys("8899336624");
			SubmitBtn.click();
		} else {
			wait.until(ExpectedConditions.elementToBeClickable(SubmitBtn));
			SubmitBtn.click();
		}
	}

}
