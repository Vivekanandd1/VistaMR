package Testcases;

import org.testng.Assert;
import AbstractComponent.BaseLineTest;
import DataSources.DataProviders;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import org.testng.annotations.Test;



public class LoginLogout extends BaseLineTest{
	
	
	@Description("This test case is for Login/Logout functinality")
    @Owner("Vivekanand Deshmukh")
	@Test(dataProvider = "driver", dataProviderClass = DataProviders.class)
	public void LogIn(String Email, String Password) throws InterruptedException {
		data.Login(Email, Password);			
		data.Logout();
		String ActualTitles = driver.getTitle();
		Assert.assertEquals(ActualTitles, "Kreditz | Vista");
	}

}
