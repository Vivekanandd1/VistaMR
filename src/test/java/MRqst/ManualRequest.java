package MRqst;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

public class ManualRequest extends BaseLineTest  {

//	String Email = "snaps.deshmukh@gmail.com";
//	String Password = "Password123";

	@Test(dataProvider = "CredsDB", dataProviderClass = DataProviders.class)
	public void ManualRequests(String Email, String Password, String Name) throws InterruptedException {
		data.Login(Email, Password);
		data.FormFillup(Name, Email);
		data.Pageload();
		data.Redirection();
		data.Pageload();
		driver.switchTo().defaultContent();
		data.Consent();
		data.WindowShuffleChild();
		driver.switchTo().defaultContent();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div/h1[text()='Success!']")));
		System.out.println(driver.getTitle());
		data.Pageload();
		data.WindowShuffleParent();
		System.out.println("turned for parent");
		data.Pageload();
	}

}
