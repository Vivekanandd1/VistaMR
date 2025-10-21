package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import AbstractComponent.BaseLineTest;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;

public class APICall extends BaseLineTest {

	WebDriver driver;
	WebDriverWait wait;
	By SettingMenu = By.xpath("//a[normalize-space()='Settings']");
	By ApiSettingMenu = By.xpath("//a[normalize-space()='Api settings']");
	By SecretBtn = By.xpath("//a[normalize-space()='Regenerate Client Secret']");
	By ConfirmBtn = By.xpath("//button[normalize-space()='Confirm']");
	By Secret = By.xpath("(//div[@class='client-id-serect']//li/label)[4]");

	public APICall(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
	}

	public String GetSecret() {
		driver.findElement(SettingMenu).click();
		wait.until(ExpectedConditions.elementToBeClickable(ApiSettingMenu));
		driver.findElement(ApiSettingMenu).click();
		Actions action = new Actions(driver);
		action.moveToElement(driver.findElement(SecretBtn)).perform();
		wait.until(ExpectedConditions.elementToBeClickable(SecretBtn));
		driver.findElement(SecretBtn).click();
		driver.findElement(ConfirmBtn).click();
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(ConfirmBtn)));
		return driver.findElement(Secret).getText();
	}

	public String GetToken(String Secret) {
		RequestSpecification res = RestAssured.given();
		res.baseUri("https://vista.kreditz-dev.com");
		res.basePath("/kreditz/api/v3/authorizations/access_token");
		res.queryParam("client_id", "c71f1c07cb4b46f074c2592d1c7761").queryParam("client_secret", Secret);

		Response response = res.post();
		ResponseBody<?> resBody = response.getBody();
		JsonPath JsnPth = resBody.jsonPath();
		String BearerToken = JsnPth.get("data.access_token");
		return BearerToken;
	}

}
