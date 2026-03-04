package Pages;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import AbstractComponent.BaseLineTest;

public class Datas extends BaseLineTest {

	String URL = "https://vista.kreditz-dev.com/login";
	WebDriver driver;
	WebDriverWait wait;

	private By Nordea = By.xpath("//a[normalize-space()='Nordea']");
	private By Handlesbanken = By.xpath("//a[normalize-space()='Handelsbanken']");
	private By RecName = By.id("recipient_name");
	private By NewReq = By.cssSelector("span.menu-icon-new-request");
	private By EmailBox = By.id("e-post");
	private By CaseIDBox = By.xpath("//input[@placeholder='Case id']");
	private By CertType = By.cssSelector("select[name='type']");
	private By Country = By.id("request-country-select");
	private By Mode = By.id("account_verification");
	private By Environment = By.id("env");
	private By FormSubmitBtn = By.id("submit-check");
	private By SSNField = By.id("ssn");
	private By SubmitBtn = By.id("submit");
	private By ReqURL = By.id("request_url_copy");
	private By LoginEmail = By.id("kreditz_email");
	private By LoginPassword = By.id("kreditz_current_password");
	private By LoginBtn = By.cssSelector("button[type='submit']");
	private By langDropdownLocator = By.xpath("//div[contains(@class,'custom-dropdown-language')]");
	private By Profile = By.xpath("//div[contains(@class,'dropdown-username-profile')]");
	private By LogoutBtn = By.xpath("//a[normalize-space()='Sign out']");
	private By NextBtn = By.id("next-btn");
	private By RequestLogHeader = By.xpath("//div/h3[text()='Request Log']");

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

		boolean languageSelected = false;

		// Check if English already available
		List<WebElement> baseEnglish = driver.findElements(By.xpath("//a[contains(@onclick,'English')]"));

		if (!baseEnglish.isEmpty()) {
			wait.until(ExpectedConditions.elementToBeClickable(baseEnglish.get(0))).click();
			System.out.println("English is Preselected");
			return;
		}

		// If not, try other languages
		By[] languages = { By.xpath("//a[normalize-space()='Engelska']"), By.xpath("//a[normalize-space()='Englisch']"),
				By.xpath("//a[normalize-space()='Inglés']") };

		for (By langLocator : languages) {
			try {
				List<WebElement> langElements = driver.findElements(langLocator);
				if (!langElements.isEmpty()) {

					wait.until(ExpectedConditions.elementToBeClickable(langElements.get(0))).click();

					WebElement submitBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("button_id")));
					submitBtn.click();

					languageSelected = true;
					System.out.println("✅ Login completed successfully with language selection.");
					break;
				}
			} catch (Exception e) {
				// Continue checking next language
			}
		}

		if (!languageSelected) {
			System.out.println("❌ Language selection failed — no matching option found.");
		}
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
		wait.until(ExpectedConditions.visibilityOfElementLocated(RequestLogHeader));
		wait.until(ExpectedConditions.elementToBeClickable(Profile)).click();
		wait.until(ExpectedConditions.elementToBeClickable(LogoutBtn)).click();
	}

}
