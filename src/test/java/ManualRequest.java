import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class ManualRequest {
	
	public static void main(String[] args) {
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://pre-vista.kreditz.com/login");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.findElement(By.id("kreditz_email")).sendKeys("snaps.deshmukh@gmail.com");
		driver.findElement(By.id("kreditz_current_password")).sendKeys("Password123");
		driver.findElement(By.cssSelector("button[type='submit']")).click();
		driver.findElement(By.cssSelector("span.menu-icon-new-request")).click();
		driver.findElement(By.id("recipient_name")).sendKeys("Bruce Wayne");
		driver.findElement(By.id("e-post")).sendKeys("snaps.deshmukh@gmail.com");
		WebElement types = driver.findElement(By.cssSelector("select[name='type']"));
		Select slct = new Select(types);
		slct.selectByValue("customer");
		WebElement Markets=driver.findElement(By.id("request-country-select"));
		Select slct1 = new Select(Markets);
		slct1.selectByValue("1");
		
	}

}
