package AbstractComponent;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import Pages.APICall;
import Pages.BusinessRequest;
import Pages.CertificateLogs;
import Pages.Datas;
import Pages.UserCreation;


@Listeners({io.qameta.allure.testng.AllureTestNg.class})
public class BaseLineTest {

	public WebDriver driver;
	public WebDriverWait wait;
	public Datas data;
	public UserCreation User;
	public APICall API;
	public CertificateLogs CrtfctLgs;
	public BusinessRequest BR;
	String Browser = "chrome";
	String appUrl = "https://vista.kreditz-dev.com/login";
	

	public WebDriver start() throws IOException {
    /*Chrome setup*/
		if(Browser.equalsIgnoreCase("chrome")) {
			ChromeOptions opt = new ChromeOptions();
			opt.addArguments("--incognito");
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
		wait = new WebDriverWait(driver, Duration.ofMinutes(2));
		return driver;
	}

	@BeforeMethod(alwaysRun = true)
	public Datas launchApp() throws IOException {
		driver = start();
		data = new Datas(driver, wait);
		User = new UserCreation(driver, wait);
		API = new APICall(driver, wait);
		CrtfctLgs = new CertificateLogs(driver, wait);
		BR = new BusinessRequest(driver, wait);
		driver.get(appUrl);
		return data;
	}

	@AfterMethod
	public void teardown() {
		 if (driver != null) {
	            driver.quit();
	        }
	}
	
	public void Pageload() throws InterruptedException {
		 Thread.sleep(2000);
	}
	
	public Select select(WebElement E, String S) {
		Select slct = new Select(E);
		slct.selectByValue(S);
		return slct;
	}

}
