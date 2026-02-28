package Testcases;

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
	public void LogIn() throws InterruptedException {
		String Email = ConfigReader.get("Email");
		String Password = ConfigReader.get("Password");

		if (Email == null || Password == null) {
			throw new RuntimeException("Credentials not configured!");
		}

		data.Login(Email, Password);
		data.Logout();
		String ActualTitles = driver.getTitle();
		Assert.assertEquals(ActualTitles, "Kreditz | Vista");
	}

}
