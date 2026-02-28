package Testcases;

import org.testng.annotations.Test;
import com.github.javafaker.Faker;
import AbstractComponent.BaseLineTest;
import DataSources.ConfigReader;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;

public class CreateUserTest extends BaseLineTest {

	static Faker faker = new Faker();
	String Fullname = faker.name().fullName();
	String Emails = faker.internet().emailAddress();
	static String PhoneNumber = faker.numerify("##########");

	@Description("This test case is for UserCreation functionality")
	@Owner("Vivekanand Deshmukh")
	@Test
	public void UserCreation() throws InterruptedException {
		String Email = ConfigReader.get("Email");
		String Password = ConfigReader.get("Password");

		if (Email == null || Password == null) {
			throw new RuntimeException("Credentials not configured!");
		}

		data.Login(Email, Password);
		User.CreateUser(Fullname, Emails, PhoneNumber);
		User.UserDeletion(Fullname);
	}

}
