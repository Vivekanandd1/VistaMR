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

public class CorporateRequestTest extends BaseLineTest {

	String Name;

	@Description("This test case is for E2E manual Corporate request completion")
	@Owner("Vivekanand Deshmukh")
	@Test
	public void ManualCorporateRequests() throws InterruptedException {
		String Email = ConfigReader.get("Email");
		String Password = ConfigReader.get("Password");

		if (Email == null || Password == null) {
			throw new RuntimeException("Credentials not configured!");
		}

		Faker faker = new Faker();
		Name = faker.name().fullName();
		data.Login(Email, Password);
		BR.FormFillup(Name, Email);
		data.Redirection();
		driver.switchTo().defaultContent();
		BR.Consent();
		data.WindowShuffleChild();
		driver.switchTo().defaultContent();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div/h1[text()='Success!']")));
		data.WindowShuffleParent();
		String ActualTitle = driver.getTitle();
		Assert.assertEquals(ActualTitle, "Kreditz | Vista - New request");
	}

}
