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
import org.openqa.selenium.support.ui.Select;
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
		Nordea = driver.findElement(By.xpath("//a[normalize-space()='Nordea']"));
		Handlesbanken = driver.findElement(By.xpath("//a[normalize-space()='Handelsbanken']"));
		wait.until(ExpectedConditions.elementToBeClickable(Handlesbanken));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,950)");
		Handlesbanken.click();
		WebElement NextBtn = driver.findElement(By.id("next-btn"));
		wait.until(ExpectedConditions.elementToBeClickable(NextBtn));
		NextBtn.click();
		WebElement SSNField = driver.findElement(By.id("ssn"));
		WebElement SubmitBtn = driver.findElement(By.id("submit"));
		wait.until(ExpectedConditions.elementToBeClickable(SSNField));
		if (SSNField.isDisplayed()) {
			SSNField.sendKeys("201212121214");
			SubmitBtn.click();
		} else {
			wait.until(ExpectedConditions.elementToBeClickable(SubmitBtn));
			SubmitBtn.click();
		}
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

	public Select select(WebElement E, String S) {
		Select slct = new Select(E);
		slct.selectByValue(S);
		return slct;
	}

	public void Redirection() throws InterruptedException {
//		String URLs = driver.findElement(By.id("request_url_copy")).getAttribute("value");
//		Thread.sleep(2000);
//		driver.switchTo().newWindow(WindowType.TAB);
//		driver.navigate().to(URLs);

		String url = driver.findElement(By.id("request_url_copy")).getAttribute("value");
		String originalWindow = driver.getWindowHandle();
		int retryCount = 0;
		boolean success = false;

		while (retryCount < 3 && !success) {
			try {
				((JavascriptExecutor) driver).executeScript("window.open(arguments[0], '_blank');", url);
				Thread.sleep(2000);

				Set<String> allWindows = driver.getWindowHandles();
				for (String win : allWindows) {
					if (!win.equals(originalWindow)) {
						driver.switchTo().window(win);
						success = true;
						break;
					}
				}
			} catch (Exception e) {
				retryCount++;
				Thread.sleep(2000);
			}
		}

		if (!success) {
			throw new RuntimeException("failed to open url in new tab after retries");
		}

	}

	public void Login(String Email, String Password) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// Enter credentials
		driver.findElement(By.id("kreditz_email")).sendKeys(Email);
		driver.findElement(By.id("kreditz_current_password")).sendKeys(Password);
		driver.findElement(By.cssSelector("button[type='submit']")).click();

		// Click the language Dropdown and wait for it
		WebElement langDropdown = wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//div[contains(@class,'custom-dropdown-language')]")));
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
