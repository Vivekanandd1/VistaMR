package Testcases;

import org.testng.annotations.Test;

import AbstractComponent.BaseLineTest;
import MRqst.DataProviders;

public class LoginLogout extends BaseLineTest{
	
	@Test(dataProvider = "CredsDB", dataProviderClass = DataProviders.class)
	public void LogInOut(String Email, String Password,String Name) throws InterruptedException {
		data.Login(Email, Password);
		data.Logout();
		
	}

}
