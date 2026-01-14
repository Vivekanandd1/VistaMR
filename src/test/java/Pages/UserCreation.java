package Pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import AbstractComponent.BaseLineTest;

public class UserCreation extends BaseLineTest {

	WebDriver driver;
	WebDriverWait wait;

	private By userManagementMenu = By.xpath("//a[normalize-space()='User Management']");
	private By createUserBtn = By.xpath("//a[normalize-space()='Create User']");
	private By userName = By.id("organization_user_name");
	private By userEmail = By.id("organization_user_email");
	private By userPhone = By.id("organization_user_phone_number");
	private By submitBtn = By.className("submit_new_user");
	private By alertMessage = By.xpath("//div[@class='alert-message']");
	private By userRows = By.xpath("//tbody/tr");

	public UserCreation(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
	}

	public void CreateUser(String Fullname, String Emails, String PhoneNumber) {
		wait.until(ExpectedConditions.elementToBeClickable(userManagementMenu)).click();
		wait.until(ExpectedConditions.elementToBeClickable(createUserBtn)).click();

		driver.findElement(userName).sendKeys(Fullname);
		driver.findElement(userEmail).sendKeys(Emails);
		driver.findElement(userPhone).sendKeys(PhoneNumber);
		driver.findElement(submitBtn).click();

		wait.until(ExpectedConditions.invisibilityOfElementLocated(alertMessage));

	}

	public void UserDeletion(String Fullname) {
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(userRows));
		List<WebElement> rows = driver.findElements(userRows);

		for (WebElement row : rows) {

			String name = row.findElement(By.xpath("./td[2]")).getText();

			if (name.equalsIgnoreCase(Fullname)) {

				System.out.println("Deleting user: " + Fullname);

				row.findElement(By.xpath(".//a[@class='dropdown-toggle']")).click();
				row.findElement(By.xpath(".//a[@data-commit='Deactivate']")).click();
				row.findElement(By.xpath("//button[text()='Deactivate']")).click();

				wait.until(ExpectedConditions.invisibilityOfElementLocated(alertMessage));
				break;
			}
		}

	}

}
