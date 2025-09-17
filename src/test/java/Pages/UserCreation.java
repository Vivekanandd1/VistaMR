package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import AbstractComponent.BaseLineTest;


public class UserCreation extends BaseLineTest{
	
	WebDriver driver;
	WebDriverWait wait;
    public UserCreation UC;
	public UserCreation(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
	}
	
	public void CreateUser() {
	   driver.findElement(By.xpath("//a[@href='/organizations/settings']")).click();
	   
	}
}
