package AbstractComponent;
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

import MRqst.Datas;

public class BaseLineTest {

	public WebDriver driver;
	public WebDriverWait wait;
	public Datas data;
	String Browser = "edge";
	

	public WebDriver start() {
    /*Chrome setup*/
		if(Browser.equalsIgnoreCase("chrome")) {
			ChromeOptions opt = new ChromeOptions();
			opt.addArguments("guest");
			opt.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
			driver = new ChromeDriver(opt);
		}

		else if(Browser.equalsIgnoreCase("edge")) {
		EdgeOptions opt = new EdgeOptions();
		opt.addArguments("guest");
		opt.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
		driver = new EdgeDriver(opt);
		}
		
		/*This is something we have to make more dynamic*/
		else if(Browser.equalsIgnoreCase("firefox")) {
		driver = new FirefoxDriver();
		}
		
		else {
			System.out.println("Might be browser value is wrong or FW not equipped with this browser "+ Browser);
		}
		
		
		
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
