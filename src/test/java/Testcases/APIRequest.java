package Testcases;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import AbstractComponent.BaseLineTest;
import DataSources.DataProviders;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;


class APIRequest extends BaseLineTest{


	@Description("This test case is for UserCreation functinality")
    @Owner("Vivekanand Deshmukh")
	@Test(priority = 0,dataProvider = "driver", dataProviderClass = DataProviders.class)
	public void APICallReq(String Email, String Password) throws InterruptedException {
		data.Login(Email, Password);			
		String text = API.GetSecret();
		String token = API.GetToken(text);
		System.out.println(token);
	}
	
}
