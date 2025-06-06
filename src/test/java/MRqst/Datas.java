package MRqst;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Datas extends BaseLineTest{
	
	String URL = "https://pre-vista.kreditz.com/login";
	WebDriver driver;
	WebDriverWait wait;
	
	
	public Datas(WebDriver driver,WebDriverWait wait) {
		this.driver=driver;
		this.wait = wait;
	}
	
	public void Consent() {
		WebElement Bank = driver.findElement(By.xpath("//a[normalize-space()='Nordea']"));
        wait.until(ExpectedConditions.elementToBeClickable(Bank));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,950)");
        Bank.click();
        WebElement NextBtn = driver.findElement(By.id("next-btn"));
        wait.until(ExpectedConditions.elementToBeClickable(NextBtn));
        NextBtn.click();
        WebElement SSNField = driver.findElement(By.id("ssn"));
        WebElement SubmitBtn = driver.findElement(By.id("submit"));
        wait.until(ExpectedConditions.elementToBeClickable(SSNField));
        if(SSNField.isDisplayed()) {
        	SSNField.sendKeys("201212121212");
        	SubmitBtn.click();
        }
        else {
        wait.until(ExpectedConditions.elementToBeClickable(SubmitBtn));	
        SubmitBtn.click();
        }
	}
	public void FormFillup(String Name, String Email) {
		driver.findElement(By.cssSelector("span.menu-icon-new-request")).click();
		driver.findElement(By.id("recipient_name")).sendKeys(Name);
		driver.findElement(By.id("e-post")).sendKeys(Email);
		driver.findElement(By.xpath("//input[@placeholder='Case id']")).sendKeys(LocalDateTime.now().toString());
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

	
	public Select select(WebElement E, String S) {  
		Select slct = new Select(E);
		slct.selectByValue(S);
		return slct;
	}
	
	
	public void Redirection() throws InterruptedException {
		String URLs = driver.findElement(By.id("request_url_copy")).getAttribute("value");
		Thread.sleep(2000);
		driver.switchTo().newWindow(WindowType.TAB);
        driver.navigate().to(URLs);
	}
	
	public void Login(String Email, String Password) {
		driver.get(URL);
		driver.findElement(By.id("kreditz_email")).sendKeys(Email);
		driver.findElement(By.id("kreditz_current_password")).sendKeys(Password);
		driver.findElement(By.cssSelector("button[type='submit']")).click();
	}
	
	public void WindowShuffleChild() {
		  Set<String> Wind = driver.getWindowHandles();
	        Iterator<String> Winds = Wind.iterator();
	        String Parent = Winds.next();
	        String Child = Winds.next();
	        driver.switchTo().window(Child);
	}
	
	public void WindowShuffleParent() {
		  Set<String> Wind = driver.getWindowHandles();
	        Iterator<String> Winds = Wind.iterator();
	        String Parent = Winds.next();
	        String Child = Winds.next();
	        driver.switchTo().window(Parent);
	}
	
	public void Logout() {
		WebElement Profile = driver.findElement(By.cssSelector("a.dropdown-toggle.dropdown-toggle-profile"));
		wait.until(ExpectedConditions.elementToBeClickable(Profile));
		Profile.click();
		WebElement Logout = driver.findElement(By.xpath("//a[normalize-space()='Sign out']"));
		wait.until(ExpectedConditions.elementToBeClickable(Logout));
		Logout.click();
	}

}
