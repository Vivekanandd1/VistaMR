package MRqst;
import java.time.Duration;
import java.util.Collections;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseLineTest {

	WebDriver driver;
	WebDriverWait wait;
	public Datas data;
	

	public WebDriver start() {
    /*Chrome setup*/
//		ChromeOptions opt = new ChromeOptions();
//		opt.addArguments("guest");
//		opt.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
//		driver = new ChromeDriver(opt);
		
//		EdgeOptions opt = new EdgeOptions();
//		opt.addArguments("guest");
//		opt.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
//		driver = new EdgeDriver(opt);
		
		/*This is something we have to make more dynamic*/
		
		driver = new FirefoxDriver();
		
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
	
	public void Pageload() throws InterruptedException {
        Thread.sleep(2000);

	}

}
