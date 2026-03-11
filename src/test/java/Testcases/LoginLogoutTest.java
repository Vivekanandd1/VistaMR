package Testcases;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import AbstractComponent.BaseLineTest;
import DataSources.ConfigReader;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import org.testng.annotations.Test;

public class LoginLogoutTest extends BaseLineTest {

	@Description("This test case is for Login/Logout functinality")
	@Owner("Vivekanand Deshmukh")
	@Test
	public void LogIn() {
		String Email = ConfigReader.get("Email");
		String Password = ConfigReader.get("Password");

		if (Email == null || Password == null) {
			throw new RuntimeException("Credentials not configured!");
		}

		data.Login(Email, Password);
		data.Logout();
		String expectedTitle = "Kreditz | Vista";

		wait.until(ExpectedConditions.titleIs(expectedTitle));

		String actualTitle = driver.getTitle();

		Assert.assertEquals(actualTitle, expectedTitle, "Page title mismatch after login!");
	}

}
