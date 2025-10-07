package ToBeDeleted;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RightClick {

	
	public static void main(String[] args) {
		
	    WebDriver driver =  new ChromeDriver();
	    driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		driver.get("https://www.google.com/");
		
		/*1.Right click on page or a element*/
		Actions act = new Actions(driver);
		/*On Page*/
//		act.contextClick().build().perform();
		/*On element*/
		WebElement  dd  = driver.findElement(By.xpath("//div[@class='k1zIA rSk4se']"));
		act.contextClick(dd).build().perform();
	}
}
