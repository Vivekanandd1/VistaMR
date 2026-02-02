package Pages;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import AbstractComponent.BaseLineTest;

public class Datas extends BaseLineTest {

	String URL = "https://vista.kreditz-dev.com/login";
	WebDriver driver;
	WebDriverWait wait;

	By Nordea = By.xpath("//a[normalize-space()='Nordea']");
	By Handlesbanken = By.xpath("//a[normalize-space()='Handelsbanken']");
	By RecName = By.id("recipient_name");
	By NewReq = By.cssSelector("span.menu-icon-new-request");
	By EmailBox = By.id("e-post");
	By CaseIDBox = By.xpath("//input[@placeholder='Case id']");
	By CertType = By.cssSelector("select[name='type']");
	By Country = By.id("request-country-select");
	By Mode = By.id("account_verification");
	By Environment = By.id("env");
	By FormSubmitBtn = By.id("submit-check");
	By SSNField = By.id("ssn");
	By SubmitBtn = By.id("submit");
	By ReqURL = By.id("request_url_copy");
	By LoginEmail = By.id("kreditz_email");
	By LoginPassword = By.id("kreditz_current_password");
	By LoginBtn = By.cssSelector("button[type='submit']");
	By langDropdownLocator = By.xpath("//div[contains(@class,'custom-dropdown-language')]");
	By Profile = By.xpath("//div[contains(@class,'dropdown-username-profile')]");
	By LogoutBtn = By.xpath("//a[normalize-space()='Sign out']");
	By NextBtn = By.id("next-btn");

	public Datas(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
	}

	public void Consent() {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,950)");
		wait.until(ExpectedConditions.elementToBeClickable(Handlesbanken)).click();
		wait.until(ExpectedConditions.elementToBeClickable(NextBtn)).click();
		wait.until(ExpectedConditions.elementToBeClickable(SSNField)).sendKeys("201212121214");
		wait.until(ExpectedConditions.elementToBeClickable(SubmitBtn)).click();

	}

	public void FormFillup(String Name, String Email) {
		driver.findElement(NewReq).click();
		wait.until(ExpectedConditions.elementToBeClickable(RecName)).sendKeys(Name);
		driver.findElement(EmailBox).sendKeys(Email);
		driver.findElement(CaseIDBox).sendKeys(LocalDateTime.now().toString());
		WebElement types = driver.findElement(CertType);
		select(types, "customer");
		WebElement Markets = driver.findElement(Country);
		select(Markets, "1");
		WebElement VrfTyp = driver.findElement(Mode);
		select(VrfTyp, "false");
		WebElement env = driver.findElement(Environment);
		select(env, "sandbox");
		driver.findElement(FormSubmitBtn).click();
	}

	public void Redirection() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

		String url = driver.findElement(ReqURL).getAttribute("value");

		if (url == null || url.isEmpty()) {
			throw new RuntimeException("URL is null or empty");
		}

		String originalWindow = driver.getWindowHandle();

		// Open new tab
		driver.switchTo().newWindow(WindowType.TAB);
		driver.get(url);

		// Wait for new tab
		wait.until(ExpectedConditions.numberOfWindowsToBe(2));

		// Switch to new tab
		for (String windowHandle : driver.getWindowHandles()) {
			if (!windowHandle.equals(originalWindow)) {
				driver.switchTo().window(windowHandle);
				break;
			}
		}
	}

	public void Login(String Email, String Password) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// Enter credentials
		driver.findElement(LoginEmail).sendKeys(Email);
		driver.findElement(LoginPassword).sendKeys(Password);
		driver.findElement(LoginBtn).click();
        wait.until(ExpectedConditions.elementToBeClickable(langDropdownLocator)).click();

		// English option in list if English is preselected
		WebElement BaseEnglish = driver.findElement(By.xpath("//a[contains(@onclick,'English')]"));
		// List of possible language locators
		By[] languages = { By.xpath("//a[normalize-space()='Engelska']"), By.xpath("//a[normalize-space()='Englisch']"),
				By.xpath("//a[normalize-space()='Inglés']") };

		boolean languageSelected = false;

		if (BaseEnglish.isDisplayed() == true) {
			BaseEnglish.click();
			System.out.println("English is Preselected");
		}

		else {
			for (By langLocator : languages) {
				try {
					WebElement langElement = driver.findElement(langLocator);
					if (langElement.isDisplayed()) {
						wait.until(ExpectedConditions.elementToBeClickable(langElement)).click();
						languageSelected = true;
						WebElement submitBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("button_id")));
						submitBtn.click();
						System.out.println("✅ Login completed successfully with language selection.");
						break;
					}
				} catch (NoSuchElementException e) {
					// Ignore and continue checking next
				}
			}

			if (!languageSelected) {
				System.out.println("❌ Language selection failed — no matching option found.");
			}
		}

		// Click final submit button

	}

	public void WindowShuffleChild() {
		Set<String> Wind = driver.getWindowHandles();
		Iterator<String> Winds = Wind.iterator();
		String Parent = Winds.next();
		String Child = Winds.next();
		driver.switchTo().window(Child);
	}

	public void WindowShuffleParent() {
		Set<String> Wind = driver.getWindowHandles();
		Iterator<String> Winds = Wind.iterator();
		String Parent = Winds.next();
		String Child = Winds.next();
		driver.switchTo().window(Parent);
	}

	public void Logout() {
		wait.until(ExpectedConditions.elementToBeClickable(Profile)).click();
		wait.until(ExpectedConditions.elementToBeClickable(LogoutBtn)).click();
	}

}
