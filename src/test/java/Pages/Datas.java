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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import AbstractComponent.BaseLineTest;

public class Datas extends BaseLineTest {

	String URL = "https://vista.kreditz-dev.com/login";
	WebDriver driver;
	WebDriverWait wait;
	WebElement Nordea;
	WebElement Handlesbanken;

	public Datas(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
	}

	public void Consent() {
		By Nordea = By.xpath("//a[normalize-space()='Nordea']");
		By Handlesbanken = By.xpath("//a[normalize-space()='Handelsbanken']");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,950)");
		wait.until(ExpectedConditions.elementToBeClickable(Handlesbanken)).click();
//		js.executeScript("arguments[0].click()",Handlesbanken);
		WebElement NextBtn = driver.findElement(By.id("next-btn"));
		wait.until(ExpectedConditions.elementToBeClickable(NextBtn)).click();
		By SSNField = By.id("ssn");
		By SubmitBtn = By.id("submit");
		wait.until(ExpectedConditions.elementToBeClickable(SSNField)).sendKeys("201212121214");
		wait.until(ExpectedConditions.elementToBeClickable(SubmitBtn)).click();
		
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
		select(types, "customer");
		WebElement Markets = driver.findElement(By.id("request-country-select"));
		select(Markets, "1");
		WebElement VrfTyp = driver.findElement(By.id("account_verification"));
		select(VrfTyp, "false");
		WebElement env = driver.findElement(By.id("env"));
		select(env, "sandbox");
		driver.findElement(By.id("submit-check")).click();
	}



	public void Redirection() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

	    String url = driver.findElement(By.id("request_url_copy"))
	                       .getAttribute("value");

	    if (url == null || url.isEmpty()) {
	        throw new RuntimeException("URL is null or empty");
	    }

	    String originalWindow = driver.getWindowHandle();

	    // Open new tab
	    ((JavascriptExecutor) driver)
	            .executeScript("window.open(arguments[0], '_blank');", url);

	    // Wait for new tab
	    wait.until(ExpectedConditions.numberOfWindowsToBe(2));

	    // Switch to new tab
	    for (String windowHandle : driver.getWindowHandles()) {
	        if (!windowHandle.equals(originalWindow)) {
	            driver.switchTo().window(windowHandle);
	            break;
	        }
	    }

	    // Optional: wait until page is loaded
//	    wait.until(webDriver ->
//	            ((JavascriptExecutor) webDriver)
//	                    .executeScript("return document.readyState")
//	                    .equals("complete"));

	}

	public void Login(String Email, String Password) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// Enter credentials
		driver.findElement(By.id("kreditz_email")).sendKeys(Email);
		driver.findElement(By.id("kreditz_current_password")).sendKeys(Password);
		driver.findElement(By.cssSelector("button[type='submit']")).click();

		// Click the language Dropdown and wait for it
		By langDropdownLocator = By.xpath("//div[contains(@class,'custom-dropdown-language')]");
		WebElement langDropdown =  wait.until(ExpectedConditions.elementToBeClickable(langDropdownLocator));		
		langDropdown.click();

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
		WebElement Profile = driver.findElement(By.xpath("//div[contains(@class,'dropdown-username-profile')]"));
		wait.until(ExpectedConditions.elementToBeClickable(Profile));
		Profile.click();
		WebElement Logout = driver.findElement(By.xpath("//a[normalize-space()='Sign out']"));
		wait.until(ExpectedConditions.elementToBeClickable(Logout));
		Logout.click();
	}

}
