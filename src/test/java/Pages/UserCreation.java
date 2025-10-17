package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import AbstractComponent.BaseLineTest;

public class UserCreation extends BaseLineTest {

	WebDriver driver;
	WebDriverWait wait;

	public UserCreation(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
	}

	public void CreateUser(String Fullname, String Emails, String PhoneNumber) {
		WebElement UserMenu = driver.findElement(By.xpath("//a[normalize-space()='User Management']"));
		wait.until(ExpectedConditions.elementToBeClickable(UserMenu));
		UserMenu.click();
		driver.findElement(By.xpath("//a[normalize-space()='Create User']")).click();
		driver.findElement(By.id("organization_user_name")).sendKeys(Fullname);
		driver.findElement(By.id("organization_user_email")).sendKeys(Emails);
		driver.findElement(By.id("organization_user_phone_number")).sendKeys(PhoneNumber);
		driver.findElement(By.className("submit_new_user")).click();
	}

}
