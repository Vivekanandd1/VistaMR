package Testcases;

import org.testng.annotations.Test;

import AbstractComponent.BaseLineTest;
import DataSources.DataProviders;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;

public class DashboardfunctionsTest extends BaseLineTest {
	
	String Certificatenumber = "51818";
	
	@Description("This test case is for Dashboard Search and Filter Functionalities")
    @Owner("Vivekanand Deshmukh")
	@Test(priority = 0,dataProvider = "driver", dataProviderClass = DataProviders.class)
	public void UserCreation(String Email, String Password) throws InterruptedException {
		data.Login(Email, Password);	
		CrtfctLgs.CertificateSearch(Certificatenumber);
		
	}

}
