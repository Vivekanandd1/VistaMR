import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

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
		
	}

}
