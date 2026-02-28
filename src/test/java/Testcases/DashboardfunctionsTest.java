package Testcases;

import org.testng.annotations.Test;

import AbstractComponent.BaseLineTest;
import DataSources.ConfigReader;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;

public class DashboardfunctionsTest extends BaseLineTest {

	String Certificatenumber = "51818";

	@Description("This test case is for Dashboard Search and Filter Functionalities")
	@Owner("Vivekanand Deshmukh")
	@Test
	public void CertSearch() throws InterruptedException {
		String Email = ConfigReader.get("Email");
		String Password = ConfigReader.get("Password");

		if (Email == null || Password == null) {
			throw new RuntimeException("Credentials not configured!");
		}

		data.Login(Email, Password);
		CrtfctLgs.CertificateSearch(Certificatenumber);

	}

}
