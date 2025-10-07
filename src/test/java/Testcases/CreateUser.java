package Testcases;

import org.testng.annotations.Test;
import com.github.javafaker.Faker;
import AbstractComponent.BaseLineTest;
import DataSources.DataProviders;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;



public class CreateUser extends BaseLineTest {
	
	static Faker faker = new Faker();
	String Fullname = faker.name().fullName();
	String Emails = faker.internet().emailAddress();
	static String PhoneNumber = faker.numerify("##########");
	
	@Description("This test case is for UserCreation functinality")
    @Owner("Vivekanand Deshmukh")
	@Test(priority = 0,dataProvider = "CredsDB", dataProviderClass = DataProviders.class)
	public void UserCreation(String Email, String Password) throws InterruptedException {
		data.Login(Email, Password);			
		User.CreateUser(Fullname, Emails, PhoneNumber);
	}
	
	

}
