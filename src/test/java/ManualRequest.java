import java.time.Duration;
import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

public class ManualRequest extends Datas  {
	
	@Test
	public void ManualRequests() throws InterruptedException {
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofMinutes(15));
		Login(driver);
		FormFillup(driver);
		Thread.sleep(3000);
		Redirection(driver);
        Set<String> Wind = driver.getWindowHandles();
        Iterator<String> Winds = Wind.iterator();
        String Parent = Winds.next();
        String Child = Winds.next();
        Thread.sleep(2000);
        driver.switchTo().defaultContent();
        Consent(driver, wait);
        driver.switchTo().window(Child);
        driver.switchTo().defaultContent();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p/a[text()='verifications@leovegas.com']")));
        System.out.println(driver.getTitle());
        Thread.sleep(2000);
        driver.switchTo().window(Parent);
        System.out.println("turned for parent");
        Thread.sleep(2000);
        driver.quit();
        
	}
	
	

}
