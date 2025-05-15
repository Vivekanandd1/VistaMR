package MRqst;

import org.testng.annotations.Test;

public class LoginLogout extends BaseLineTest{
	
	@Test(dataProvider = "CredsDB", dataProviderClass = DataProviders.class)
	public void LogInOut(String Email, String Password,String Name) throws InterruptedException {
		data.Login(Email, Password);
		data.Logout();
		
	}

}
