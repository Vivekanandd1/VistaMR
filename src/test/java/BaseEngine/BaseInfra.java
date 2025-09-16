package BaseEngine;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import PageObjects.Datas;

public class BaseInfra {
	
	public WebDriver driver;
	public WebDriverWait wait;
	public Datas data;
	String appUrl = "https://pre-vista.kreditz.com/login";
	String browser = "chrome";


	    public WebDriver start() {
//	        switch (browser.toLowerCase()) {
//	            case "firefox":
//	                driver = new FirefoxDriver();
//	                break;
//	            case "edge":
//	                driver = new EdgeDriver();
//	                break;
//	            case "chrome":
//	            default:
//	                driver = new ChromeDriver();
//	        }
	        driver = new ChromeDriver();
	        driver.manage().window().maximize();
	        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	        wait = new WebDriverWait(driver, Duration.ofMinutes(3)); // 30s is usually enough
	        return driver;
	    }

	  
	    @BeforeMethod(alwaysRun = true)
	    public Datas launchApp(String appUrl) {
	        driver = start();
	        data = new Datas(driver, wait);
	        driver.get(appUrl);
	        return data;
	    }

	 
	    @AfterMethod(alwaysRun = true)
	    public void teardown() {
	        if (driver != null) {
	            driver.quit();
	        }
	    }

	    /**
	     * Replace Thread.sleep with explicit wait
	     */
	    public void waitForPageLoad() {
	        // Example: wait until JS is ready
	        new WebDriverWait(driver, Duration.ofSeconds(15))
	                .until(webDriver -> ((String) ((org.openqa.selenium.JavascriptExecutor) driver)
	                        .executeScript("return document.readyState")).equals("complete"));
	    }

}
