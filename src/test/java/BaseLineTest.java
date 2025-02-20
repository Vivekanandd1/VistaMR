import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;


public class BaseLineTest {
	
	String URL = "https://pre-vista.kreditz.com/login";
	String Email = "snaps.deshmukh@gmail.com";
	String Password = "Password123";
	
	WebDriver driver;
	WebDriverWait wait;
	public Datas data;
	
	
	public WebDriver start() {
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		wait= new WebDriverWait(driver, Duration.ofMinutes(15));
		return driver;
		
	}
	
	
	@BeforeMethod(alwaysRun = true)
	public Datas launchApp() {
		driver = start();
		data = new Datas(driver, wait);
		data.Login();
		return data;
	}
	
	
	@AfterMethod
	public void teardown() {
		driver.quit();  
	}

}
