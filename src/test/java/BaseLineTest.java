import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;

public class BaseLineTest {
	
	WebDriver driver;
	WebDriverWait wait;
	
	@BeforeTest
	public Datas start() {
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		wait= new WebDriverWait(driver, Duration.ofMinutes(15));
		return new Datas();
	}
	
	public void teardown() {
		driver.quit();  
	}

}
