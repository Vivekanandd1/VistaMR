import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class ManualRequest extends Datas  {
	
	public static void main(String[] args) throws InterruptedException {
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get(URL);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.findElement(By.id("kreditz_email")).sendKeys(Email);
		driver.findElement(By.id("kreditz_current_password")).sendKeys(Password);
		driver.findElement(By.cssSelector("button[type='submit']")).click();
		driver.findElement(By.cssSelector("span.menu-icon-new-request")).click();
		driver.findElement(By.id("recipient_name")).sendKeys(Name);
		driver.findElement(By.id("e-post")).sendKeys(Email);
		WebElement types = driver.findElement(By.cssSelector("select[name='type']"));
		Select slct = new Select(types);
		slct.selectByValue("customer");
		WebElement Markets=driver.findElement(By.id("request-country-select"));
		Select slct1 = new Select(Markets);
		slct1.selectByValue("1");
		WebElement VrfTyp=driver.findElement(By.id("account_verification"));
		Select slct2 = new Select(VrfTyp);
		slct2.selectByValue("true");
		WebElement env=driver.findElement(By.id("env"));
		Select slct3 = new Select(env);
		slct3.selectByValue("sandbox");
		driver.findElement(By.id("submit-check")).click();
		Thread.sleep(3000);
		String URL = driver.findElement(By.id("request_url_copy")).getAttribute("value");
		System.out.println(URL);
		driver.findElement(By.className("copy-button")).click();
//		driver.navigate().to
	}

}
