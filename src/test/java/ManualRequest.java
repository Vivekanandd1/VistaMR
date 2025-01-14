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

public class ManualRequest extends Datas  {
	
	public static void main(String[] args) throws InterruptedException {
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofMinutes(15));
		Login(driver);
		FormFillup(driver);
		Thread.sleep(3000);
		String URLs = driver.findElement(By.id("request_url_copy")).getAttribute("value");
		driver.switchTo().newWindow(WindowType.TAB);
        driver.navigate().to(URLs);
        Set<String> Wind = driver.getWindowHandles();
        Iterator<String> Winds = Wind.iterator();
        String Parent = Winds.next();
        String Child = Winds.next();
        Thread.sleep(2000);
        driver.switchTo().defaultContent();
        WebElement Bank = driver.findElement(By.cssSelector("a[data-bank-name='Nordea']"));
        wait.until(ExpectedConditions.elementToBeClickable(Bank));
        Bank.click();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,800)");
        WebElement NextBtn = driver.findElement(By.id("next-btn"));
        wait.until(ExpectedConditions.elementToBeClickable(NextBtn));
        NextBtn.click();
        WebElement SubmitBtn = driver.findElement(By.id("submit"));
        wait.until(ExpectedConditions.elementToBeClickable(SubmitBtn));
        SubmitBtn.click();
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
	
	public static void FormFillup(WebDriver driver) {
		driver.findElement(By.cssSelector("span.menu-icon-new-request")).click();
		driver.findElement(By.id("recipient_name")).sendKeys(Name);
		driver.findElement(By.id("e-post")).sendKeys(Email);
		WebElement types = driver.findElement(By.cssSelector("select[name='type']"));
        select(types, "customer");
		WebElement Markets=driver.findElement(By.id("request-country-select"));
		select(Markets, "1");
		WebElement VrfTyp=driver.findElement(By.id("account_verification"));
		select(VrfTyp, "false");
		WebElement env=driver.findElement(By.id("env"));
		select(env, "sandbox");
		driver.findElement(By.id("submit-check")).click();
	}
	public static void Login(WebDriver driver) {
		driver.get(URL);
		driver.findElement(By.id("kreditz_email")).sendKeys(Email);
		driver.findElement(By.id("kreditz_current_password")).sendKeys(Password);
		driver.findElement(By.cssSelector("button[type='submit']")).click();
	}
	
	public static Select select(WebElement E, String S) {  
		Select slct = new Select(E);
		slct.selectByValue(S);
		return slct;
	}
	

}
