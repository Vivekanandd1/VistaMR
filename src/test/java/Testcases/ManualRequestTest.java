package Testcases;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.github.javafaker.Faker;
import AbstractComponent.BaseLineTest;
import DataSources.ConfigReader;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.testng.Tag;

public class ManualRequestTest extends BaseLineTest {

	String Name;

	@Description("This test case is for E2E manual request completion")
	@Owner("Vivekanand Deshmukh")
	@Tag("Manual Request")
	@Test
	public void ManualRequests() throws InterruptedException {

		String Email = ConfigReader.get("Email");
		String Password = ConfigReader.get("Password");

		if (Email == null || Password == null) {
			throw new RuntimeException("Credentials not configured!");
		}
		Faker faker = new Faker();
		Name = faker.name().fullName();
		data.Login(Email, Password);
		data.FormFillup(Name, Email);
		data.Pageload();
		data.Redirection();
		data.Pageload();
		driver.switchTo().defaultContent();
		data.Consent();
		data.WindowShuffleChild();
		driver.switchTo().defaultContent();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div/h1[text()='Success!']")));
		data.Pageload();
		data.WindowShuffleParent();
		data.Pageload();
		String ActualTitle = driver.getTitle();
		Assert.assertEquals(ActualTitle, "Kreditz | Vista - New request");
	}

}
