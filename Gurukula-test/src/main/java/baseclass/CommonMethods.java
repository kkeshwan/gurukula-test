package baseclass;

import static org.testng.AssertJUnit.assertEquals;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.BeforeTest;

public class CommonMethods {
	
	public static WebDriver driver;
	
	public static WebDriver initializeDriver() {
      
	//   driver = gurukulaTest.FVTests.initDriver();
		driver = gurukulaTest.FVTests.initDriver();
		return driver;
	}

	
	public static void launchApp(String appUrl) {
		driver = initializeDriver();
		driver.get(appUrl);
		
	}

	public static void login(String appUrl, String username, String password) {
		launchApp(appUrl);
		driver.findElement(By.linkText("login")).click();
		driver.findElement(By.id("username")).sendKeys(username);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.xpath("//button[text()='Authenticate']")).click();
	}
	public static void logout()
	{
        driver.findElement(By.cssSelector(".dropdown:nth-child(4) .caret")).click();
        driver.findElement(By.cssSelector(".ng-scope:nth-child(4) .ng-scope")).click();
    }


	public static void enterData(String data, WebElement element) {
		element.click();
		element.clear();
		element.sendKeys(data);   
		
	}
	
	public static void navEntitiesBranch()
	{
		driver.findElement(By.xpath("//span[@class='hidden-tablet ng-scope' and contains(.,'Entities')]")).click();
		driver.findElement(By.linkText("Branch")).click();
	}
	
	public static void jsScriptClick(WebElement element)
	{
		System.out.println("inside executor");
	JavascriptExecutor jse2 = (JavascriptExecutor)driver;
	jse2.executeScript("arguments[0].scrollIntoView()", element); 
	JavascriptExecutor executor = (JavascriptExecutor)driver;
	executor.executeScript("arguments[0].click();", element);
	}
	
	public static void navEntitiesStaff()
	{
		driver.findElement(By.xpath("//span[@class='hidden-tablet ng-scope' and contains(.,'Entities')]")).click();
		driver.findElement(By.linkText("Staff")).click();
	}
	public static void save()
	{
	driver.findElement(By.xpath("//button[@class='btn btn-primary ng-scope']")).click();
	}
	
	public static void verifyError(String expectedError)
	{
	String actualMsg = driver
			.findElement(
					(By.xpath("//div[@class='alert alert-danger ng-scope']")))
			.getText();
	System.out.println("actualMsg  " + actualMsg);
	System.out.println("expectedMsg  " + expectedError);
	// Validate the Error message
	assertEquals(expectedError, actualMsg);
	}
}
