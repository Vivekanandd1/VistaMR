package TestClasses;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseLineTest {

	WebDriver driver;
	WebDriverWait wait;
	public Datas data;

	public WebDriver start() {
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		wait = new WebDriverWait(driver, Duration.ofMinutes(15));
		return driver;
	}

	@BeforeMethod(alwaysRun = true)
	public Datas launchApp() {
		driver = start();
		data = new Datas(driver, wait);
		return data;
	}

	@AfterMethod
	public void teardown() {
		driver.quit();
	}


}
